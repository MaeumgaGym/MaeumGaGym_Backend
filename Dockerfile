FROM openjdk:17

ARG JAR_FILE=/maeumgagym-infrastructure/build/libs/*.jar
COPY ${JAR_FILE} application.jar

ARG PROFILE
ENV PROFILE ${PROFILE}

ARG DB_USERNAME
ENV DB_USERNAME ${DB_USERNAME}

ARG DB_PASSWORD
ENV DB_PASSWORD ${DB_PASSWORD}

ARG DB_URL
ENV DB_URL ${DB_URL}

ARG JWT_SECRET_KEY
ENV JWT_SECRET_KEY ${JWT_SECRET_KEY}

ARG JWT_ACCESS_EXP
ENV JWT_ACCESS_EXP ${JWT_ACCESS_EXP}

ARG JWT_REFRESH_EXP
ENV JWT_REFRESH_EXP ${JWT_REFRESH_EXP}

ARG JWT_HEADER
ENV JWT_HEADER ${JWT_HEADER}

ARG JWT_PREFIX
ENV JWT_PREFIX ${JWT_PREFIX}

ARG GRANT_TYPE
ENV GRANT_TYPE ${GRANT_TYPE}

ARG CLIENT_ID
ENV CLIENT_ID ${CLIENT_ID}

ARG REDIS_HOST
ENV REDIS_HOST ${REDIS_HOST}

ARG REDIS_PORT
ENV REDIS_PORT ${REDIS_PORT}

ARG FILE_SERVER_SECRET_KEY
ENV FILE_SERVER_SECRET_KEY ${FILE_SERVER_SECRET_KEY}

ARG FILE_SERVER_URL
ENV FILE_SERVER_URL ${FILE_SERVER_URL}

ARG VIDEO_ID_SAVE_TTL
ENV VIDEO_ID_SAVE_TTL ${VIDEO_ID_SAVE_TTL}

ARG SUFFIX_PATH
ENV SUFFIX_PATH ${SUFFIX_PATH}

ARG SWAGGER_PATH
ENV SWAGGER_PATH ${SWAGGER_PATH}

ARG SWAGGER_UI_PATH
ENV SWAGGER_UI_PATH ${SWAGGER_UI_PATH}

ARG FRONT_LOCAL
ENV FRONT_LOCAL ${FRONT_LOCAL}

ARG BACK_LOCAL
ENV BACK_LOCAL ${BACK_LOCAL}

ARG BACK_DOMAIN
ENV BACK_DOMAIN ${BACK_DOMAIN}

ENV SPRING_PROFILES_ACTIVE ${PROFILE}

ARG CSRF_HEADER
ENV CSRF_HEADER ${CSRF_HEADER}

ARG CSRF_COOKIE
ENV CSRF_COOKIE ${CSRF_COOKIE}

ARG CSRF_PARAMETER
ENV CSRF_PARAMETER ${CSRF_PARAMETER}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
