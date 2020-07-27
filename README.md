# Tech Challenge

### Build & Run
To start the application, navigate to the project directory and run `./gradlew bootRun`, or use your favourite IDE to start the application.
The root of the API is at `localhost:8080/api`.

I have added a test endpoint to easily setup some test data. You will find it at `/test/setup`.


### Technical choices
The project is built with **Java 11**.
I picked **Spring Boot** as the base framework to build the service, and used an in-memory **H2 DB** for a lightweight datastore.
Even though I have more experience with Dropwizard, I think Spring Boot is more lightweight and easier to get going with minimal configuration.
Plus it gives me a bit of practice on it, which is always a plus.

In terms of libraries, I used my regular go-to's which are fairly standard in the business:
* **Spring Data** to handle the data access layer
* **Lombok** to reduce boilerplate code
* **Guava** for helper methods
* **Mockito** & **AssertJ** for testing
* **Spring MockMvc** to test controller endpoints

I have used the standard Spring file structure for the most part, and built the application like a typical web service.


### Room for improvement
There is definitely a lot of room for improvement with this application.
I didn't spend time working on logging, and exception handling is very basic. This could definitely be improved for enhanced user feedback.
On the database side, I used basic integer sequential primary keys. This could be improved to use UUIDs with a little bit of work to store them efficiently, making the data model much more scalable especially on distributed databases.
The model around Match and Tournament could be made more generic to support different types of events and multi-event structures.
Testing could be improved as well, especially with some form of API contract testing, and more integration tests.