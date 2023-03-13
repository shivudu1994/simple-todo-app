FROM debian:stable-slim
RUN apt-get update && apt-get install -y openjdk-17-jdk maven
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests
CMD ["java", "-jar", "target/simple-todo-backend-0.0.1-SNAPSHOT.jar"]