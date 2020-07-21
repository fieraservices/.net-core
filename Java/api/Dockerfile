FROM openjdk:14
LABEL  description="fieraRestAPI"
EXPOSE 8080
ADD target/api-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
