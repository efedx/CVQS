# Toyota & 32 Bit CVQS Back-end Project

This project can be thought as a factory panel which has the following services. Added to that, every service has logging, exception handling and unit testing.

### Eureka Server
- Every container registers itself to Eureka server which enables load balancing and not-being-tied-to-the-port-numbers by client discovery

### API Gateway
- It has a authentication filter that intercepts every request except for '/login' and '/registerAdmin'.
- Then makes a rest request to security container. If there is no error then direct the request to the corresponding container.

### Employee Service
- Used for registering, updating, deleting employees and logging in.
- '/login' makes a rest reques to security container via feign client and retrieves the JWT.

### Terminal Service

- Used for registering and listing active terminals.
- When a terminal is registered, it sends a message to the 'notification_terminal_queue' with the terminal and department name, and 'isActive' value.
- Active terminals can be listed with paging and sorting. When a 'defectName' is provided it returns the terminals containing that terminal.

### Notification Service

- Listens the 'notification_terminal_queue' and when gets a new message, puts a message to the employee that has the same terminal and department name value.
- An example is "terminal RIO at department INSPECTION is now in/active"

### Defect Service
- Used for registering and listing terminals.
- Also, an defect image can be rendered with green dots on the corresponding defect positions.
- Defects can be listed with paging and sorting. You can either list all defects, also provide a defectName to return vehicles having that defect or providing a vehicle id, list all of its defects.

### Security Service

- Used for validating and generating JWTs, and authorization with roles.
- '/userManagement': ADMIN
- '/registerDefects': OPERATOR
- '/defeccts': LEADER
- others: AUTHENTICATED
- When a request is recieved to the urls above, it is intercepted by JwtValidationFilter. Validates the token and sets the security context.
- A JWT can be generated userManagementng username password authentication.

## API Reference

#### Register Employees

```http
  Post http://localhost:8080/registerAdmin
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Body** | `List<RegisterRequestDto>` | **Required** Employees to register |

- No security check

---

#### Get a JWT

```http
  Post http://localhost:8080/login
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Body** | `LoginRequestDto` | **Required** Username and password to login |

- No security check

---

#### Register Employees

```http
  Post localhost:8080/userManagement/registerEmployee
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Body** | `List<RegisterRequestDto>` | **Required** Employees to register |

- Requires ADMIN role

---

#### Update Employee

```http
  PUT http://localhost:8080/userManagement/updateEmployeeById/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** id | `Long` | **Required** Id of the employee to update |
| **Body** | `UpdateRequestDto` | **Required** Involves |

- Updates the user
- Requires ADMIN role

---

#### Delete Employee

```http
  Post http://localhost:8080/userManagement/deleteEmployeeById/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** id | `Long` | **Required** Id of the employee to delete |

- Set isDeleted field in the database "true"
- Requires ADMIN role

---

#### Register Terminals

```http
  Post http://localhost:8080/terminals/registerTerminals
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Body**| `List<RegisterTerminalDto>` | **Required** Terminals to register|

- Terminals to register
- When a terminal is registered, it sends a notification request to Rabbit MQ queue. Notification container listens the queue and when it gets a new message, it puts a message for the users have the same department and terminal as the newly registered terminal

- Requires 'authenticated'
---

#### Get Active Terminals

```http
  GET http://localhost:8080/terminals/listTerminals/page/{pageNumber}?{sortDirection=}&{terminalName=}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** pageNumber | `Long` | **Required** Page number to be shown |
| **Request Variable** sortDirection | `Long` | **Required** asc or desc |
| **Request Variable** terminalName | `Long` | **Not Required** terminal name to filter |

- Returns 'active' terminals.
- If a terminalName is specified it filters, only the terminals that has the same name value are returned.
- Requires 'authenticated'

---

#### Register defects

```http
  Post http://localhost:8080/registerDefects
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Body** registerDefectDto | `RegisterDefectDto` | **Required** Defects to register |
| **Body** defectImage | `.png` | **Required** Defect Image

- Requires OPERATOR role

---

#### Get Defect Image

```http
  GET http://localhost:8080/defects/getDefectImage/{defectId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** id | `Long` | **Required** Id of the defect to get image rendered |

- Returns the image saved while registering the defect, with green point on it based on location.
- Requires LEADER role

---

#### Get Defects By Vehicle

```http
  GET http://localhost:8080/defects/getDefectsByVehicle/{vehicleId}/page/{pageNumber}?{sortDirection=}&{sortField=}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** vehicleId | `Long` | **Required** Variable id to select defects |
| **Path Variable** pageNumber | `int` | **Required** Page number to be shown |
| **Request Variable** sortDirection | `String` | **Required** asc or desc |
| **Request Variable** sortField | `String` | **Required** Field to sort the items e.g. 'defectName' |

- Returns the defects of a particular vehicle
- Requires LEADER role

---

#### Get All Defects

```http
  GET http://localhost:8080/defects/getAllDefects/page/{pageNumber}?{sortDirection=}&{sortField=}&{defectName=}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| **Header** Authorization | `String` | **Required** Your JWT |
| **Path Variable** pageNumber | `int` | **Required** Page number to be shown |
| **Request Variable** sortDirection | `String` | **Required** asc or desc |
| **Request Variable** sortField | `String` | **Required** Field to sort the items e.g. 'defectName'|
| **Request Variable** defectName | `String` | **Not Required** Defect name to filter |


- Returns all defect. If a defect name is provided, it returns all the vehicles have that defect.
- Requires LEADER role

---

## DTO Reference

```json
RegisterRequestDto & UpdateRequestDto
{
    "username": "username",
    "password": "password",
    "email": "user@email.com",
    "roleSet": [{"roleName": "ROLE_OPERATOR"}],
    "department": "ASSEMBLY",
    "terminal": "ANL"
}
```

```json
LoginRequestDto
{
    "username": "1",
    "password": "1"
}
```

```json
RegisterTerminalDto
{
    "departmentName": "ASSEMBLY",
    "terminalList":
    [
        {
            "terminalName": "ANL",
            "isActive": "FALSE"
        
        }
    ]
}
```

```json
RegisterDefectDto
Note: do not forget to add a .png image
{
    "vehicleNo": 58,
    "defectList": [
        {
            "defectName": "D",
            "locationList":
            [
                {"location": [340, 220]},
                {"location": [300, 400]},
                {"location": [350, 250]}
            ]
        }
```
---
## Installation

In the parent module run the following

```bash
  mvn package -DskipTests
```
Then for modules build the docker image. I don't know if there is a shortcut for that for the time being.
```bash
  docker build -t {module_name} .
```
Finally, you can use docker compose in the parent module
```bash
  docker-compose up
```
You can also run docker-compose file in intellij.

