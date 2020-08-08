FROM adoptopenjdk/openjdk11

WORKDIR /app
COPY ./.mvn ./.mvn
COPY ./pom.xml ./
COPY ./mvnw ./mvnw
RUN ./mvnw dependency:go-offline

COPY src/ src/
#CMD ["./mvnw", "spring-boot:run","-Dmaven.test.skip=true"]