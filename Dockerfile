FROM maven:3.6.3-jdk-8
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean compile assembly:single
CMD ["java", "-jar", "target/Solutions-1.0.jar"]