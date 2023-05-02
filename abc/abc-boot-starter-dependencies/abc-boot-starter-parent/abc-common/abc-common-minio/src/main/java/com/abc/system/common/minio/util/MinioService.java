package com.abc.system.common.minio.util;

import com.abc.system.common.minio.config.MinioConfig;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * MinioService
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
    public List<Bucket> getAllBuckets() throws MinioException {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("⚠️ Minio异常：{}", e.getMessage());
            throw new MinioException(e.getMessage());
        }
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
            log.error("⚠️ Minio异常：{}", e.getMessage());
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
            log.error("⚠️ Minio异常：{}", e.getMessage());
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
            log.error("⚠️ Minio异常：{}", e.getMessage());
            throw new MinioException(e.getMessage());
        }
        return true;
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
            log.error("⚠️ Minio获得所有对象异常：{}", e.getMessage());
            throw new MinioException(e.getMessage());
        }
        return items;
    }


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
        if (!StringUtils.hasLength(originalFilename)) {
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
            log.error("⚠️ Minio上传文件异常，请首先检查是否具有上传权限！" + e.getMessage());
            throw new MinioException(e.getMessage());
        }
        // ok → 返回上传文件存储的名称
        return originalFilename;
    }

    /**
     * 预览文件
     *
     * @param fileName 文件名
     * @return 成功返回预览url
     * @throws MinioException MinioException
     */
    public String preview(String fileName) throws MinioException {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("⚠️ Minio文件预览异常！" + e.getMessage());
            throw new MinioException(e.getMessage());
        }
    }


    /**
     * 文件下载，直接将数据流写入response
     *
     * @param fileName 文件名称
     * @param res      response 用于将下载文件直接写入response
     * @throws MinioException MinioException
     */
    public void download(String fileName, HttpServletResponse res) throws MinioException {
        res.setCharacterEncoding("utf-8");
        // CORS not allow customized headers, you must export every customized header manually.
        // Axios will transfer header-name to lowercase!!!
        res.setHeader("Access-Control-Expose-Headers", "File-Name,File-Type");
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            // CORS, set Content-Length for axios
            res.setHeader("Content-Length", response.headers().get("Content-Length"));
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
            log.error("⚠️ Minio文件下载异常，请检查文件是否存在！" + e.getMessage());
            throw new MinioException(e.getMessage());
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
            log.error("⚠️ Minio异常：{}", e.getMessage());
            throw new MinioException(e.getMessage());
        }
    }

}


