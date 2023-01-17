FROM maven:3.8.3-openjdk-17
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean compile assembly:single
CMD ["java", "-jar", "target/Solutions-1.0.jar"]