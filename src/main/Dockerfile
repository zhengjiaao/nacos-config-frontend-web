FROM java:8-jre
ENV NACOS_SERVER 127.0.0.1:8848
ADD *.war /app.jar
ENTRYPOINT [ "sh", "-c", "java -Duser.timezone=GMT+08 -jar /app.jar" ]