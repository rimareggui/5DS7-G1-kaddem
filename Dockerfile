FROM openjdk:11-jre-slim
EXPOSE 8089
ADD target/khaddem-4.0.jar khaddem-4.0.jar
ENTRYPOINT ["java","-jar","/khaddem-4.0.jar"]