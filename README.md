# Toyota_Spring_Project


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
| **Path** id | `Long` | **Required** Id of the employee to update |
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
| **Path** id | `Long` | **Required** Id of the employee to delete |

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
| **Request Variable** defectName | `String` | **Not Required** asc or desc |


- Returns all defect. If a defect name is provided, it returns all the vehicles have that defect.
- Requires LEADER role

---
