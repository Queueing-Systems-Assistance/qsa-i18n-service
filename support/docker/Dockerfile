FROM openjdk:11

USER root

EXPOSE 8080

ENV DIRPATH /qsa-storage/qsa-i18n-service
RUN mkdir -p $DIRPATH

COPY ./qsa-i18n-server/build/libs/qsa-i18n-service-final.jar $DIRPATH/qsa-i18n-service.jar
COPY ./support/docker/init.sh $DIRPATH/init.sh
RUN chmod 755 $DIRPATH/init.sh
WORKDIR $DIRPATH

HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "/qsa-storage/qsa-i18n-service/init.sh"]