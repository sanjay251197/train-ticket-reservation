# Spring Boot application for Train Ticket Management.

## Getting Started

### Prerequisites

- Java 21
- Maven (for building the project)

### Requirement
I want to board a train from London to France. The train ticket will cost $5. Details included in the receipt are From, To, User , price paid. User should include first and last name, email address

- An API where you can submit a purchase for a ticket.
- An API that shows the details of the receipt for the user.
- An API that lets you view the users and seat they are allocated by the requested section.
- An API to remove a user from the train.
- An API to modify a user's seat.


### API Overview

#### The full API documentation is available in the Swagger UI:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

| Method | Endpoint                | Description                    |
|--------|--------------------------|--------------------------------|
| POST    | `/booking/purchase` | Create a new ticket         |
| GET   | `/booking/view/{receiptId}`           | Fetch ticket using receipt id            |
| GET    | `/booking/{trainNumber}/sections/{section}`      | Get user and seat details based on section and train       |
| DELETE | `/booking/trains/{trainNumber}/users/{email}`      | Delete a tickets of a user       |
| PUT | `/booking/update/{trainNumber}/email/{email}/newseat/{newseat}`      | Update a ticket of a user       |

#### 1. Create A New Ticket

**Request**
```http
POST /booking/purchase
Content-Type: application/json

{
  "from": "string",
  "to": "string",
  "user": {
    "firstName": "string",
    "lastName": "string",
    "email": "string"
  },
  "price": 0
}
```

**Response**
```http
{
  "receiptId": "string",
  "from": "string",
  "to": "string",
  "user": {
    "firstName": "string",
    "lastName": "string",
    "email": "string"
  },
  "price": 0,
  "section": "string",
  "seat": 0,
  "trainNumber": 0
}
```


#### 2. Fetch Ticket Using Receipt Id

**Request**
```http
GET /booking/view/{receiptId}
Content-Type: application/json
```

**Response**
```http
{
  "receiptId": "string",
  "from": "string",
  "to": "string",
  "user": {
    "firstName": "string",
    "lastName": "string",
    "email": "string"
  },
  "price": 0,
  "section": "string",
  "seat": 0,
  "trainNumber": 0
}
```

#### 3. Get User And Seat Details Based On Section And Train

**Request**
```http
GET /booking/{trainNumber}/sections/{section}
Content-Type: application/json
```

**Response**
```http
[
  {
    "userName": "string",
    "seatNumber": 0
  }
]
```

#### 4. Delete A Tickets Of A User

**Request**
```http
DELETE /booking/trains/{trainNumber}/users/{email}
Content-Type: application/json
```

**Response**
```http
string
```

#### 5. Update Ticket Of A User

**Request**
```http
PUT /booking/update/{trainNumber}/email/{email}/newseat/{newseat}
Content-Type: application/json
```

**Response**
```http
string
```
