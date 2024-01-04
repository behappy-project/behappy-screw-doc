# 构建阶段
FROM centos:8 as build
WORKDIR /user/src/app
COPY prepare.sh CentOS-8-reg.repo ./
RUN sh prepare.sh
COPY . .
# 1.执行构建vue,2.执行构建maven
RUN source /etc/profile \
    && cd vue \
    && npm i --registry=http://registry.npm.taobao.org/ \
    && npm run build \
    && cd ../ \
    && mvn clean package -Pmaster -DskipTests
# 运行阶段
FROM openjdk:17.0.2 as runtime
WORKDIR /user/src/app
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ARG JAR_FILE=/user/src/app/screw/target/*.jar
ENV JAVA_OPTS="-Xms256m -Xmx256m -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -jar app.jar"]
