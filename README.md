## Spring cloud stream with kafka

In this example we are going to stop and start a consumer:

- kafka
- zookeeper
- schema registry

Start Kafka:

```shell script
docker-compose up -d
```

Without docker-compose

```shell script
 docker run --rm -p 2181:2181 -p 3030:3030 -p 8081-8083:8081-8083 -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=127.0.0.1  lensesio/fast-data-dev:2.6
```

launch command

```shell script
mvn clean install
```