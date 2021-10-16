class UserInfoResponseDto {
  int userid;
  String username;
  String email;
  String role;

  UserInfoResponseDto.fromJson(Map<dynamic, dynamic> json)
      : userid = json["id"],
        username = json["username"],
        email = json["email"],
        role = json["role"];
}
