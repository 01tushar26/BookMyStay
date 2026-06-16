#extract the image of jdk
# FROM eclipse-temurin:21-jdk-alpine
#
# WORKDIR /app
#
# COPY . .
#
# RUN chmod +x mvnw
#
# RUN ./mvnw clean package -DskipTests
#
# EXPOSE 8080
#
# ENV SPRING_PROFILES_ACTIVE=dev
# CMD ["java","-jar","target/hotelManagementsystem-AIRBNB-0.0.1-SNAPSHOT.jar"]


FROM eclipse-temurin:21-jdk-alpine
ADD target/server-0.0.1-SNAPSHOT.jar bookMyStay.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookMyStay.jar"]
