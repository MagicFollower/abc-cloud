#### abc-common-minio组件文档

---

##### 🚀版本：0.0.1-SNAPSHOT

- minio官方并没有提供spring-boot的启动器，如果利用springboot自动装配的特性，需要二次封装；
- 本版本组件封装支持`单节点Minio`
- 对外暴露配置参数如下

```yaml
minio:
	endpoint: 192.168.204.101:9090
	accessKey: *******
	secretKey: **************
	bucketName: exampleBucket
```

- `SpringIOC容器`中注入`MinioService`对象，提供封装后的文件上传下载等API。

```conf
List<Bucket> getAllBuckets() throws MinioException 
boolean bucketExists(String bucketName) throws MinioException 
boolean addBucket(String bucketName) throws MinioException 
boolean removeBucket(String bucketName) throws MinioException 
```

```conf
List<Item> listObjects() throws MinioException   // 获取springboot配置的bucketName中所有对象
```

```conf
String upload(MultipartFile file) throws MinioException  // 返回上传文件名
void download(String fileName, HttpServletResponse res) throws MinioException // 自定义下载
String preview(String fileName) throws MinioException  // 预览url
boolean remove(String fileName) throws MinioException  // 删除文件
```

⚠️注意：

- 这里的`MinioException`是<u>`minio`官方的异常（继承自Exception）</u>，需要用户在程序中显式捕获并处理。`Exception` 和`RuntimeException` 不同，后者不强制在程序中显式捕获处理。

- 当前组件提供了web环境下单文件的上传（`MultipartFile`），多文件上传请在业务逻辑中手动控制。
  - 上传多个文件有多种实现：1. 多个文件上传；2.多个文件打包上传。

⚠️使用约定：

- 请不要再业务逻辑中随意创建/删除Bucket，上传文件/下载文件会默认使用`springboot配置文件` 中的bucket。

⚠️关于文件下载API：

- 填充Content-Length请求头 → 数据大小
  - Axios可使用该请求头实现进度条
- <u>新增File-Name请求头 → 文件名</u>
- <u>新增File-Type请求头 → 文件类型（Content-Type）</u>

⬇️

使用当前下载接口，你可以同时实现前端预览和下载功能：

> 示例使用Axios

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件预览</title>
    <script src="./axios.min.js"></script>
    <script>
        function previewFile() {
            let axios1 = axios.create({
                baseURL: 'http://127.0.0.1:9091',
                timeout: 30000,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });
            // 从后台获取数据
            axios1.get('/images/download_s', {
                responseType: 'blob'
            }).then(res => {
                let url = window.URL.createObjectURL(new Blob([res.data], {'type': res.headers['file-type']}));
                window.open(url);
                window.URL.revokeObjectURL(url)
            });
        }
    </script>
</head>
<body>

<button onclick="previewFile()">预览文件</button>
</body>
</html>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件下载</title>
    <script src="./axios.min.js"></script>
    <script>
        function downloadFile() {
            let axios1 = axios.create({
                baseURL: 'http://127.0.0.1:9091',
                timeout: 30000,
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });
            // 从后台获取数据
            axios1.get('/images/download_s', {
                responseType: 'blob'
            }).then(res => {
                let url = window.URL.createObjectURL(new Blob([res.data], {'type': res.headers['file-type']}));
                let a = document.createElement('a');
                a.href = url;
                a.download = res.headers['file-name'];
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            });
        }
    </script>
</head>
<body>

<button onclick="downloadFile()">下载文件</button>
</body>
</html>
```

