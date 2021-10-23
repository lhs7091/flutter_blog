import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class UserProvider extends GetConnect {
  Future<Response> signUp(Map data) => post("$host/api/v1/signUp", data);

  Future<Response> login(Map data) => post("$host/api/login", data);

  Future<Response> changeUserInfo(String userId, Map data) {
    // var accessKey =
    //     "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmIiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2FwaS9sb2dpbiIsImV4cCI6MTYzMzg3MzI5N30.hz9ND50EhRQ-S5KztXFi-dAXrKAw0cuWXZgjyINm3iTsJ4KfnnlB-iC0FzzhfGYOd2-smssZzSmWYXWjXuhMRQ";
    return put("$host/api/v1/user/$userId", data,
        headers: {"Authorization": BEARER + "$accessKey"});
  }

  Future<Response> refreshToken() => get("$host/api/v1/token/refresh",
      headers: {"Authorization": BEARER + "$refreshKey"});
}
