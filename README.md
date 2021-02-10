# fire - person apis

This is a sample RESTful project which provides a service for storing, updating,
retrieving and deleting Person entities.
This implementation focuses on the following aspects:
* API implementation with API first approach - Swagger
* Logging with Aspects Oriented Programming fundamentals
* Custom Error Handling using Controller Advices
* Use of Mappers for POJOs e.g. MapStruct
* Implementation of Lombok
* Security with Basic Authentication
* Test Cases with Mockito
* Integration of Docker - create a docker image of your application

## How to build the application

There are 2 ways to build the project and create the executable jar:

* Run the below command to build the project which would generate the required Controllers, POJOs and mapper classes.
```bash
mvn clean install
````
OR 

* For generating the docker image as well, run the below command
```bash
mvn clean install -Ddocker
```
#### Note: 
Ensure that you have docker installed and running on your machine in order to create the docker image.
After successful build, you can check the docker image built using the command :

    docker images

## How to run the application

There are 2 ways to run the application:

* Run the below command after you have successfully built the jar

    
    java -jar fire-1.0.jar

OR 

* If you wish to run the app in a docker container, make sure you have created the docker image using the above mentioned build command (2nd method). Then, run the below command:

    
    docker run -p 8080:8080 fire
    
    
## How to access the APIs

There is a postman collection at the root directory in the repository called "personAPIs.postman_collection.json"

[personAPIs.postman_collection.json](https://github.com/VivekThusu/fire/blob/master/personAPIs.postman_collection.json)    

Import the collection in postman and it has all the implemented APIs.

In either case, the application will run on localhost and port: 8080

