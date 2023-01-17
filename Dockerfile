FROM maven:3.8-jdk-11
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests
CMD ["java", "-jar", "target/Solutions-1.0.jar"]