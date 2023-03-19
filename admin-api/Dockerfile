FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} admin-tsp.jar
ENTRYPOINT ["java","-jar","/admin-tsp.jar"]
