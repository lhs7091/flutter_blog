class SignUpRequestDto {
  String username;
  String email;
  String password;

  SignUpRequestDto(this.username, this.email, this.password);

  Map<String, String> toJson() =>
      {"username": username, "email": email, "password": password};
}
