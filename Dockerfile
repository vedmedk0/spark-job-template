FROM bitnami/spark:3.2.1

COPY ./spark-job-template-app.jar /

USER root

WORKDIR /opt/bitnami/spark
ENTRYPOINT ./bin/spark-submit --class app.Start --conf "spark.jars.ivy=/tmp/.ivy" /spark-job-template-app.jar
