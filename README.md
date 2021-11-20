# flutter_blog

## TODO
  1. ~~when call /api/v1/board, user id compare with user info of board. if equal, go ahead, not eaual, error~~  
  2. reply function(Logic, UXUI)
  3. ~~deploy heroku~~
  4. ~~real function check by app~~

## Implement  
  1. BeckEnd : Springboot  
  2. FrontEnd : Flutter  
  3. DB : H2

## App Implement
<img src="https://github.com/lhs7091/flutter_blog/blob/master/picture/flutter_blog.gif" width="240">

## Class Diagram
<img src="https://github.com/lhs7091/flutter_blog/blob/master/picture/diagram.png">

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


### BoardController
| No. | name                    | mapping type |  URL                               | RequestBody                                                                                                                                                                                                                                                          | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|-----|-------------------------|--------------|------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1   | write board                 | POST         | /api/v1/board/{user id}                     | `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`  "title" : "title", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "content": "content" `<br>`}`                                                                                       |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"title": "title123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"content": "content123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"user": {` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": "2",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username":"test",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "test@test.com",` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"roles":"ROLE_USER",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"replys": "replys",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"` <br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`|                                                     
| 2   | update board                 | PUT         | /api/v1/board/{board id}                     | `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`  "title" : "title", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "content": "content" `<br>`}`                                                                                       |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"title": "title123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"content": "content123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"user": {` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": "2",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username":"test",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "test@test.com",` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"roles":"ROLE_USER",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"replys": "replys",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"` <br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`| 
| 3   | delete board                 | DELETE         | /api/v1/board/{board id}                     | `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`  "title" : "title", ` <br>&nbsp;&nbsp;&nbsp;&nbsp; ` "content": "content" `<br>`}`                                                                                       |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": null` <br>&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>`}`| 
| 4   | get all board                 | GET         | /api/v1/board                     |                                                                                        |  `{`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"code": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"msg": "SUCCESS",`<br>&nbsp;&nbsp;&nbsp;&nbsp;`"data": [ {`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": 1,`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"title": "title123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"content": "content123",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"user": {` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id": "2",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"username":"test",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"email": "test@test.com",` <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"roles":"ROLE_USER",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"replys": "replys",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"created": "2021-11-03T16:02:26.602667",`<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"updated": "2021-11-03T16:02:26.602667"` <br>&nbsp;&nbsp;&nbsp;&nbsp;`} ]`<br>`}`| 

## Exception Handler
Implement expcetion class for total project with @RestControllerAdvice annotation.   
So all of exception can be controlled simply.
```
@RestControllerAdvice
public class ResponseException {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    ResponseEntity<ResForm<String>> handlerIllegalArgumentException(IllegalArgumentException e){
        return createResult(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage());
    }
}
```

## Global URL Implement
1. Install npm
2. npm install -g localtunnel
3. lt --port 8080
```
aaa@aaa-MacBookAir ~ % npm install -g localtunnel

added 22 packages, and audited 22 packages in 2s

3 packages are looking for funding
  run `npm fund` for details

found 0 vulnerabilities
npm notice 
npm notice New major version of npm available! 7.0.15 -> 8.1.0
npm notice Changelog: https://github.com/npm/cli/releases/tag/v8.1.0
npm notice Run npm install -g npm@8.1.0 to update!
npm notice 
aaa@aaa-MacBookAir ~ % lt --port 8080            
your url is: https://bright-chicken-22.loca.lt
```

## heroku release
 - [Heroku java version](https://devcenter.heroku.com/articles/java-support#supported-java-versions)
 
 - [relase complete](https://fierce-beyond-79329.herokuapp.com)

## app.apk
 - build : flutter build apk --target-platform android-arm,android-arm64
 - [download](https://drive.google.com/file/d/1DKaDflJfDK0vh2iXeWojYff936ijW2Up/view?usp=sharing)

## api log check
```
2021-11-16T07:24:19.317893+00:00 heroku[router]: at=info method=POST path="/api/v1/signUp" host=fierce-beyond-79329.herokuapp.com request_id=71f3a300-1740-463a-8677-10ddadbd5df9 fwd="126.186.239.57" dyno=web.1 connect=0ms service=998ms status=201 bytes=523 protocol=https
2021-11-16T07:31:31.992692+00:00 heroku[router]: at=info method=POST path="/api/login" host=fierce-beyond-79329.herokuapp.com request_id=9767aa2c-d20e-40fe-a6ca-513cfa36f6dc fwd="126.186.239.57" dyno=web.1 connect=0ms service=256ms status=200 bytes=1015 protocol=https
```
 
