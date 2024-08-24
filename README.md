# SearchSupplierRepository

# Supplier API

This API allows you to search for suppliers based on location, nature of business, and manufacturing process. Below are the details of the available endpoints, along with example cURL commands for consuming them.

## Endpoints
 `/api/supplier/query`

## **Prerequisites**
-**Java 11+**
-**Spring Boot 2.5+**
-**A running PostgreSQL database**

#### 1. Search for Suppliers

- **Endpoint**: `/api/supplier/query`
- **Method**: `POST`
- **Description**: Retrieve a list of suppliers based on location, nature of business, and manufacturing process.
- **Request Body**:
  - `location`: The location (e.g., city) where the supplier is based.
  - `natureOfBusiness`: The nature of the supplier's business. Possible values: `small_scale`, `medium_scale`, `large_scale`.
  - `manufacturingProcesses`: The manufacturing process the supplier is capable of. Possible values: `moulding`, `3d_printing`, `casting`, `coating`.
  - `page`: The page number for pagination.
  - `limit`: The number of results per page.

##### Example cURL Command:
Success Response
```bash
curl -X POST http://localhost:8080/api/supplier/query \
-H "Content-Type: application/json" \
-d '{
    "location": "India",
    "natureOfBusiness": "small_scale",
    "manufacturingProcesses": "3d_printing",
    "page": 0,
    "limit": 10
}'

Example Response:
[
    {
        "supplierId": 1,
        "companyName": "3D Makers India",
        "website": "http://3dmakerindia.com",
        "location": "India",
        "natureOfBusiness": "small_scale",
        "manufacturingProcesses": "3d_printing"
    },
    {
        "supplierId": 2,
        "companyName": "Innovative Print Co.",
        "website": "http://innovativeprint.com",
        "location": "India",
        "natureOfBusiness": "small_scale",
        "manufacturingProcesses": "3d_printing"
    }
]
```

###### 2. Handle Errors
Description: The API returns different status codes and messages depending on the input and internal errors.
Example Scenarios:
Invalid Nature of Business

cURL Command:
1. Invalid nature of bussiness
```bash
curl -X POST http://localhost:8080/api/supplier/query \
-H "Content-Type: application/json" \
-d '{
    "location": "India",
    "natureOfBusiness": "invalid_scale",
    "manufacturingProcesses": "3d_printing",
    "page": 0,
    "limit": 10
}'

 //Response: It throws error message

   "Invalid nature of business"
```

2. No Supplies found
```bash
curl -X POST http://localhost:8080/api/supplier/query \
-H "Content-Type: application/json" \
-d '{
    "location": "Mars",
    "natureOfBusiness": "small_scale",
    "manufacturingProcesses": "3d_printing",
    "page": 0,
    "limit": 10
}'

//Response: It throws error message

  "No suppliers found for the given criteria"
```
3. Invalid manufacturing process
```bash
curl -X POST http://localhost:8080/api/supplier/query \
-H "Content-Type: application/json" \
-d '{
    "location": "Mars",
    "natureOfBusiness": "small_scale",
    "manufacturingProcesses": "Invalid_Manufacturing_process",
    "page": 0,
    "limit": 10
}'

// Response: Error Message

"Invalid manufacturing process"

```

**Pagination**

The API supports pagination via the page and limit parameters in the request body. If there are more results than the specified limit, they can be retrieved by increasing the page parameter.

