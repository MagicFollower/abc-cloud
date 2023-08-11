package com.abc.system.common.minio.util;

import com.abc.system.common.minio.config.MinioConfig;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Minio服务实例
 * <pre>
 * 1.该实例将自动被注入SpringIOC容器;
 * 2.该实例接口将尽量与S3的接口名称保持一致。
 * </pre>
 *
 * @Description MinioService 详细介绍
 * @Author Trivis
 * @Date 2023/5/2 12:22
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    private final MinioConfig prop;

    // https://min.io/docs/minio/linux/developers/java/API.html
    private final MinioClient minioClient;

    /**
     * 获取全部bucket
     *
     * @return List of Bucket
     * @throws MinioException MinioException
     */
    public List<Bucket> listBuckets() throws MinioException {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#listBuckets异常: ", e);
            throw new MinioException(e.getMessage());
        }
    }

    /**
     * 获得所有对象
     *
     * @return 存储bucket内文件对象信息（Item列表，注意Item使用FluentBean模式）
     * @throws MinioException MinioException
     */
    public List<Item> listObjects() throws MinioException {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(prop.getBucketName())
                        .build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#listObjects异常: ", e);
            throw new MinioException(e.getMessage());
        }
        return items;
    }

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     * @throws MinioException MinioException
     */
    public boolean bucketExists(String bucketName) throws MinioException {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#bucketExists异常: ", e);
            throw new MinioException(e.getMessage());
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return boolean
     * @throws MinioException MinioException
     */
    public boolean addBucket(String bucketName) throws MinioException {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#addBucket异常: ", e);
            throw new MinioException(e.getMessage());
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return boolean
     * @throws MinioException MinioException
     */
    public boolean removeBucket(String bucketName) throws MinioException {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#removeBucket异常: ", e);
            throw new MinioException(e.getMessage());
        }
        return true;
    }

    /* ======================================================= */
    /* =============【上传】、【下载】、【预览】、【删除】============ */
    /* ======================================================= */

    /**
     * 文件上传
     *
     * @param file 文件
     * @return String 成功返回文件名
     * @throws MinioException MinioException
     */
    public String upload(MultipartFile file) throws MinioException {
        String originalFilename = file.getOriginalFilename();
        // hasText + hasLength
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException("文件名为空!");
        }
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(originalFilename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#upload异常: ", e);
            throw new MinioException(e.getMessage());
        }
        // ok → 返回上传文件存储的名称
        return originalFilename;
    }

    /**
     * 文件下载，直接将数据流写入response
     *
     * @param fileName 文件名称
     * @param res      response 用于将下载文件直接写入response
     * @throws MinioException MinioException
     */
    public void download(String fileName, HttpServletResponse res) throws MinioException {
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // CORS not allow customized headers, you must export every customized header manually.
        // Axios will transfer header-name to lowercase!!!
        res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "File-Name,File-Type");
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            // CORS, set Content-Length for axios
            // 😄now, you can use this header-attribute to fill your progress-bar!
            res.setHeader(HttpHeaders.CONTENT_LENGTH, response.headers().get("Content-Length"));
            res.addHeader("File-Name", fileName);
            res.addHeader("File-Type", response.headers().get("Content-Type"));
            final byte[] buf = new byte[1024 * 5];
            int len;
            try (ServletOutputStream stream = res.getOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    stream.write(buf, 0, len);
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#download异常: ", e);
            throw new MinioException(e.getMessage());
        }
    }

    /**
     * 预览文件（获取预签名对象URL）
     *
     * <pre>
     * 1.getPresignedObjectUrl 方法不会直接抛出异常来指示文件不存在。
     *   如果调用该方法来获取预签名的对象 URL，且该对象不存在，它将返回一个指向不存在对象的 URL。
     * 2.要在文件不存在时抛出异常而不是返回一个不存在的 URL，您可以使用 MinIO Java 客户端中的 statObject 方法来检查对象是否存在，然后再决定是否获取预签名的 URL。
     *   statObject 方法将返回一个对象元数据（ObjectStat）或抛出一个 ErrorResponseException 异常，您可以基于此异常判断是否存在文件。
     * </pre>
     *
     * @param fileName 文件名
     * @return 成功返回预览url, 失败返回空字符串
     */
    public String preview(String fileName) throws MinioException {
        // 查看文件地址
        GetPresignedObjectUrlArgs previewArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .expiry(30, TimeUnit.MINUTES)  // 30分钟失效，默认7天
                .method(Method.GET)
                .build();
        StatObjectArgs fileStats = StatObjectArgs.builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .build();
        try {
            minioClient.statObject(fileStats);
            return minioClient.getPresignedObjectUrl(previewArgs);
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#preview异常, path:{}: ", Paths.get(prop.getBucketName(), fileName), e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 删除
     *
     * @param fileName 文件名
     * @return 移除失败返回false
     * @throws MinioException MinioException
     */
    public boolean remove(String fileName) throws MinioException {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
            return true;
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#remove异常: ", e);
            throw new MinioException(e.getMessage());
        }
    }

}


