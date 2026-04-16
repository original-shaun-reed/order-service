# Order Processing — Technical Test

**Time allowed:** 1 hour
**Level:** Mid-level Java / Spring Boot

---

## The Scenario

You are joining a team that runs a small e-commerce platform built as microservices.
A pre-built **Inventory Service** already exists and is running.
Your task is to build the **Order Service** that integrates with it.

---

## What Is Provided

| Component | Port | Status |
|---|---|---|
| `inventory-service` | 8081 | **Complete — start this before you begin** |
| `order-service` | 8080 | **Skeleton — you build this** |

### Starting the Inventory Service

```bash
cd inventory-service
./mvnw spring-boot:run
```

Verify it is running:
```bash
curl http://localhost:8081/inventory/BUSINESSCARD
```
Expected response:
```json
{"productId":"BUSINESSCARD","name":"Moo Business Card","quantity":50}
```

### Available Products

| Product ID     | Name | Stock |
|----------------|---|-------|
| `BUSINESSCARD` | Moo Business Card | 50    |
| `POSTCARD`     | Moo Postcard | 20    |
| `PEN`          | Moo Wonky pen | 5     |
| `STICKER`      | Moo Amazing Sticker | 300   |

---

## Inventory Service API

### Check Stock

```
GET /inventory/{productId}
```
Returns `200 OK`:
```json
{"productId":"BUSINESSCARD","name":"Moo Business Card","quantity":50}
```
Returns `404 Not Found` if product does not exist.

### Reserve Stock

```
POST /inventory/{productId}/reserve
Content-Type: application/json

{"quantity": 3}
```

Returns `200 OK` on success:
```json
{"reserved": true, "remaining": 47}
```

Returns `409 Conflict` if insufficient stock:
```json
{"reserved": false, "reason": "Insufficient stock. Available: 2"}
```

---

### Starting the Order Service

```bash
cd order-service
./mvnw spring-boot:run
```

Verify it is running:
```bash
curl http://localhost:8080/orders/nonexistent
```
Expected response:
```json
{"message": "Order 'nonexistent' not found"}
```

> The Order Service depends on the Inventory Service running at `http://localhost:8081`. Start the Inventory Service first.

---

## Order Service API

### Create Order

```
POST /orders
Content-Type: application/json
```

```json
{
  "productId": "BUSINESSCARD",
  "quantity": 2,
  "customerId": "cust-123"
}
```

Returns `201 Created` on success:
```json
{
  "orderId": "a1b2c3d4",
  "customerId": "cust-123",
  "productId": "BUSINESSCARD",
  "quantity": 2,
  "status": "CONFIRMED",
  "createdAt": "2024-11-01T10:00:00Z"
}
```

Returns `409 Conflict` if insufficient stock:
```json
{"error": "Insufficient stock for BUSINESSCARD."}
```

Returns `404 Not Found` if the product does not exist.

Returns `503 Service Unavailable` if the Inventory Service is unreachable:
```json
{"message": "Inventory service unreachable"}
```

Returns `400 Bad Request` if the request body is missing or invalid:
```json
{
    "message": "Customer ID is required"
}
```

---

### Get Order

```
GET /orders/{orderId}
```

Returns `200 OK`:
```json
{
  "orderId": "a1b2c3d4",
  "customerId": "cust-123",
  "productId": "BUSINESSCARD",
  "quantity": 2,
  "status": "CONFIRMED",
  "createdAt": "2024-11-01T10:00:00Z"
}
```

Returns `404 Not Found` if the order does not exist:

```json
{
    "message": "Order '61f2f755-3495-4b47-b88b-e17fa56e0c5d' not found"
}
```