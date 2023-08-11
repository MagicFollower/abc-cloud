package com.abc.system.common.minio.util;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.file.RemoteFileDownloadException;
import com.abc.system.common.minio.config.MinioConfig;
import com.abc.system.common.response.BaseResponse;
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
    /* ======【存在检测】、【上传】、【下载】、【预览】、【删除】======== */
    /* ======================================================= */

    /**
     * 文件存在检测
     *
     * @param fileName 文件名
     * @return BaseResponse of Boolean
     */
    public BaseResponse<Boolean> objectExists(String fileName) {
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            StatObjectArgs fileStats = StatObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(fileName)
                    .build();
            minioClient.statObject(fileStats);
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
            response.setResult(true);
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#objectExists异常: ", e);
            response.fill(SystemRetCodeConstants.REMOTE_FILE_NOT_FOUND);
            response.setResult(false);
        }
        return response;
    }

    /**
     * 文件上传
     * <pre>
     * 1.文件名称相同会覆盖
     * </pre>
     *
     * @param multipartFile 文件
     * @return String 成功/失败均返回文件名
     * @throws MinioException MinioException
     */
    public BaseResponse<String> upload(MultipartFile multipartFile) throws MinioException {
        BaseResponse<String> response = new BaseResponse<>();
        String originalFilename = StringUtils.EMPTY;
        try {
            originalFilename = multipartFile.getOriginalFilename();
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(originalFilename)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build();
            minioClient.putObject(objectArgs);
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#upload异常: ", e);
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR);
        }
        response.setResult(originalFilename);
        return response;
    }

    /**
     * 文件下载，直接将数据流写入response
     * <pre>
     * 1.异常将交由调用方Service/Controller进行处理与控制。
     *   -> 如果在Controller层调用，可以直接手动拦截或使用全局异常拦截抛出【系统繁忙】并记录日志；
     *   -> 如果在Service层调用，直接将错误信息原封不动向上抛出至Controller。
     * </pre>
     *
     * @param fileName 文件名称
     * @param res      response 用于将下载文件直接写入response
     * @throws RemoteFileDownloadException 远程文件下载异常
     */
    public void download(String fileName, HttpServletResponse res) throws RemoteFileDownloadException {
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // CORS not allow customized headers, you must export every customized header manually.
        // Axios will transfer header-name to lowercase!!!
        res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "File-Name,File-Type");
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            // CORS, set Content-Length for axios
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
            throw new RemoteFileDownloadException(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR);
        }
    }

    /**
     * 预览文件（获取预签名对象URL）
     *
     * <pre>
     * 1.getPresignedObjectUrl 方法不会直接抛出异常来指示文件不存在。
     *   如果调用该方法来获取预签名的对象 URL，且该对象不存在，它将返回一个指向不存在对象的 URL。
     *
     * 2.接口使用示例:
     * {@code
     *     @GetMapping("/preview")
     *     public ResponseData<String> demo01() {
     *         final ResponseProcessor<String> rp = new ResponseProcessor<>();
     *         final String imgName = "img/txx.png";
     *         BaseResponse<String> preview = minioService.preview(imgName);
     *         if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(preview.getCode())) {
     *             return rp.setErrorMsg(preview.getCode(), preview.getMsg());
     *         }
     *         return rp.setData(preview.getResult());
     *     }
     * }
     * </pre>
     *
     * @param fileName 文件名
     * @return BaseResponse with URL (文件不存在时、其他异常时将返回【系统繁忙】)
     */
    public BaseResponse<String> preview(String fileName) {
        BaseResponse<String> response = new BaseResponse<>();
        try {
            GetPresignedObjectUrlArgs previewArgs = GetPresignedObjectUrlArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(fileName)
                    .expiry(30, TimeUnit.MINUTES)  // 30分钟失效，缺省7天
                    .method(Method.GET)
                    .build();
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
            response.setResult(minioClient.getPresignedObjectUrl(previewArgs));
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#preview异常, path:{}: ", Paths.get(prop.getBucketName(), fileName), e);
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR);
            response.setResult(StringUtils.EMPTY);
        }
        return response;
    }

    /**
     * 删除
     * <pre>
     * 1.removeObject删除时，在文件不存在不会抛出异常😊
     * </pre>
     *
     * @param fileName 文件名
     * @return BaseResponse of Boolean
     */
    public BaseResponse<Boolean> remove(String fileName) {
        BaseResponse<Boolean> response = new BaseResponse<>();
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(fileName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
            response.setResult(true);
        } catch (Exception e) {
            log.error(">>>>>>>>|Minio#remove异常: ", e);
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR);
            response.setResult(false);
        }
        return response;
    }
}


