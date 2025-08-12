FROM azul/zulu-openjdk-alpine:17

# Set the working directory inside the container
WORKDIR /app

#copy the jar into container
COPY build/libs/couponHub-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

# run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
