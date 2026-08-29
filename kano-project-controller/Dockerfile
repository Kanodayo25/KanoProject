# =====================================================
# kano-project-controller 镜像（部署于 Ubuntu 服务器）
# 构建（在 kano-project-controller 目录下执行）：
#   docker build -t kano-controller:1.0.0 .
# 运行：
#   docker run -d --name kano-controller --network=host --restart=always kano-controller:1.0.0
# =====================================================

# JRE 17 + Ubuntu 22.04 (jammy)：openjdk 官方镜像已停止维护，
# alpine 使用 musl libc，与部分原生库存在兼容问题，故改用 Ubuntu 系镜像
FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="Kano"

# 时区对齐北京时间，日志时间戳不再差 8 小时
ENV TZ=Asia/Shanghai

# JVM 参数：按容器内存上限自适应堆大小（可用 docker run -e JAVA_OPTS=... 覆盖）
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -Dfile.encoding=UTF-8"

# 创建非 root 用户，以最小权限运行。
# useradd -r 是系统账号，默认不建家目录；而 Dubbo 启动时会写 ~/.dubbo/ 元数据缓存，
# 没有 /home/kano（/home 归 root 所有，kano 无权创建）就会抛
# "Failed to create directory /home/kano/.dubbo!"，所以这里手动建好家目录
RUN groupadd -r kano && useradd -r -g kano kano \
    && mkdir -p /home/kano \
    && chown kano:kano /home/kano

# 拷贝打包好的 jar（先本地 mvn package，或直接把 jar 上传到 target/ 目录）
ARG JAR_FILE=kano-project-controller-1.0.0-SNAPSHOT.jar
COPY --chown=kano:kano ${JAR_FILE} /app/kano-controller.jar

WORKDIR /app
# /app 由 WORKDIR 以 root 创建，改为 kano 所有，否则日志文件 controller.log 写不进去
RUN chown kano:kano /app

# 应用端口（application.properties 中 server.port=8099，旧版 8081 是错的）
EXPOSE 8099

# 健康检查：TCP 探测应用端口（Spring Boot 启动慢，start-period 放宽到 120s）
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=3 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8099'

USER kano

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/kano-controller.jar"]
