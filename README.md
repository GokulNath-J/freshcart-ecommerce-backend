## FreshCart — E-Commerce Backend

A Spring Boot backend for a grocery-style marketplace where sellers list products, users pay from a wallet, and prices drop automatically as products approach their expiry date.

## Features
- JWT authentication with role-based access (USER, SELLER, ADMIN) via @PreAuthorize
- Wallet system: add money, pay for orders
- Three-step checkout: view price and discount → create order with the final price → pay
- Automatic expiry discounts and manual seller discounts
- Expired product archiving into a separate table (nothing is hard-deleted)
- Pagination on product search
- Global exception handling with consistent JSON errors (@RestControllerAdvice)
- Database indexing on product category
- Spring Boot Actuator health endpoint
- Dockerized with Docker Compose, health checks and memory limits
- Jenkins CI: a GitHub webhook triggers build and containerization on every push

## Tech stack

- Java, Spring Boot 4.1
- Spring Security, JWT (jjwt 0.12)
- MySQL 8, Spring Data JPA, Hibernate
- Redis
- Maven
- Docker, Docker Compose 
- Jenkins
- AWS EC2 (free tier)

## Design decisions

- Why optimistic locking instead of pessimistic? Conflicts on the same product at the same instant are rare, so locking every read would cost more than it saves. @Version adds a version check to the UPDATE, and only the rare collision pays any price.

- Why cache products but not wallets? A slightly stale product page is harmless. A stale wallet balance could allow overdrafts. Redis serves browsing; the database is the source of truth for anything that moves money, and stock is always re-checked inside the transaction.

- Why a monolith? It runs in 1 GB of RAM on a free-tier instance, is simpler to test, and fits the project scope. A previous project covers microservices.

- Why soft-archive expired products? Past orders still reference them. Moving them to an ExpiredProducts table keeps history intact.

## API overview

- POST	/user/createNewuser	(Public) Register a user
- POST	/user/login	Public	-> Get a JWT
- POST	/user/addMoneyToWallet	(Authenticated) ->	Top up wallet
- GET	/user/getLoggedInUserDetails	(Authenticated) ->	Current user profile
- POST	/user/searchProductByCategory	(Authenticated) ->	Browse by category (paginated)
- POST	/user/searchProductByName	(Authenticated) ->	Search by name
- POST	/user/addSeller	(ADMIN) -> Promote a user to seller
- GET	/user/getAllUser	(ADMIN)	-> List users with their orders
- POST	/product/addProduct	(SELLER) ->	List a product
- PUT	/product/addDiscountToOneProduct	(SELLER) ->	Set a manual discount
- DELETE	/product/removeDiscount	(SELLER) ->	Remove a discount
- DELETE	/product/removeProduct	(SELLER) ->	Remove a product
- POST	/order/orderRequest	(Authenticated) -> Create an order
- POST	/payment/paymentRequest	(Authenticated) -> Pay and confirm
- GET	/actuator/health	(Public) ->	Health check

## Run it locally

- Prerequisites: Docker and Docker Compose, plus JDK 21 and Maven if you want to build the JAR yourself.

## Clone

- git clone https://github.com/GokulNath-J/freshcart-ecommerce-backend.git
- cd freshcart-ecommerce-backend

## Create a .env file (see .env.example; never commit this file)

- SPRING_DATASOURCE_USERNAME=
- SPRING_DATASOURCE_PASSWORD=
- SPRING_DATASOURCE_DATABASE=
- MYSQL_ROOT_PASSWORD=
- SECRET_KEY=

- Generate a strong JWT secret with openssl rand -base64 32.

## Build and start

- mvn clean package -DskipTests
- docker build -t ecommerce:0.1 .
- docker compose -f Docker-Compose.yml up -d

## Check it

- (PostMan) http://localhost:8081/actuator/health
- The app waits for MySQL's health check before starting, so the first boot takes a short while.

## Deployment on AWS EC2

- Login into your EC2 instance and run these commands, before this you need to built docker image locally and push into your docker hub and mention the docker hub user/image name
- git clone https://github.com/GokulNath-J/freshcart-ecommerce-backend.git
- cd freshcart-ecommerce-backend
- nano .env                      
- docker compose -f Docker-Compose.yml up -d

## check it

- (Postman) http://instance-public-ip:8081/actuator/health
  
## Roadmap
## Honest list of what is not done yet:
- Input validation with @Valid
- Unit tests (JUnit, Mockito) and integration tests
- Refresh tokens
- Rate limiting (Bucket4j)
- Async notifications (@Async)
- Automated EC2 deployment from Jenkins
