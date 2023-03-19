FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} front-tsp.jar
ENTRYPOINT ["java","-jar","/front-tsp.jar"]
