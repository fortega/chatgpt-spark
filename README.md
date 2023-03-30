# ChatGPT Completion using Apache Spark

## In action

https://asciinema.org/a/ivJZlhEylqYChMSexHOMMuPgL

## How to run

### Create jar

> sbt package

### Submit to spark

> spark-submit --packages "com.eed3si9n:gigahorse-apache-http_2.12:0.7.0" target/scala-2.12/*.jar ${API_KEY} questions.csv completion.csv

### Show output

> cat completion.csv/*