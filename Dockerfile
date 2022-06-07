# 构建阶段
FROM centos:7 as build
WORKDIR /user/src/app
COPY buildProject.sh .
RUN sh buildProject.sh
COPY . .
# 1.执行构建vue,2.执行构建maven
RUN source /etc/profile \
    && mvn install:install-file -Dfile="lib/screw-core-1.0.6-SNAPSHOT.jar" -DgroupId=cn.smallbun.screw -DartifactId=screw-core -Dversion=1.0.6-SNAPSHOT -Dpackaging=jar \
    && cd vue \
    && npm i --registry=http://registry.npm.taobao.org/ \
    && npm run build \
    && cd ../ \
    && mvn clean package -Pmaster -DskipTests
# 运行阶段
FROM openjdk:11 as runtime
WORKDIR /user/src/app
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ARG JAR_FILE=/user/src/app/screw/target/*.jar
ENV JAVA_OPTS="-Xms128m -Xmx256m -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["sh","-c","java -jar app.jar ${JAVA_OPTS}"]