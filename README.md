# APA_Project

A Java web application project built with Jakarta EE, Jersey (JAX-RS), and JPA.

## Technologies Used

- Java 21
- Jakarta Servlet API 6.0
- Jersey 3.1.6 (JAX-RS implementation)
- Eclipse Persistence (JPA)
- Maven
- JUnit 5.10.2

## Project Structure

```
APA_Project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/apa_project/
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml
│   │   └── webapp/
│   │       ├── index.jsp
│   │       └── WEB-INF/
│   │           └── web.xml
│   └── test/
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.x
- A Jakarta EE compatible application server (e.g., Apache Tomcat 10+, GlassFish, WildFly)

### Building the Project

```bash
mvnw clean package
```

Or on Windows:
```cmd
mvnw.cmd clean package
```

### Running the Application

1. Build the project using Maven
2. Deploy the generated WAR file from `target/` directory to your application server
3. Access the application at `http://localhost:8080/APA_Project/`

## License

This project is created for educational purposes.

