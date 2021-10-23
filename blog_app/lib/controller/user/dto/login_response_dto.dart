class LoginResponseDto {
  String? accessToken;
  String? refreshToken;
  String? userId;
  String? username;
  String? userEmail;
  String? userRole;

  LoginResponseDto();

  LoginResponseDto.fromJson(Map<dynamic, dynamic> json)
      : accessToken = json["access_token"],
        refreshToken = json["refresh_token"],
        userId = json["user_id"],
        username = json["user_name"],
        userEmail = json["user_email"],
        userRole = json["user_role"];
}
