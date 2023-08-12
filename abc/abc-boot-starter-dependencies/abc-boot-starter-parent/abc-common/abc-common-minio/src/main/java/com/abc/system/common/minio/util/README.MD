## 文件下载接口最佳实践 [MinioService]

`创建时间：2023年08月12日 14:52:08`

- SpringBoot
- Axios
  - https://axios-http.com/docs/res_schema 
  - `<script src="https://unpkg.com/axios/dist/axios.min.js"></script>`
  - `<script src="https://cdn.bootcdn.net/ajax/libs/axios/1.2.3/axios.min.js"></script>`

---

#### 一、CORS

```java
@Configuration
public class CORSConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/minio-demo01/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }
}
```



#### 二、Controller

```java
@GetMapping("/download")
public ResponseData<String> demo02(HttpServletResponse response) {
    final ResponseProcessor<String> rp = new ResponseProcessor<>();
    final String imgName = "img/X.png";
    try {
        minioService.download(imgName, response);
        return null;
    } catch (RemoteFileDownloadException e) {
        log.error(">>>>>>>>|{}:{}|<<<<<<<<", e.getErrorCode(), e.getMessage());
        return new ResponseProcessor<String>().setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
    }
}
```

```java
@GetMapping("/download_another_way")
public void demo02(HttpServletResponse response) {
    final ResponseProcessor<String> rp = new ResponseProcessor<>();
    final String imgName = "img/灰原哀.png";
    minioService.download(imgName, response);
}
```

```java
@ControllerAdvice
@ConditionalOnClass({Controller.class, RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<?> handleException(Exception e) {
        log.error(String.format(">>>>>>>>|全局异常拦截生效中: %s|<<<<<<<<", e.getMessage()));
        return new ResponseProcessor<>().setErrorMsg(SystemRetCodeConstants.SYSTEM_BUSINESS);
    }
}
```



#### 三、HTML（with Axios）

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件下载</title>
    <script src="./js/axios.min.js"></script>
    <script>
        function downloadFile() {
            let axios1 = axios.create({
                baseURL: 'http://127.0.0.1:10000',
                timeout: 30000,
                // 默认是json类型数据
                headers: {
                    // 'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });
            // 从后台获取数据
            axios1.get('/minio-demo01/download', {
                responseType: 'blob'
            }).then(res => {
                const { 'file-name': fileNameURLEncoded, 'file-type': fileType } = res.headers;
                //let url = window.URL.createObjectURL(new Blob([res.data], {'type': res.headers['file-type']}));
                const url = URL.createObjectURL(new Blob([res.data], { type: fileType }));
                const link = document.createElement('a');
                link.href = url;
                // URL解码
                link.download = decodeURIComponent(fileNameURLEncoded);
                link.style.display = 'none';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                URL.revokeObjectURL(url);
            }).catch(e=>{
                if (e.response) {
                    // 获取接口返回的Blob数据
                    let blobData = e.response.data;
                    // 将Blob数据转换为JSON格式
                    let response = new Response(blobData);
                    response.json().then(jsonData => {
                        // 在这里展示JSON数据，例如将其显示在错误消息的提示框中或将其显示在页面上的某个区域
                        console.log(jsonData);
                    });
                }
            });
        }
    </script>
</head>
<body>

<button onclick="downloadFile()">下载文件</button>
</body>
</html>
```

