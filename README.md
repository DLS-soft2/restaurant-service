# restaurant-service

Restaurant Profile Service for the food delivery platform. Manages restaurant data including menus, opening hours, and availability for customer ordering.

## Tech Stack

- **Framework:** Spring Boot 3.2 (Java 21)
- **Database:** PostgreSQL (Spring Data JPA)
- **GraphQL:** Spring for GraphQL
- **Auth:** Keycloak (JWT via API Gateway) + shared RBAC library
- **CI/CD:** GitHub Actions → GHCR

## API

The service exposes both REST and GraphQL endpoints. REST is used for standard CRUD operations, while GraphQL allows the frontend to request exactly the fields it needs — reducing over-fetching.

### REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/restaurant` | List all restaurants |
| GET | `/api/restaurant/{id}` | Get restaurant by ID |
| GET | `/api/restaurant/name/{name}` | Get restaurant by name |
| GET | `/api/restaurant/available/{isAvailable}` | Get restaurants by availability |
| GET | `/api/restaurant/open/{isOpen}` | Get restaurants by open status |
| GET | `/api/restaurant/location/{location}` | Get restaurants by location |
| POST | `/api/restaurant` | Create restaurant |
| PUT | `/api/restaurant/{id}` | Update restaurant |
| DELETE | `/api/restaurant/{id}` | Delete restaurant |
| GET | `/api/menu-item` | List all menu items |
| GET | `/api/menu-item/{id}` | Get menu item by ID |
| GET | `/api/menu-item/restaurant/{restaurantId}` | Get menu items by restaurant |
| POST | `/api/menu-item` | Create menu item |
| PUT | `/api/menu-item/{id}` | Update menu item |
| DELETE | `/api/menu-item/{id}` | Delete menu item |

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
│   │   │   │   ├── CorsConfig.java         # CORS configuration
│   │   │   │   └── SecurityConfig.java     # Security configuration
│   │   │   ├── Controller/
│   │   │   │   ├── RestaurantController.java  # REST CRUD endpoints
│   │   │   │   └── MenuItemController.java    # REST CRUD endpoints
│   │   │   ├── DTO/
│   │   │   │   ├── RestaurantRequest.java
│   │   │   │   ├── RestaurantResponse.java
│   │   │   │   ├── MenuItemRequest.java
│   │   │   │   └── MenuItemResponse.java
│   │   │   ├── Entity/
│   │   │   │   ├── Restaurant.java         # Database entity
│   │   │   │   └── MenuItem.java           # Database entity
│   │   │   ├── GraphQL/
│   │   │   │   └── RestaurantResolver.java # GraphQL queries and mutations
│   │   │   ├── Repository/
│   │   │   │   ├── RestaurantRepository.java
│   │   │   │   └── MenuItemRepository.java
│   │   │   ├── Service/
│   │   │   │   ├── RestaurantService.java
│   │   │   │   └── MenuItemService.java
│   │   │   └── RestaurantServiceApplication.java
│   │   └── resources/
│   │       ├── graphql/
│   │       │   └── schema.graphqls         # GraphQL schema definition
│   │       └── application.properties
│   └── test/
│       ├── java/com/dls/restaurantservice/
│       └── resources/
│           └── application.properties      # H2 in-memory test database
├── docker-compose.yaml                     # Local dev: PostgreSQL + service
├── Dockerfile                              # Multi-stage production build
└── pom.xml                                 # Dependencies and versioning
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

- REST + Swagger UI: http://localhost:8002/swagger-ui.html
- GraphQL playground: http://localhost:8002/graphiql

## Run Tests

Tests use H2 in-memory database — no database setup needed:

```bash
./mvnw test
```

## CI/CD

Automated via GitHub Actions:

- **On PR to main:** Run tests and verify version bump in `pom.xml`
- **On merge to main:** Docker image built and pushed to `ghcr.io/dls-soft2/restaurant-service:<version>`
