FROM node:23-alpine AS nodebuilder

WORKDIR /tailwind
COPY package.json package-lock.json ./
RUN npm install
COPY src/main/resources/static/css ./css
RUN npx tailwindcss -i css/input.css -o css/styles.css



FROM eclipse-temurin:23-jdk AS builder

LABEL name="readingcompanion"

ARG COMPILE_DIR=/compiledir

WORKDIR ${COMPILE_DIR}

# Copy Maven wrapper and project files
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# Install dos2unix, ensure `mvnw` has executable permissions and build the project
RUN chmod +x ./mvnw
RUN ./mvnw package -Dmaven.test.skip=true



FROM openjdk:23-jdk-oracle

ARG WORKDIR=/app

WORKDIR ${WORKDIR}

COPY --from=builder /compiledir/target/readingcompanion-0.0.1-SNAPSHOT.jar app.jar
COPY --from=nodebuilder /tailwind/css/styles.css /app/css/styles.css

ENV SERVER_PORT=8080
# ENV PUBLISHSERVER_URL=https://publishing-production-d35a.up.railway.app

EXPOSE ${SERVER_PORT}

# HEALTHCHECK --interval=60s --timeout=5s --start-period=120s \
#     CMD curl -s -f http://localhost:${SERVER_PORT}/status || exit 1

ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar app.jar