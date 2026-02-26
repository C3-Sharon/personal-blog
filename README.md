# 个人博客后端 API

基于 Spring Boot 3 + MyBatis 的个人博客系统后端，提供 RESTful API 接口,前端：https://github.com/C3-Sharon/blog-frontend

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [功能](#功能)
- [使用教程](#使用教程)
- [部署到服务器](#部署到服务器)
- [API 接口文档](#api-接口文档)
- [项目结构](#项目结构)
- [完成日志](#完成日志)
- [作者](#作者)
- [许可证](#许可证)

## 项目简介
这是一个个人博客后端项目，支持博客管理、留言互动和作品展示功能。采用前后端分离架构，所有接口返回 JSON 数据。

## 技术栈
- **Spring Boot 3.1.6** - 核心框架
- **MyBatis** - 持久层框架
- **MySQL 8.0** - 数据库
- **Maven** - 项目构建
- **Flyway** - 数据库版本管理（自动化建表）
- **Spring AOP** - 切面编程
- **Lombok** - 简化代码
- **CommonMark** - Markdown 渲染

## 功能

### 博客管理
- 博客列表（支持分页、搜索）
- 博客详情（支持评论）
- 发布博客（支持 Markdown）（管理员）
- 编辑博客（管理员）
- 删除博客（管理员）
- 图片上传（管理员）

### 留言板
- 获取留言列表
- 发表留言
- 删除留言（管理员）

### 展厅
- 作品列表（分页、分类）
- 作品上传（管理员）
- 作品编辑（管理员）
- 作品删除（管理员）
- 文件下载

### 管理员
- 登录/登出
- Session 会话管理
- 权限验证

## 使用教程

### 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- Git（可选，用于克隆代码）

### 克隆项目
```bash
git clone https://github.com/C3-Sharon/personal-blog.git
cd personal-blog
```

### 数据库配置
```sql
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 修改配置文件`src/main/resources/application.properties`：
```properties
# 自定义数据库连接（使用环境变量或直接修改）以及限制文件上传大小
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
spring.servlet.multipart.max-file-size=15MB
spring.servlet.multipart.max-request-size=15MB

# 核心路径与跨域配置 (保持默认或根据下方环境变量修改)
upload.path=${user.dir}/uploads/
app.cors.allowed-origin=${CORS_ORIGIN:http://localhost:5173}
```

### 环境变量设置
#### Windows（临时）
```bash
set DB_USERNAME=root
set DB_PASSWORD=你的数据库密码
set ADMIN_USERNAME=admin
set ADMIN_PASSWORD=你的后台登录密码
set CORS_ORIGIN=http://localhost:5173
```

#### Windows（永久）
- 打开“系统属性” → “高级” → “环境变量”
- 在“用户变量”中新建以下条目：
    - 变量名：`DB_USERNAME`，变量值：`root`
    - 变量名：`DB_PASSWORD`，变量值：`123456`
    - 变量名：`ADMIN_USERNAME`，变量值：`admin`
    - 变量名：`ADMIN_PASSWORD`，变量值：`123456`
    - 变量名：`CORS_ORIGIN`,变量值：`http://localhost:5173`

#### Mac/Linux
```bash
export DB_USERNAME=root
export DB_PASSWORD=你的数据库密码
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=你的后台登录密码
export CORS_ORIGIN=http://localhost:5173
```

### 运行项目
```bash
# 使用 Maven
mvn spring-boot:run
```

### 打包部署
```bash
# 打包（跳过测试）
mvn clean package -Dmaven.test.skip=true

# 运行 jar 包（指定所有核心参数）
java -jar target/personal-blog-0.0.1-SNAPSHOT.jar \
  --DB_USERNAME=root \
  --DB_PASSWORD=你的数据库密码 \
  --ADMIN_USERNAME=admin \
  --ADMIN_PASSWORD=你的安全密码 \
  --CORS_ORIGIN=http://你的域名或IP
````

## 部署到服务器

### 购买服务器

### 服务器环境配置
```bash
# 更新系统
apt update && apt upgrade -y

# 安装必要组件
apt install openjdk-17-jdk mysql-server nginx git unzip -y
```

### 部署后端
```bash
# 准备目录
mkdir -p /var/www/blog
cd /root/personal-blog

# 启动后端 (注意设置文件上传大小限制)
nohup java -jar personal-blog-0.0.1-SNAPSHOT.jar \
  --DB_PASSWORD=你的数据库密码 \
  --CORS_ORIGIN=http://你的服务器IP \
  --ADMIN_USERNAME=你的后台用户名 \
  --ADMIN_PASSWORD=你的后台登录密码 \
  --spring.servlet.multipart.max-file-size=50MB \
  --spring.servlet.multipart.max-request-size=50MB \
  > blog.log 2>&1 &
```

### 部署前端
```bash
# 修改 API 地址（写入你的服务器公网 IP 或域名）
echo "VITE_API_BASE_URL=http://你的服务器IP/api" > .env.production

# 打包并部署到 Nginx 目录
npm install
npm run build
cp -r dist/* /var/www/html/
```

### 配置 Nginx
```bash
vim /etc/nginx/sites-available/default
```

写入：
```nginx
# 为了让 Nginx 有权限读取 /root 下的图片，建议将第一行修改为：
# user root; 

server {
    listen 80;
    server_name 你的服务器IP;
    
    # 允许上传大图
    client_max_body_size 50M;

    location / {
        root /var/www/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 接口转发：注意末尾的斜杠
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
    }

    # 图片资源直接映射：效率更高，解决详情页照片不显示
    location /uploads/ {
        alias /root/personal-blog/uploads/;
    }
}
```

```bash
# 重启 nginx
systemctl restart nginx
```

### 配置防火墙
```bash
# 开放端口
ufw allow 80
ufw allow 8080
ufw allow 22
```

### 访问你的网站
```
http://你的服务器IP
```
## API 接口文档

### 博客接口
|  方法   |            路径            | 说明 |
|:-----:|:------------------------:|:----:|
|`GET`	 |       `/api/blogs`       |获取博客列表（支持 `page`, `size`, `keyword`）|
|`GET`	 |    `/api/blogs/{id}`     |获取博客详情|
|`POST` |    `/api/admin/blogs`    |发布博客（需登录）|
|`POST` | `/api/admin/blogs/{id}`  |更新博客（需登录）|
|`POST` | `/api/admin/delete/{id}` |删除博客（需登录）|

### 留言接口
|    方法     |             路径              |说明|
|:---------:|:---------------------------:|:----:|
|  `GET`	   |`/api/blogs/{blogId}/comments` |获取留言列表|
|  `POST`	  |` /api/blogs/{blogId}/comments `|发表留言|
| `DELETE`  | `/api/admin/comments/{id} ` |删除留言（需登录）|

### 展厅接口
|    方法    |        路径        | 说明 |
|:--------:|------------------|:----:|
|  `GET`	  |  `/api/gallery`  |	获取作品列表|
|  `GET`	  | `/api/gallery/{id}` |	获取作品详情|
|  `POST`  |   `/api/gallery`   |上传作品（需登录） |
|  `PUT`	  |`/api/gallery/{id}`	|更新作品（需登录）|
| `DELETE` |`/api/gallery/{id}`|	删除作品（需登录）|

### 管理员接口
|  方法  |         路径          |     说明     |
|:----:|:-------------------:|:----------:|
|`POST`| `/api/admin/login`  |   登录   |
|`GET`	| `/api/admin/logout` |     登出     |
|`GET`	| `/api/admin/check`  |   检查登录状态   |

## 项目结构
```
text
src/main/java/com/sharon/blog/
├── config/              
├── controller/         
├── handler/              
├── mapper/            
├── pojo/                
├── service/           
└── util/             

src/main/resources/
├── db/migration/       
├── mapper/             
└── application.properties
```

## 完成日志
### 总体说明：
```text
   我的学习进度在寒假之前都相当缓慢（到期末周的时候学到反射），在寒假学习速度提升（截止到2.20大致学完spring）
速度提升的同时带来的有知识掌握不牢，思维混乱的问题（当时我对该怎么有条理地构建一个项目一头雾水），我个人选择的处理方式是早点启动项目的构建
（2.12开始，此时刚开始spring MVC的学习）， 通过项目驱动学习的方式加深我对知识点的印象，因此前面部分我基本是照着ai的建议一步一步跟着敲的，
在每日完成的记录中可以看到采用了混合开发（后期自行改为前后端分离），使用JPA（中期自行改为mybatis），并在项目完成之后花了很多时间梳理项目
构建相关的要点。我觉得这可能是较为适合我的学习方式，通过此项目我巩固了前面我所学到的东西也学到了不少新的附加的知识同时也积累了经验。
```
### 项目选择：
```text
   之所以选择博客框架是因为我是有写每日日程或记录习惯的人，创建一个博客框架十分实用，在某种意义上可以督促我
按时完成每天的记录且能督促我及时总结（无论是学习心得还是生活体验感想）；
```
### 功能设计
```text
   比起一个考核作业我更希望这个项目能成为我长期使用的工具，并随着我能力的提升不断更新，所以在设计某些功能的时候（上传照片并展示，展厅，支持markdown，包括我在部署到服务器的时候出现了仍需手动建表后期进行了优化）
并没有考虑到超纲的问题，直接按着ai给的指示敲了，项目完成之后也尽力理解了相关代码的作用机制。以下是我对一些功能的说明：
 留言板：参考以前使用过的pome，朋友们之间可以实现对帖主发布内容进行反馈与评论，我比较喜欢这样的社交形式，于是应用到我的博客框架中；
 展厅：我是喜欢画画的人，于是想在我自己的博客中集中地展示我的作品，同时也有想分享一些文档，于是设计了展厅并进行了分区；
 社交软件按钮：既然是展示个人信息的博客，我也希望在其中可以跳转到我的一些账号上，所以设计了这个按钮；
```
### 时间线
#### 2.12（开始）
```text
使用Spring Initializr创建项目，选择JDK 17、Spring Web、Thymeleaf、JPA、H2;
编写HomeController，实现@GetMapping("/")返回index.html;
创建templates/index.html，页面显示“Hello Blog”;
```
#### 2.13（快速实现增删改查，增加社交帐号按钮）
```text
创建Blog实体（id, title, content, createdAt）（使用Lombok简化）；
创建BlogRepository接口，继承JpaRepository，添加findAllByOrderByCreatedAtDesc方法；
在主类中添加CommandLineRunner，启动时插入两条测试数据；
访问H2控制台，验证表和数据生成；
重构HomeController为BlogController，注入BlogRepository，实现列表和详情页；
创建list.html和detail.html，用Thymeleaf展示数据，添加404处理；
创建fragments/common.html，提取公共导航栏和页脚；
创建static/css/style.css（AI生成），统一页面样式；
在首页添加GitHub和B站社交按钮；
```
#### 2.14（实现管理员功能，切换数据库为MySQL）
```text
创建BlogService接口和BlogServiceImpl实现类，引入分层架构；
创建AdminController，实现博客发布、编辑、删除功能（含表单页面）；
在博客列表页添加“编辑”“删除”按钮，实现删除确认弹窗；
创建admin/login.html登录页面，实现硬编码登录验证（用户名密码）；
创建LoginInterceptor拦截器，保护/admin/**路径，排除登录页；
在WebConfig中配置拦截器，并在BlogController中根据Session显示管理按钮；
在导航栏添加登录/登出链接，根据登录状态动态显示；
创建blog数据库，修改application.properties切换数据库；
删除CommandLineRunner自动插入数据（避免重复）；
```
#### 2.15（改为Mybatis，实现分页查询，实现模糊查询）
```text
移除JPA依赖，添加Mybatis Starter，配置Mybatis（mapper-locations、驼峰映射）；
创建BlogMapper接口和BlogMapper.xml，手写SQL实现CRUD；
修改Blog实体，删除JPA注解；
实现分页查询：selectPage和countAll，创建PageResult类；
修改Controller接收page参数，返回分页结果，并在list.html添加分页导航；
实现模糊搜索：在Mapper中添加searchBlogs和countSearch方法；
```
#### 2.16（实现图片上传并展示，初步实现留言板）
```text
创建ImageUploadController，处理图片上传，保存到uploads/目录；
配置静态资源映射，使上传的图片可访问；
在admin/new.html和edit.html添加图片上传按钮；
创建Comment实体、CommentMapper、CommentService；
在博客详情页显示留言列表，添加发表留言表单；
修复留言blog_id为null的错误（忘记set）；
修复createdAt字段名拼写错误（createAt→createdAt）；
```
#### 2.17（完善留言板功能，创建展厅）
```text
创建ArtWork实体、ArtWorkMapper、ArtWorkService；
创建GalleryController，提供展厅列表、上传、编辑、删除接口；
创建gallery.html和gallery-form.html前端页面；
实现作品分页、分类切换（绘画/笔记）、文件下载；
在留言列表添加管理员删除按钮，实现删除接口；
```
#### 2.18（前后端分离）
```text
新建Vue 3 + Vite项目，安装vue-router、axios、marked；
创建api目录，封装request.js和博客、留言、展厅、登录等API；
创建HomeView.vue，对接后端/api/blogs接口，实现列表、分页、搜索；
创建DetailView.vue，对接详情和留言接口，添加Markdown渲染（使用marked）；
创建LoginView.vue，实现登录，保存sessionStorage；
创建AdminNewView.vue、AdminEditView.vue，对接发布和编辑；
创建GalleryView.vue和GalleryForm.vue，对接展厅功能；
配置路由和路由守卫，保护后台页面；
在App.vue添加全局导航栏，动态显示登录状态；
配置Vite代理解决跨域，修复图片显示问题；
修复大量Vue语法错误（导入、变量名冲突等），处理v-html样式穿透（:deep()）；
```
#### 2.19（环境变量处理，删除混合开发模式下的代码）
```text
将所有敏感信息（数据库密码、管理员账号）改为从环境变量读取；
修改login方法，使用ADMIN_USERNAME和ADMIN_PASSWORD环境变量；
在IDEA中配置环境变量，确保本地运行正常；
修复退出登录后编辑/删除按钮仍显示的问题（强制刷新页面）；
删除混合模式遗留的Controller、Thymeleaf模板、JPA相关配置；
清理application.properties中的无用配置；
删除无用的测试图片文件；
```
#### 2.20（写README）
```text
编写前后端README.md
前后端代码分别推送到GitHub
将代码部署到云服务器，购买域名
```
#### 2.23（深入学习项目，引入AOP思维优化后端）
```text
引入AOP思想，对后端通用逻辑进行横向抽取；
```
#### 2.24（深入学习项目，全栈适配）
```text
引入 Flyway 迁移框架；
引入 ${user.dir} 动态路径;
```
#### 2.25(深入学习项目，完善后端优化)
```text
完善后端逻辑的横向抽取（ApiGalleryController之前忘记处理了）;
```
#### 2.26（深入学习项目，抽离前端 Origin）
```text
代码中的CORS_ORIGIN彻底抽离至配置文件;
更新README使用教程；
```

## 作者
三碳化合物 (C3-Sharon)

## 许可证
MIT
