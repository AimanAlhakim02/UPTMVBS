# VENUE BOOKING SYSTEM
UPTM VBS is a Spring Boot application with CRUD operations and authentication-based login using Spring Security. It uses MySQL as database and Thymeleaf as templating engine.

## First steps
1. Create new database, new user and grant privileges or use root user.

2. Change src/main/resources/application.properties file as necesary.

3. Import uptmvbs.sql (Optional step, can be skipped but you'd have to create new index).

4. Run LibraryAppApplication

### Create new index
1. If you didn't import library.sql, initially run LibraryAppApplication.

2. Once the database is populated with tables, stop LibraryAppApplication.

3. Run the following query on database to create a new index for table books.

```ALTER TABLE books ADD FULLTEXT full_text_index (isbn, title, genre);```

4. Run LibraryAppApplication.
