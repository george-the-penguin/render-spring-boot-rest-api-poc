# render-spring-boot-rest-api-poc

This is a simple Spring Boot REST API Proof of Concept (POC) project.

The goal of this project is to demonstrate how to create a REST API using Spring Boot, and it can run properly in the 
Render ecosystem.

The idea to create this project arises in response to several questions asked by participants of the **Devs Latam** 
initiative from the **TryCatch** development community.

## Technologies

The following is the list of technologies used to build this project:

- [Java 17](https://openjdk.org/projects/jdk/17/)
- [Spring Boot 3.0.6](https://spring.io/)
- [Apache Maven 3.9.1](https://maven.apache.org/)
- [Lombok 1.18.26](https://projectlombok.org/)
- [OpenAPI 3.0.3](https://www.openapis.org/)
- [PostgreSQL 15](https://www.postgresql.org/)
- [Docker 20.10.24](https://www.docker.com/)
- [Render](https://render.com/)

## Create a Render's PostgreSQL database

To create a PostgreSQL database in Render, you need to follow the following steps:

1. Go to the [Render's dashboard](https://dashboard.render.com/).
2. Click on the **New+** button, and then click on the **PostgreSQL** option.
3. In the **New PostgreSQL** form, you need to fill the following fields:
   - **Name**: The name of the database resource in Render.
   - **Database**: The name of the database instance.
   - **User**: The username of the database.
   - **Region**: The region where the database will be created.
   - **PostgreSQL Version**: The PostgreSQL version. I recommend using the latest version.
   - **Instance Type**: The instance type. I recommend using the **Free** instance type for academic purposes.
4. Click on the **Create Database** button.

## How to build

To build this project, you need to have the following installed on your machine:

- Java 17
- Apache Maven 3
- Docker 20

To build the project, you need to run the following command:

```bash
mvn clean package
```

_NOTE: This command must be executed in the root directory of the project._

To build the Docker image, you need to run the following command:

```bash
docker build -t render-spring-boot-rest-api-poc .
```

_NOTE: This command must be executed in the root directory of the project._

## How to run

To run the Docker container, you need to run the following command:

```bash
docker run -e DB_URL="DB_URL" -e DB_USER=DB_USER -e DB_PASSWD=DB_PASSWD -p 8080:8080 render-spring-boot-rest-api-poc
```

Where:
- `DB_URL` is the URL of the PostgreSQL database.
- `DB_USER` is the user of the PostgreSQL database.
- `DB_PASSWD` is the password of the PostgreSQL database.

These data must be provided as environment variables to the Docker container, and they can be obtained from the 
Render's database resource, within the **Connections** section.

Example:

```bash
docker run -e DB_URL="jdbc:postgresql://postgres.render.com:5432/render-spring-boot-rest-api-poc" -e DB_USER=postgres -e DB_PASSWD=postgres -p 8080:8080 render-spring-boot-rest-api-poc
```

## How to deploy on Render

To deploy this project on Render, you need to follow the following steps:

1. Go to the [Render's dashboard](https://dashboard.render.com/).
2. Click on the **New+** button, and then click on the **Web Service** option.
3. In the **Create a new Web Service** form, you need to fill the following fields:
   - **Connect a repository**: Select the repository where the project is located. Click on the **Connect** button to continue.
4. In the next form, you need to fill the following fields:
   - **Name**: The name of the web service.
   - **Region**: The region where the web service will be created.
   - **Branch**: The branch of the repository to deploy.
   - **Runtime**: The runtime of the web service. For this case, you need to select the **Docker** option.
   - **Instance Type**: The instance type. I recommend using the **Free** instance type for academic purposes.
   - In the **Advanced** section, click the **Add Environment Variable** button, and then add the following environment variables:
     - **PORT**: The port where the web service will be listening. For this case, you need to use the **8080** value. 
     - **DB_URL**: The URL of the PostgreSQL database.
     - **DB_USER**: The user of the PostgreSQL database.
     - **DB_PASSWD**: The password of the PostgreSQL database.
5. Click the **Create Web Service** button.

## How to use

To use this project, you need to follow the following steps:

1. Use Postman or any other tool to make HTTP requests. You can find the OpenAPI specification of this project in the 
   `/v3/api-docs` endpoint of the web service.
2. You can use the Swagger UI to make HTTP requests. You can find the Swagger UI in the `/swagger-ui/index.html` endpoint of 
   the web service.

## Author

- **Jorge Garcia** - George the Penguin
  - [Website](https://georgethepenguin.dev/)
  - [GitHub](https://github.com/george-the-penguin)
  - [Twitter](https://twitter.com/mrgeorgepenguin)
  - [Instagram](https://www.instagram.com/mrgeorgepenguin/)
  - [TikTok](https://www.tiktok.com/@george_the_penguin)
  - [YouTube](https://www.youtube.com/@GeorgeThePenguin)
  - [Twitch](https://www.twitch.tv/george_the_penguin)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details

## Acknowledgments

- **TryCatch** - [Social Media Links](https://linktr.ee/trycatch.tv)

## Disclaimer

This project is not affiliated, associated, authorized, endorsed by, or in any way officially connected with Render, 
or any of its subsidiaries or its affiliates. The official Render website can be found at https://render.com. 
The name "Render" as well as related names, marks, emblems and images are registered trademarks 
of their respective owners.
