import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:validators/validators.dart';

class LoginScreen extends StatelessWidget {
  final UserController _userController = Get.put(UserController());

  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Container(
        alignment: Alignment.center,
        padding: const EdgeInsets.only(top: 30, bottom: 60),
        child: Column(
          children: [
            _buildTitle(),
            SizedBox(height: 10),
            _buildForm(),
            SizedBox(height: 20),
            _buildSignupLink()
          ],
        ),
      ),
    );
  }

  Widget _buildSignupLink() {
    return Padding(
      padding: const EdgeInsets.only(right: 20.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Text("아직 회원가입 안하셨어요?"),
          TextButton(
            onPressed: () {
              Get.to(() => SignUpScreen());
            },
            child: Text("회원가입"),
          ),
        ],
      ),
    );
  }

  Widget _buildTitle() {
    return Text(
      "LOGIN",
      style: TextStyle(
        fontSize: 25,
      ),
    );
  }

  Widget _buildForm() {
    return Container(
      width: 330,
      decoration: BoxDecoration(
        color: Style.LightBrown,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Form(
        key: _formKey,
        child: Column(
          children: [
            TextFormField(
              decoration: InputDecoration(
                hintText: "USERNAME",
              ),
              textAlign: TextAlign.left,
              controller: _usernameController,
              validator: (value) {
                if (!isAlphanumeric(value!))
                  return "please input alphabet or number";
                else if (isNull(value))
                  return "please input usernane";
                else
                  return null;
              },
            ),
            SizedBox(height: 20),
            TextFormField(
              obscureText: true,
              decoration: InputDecoration(
                hintText: "Password",
              ),
              textAlign: TextAlign.left,
              controller: _passwordController,
              validator: (value) {
                if (isNull(value))
                  return "please input usernane";
                else
                  return null;
              },
            ),
            SizedBox(height: 20),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                primary: Style.AccentBlue,
                minimumSize: Size(double.infinity, 50),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              onPressed: () async {
                // validation check clear
                if (_formKey.currentState!.validate()) {
                  int result = await _userController.login(
                      _usernameController.text.trim(),
                      _passwordController.text.trim());
                  // 로그인 완료
                  if (result == 1)
                    Get.off(() => HomeScreen());
                  else
                    Get.snackbar("Login Failed",
                        "please check username or password again");
                }
              },
              child: Text("ENTER"),
            ),
          ],
        ),
      ),
    );
  }
}
