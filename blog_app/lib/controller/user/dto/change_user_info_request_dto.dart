class ChangeUserInfo {
  String username;
  String email;
  String currentPassword;
  String newPassword;

  ChangeUserInfo(
      this.username, this.email, this.currentPassword, this.newPassword);

  Map<String, String> toJson() => {
        "username": username,
        "email": email,
        "currentPassword": currentPassword,
        "newPassword": newPassword
      };
}
