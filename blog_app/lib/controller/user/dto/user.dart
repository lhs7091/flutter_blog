class User {
  int userid;
  String username;
  String email;
  String role;

  User.fromJson(Map<dynamic, dynamic> json)
      : userid = json["id"],
        username = json["username"],
        email = json["email"],
        role = json["roles"];

  Map<String, dynamic> toJson() =>
      {"id": userid, "username": username, "email": email, "role": role};
}
