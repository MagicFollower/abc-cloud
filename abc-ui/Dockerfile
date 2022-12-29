#使用 nginx最新版本作为基础镜像
FROM 192.168.204.111:41443/utils/nginx:1.23.0

#将当前文件夹的dist文件复制到容器的/usr/share/nginx/html目录
COPY ./dist /usr/share/nginx/html/

#覆盖默认的nginx.conf
# nginx原生镜像不支持直接映射配置文件，支持映射文件夹
COPY nginx.conf /etc/nginx/nginx.conf

#声明运行时容器暴露的端口（容器提供的服务端口）
#EXPOSE 80

ENTRYPOINT  ["nginx", "-g", "daemon off;"]
