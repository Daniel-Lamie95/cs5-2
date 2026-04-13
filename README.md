# APA_Project

A Spring Boot web application using Thymeleaf templates.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring MVC
- Thymeleaf
- Maven
- JUnit 5 (via Spring Boot starter test)

## Project Structure

```
APA_Project/
├── src/
│   ├── main/
│   │   ├── java/com/example/apa_project/
│   │   │   ├── ApaProjectApplication.java
│   │   │   └── HomeController.java
│   │   └── resources/
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/example/apa_project/
│           └── ApaProjectApplicationTests.java
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21+

### Build

```powershell
.\mvnw.cmd clean package
```

### Run

```powershell
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080/` in your browser.

## Notes

- Legacy files under `src/main/webapp/` are not used by Thymeleaf.
- This project now runs with the embedded Spring Boot server (no external app server required).

