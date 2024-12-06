# Tahap build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml dan download dependencies Maven agar dapat menggunakan cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code dan build aplikasi
COPY src ./src
RUN mvn clean package -DskipTests

# Tahap runtime
FROM openjdk:17-alpine
WORKDIR /app

# Copy jar file hasil build ke container runtime
COPY --from=build /app/target/jobConnector-0.0.1-SNAPSHOT.jar app.jar

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]
