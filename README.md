# Fadata Code Challenge

A small Spring Boot REST API for managing **items**. You can use either **Gradle** or **Maven** to build and run the project.

## Requirements

- **Java 21**
- **Gradle** (e.g. use the included wrapper) **or** **Maven** (3.6+)

## Build and run

Choose one of the two options below.

### Option A: Gradle

```bash
# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

### Option B: Maven

```bash
# Run the application
./mvnw spring-boot:run
# or, if Maven is installed: mvn spring-boot:run

# Run tests
./mvnw test
# or: mvn test
```

If you don't have the Maven Wrapper (`mvnw`), generate it with:

```bash
mvn -N wrapper:wrapper
```

---

## What we expect from you

1. **Make all tests pass**  
   The project contains unit and integration tests. Your implementation must satisfy the behaviour described below so that every test passes.

2. **Keep the same API contract**  
   Do not change the HTTP endpoints, request/response shapes, or query parameters described in the **API behaviour** section. You may refactor internals (e.g. request DTO field names for JSON binding) as long as the external behaviour matches the tests.

3. **Optional improvements**  
   You may add validation, error handling, or small improvements if you like, as long as existing tests still pass and the API contract is preserved.

---

## API behaviour (test cases and expectations)

The API exposes two operations. The integration tests define the exact contract.

### 1. Create an item

- **Endpoint:** `POST /api/items`
- **Request body (JSON):** an object with a **`name`** field (the item’s name/title).  
  Example: `{"name": "My Item"}`
- **Expected response:**
  - Status: **201 Created**
  - Body: JSON object with **`id`** (non-null), **`name`**, and **`createdAt`** (ISO-8601 instant).

The integration tests send `"name"` in the JSON body and assert that the response contains the same name and a non-null id.

### 2. List items

- **Endpoint:** `GET /api/items`
- **Query parameter (optional):** **`sort`**
  - No param or unknown value: order is unspecified (e.g. insertion order).
  - **`sort=name_asc`**: items ordered by **name** ascending (A–Z).
  - **`sort=created_desc`**: items ordered by **createdAt** descending (newest first).
- **Expected response:**
  - Status: **200 OK**
  - Body: JSON array of item objects, each with **`id`**, **`name`**, and **`createdAt`**.

**Integration test scenarios:**

| Scenario | Behaviour |
|----------|-----------|
| **Empty list** | `GET /api/items` with no items returns `200` and an empty array `[]`. |
| **Create and list** | After creating one item with `POST /api/items` and body `{"name": "Test Item"}`, `GET /api/items` returns status `200` and an array of length 1, with the created item (same name, non-null id). |
| **Create accepts `name`** | Creating with body `{"name": "From Name Field"}` returns `201` and response body with `name` equal to `"From Name Field"`. |
| **Sort by name ascending** | After creating items (e.g. "Banana", "Apple"), `GET /api/items?sort=name_asc` returns them in ascending name order (e.g. "Apple" first, then "Banana"). |
| **Sort by created descending** | After creating items in order (e.g. "First", then "Second"), `GET /api/items?sort=created_desc` returns newest first (e.g. "Second" first, then "First"). |

Your implementation must satisfy these cases so that **all tests pass** (both unit and integration).

---

## Project structure (overview)

- **`/api/items`** – REST controller for creating and listing items.
- **Service layer** – business logic (create, find all with optional sort).
- **Repository** – persistence (in-memory implementation is provided).
- **Models** – `Item` (domain), `ItemResponse` (API response DTO).

You can add or refactor packages/classes internally; keep the public API and test expectations as above.

---

## Summary for the candidate

- Use **either Gradle or Maven** to run and test the project.
- **Ensure all existing tests pass** by implementing or aligning the API with the behaviour above (create with **`name`** in body, list with optional **`sort`** query param: `name_asc` or `created_desc`).
- Do not change the endpoints or the expected response shapes; you may adjust request DTOs (e.g. to accept **`name`**) so that the integration tests pass.
- Optionally add validation or error handling without breaking the current contract.

Good luck.
