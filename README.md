# restaurant-service

Restaurant Profile Service for the food delivery platform. Manages restaurant data including menus, opening hours, and availability for customer ordering.

## Tech Stack

- **Framework:** Spring Boot 3.2 (Java 21)
- **Database:** PostgreSQL (Spring Data JPA)
- **GraphQL:** Spring for GraphQL
- **API Docs:** Swagger UI (springdoc-openapi)
- **Auth:** Keycloak (JWT via API Gateway) + shared RBAC library
- **CI/CD:** GitHub Actions → GHCR

## API

The service exposes both REST and GraphQL endpoints. REST is used for standard CRUD operations, while GraphQL allows the frontend to request exactly the fields it needs — reducing over-fetching.

### REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/restaurant` | List all restaurants |
| GET | `/api/v1/restaurant/{id}` | Get restaurant by ID |
| GET | `/api/v1/restaurant/name/{name}` | Get restaurant by name |
| GET | `/api/v1/restaurant/available/{isAvailable}` | Get restaurants by availability |
| GET | `/api/v1/restaurant/open/{isOpen}` | Get restaurants by open status |
| GET | `/api/v1/restaurant/location/{location}` | Get restaurants by location |
| GET | `/api/v1/restaurant/paged` | List restaurants with pagination (`?page=0&size=10`) |
| GET | `/api/v1/restaurant/paged/available/{isAvailable}` | Paginated restaurants by availability |
| GET | `/api/v1/restaurant/paged/open/{isOpen}` | Paginated restaurants by open status |
| GET | `/api/v1/restaurant/search` | Search restaurants by query (`?query=pizza`) |
| POST | `/api/v1/restaurant/add` | Create restaurant |
| PUT | `/api/v1/restaurant/update/{id}` | Update restaurant |
| DELETE | `/api/v1/restaurant/delete/{id}` | Delete restaurant |
| GET | `/api/v1/menu-item` | List all menu items |
| GET | `/api/v1/menu-item/{id}` | Get menu item by ID |
| GET | `/api/v1/menu-item/restaurant/{restaurantId}` | Get menu items by restaurant |
| POST | `/api/v1/menu-item/add` | Create menu item |
| PUT | `/api/v1/menu-item/update/{id}` | Update menu item |
| DELETE | `/api/v1/menu-item/delete/{id}` | Delete menu item |

### Error Handling

The service returns standard HTTP status codes:

| Status | Meaning |
|--------|---------|
| `200` | Success |
| `400` | Invalid input (e.g. missing name, invalid email) |
| `404` | Resource not found |
| `409` | Conflict (e.g. duplicate email) |
| `500` | Unexpected server error |

### GraphQL

Available at `/graphiql` (includes interactive GraphiQL playground).

**Queries:** `getAllRestaurants`, `getRestaurantById(restaurantId)`, `getRestaurantByName(name)`, `getRestaurantsByAvailability(isAvailable)`, `getRestaurantsByOpenStatus(isOpen)`, `getMenuItemsByRestaurantId(restaurantId)`, `getMenuItemById(menuItemId)`

**Mutations:** `addRestaurant(input)`, `updateRestaurant(restaurantId, input)`, `deleteRestaurant(restaurantId)`, `addMenuItem(input)`, `updateMenuItem(menuItemId, input)`, `deleteMenuItem(menuItemId)`

Example query:

```graphql
query {
  getAllRestaurants {
    restaurantId
    name
    address
    isOpen
    isAvailable
  }
}
```

## Project Structure

```
restaurant-service/
├── src/
│   ├── main/
│   │   ├── java/com/dls/restaurantservice/
│   │   │   ├── Configuration/
│   │   │   │   ├── CorsConfig.java             # CORS configuration
│   │   │   │   ├── SecurityConfig.java         # Security configuration
│   │   │   │   └── GlobalExceptionHandler.java # Global error handling
│   │   │   ├── Controller/
│   │   │   │   ├── RestaurantController.java   # REST CRUD endpoints
│   │   │   │   └── MenuItemController.java     # REST CRUD endpoints
│   │   │   ├── DTO/
│   │   │   │   ├── RestaurantRequest.java
│   │   │   │   ├── RestaurantResponse.java
│   │   │   │   ├── MenuItemRequest.java
│   │   │   │   ├── MenuItemResponse.java
│   │   │   │   └── PageResponse.java           # Pagination wrapper
│   │   │   ├── Entity/
│   │   │   │   ├── Restaurant.java             # Database entity
│   │   │   │   └── MenuItem.java               # Database entity
│   │   │   ├── GraphQL/
│   │   │   │   └── RestaurantResolver.java     # GraphQL queries and mutations
│   │   │   ├── Repository/
│   │   │   │   ├── RestaurantRepository.java
│   │   │   │   └── MenuItemRepository.java
│   │   │   ├── Service/
│   │   │   │   ├── RestaurantService.java
│   │   │   │   └── MenuItemService.java
│   │   │   └── RestaurantServiceApplication.java
│   │   └── resources/
│   │       ├── graphql/
│   │       │   └── schema.graphqls             # GraphQL schema definition
│   │       └── application.properties
│   └── test/
│       ├── java/com/dls/restaurantservice/
│       │   ├── RestaurantControllerTest.java
│       │   ├── RestaurantGraphQLTest.java
│       │   └── MenuItemControllerTest.java
│       └── resources/
│           └── application.properties          # H2 in-memory test database
├── docker-compose.yaml                         # Local dev: PostgreSQL + service
├── Dockerfile                                  # Multi-stage production build
└── pom.xml                                     # Dependencies and versioning
```

## Development

```bash
./mvnw spring-boot:run
```

Requires a running PostgreSQL instance. See Docker Compose below for an easier setup.

## Run with Docker Compose

Starts PostgreSQL and the service together:

```bash
docker compose up --build
```

- REST API: http://localhost:8002/api/restaurant
- Swagger UI: http://localhost:8002/swagger-ui.html
- API docs (JSON): http://localhost:8002/api-docs
- GraphQL playground: http://localhost:8002/graphiql
- Health check: http://localhost:8002/actuator/health

## Run Tests

Tests use H2 in-memory database — no database setup needed:

```bash
./mvnw test
```

## CI/CD

Automated via GitHub Actions:

- **On PR to main:** Run tests and verify version bump in `pom.xml`
- **On merge to main:** Docker image built and pushed to `ghcr.io/dls-soft2/restaurant-service:<version>`