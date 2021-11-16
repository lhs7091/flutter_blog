import 'dart:developer';

import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class UserController extends GetxController {
  final _userRepository = UserRepository();
  final token = LoginResponseDto().obs;

  Future<dynamic> signUp(String username, String email, String password) async {
    SignUpRequestDto signUpRequestDto =
        SignUpRequestDto(username, email, password);
    ResponseDto responseDto = await _userRepository.signUp(signUpRequestDto);

    if (responseDto.code == 201)
      return 1;
    else
      return responseDto.data;
  }

  Future<int> login(String username, String password) async {
    LoginResponseDto loginResponseDto =
        await _userRepository.login(username, password);

    try {
      this.token.value = loginResponseDto;
      accessKey = loginResponseDto.accessToken!;
      refreshKey = loginResponseDto.refreshToken!;
      return 1;
    } catch (e) {
      return -1;
    }
  }

  void logout() {
    log("logout");
    this.token.value = LoginResponseDto();
  }

  Future<String> editUserProfile(String username, String email,
      String currentPassword, String newPassword) async {
    var responseData = await _userRepository.changeUserInfo(
        this.token.value.userId!,
        username,
        email,
        currentPassword,
        newPassword);
    print(responseData.runtimeType);

    if (responseData.runtimeType == User && responseData != null) {
      this.token.value.username = responseData.username;
      this.token.value.userEmail = responseData.email;
      return "1";
    } else if (responseData.runtimeType == String && responseData != null) {
      return responseData;
    } else {
      return "-1";
    }
  }

  Future<int> refreshToken() async {
    var res = await _userRepository.refreshToken();

    if (res != null) {
      accessKey = res["access_token"];
      refreshKey = res["refresh_token"];
      return 1;
    }
    return -1;
  }
}
