class LoginRequestDto {
  final String username;
  final String password;

  LoginRequestDto(this.username, this.password);

  Map<String, String> toJson() => {"username": username, "password": password};
}
