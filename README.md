# Project: HelpDesk
# 📁 Collection: UserModel 


## End-point: Register user
### Method: POST
>```
>http://localhost:9090/api/user/create
>```
### Body (**raw**)

```json
{
    "name": "officer",
    "email": "off@nai.com",
    "phoneNumber": "01324560570",
    "officeId": 3,
    "password": "nullhobe",
    "designation": "OFFICER"
}
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Login user
### Method: POST
>```
>http://localhost:9090/api/user/login
>```
### Body (**raw**)

```json
{
    "email": "superadmin@admin.com",
    "password": "12345678"
}
```

### 🔑 Authentication noauth

|Param|value|Type|
|---|---|---|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: get users
### Method: GET
>```
>http://localhost:9090/api/user/auth/all
>```
### Body (**raw**)

```json

```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: update user
### Method: PUT
>```
>http://localhost:9090/api/user/auth/3
>```
### Body (**raw**)

```json
{
    "name": "bbb bbb",
    "email": "bbb@nai.com",
    "phoneNumber": "01324560579",
    "officeId": 3,
    "password": "pepepe"
}
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
# 📁 Collection: Issue 


## End-point: new issue
### Method: POST
>```
>http://localhost:9090/api/issue/new
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|title|test title 2 |text|
|description|description 2|text|
|attachments|/C:/Users/KIT/Desktop/background.jpg|file|
|category|submission|text|


### 🔑 Authentication bearer

|Param|value|Type|
|---|---|---|
|token|eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoiU1VQRVJBRE1JTiIsImRlc2lnbmF0aW9uIjoiU1VQRVJBRE1JTiIsImVtYWlsIjoic3VwZXJhZG1pbkBhZG1pbi5jb20iLCJzdWIiOiJzdXBlcmFkbWluQGFkbWluLmNvbSIsImlhdCI6MTczMDgwMzA4NywiZXhwIjoxNzMwODg5NDg3fQ.Eyir9lBc7XH-o6NROgfrMTNkMgmfi1VVb2hCIxI_H9XEZAke0koMtu4_nB4ke090|string|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: update issue
### Method: PUT
>```
>http://localhost:9090/api/issue/1
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|title|first one|text|
|description|first one|text|
|status|REJECTED|text|
|attachments|/C:/Users/KIT/Desktop/background.jpg,/C:/Users/KIT/Desktop/complainant save response.txt,/C:/Users/KIT/Desktop/download.png,/C:/Users/KIT/Desktop/json.txt|file|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: delete issue
### Method: DELETE
>```
>http://localhost:9090/api/issue/20
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|title|updated|text|
|attachments|/C:/Users/KIT/Desktop/background.jpg,/C:/Users/KIT/Desktop/download.png|file|
|description|updated description|text|
|status|REJECTED|text|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get issue
### Method: GET
>```
>http://localhost:9090/api/issue/1
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|title|test title|text|
|attachments|/C:/Users/KIT/Pictures/Screenshots/Screenshot 2024-09-19 134810.png,/C:/Users/KIT/Pictures/Screenshots/Screenshot 2024-09-22 112206.png|file|
|description|test description|text|
|status|OPEN|text|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: reply issue
### Method: POST
>```
>http://localhost:9090/api/issue_reply/2
>```
### Body (**raw**)

```json
{
    "comment": "need to reopen",
    "updatedStatus": "OPEN"
}
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get issue history
### Method: GET
>```
>http://localhost:9090/api/issue_reply/2
>```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get issues by user
### Method: GET
>```
>http://localhost:9090/api/issue/user/3
>```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
# 📁 Collection: Dashboard 


## End-point: Get all by status
### Method: POST
>```
>http://localhost:9090/api/dashboard/status
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|status|OPEN|text|


### 🔑 Authentication bearer

|Param|value|Type|
|---|---|---|
|token|eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoiU1VQRVJBRE1JTiIsImRlc2lnbmF0aW9uIjoiU1VQRVJBRE1JTiIsImVtYWlsIjoic3VwZXJhZG1pbkBhZG1pbi5jb20iLCJzdWIiOiJzdXBlcmFkbWluQGFkbWluLmNvbSIsImlhdCI6MTczMDgwMjkyOSwiZXhwIjoxNzMwODg5MzI5fQ.JGENi8aw8gSQfR3ZSEmYRK7R8rvznTL8ci1gmbpCYb4fQJGsh8OfcDc7ekPfDeSa|string|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get all by month
### Method: POST
>```
>http://localhost:9090/api/dashboard/current_month
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|YearMonth|2024-11-30|text|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get all between months
### Method: POST
>```
>http://localhost:9090/api/dashboard/between_month
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|startYearMonth|2024-9-30|text|
|endYearMonth|2024-11-30|text|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Find by tracking number
### Method: POST
>```
>http://localhost:9090/api/dashboard/tracking?trackingNumber=44214584789
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|startYearMonth|2024-9-30|text|
|endYearMonth|2024-10-30|text|


### Query Params

|Param|value|
|---|---|
|trackingNumber|44214584789|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Find by Title or description
### Method: POST
>```
>http://localhost:9090/api/dashboard/find?input=2
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|startYearMonth|2024-9-30|text|
|endYearMonth|2024-10-30|text|


### Query Params

|Param|value|
|---|---|
|input|2|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get statistics
### Method: GET
>```
>http://localhost:9090/api/dashboard/count
>```
### Body formdata

|Param|value|Type|
|---|---|---|
|startYearMonth|2024-9-30|text|
|endYearMonth|2024-10-30|text|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
# 📁 Collection: Attachments 


## End-point: Get attachments by issue
### Method: GET
>```
>http://localhost:9090/api/attachments/issue/2
>```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get attachments by issue Copy
### Method: GET
>```
>http://localhost:9090/api/attachments/2
>```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
# 📁 Collection: SuperAdmin 


## End-point: update user
### Method: PUT
>```
>http://localhost:9090/api/auth/su/update/user
>```
### Body (**raw**)

```json
{
    "name": "bbb bbb",
    "email": "aaa@nai.com",
    "phoneNumber": "01324560579",
    "officeId": 3,
    "password": "nullhobe",
    "designation": "ADMIN"
}
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get permissions
### Method: GET
>```
>http://localhost:9090/api/auth/su/permissions
>```
### 🔑 Authentication bearer

|Param|value|Type|
|---|---|---|
|token|eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoiU1VQRVJBRE1JTiIsImRlc2lnbmF0aW9uIjoiU1VQRVJBRE1JTiIsImVtYWlsIjoic3VwZXJhZG1pbkBhZG1pbi5jb20iLCJzdWIiOiJzdXBlcmFkbWluQGFkbWluLmNvbSIsImlhdCI6MTczMDgwMzA4NywiZXhwIjoxNzMwODg5NDg3fQ.Eyir9lBc7XH-o6NROgfrMTNkMgmfi1VVb2hCIxI_H9XEZAke0koMtu4_nB4ke090|string|



⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Get details by role
### Method: GET
>```
>http://localhost:9090/api/auth/su/role/1
>```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

## End-point: Update permissions by role
### Method: PUT
>```
>http://localhost:9090/api/auth/su/role/edit/1
>```
### Body (**raw**)

```json
{
    "permissionList": [1,7,8,10]
}
```


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
_________________________________________________
Powered By: [postman-to-markdown](https://github.com/bautistaj/postman-to-markdown/)
