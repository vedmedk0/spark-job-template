# Run instructions

Requirements - docker, sbt
```shell

# run minio storage for files
docker run --name template-minio \
 -e MINIO_ACCESS_KEY=template_access -e MINIO_SECRET_KEY=template_secret -e MINIO_CONSOLE_ADDRESS=":34027"\
 -p 34027:34027 -p 9001:9000 -v "$(pwd)"/minio:/data -d docker.io/minio/minio:latest "server" "/data"
# for local run
sbt run

# for dockerized app consider next steps
# assemble fat jar for spark application. test-task-spark-app.jar should appear in the root directory
sbt assembly

# build docker image
docker build . -t spark-job-template

# run spark application in container
docker run -it --rm -e APP_S3_ENDPOINT="http://host.docker.internal:9001" -p 4040:4040 --name spark-job-template-app spark-job-template
```

run_dockerized.sh - script with all setup-run-teardown dockerized process
