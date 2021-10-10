import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:validators/validators.dart';

class SignUpScreen extends StatelessWidget {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
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
            _buildForm(context),
          ],
        ),
      ),
    );
  }

  Widget _buildTitle() {
    return Text(
      "Sign Up",
      style: TextStyle(
        fontSize: 25,
      ),
    );
  }

  Widget _buildForm(BuildContext context) {
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
                if (!isAlphanumeric(value))
                  return "please input alphabet or number";
                else if (isNull(value))
                  return "please input usernane";
                else
                  return null;
              },
            ),
            SizedBox(height: 20),
            TextFormField(
              decoration: InputDecoration(
                hintText: "EMAIL",
              ),
              textAlign: TextAlign.left,
              controller: _emailController,
              validator: (value) {
                if (!isEmail(value))
                  return "wrong email type";
                else if (isNull(value))
                  return "please input email";
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
                  return "please input password";
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
                if (_formKey.currentState.validate()) {
                  // int result = await _userController.login(
                  //     _usernameController.text.trim(),
                  //     _passwordController.text.trim());
                  // // 로그인 완료
                  // if (result == 1)
                  showAlertDialog(context, "title", "content");
                } //else
                // Get.snackbar("로그인 시도", "로그인 실패");
              },
              child: Text("ENTER"),
            ),
          ],
        ),
      ),
    );
  }

  showAlertDialog(BuildContext context, String title, String content) {
    Widget okButton = TextButton(
      onPressed: () {
        Get.off(() => LoginScreen());
      },
      child: Text("OK"),
    );

    AlertDialog alertDialog = AlertDialog(
      title: Text("$title"),
      content: Text("$content"),
      actions: [
        okButton,
      ],
    );

    showDialog(
        context: context,
        builder: (BuildContext context) {
          return alertDialog;
        });
  }
}
