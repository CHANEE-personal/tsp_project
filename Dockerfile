FROM openjdk:11
ARG JAR_FILE=build/libs/new_tsp_front-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} new_tsp_front-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/new_tsp_front-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
