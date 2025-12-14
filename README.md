# Sweet Shop Management System
1. A full-stack Sweet Shop Management System built using Spring Boot (Java) for the backend and React + Tailwind CSS for the frontend.
2. The application supports role-based authentication, inventory management, purchasing, and search functionality, following clean coding practices and test-driven development principles.

# Project Overview - This project simulates a real-world sweet shop where:

* Users can:
1. Register and log in
2. View available sweets
3. Search sweets by name, category, or price range
4. Purchase sweets (stock decreases automatically)

* Admins can:
1. Add new sweets
2. Update sweet details
3. Delete sweets
4. Restock sweets
5. Perform all user actions

* The system uses JWT-based authentication and role-based authorization to secure APIs.

# Tech Stack :-
1. Backend :- Java 17, Spring Boot, Spring Security (JWT Authentication), JPA / Hibernate, PostgreSQL, JUnit 5 & Mockito (Testing)
2. Frontend:- React (Vite), Tailwind CSS, Axios, React Router DOM
3. Tools :- Postman (API testing), Git & GitHub (Version control)

# Authentication & Authorization :- Token-based authentication using JWT
1. Roles supported: ROLE_USER, ROLE_ADMIN
2. Secured endpoints using: Spring Security filter chain, @PreAuthorize annotations

# API Endpoints

1. Auth
| Method | Endpoint             | Description           |
| ------ | -------------------- | --------------------- |
| POST   | `/api/auth/register` | Register new user     |
| POST   | `/api/auth/login`    | Login and receive JWT |

2. Sweets
| Method | Endpoint             | Access       |
| ------ | -------------------- | ------------ |
| POST   | `/api/sweets`        | Admin        |
| GET    | `/api/sweets`        | User + Admin |
| GET    | `/api/sweets/search` | User + Admin |
| PUT    | `/api/sweets/{id}`   | Admin        |
| DELETE | `/api/sweets/{id}`   | Admin        |

3. Inventory
| Method | Endpoint                    | Access |
| ------ | --------------------------- | ------ |
| POST   | `/api/sweets/{id}/purchase` | User   |
| POST   | `/api/sweets/{id}/restock`  | Admin  |

# Frontend Pages :-
1. /register → User/Admin registration
2. /login → Login page
3. / → Dashboard (User/Admin)
4. /admin → Admin Panel

# UI Features :-
1. Responsive design using Tailwind CSS
2. Conditional rendering based on role
3. Disabled purchase button when stock = 0
4. Search by: Name, Category, Min/Max price range

# Testing (TDD) :-
The backend was developed following Test-Driven Development practices.
Covered Tests - Sweet Service tests, Inventory purchase & restock tests, Exception handling cases
All tests pass successfully.

# How to Run Locally -
1. Backend :-
cd sweetshop
./mvnw spring-boot:run
Backend runs on: http://localhost:8080

2. Frontend :-
cd sweetshop-frontend
npm install
npm run dev
Frontend runs on: http://localhost:5173

3. Screenshots
Screenshots are available in the /screenshots folder showing:
Registration -> Login -> User Dashboard -> Admin Dashboard -> Add / Delete / Restock sweets -> Search results.

# My AI Usage
AI Tools Used :- ChatGPT
1. How AI Was Used -
   Writing unit test templates
   Debugging Spring Security & JWT issues
  Improving frontend component structure
2. Reflection
  AI significantly improved development speed, especially during:
  Security configuration
3. All generated code was reviewed, modified, and understood before final use.

# Author
Ronit Dalal
MCA | Java Full Stack Developer
dalalronit131@gmail.com
https://www.linkedin.com/in/ronit-dalal-4b4157267/
https://github.com/ronitdalal27
mob no - 8010166496.


