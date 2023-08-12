package com.abc.system.common.minio.service;

import com.abc.system.common.constant.SystemRetCodeConstants;
import com.abc.system.common.exception.file.RemoteFileDownloadException;
import com.abc.system.common.minio.config.MinioConfig;
import com.abc.system.common.minio.constant.ImageTypeMimeEnum;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Minio服务实例
 * <pre>
 * 1.该实例将自动被注入SpringIOC容器;
 * 2.该实例接口将尽量与S3的接口名称保持一致。
 *
 * 关于异常：
 * 1.MINIO服务下线时，接口调用会抛出异常；
 * 2.MINIO服务时间和客户端时间差异过大时，会抛出异常；
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
    private static final String[] SUPPORTED_IMAGE_SUFFIX = new String[]{".jpg", ".jpeg", ".png", ".gif"};

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
            log.error("Minio#listBuckets异常:", e);
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
            log.error("Minio#listObjects异常:", e);
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
            log.error("Minio#bucketExists异常:", e);
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
            log.error("Minio#addBucket异常:", e);
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
            log.error("Minio#removeBucket异常:", e);
            throw new MinioException(e.getMessage());
        }
        return true;
    }

    /* ======================================================= */
    /* ======【存在检测】、【上传x2】、【下载】、【预览】、【删除】====== */
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
            log.error(">>>>>>>>|objectExists|error|exception:{}|<<<<<<<<", e.getMessage());
            final String MSG = String.format("Minio#objectExists异常: [%s]",
                    Paths.get(prop.getBucketName(), fileName));
            response.fill(SystemRetCodeConstants.REMOTE_FILE_NOT_FOUND, MSG);
            response.setResult(false);
        }
        return response;
    }

    /**
     * 文件上传（MultiPart）
     * <pre>
     * 1.文件名称相同会覆盖
     * </pre>
     *
     * @param multipartFile 文件
     * @return String 成功/失败均返回上传文件Path
     */
    public BaseResponse<Path> upload(@lombok.NonNull MultipartFile multipartFile) {
        BaseResponse<Path> response = new BaseResponse<>();
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
            log.error(">>>>>>>>|upload|error|exception:{}|<<<<<<<<", e.getMessage());
            final String MSG = "Minio#objectExists异常";
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR, MSG);
        }
        response.setResult(Paths.get(prop.getBucketName(), originalFilename));
        return response;
    }

    /**
     * 文件上传(File)
     * <pre>
     * 1.文件名称相同时，上传正常，文件会覆盖；
     * 2.file=null: 参数file使用lombok#NonNull进行空参检测，抛出NPE，建议您手动处理该NPE或使用全局异常拦截；
     * 3.file不存在： 不会抛出异常，获得非成功响应
     *   3.1 这种场景不应该存在，提供当前API有两个目的：其一为解析参数中的图片base64字符串，其二上传File类型图片，后者你需要手动检测File的存在。
     *
     * 使用示例✨
     * {@code
     *    @GetMapping("/upload")
     *    public ResponseData<String> demo04() {
     *        final ResponseProcessor<String> rp = new ResponseProcessor<>();
     *        File file = new File("C:\\Users\\xpc\\Pictures\\20230729\\wallhaven-pkpk-.jpg");
     *        if (file.exists()) {
     *            BaseResponse<String> uploadImageFileRes = minioService.uploadImageFile(file);
     *            if (!SystemRetCodeConstants.OP_SUCCESS.getCode().equals(uploadImageFileRes.getCode())) {
     *                log.error(">>>>>>>>|{}:{}|<<<<<<<<", uploadImageFileRes.getCode(), uploadImageFileRes.getMsg());
     *                return rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
     *            }
     *            return rp.setData(uploadImageFileRes.getResult());
     *        } else {
     *            return rp.setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
     *        }
     *    }
     * }
     * </pre>
     *
     * @param file 文件
     * @return String 成功/失败均返回上传文件在文件服务器中PathString，文件不存在时则返回文件名PathString
     */
    public BaseResponse<String> uploadImageFile(@lombok.NonNull File file) {
        BaseResponse<String> response = new BaseResponse<>();
        String originalFilename = file.getName();
        // 1.文件类型检测（suffix）
        if (!StringUtils.endsWithAny(originalFilename, SUPPORTED_IMAGE_SUFFIX)) {
            final String MSG = "Minio#uploadImageFile异常: 文件格式不支持";
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR, MSG);
            response.setResult(Paths.get(originalFilename).toString());
            return response;
        }
        final String imageSuffixType = originalFilename
                .substring(originalFilename.lastIndexOf(".") + 1);
        // 2.上传
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(originalFilename)
                    .stream(Files.newInputStream(file.toPath()), file.length(), -1)
                    .contentType(ImageTypeMimeEnum.getMimeByType(imageSuffixType))
                    .build();
            minioClient.putObject(objectArgs);
            response.fill(SystemRetCodeConstants.OP_SUCCESS);
        } catch (Exception e) {
            log.error(">>>>>>>>|uploadImageFile|error|exception:{}|<<<<<<<<", e.getMessage());
            final String MSG = "Minio#uploadImageFile异常";
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR, MSG);
        }
        response.setResult(Paths.get(prop.getBucketName(), originalFilename).toString());
        return response;
    }

    /**
     * 文件下载，直接将数据流写入response
     * <pre>
     * 1.文件不存在会抛出异常:io.minio.errors.ErrorResponseException: The specified key does not exist.
     * 2.关于下载接口的使用示例 x2
     *   -> 你需要提前使用全局异常拦截器或手动包装异常响应给Client（用户不可见详细错误，他们只能看到"系统繁忙"，详细错误可以通过日志暴露给开发者）
     *   -> download接口已将HttpStatus设置为500在异常的情况下，前端仅需要根据HttpStatus决定是否下载/提示错误信息。
     * 2.1 使用全局异常拦截✨
     * {@code
     *     @GetMapping("/download")
     *     public void demo02(HttpServletResponse response) {
     *         final ResponseProcessor<String> rp = new ResponseProcessor<>();
     *         final String imgName = "img/X.png";
     *         minioService.download(imgName, response);
     *     }
     * }
     * 2.2 手动抛出异常✨
     * {@code
     *     @GetMapping("/download")
     *     public ResponseData<String> demo02(HttpServletResponse response) {
     *         final ResponseProcessor<String> rp = new ResponseProcessor<>();
     *         final String imgName = "img/Z.png";
     *         try {
     *             minioService.download(imgName, response);
     *             return null;
     *         } catch (RemoteFileDownloadException e) {
     *             log.error(">>>>>>>>|{}:{}|<<<<<<<<", e.getErrorCode(), e.getMessage());
     *             return new ResponseProcessor<String>().setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
     *         }
     *     }
     * }
     * </pre>
     *
     * @param fileName 文件名称
     * @param res      response 用于将下载文件直接写入response
     * @throws RemoteFileDownloadException 远程文件下载异常
     */
    public void download(String fileName, HttpServletResponse res) throws RemoteFileDownloadException {
        final String FILE_NAME_HTTP_HEADER = "File-Name";
        final String FILE_TYPE_HTTP_HEADER = "File-Type";
        // CORS not allow customized headers, you must export every customized header manually.
        // Axios will transfer header-name to lowercase!!!
        res.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "File-Name,File-Type");
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            // Set Content-Length manually for Axios!
            res.setHeader(HttpHeaders.CONTENT_LENGTH, response.headers().get("Content-Length"));
            final String fileNameInResponseHeader = fileName.substring(fileName.lastIndexOf("/") + 1);
            res.addHeader(FILE_NAME_HTTP_HEADER,
                    URLEncoder.encode(fileNameInResponseHeader, StandardCharsets.UTF_8.name()));
            res.addHeader(FILE_TYPE_HTTP_HEADER,
                    response.headers().get("Content-Type"));
            final byte[] buf = new byte[1024 * 5];
            int len;
            try (ServletOutputStream stream = res.getOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    stream.write(buf, 0, len);
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>|download|error|exception:{}|<<<<<<<<", e.getMessage());
            // Http请求状态500响应
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            final String MSG = String.format("Minio#download异常: [%s]",
                    Paths.get(prop.getBucketName(), fileName));
            throw new RemoteFileDownloadException(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR.getCode(), MSG);
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
            log.error(">>>>>>>>|preview|error|exception:{}|<<<<<<<<", e.getMessage());
            final String MSG = String.format("Minio#preview异常: [%s]", Paths.get(prop.getBucketName(), fileName));
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR, MSG);
            response.setResult(StringUtils.EMPTY);
        }
        return response;
    }

    /**
     * 删除
     * <pre>
     * 1.removeObject删除时，在文件不存在不会抛出异常
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
            log.error(">>>>>>>>|remove|error|exception:{}|<<<<<<<<", e.getMessage());
            final String MSG = String.format("Minio#remove异常: [%s]",
                    Paths.get(prop.getBucketName(), fileName));
            response.fill(SystemRetCodeConstants.REMOTE_FILE_SERVICE_ERROR, MSG);
            response.setResult(false);
        }
        return response;
    }
}


