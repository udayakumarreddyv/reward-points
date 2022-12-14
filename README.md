# Charter Reward Points API

A Spring Boot REST API service that calculates reward points for customer transactions based on spending amounts. The service provides endpoints to retrieve monthly and total reward points for customers.

## Technology Stack

- Java 8
- Spring Boot 2.7.1
- Spring Security (Basic Auth)
- Log4j2 for logging
- Docker for containerization
- OpenAPI/Swagger for API documentation
- Spring Actuator for health monitoring

## Architecture

The application follows a typical Spring Boot layered architecture. You can view the detailed architecture diagram [here](docs/architecture.mmd).

![Architecture Diagram](docs/architecture.mmd)

## Reward Points Calculation

The system calculates reward points based on transaction amounts:
- 1 point for every dollar spent over $50
- 1 additional point for every dollar spent over $100

Example: A $120 purchase = 90 points
- $50 to $100 = 50 points
- $100 to $120 = 40 points (2x points)
- Total = 90 points

## Configuration

The reward points thresholds are currently configured in the codebase:

|Amount ($)|Points per $|
|:---------|:----------|
|50|1|
|100|1|

Note: These configurations can be moved to a database for dynamic updates without redeployment.

## Sample Transaction Data

The system comes pre-configured with sample transaction data:

|Customer ID|Customer Name|Transaction Date|Amount ($)|
|:---------|:------------|:---------------|:---------|
|1|Joe|10-10-2022|123|
|1|Joe|10-26-2022|52|
|1|Joe|11-10-2022|35|
|1|Joe|11-14-2022|234|
|1|Joe|12-04-2022|74|
|2|Smith|10-10-2022|74|
|2|Smith|11-11-2022|234|
|2|Smith|12-01-2022|78|
|2|Smith|12-03-2022|123|

## API Endpoints

### Reward Points API
- GET `/rewardpoints/customerId/{customerId}`
  - Returns monthly and total reward points for a customer
  - Requires Basic Authentication
  - Response includes:
    - Customer ID
    - Customer Name
    - Monthly Points Breakdown
    - Total Points

### Documentation and Health
- Swagger UI: http://localhost:8080/swagger-ui-custom.html
- Health Check: http://localhost:8080/actuator/health

## Security

The API is secured using Basic Authentication:
- All endpoints except health check and Swagger UI require authentication
- Credentials are configured in application.properties
  - Username: user
  - Password: password
  
Note: For production deployment, credentials should be stored in a secure configuration store (e.g., AWS Parameter Store, HashiCorp Vault).

## Running the Application

### Prerequisites
- Java 8 or higher
- Maven
- Docker

### Local Development
```bash
mvn spring-boot:run
```

### Docker Deployment

#### Windows
1. Start: `start.bat`
2. Stop: `stop.bat`

#### Mac/Linux
1. Start: `./start.sh`
2. Stop: `./stop.sh`

The start script performs the following:
1. Builds the project: `mvn clean install -DskipTests`
2. Stops any running containers: `docker-compose down`
3. Builds the Docker image: `docker-compose build`
4. Starts the application: `docker-compose up`

## Testing

The project includes JUnit tests in `RewardPointsApplicationTests.java`. Run tests using:
```bash
mvn test
```


