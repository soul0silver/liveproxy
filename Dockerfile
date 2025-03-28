FROM openjdk:17
MAINTAINER wwproxy
COPY target/payos-0.0.1-SNAPSHOT.jar /home/payos-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/home/payos-0.0.1-SNAPSHOT.jar"]