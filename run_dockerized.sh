docker run --name template-minio \
 -e MINIO_ACCESS_KEY=template_access -e MINIO_SECRET_KEY=template_secret -e MINIO_CONSOLE_ADDRESS=":34027"\
 -p 34027:34027 -p 9001:9000 -v "$(pwd)"/minio:/data -d docker.io/minio/minio:latest "server" "/data" && \
sbt assembly && \
docker build . -t spark-job-template:latest && \
docker run -it --rm -e APP_S3_ENDPOINT="http://host.docker.internal:9001" -p 4040:4040 --name spark-job-template-app spark-job-template && \
docker stop template-minio && docker rm template-minio
