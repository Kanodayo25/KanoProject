# 基础镜像
FROM openjdk:17-jdk-alpine

#作者信息
LABEL maintainer='Kano'

# 拷贝打包后的jar文件到镜像中
COPY target/kano-project-provider-provider-1.0.0-SNAPSHOT.jar kano-provider.jar

# 设置工作目录
WORKDIR /app

# 暴露应用端口
EXPOSE 8081

# 启动Spring Boot应用
CMD ["java", "-jar", "kano-provider.jar"]