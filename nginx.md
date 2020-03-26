# nginx
> 反向代理（通过域名区分不同项目）  
  动静分离  
  配置https证书
```
http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
	
    server {
        listen  80;
        server_name  blog.imwj.club;
		
        listen 443 ssl;
        ssl_certificate      C://ssl//证书.pem;
        ssl_certificate_key  C://ssl//证书.key; 
		
        location /{
             proxy_set_header Host $host;
             proxy_set_header X-Real-IP $remote_addr;
             proxy_set_header REMOTE-HOST $remote_addr;
             proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
             client_max_body_size  100M;
             proxy_pass http://127.0.0.1:8081;
        }
        location /upload{
             alias C:/myblog/upload;
        }
    }
	
    server {
        listen  80;
        server_name  tmall.imwj.club;
		
        location /{
             proxy_set_header Host $host;
             proxy_set_header X-Real-IP $remote_addr;
             proxy_set_header REMOTE-HOST $remote_addr;
             proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
             client_max_body_size  100M;
             proxy_pass http://127.0.0.1:8888;
        }
        location ~\.(css|js|png|ttf|woff|woff2|eot|svg|map|jpg|gif)$ {
             root C:/tmall/webapp;
        }	
    }
}
```