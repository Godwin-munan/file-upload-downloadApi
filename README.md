# Spring Boot WebFlux File Upload and Download API

This is a sample Spring Boot WebFlux project that demonstrates how to create a REST API for uploading and downloading files. Uploaded files are saved to the file system, and file information is stored in a MongoDB database.

## Prerequisites

- Java 17
- Maven
- MongoDB

## Getting Started

1. Clone this repository:

   ```sh
   git clone https://Godwin-munan/file-upload-downloadApi.git
2. Configure MongoDB connection setting in `src/main/resource/application.yaml`
   - spring:
        data:
          mongodb:
            database: filesdb
            host: localhost
            port: 27017

3. Build the project
    - mvnw clean package

4. Run the application
     - mvnw spring-boot:run

API Endpoints

Upload a File
. Endpoint: POST `/api/files`
. RequestPart @RequestPart("image")Mono<FilePart>

Download a File
.Endpoint: GET `/api/files/fileName`
.Path Variable @PathVariable String

Technology Stack
- Spring Boot
- Spring WebFlux
- MongoDB
- Reactive Programming

Project Structure
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── munan/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── FilesController.java
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── FileData.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── FilesRepository.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── FilesService.java
│   │   │   ├── resources/
│   │   │   │   ├── application.yaml
├── pom.xml
├── README.md
