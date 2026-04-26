# Installation Guide

Follow these steps to set up and run the Placement Readiness Tracker locally.

---

## 1 Prerequisites

Ensure the following are installed:

* Java JDK 17 or higher
* Maven (or use included Maven Wrapper)
* Git (optional)
* IDE (IntelliJ IDEA / Eclipse recommended)

---

## 2️ Get the Project

### Clone Repository

```bash
git clone https://github.com/your-username/placement-readiness-tracker.git
cd placement-readiness-tracker
```


## 3️ Understand Included Build Tools

This project includes:

* `pom.xml` → Maven dependencies
* `mvnw` / `mvnw.cmd` → Maven wrapper (no need to install Maven globally)

 You can run the project using `./mvnw` instead of `mvn`.

---

## 4️ Configure Database

### Default: H2 (No setup required)

* Runs automatically
* Suitable for testing/demo

### Optional: MySQL

Edit:

```
src/main/resources/application.properties
```

Update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=youe_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

## 5️ Build the Project

### Using Maven:

```bash
mvn clean install
```

### Using Maven Wrapper:

```bash
./mvnw clean install
```

---

## 6️ Run the Application

### Option A: Maven

```bash
mvn spring-boot:run
```

### Option B: Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Option C: IDE

* Open project
* Run main Spring Boot class

---

## 7️⃣ Access the Application

Open browser:

```
http://localhost:8080
```

---

## 8️⃣ Using the App

* Register a new account
* Log in
* Add assessment scores
* View analytics and readiness

---

## 🛑 Troubleshooting

**Port already in use**

* Change port in `application.properties`:

```properties
server.port=8081
```

**Java version issues**

* Verify with:

```bash
java -version
```

**Build failures**

* Ensure dependencies download correctly
* Try:

```bash
mvn clean install -U
```

---

## You're All Set!

The project should now be running locally and ready to use.
