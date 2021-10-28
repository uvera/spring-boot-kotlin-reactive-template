FROM bellsoft/liberica-openjdk-alpine-musl:11 as builder
WORKDIR application
ARG JAR_FILE
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM bellsoft/liberica-openjdk-alpine-musl:11
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["org.springframework.boot.loader.JarLauncher"]
