# flutter_blog

## Implement  
  1. BeckEnd : Springboot  
  2. FrontEnd : Flutter  
  3. DB : H2

## Spring security
1. [JWT(Json Web Token)](https://jwt.io)
  - JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.
  - JWT structure
    - header : he type of the token, which is JWT, and the signing algorithm being used, such as HMAC SHA256 or RSA.   
      ```
      {
        "alg": "HS256",
        "typ": "JWT"
      }
      ```
    - payload : Claims are statements about an entity (typically, the user) and additional data.   
      ```
      {
        "sub": "1234567890",
        "name": "John Doe",
        "iat": 1516239022,
        "exp": 1636378897,
        "roles":[
          ROLE_USER
        ]
      }
      ```
    - signature : The signature is used to verify the message wasn't changed along the way, and, in the case of tokens signed with a private key, it can also verify that the sender of the JWT is who it says it is.
      ```
      HMACSHA256(
      base64UrlEncode(header) + "." +
      base64UrlEncode(payload),
      secret)
      ```
      
2. Authorization according to url, function.  

|content         |url               |auth       |
|----------------|------------------|-----------|
|Log in          |/api/login        |no required|
|Sign Up         |/api/v1/signUp    |no required|
|user function   |/api/v1/user/**   |ROLE_USER  |
|manager function|/api/v1/manager/**|ROLE_MANAGER|
|admin function  |/api/v1/admin/**  |ROLE_ADMIN |



## API

### UserController
| No. | name                    | mapping type |  URL                               | RequestBody                                                                                                                                                                                                                                                          | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|-----|-------------------------|--------------|------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1   | sign up                 | POST         | /api/v1/signUp                     | `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`  "username" : "username", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "email": "email", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "password": "password" `<br>`}`                                                                                       |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username": "username",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "email",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"role": "role"`<br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`|                                                     
| 2   | change user auth        | put          | /api/v1/admin/auth/{userId}/{role} |                                                                                                                                                                                                                                                                      |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username": "username",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "email",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"role": "role"`<br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`|
| 3   | change user information | put          | /api/v1/user/{userId}              | `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`  "username" : "username", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "email": "email", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "currentPassword": "currentPassword", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "newPassword": @NullAble "newPassword" `<br>`}`|  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username": "username",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "email",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"role": "role"`<br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`|
| 4   | delete user             | delete       | /api/v1/user/{userId}              |                                                                                                                                                                                                                                                                      |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": "회원삭제 완료 userId : 1"`<br>`}`|


