# betterthanebay
RESTful web service where users can post and bid on items

## How to set it up
1. Create a postgres database and set configuration parameters in `betterthanebay.yml`
1. Run `mvn clean package` to compile and create JAR
1. Run `java -jar target/betterthanebay-1.0-SNAPSHOT.jar db migrate betterthanebay.yml` to migrate database
1. Run `java -jar target/betterthanebay-1.0-SNAPSHOT.jar server betterthanebay.yml` to start the server
