import 'dart:convert';
import 'dart:developer';

import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class UserRepository {
  final _userProvider = UserProvider();

  Future<ResponseDto> signUp(SignUpRequestDto signUpRequestDto) async {
    Response res = await _userProvider.signUp(signUpRequestDto.toJson());

    try {
      final convertBody = json.decode(res.bodyString!);

      ResponseDto responseDto = ResponseDto.fromJson(convertBody);

      return responseDto;
    } catch (e) {
      log("convertUtf8 error : $e");
      return ResponseDto();
    }
  }

  Future<LoginResponseDto> login(String username, String password) async {
    LoginRequestDto dto = LoginRequestDto(username, password);
    Response res = await _userProvider.login(dto.toJson());

    try {
      final convertBody = json.decode(res.body.toString());
      if (convertBody == null) {
        return LoginResponseDto();
      }
      return LoginResponseDto.fromJson(convertBody);
    } catch (e) {
      log("convertUtf8 error : $e");
      return LoginResponseDto();
    }
  }

  Future<dynamic> changeUserInfo(String userId, String username, String email,
      String currentPassword, String newPassword) async {
    ChangeUserInfo dto =
        ChangeUserInfo(username, email, currentPassword, newPassword);

    Response res = await _userProvider.changeUserInfo(userId, dto.toJson());

    try {
      final convertBody = json.decode(res.bodyString!);
      if (res.statusCode == 200) {
        ResponseDto responseDto = ResponseDto.fromJson(convertBody);

        if (responseDto.code == 200) {
          User userInfoResponseDto = User.fromJson(responseDto.data);

          log(userInfoResponseDto.toString());

          return userInfoResponseDto;
        } else {
          log(responseDto.data);
          return responseDto.data;
        }
      } else if (res.statusCode == 403) {
        log(convertBody["error_message"]);
        return convertBody["error_message"];
      } else if (res.statusCode == 404) {
        log("404 not found");
        return "Illegal URL";
      } else {
        log("Internet Error");
        return "Internet Error";
      }
    } catch (e) {
      log("convertUtf8 error : $e");
      return null;
    }
  }

  Future<dynamic> refreshToken() async {
    Response res = await _userProvider.refreshToken();

    try {
      final convertBody = json.decode(res.bodyString!);
      if (convertBody == null) {
        return null;
      }
      return convertBody;
    } catch (e) {
      log("convertUtf8 error : $e");
      return null;
    }
  }
}
