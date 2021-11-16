import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:validators/validators.dart';

class UserInfoEditScreen extends StatelessWidget {
  final UserController _userController = Get.find();

  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
  final _currentPassController = TextEditingController();
  final _newPassController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    _usernameController.text = _userController.token.value.username!;
    _emailController.text = _userController.token.value.userEmail!;

    return Scaffold(
      resizeToAvoidBottomInset: false,
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
      "Edit Profile",
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
            // username
            Row(
              children: [
                Container(
                  width: 80,
                  child: Text("USERNAME"),
                ),
                SizedBox(width: 20),
                Expanded(
                  child: TextFormField(
                    textAlign: TextAlign.left,
                    controller: _usernameController,
                    validator: (value) {
                      if (!isAlphanumeric(value!))
                        return "please input alphabet or number";
                      else
                        return null;
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 20),

            // email
            Row(
              children: [
                Container(
                  width: 80,
                  child: Text("E-MAIL"),
                ),
                SizedBox(width: 20),
                Expanded(
                  child: TextFormField(
                    textAlign: TextAlign.left,
                    controller: _emailController,
                    validator: (value) {
                      if (!isEmail(value!))
                        return "wrong email type";
                      else
                        return null;
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 20),

            // current password
            Row(
              children: [
                Container(
                  width: 80,
                  child: Text("current password"),
                ),
                SizedBox(width: 20),
                Expanded(
                  child: TextFormField(
                    decoration: InputDecoration(
                      hintText: "Required",
                    ),
                    textAlign: TextAlign.left,
                    controller: _currentPassController,
                    validator: (value) {
                      if (!isAlphanumeric(value!))
                        return "please input alphabet or number";
                      else if (isNull(value))
                        return "please input password";
                      else
                        return null;
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 20),

            // new password
            Row(
              children: [
                Container(
                  width: 80,
                  child: Text("new password"),
                ),
                SizedBox(width: 20),
                Expanded(
                  child: TextFormField(
                    decoration: InputDecoration(
                      hintText: "when you change, required",
                    ),
                    textAlign: TextAlign.left,
                    controller: _newPassController,
                    validator: (value) {
                      if (isNull(value))
                        return null;
                      else if (!isAlphanumeric(value!))
                        return "please input alphabet or number";
                      else
                        return null;
                    },
                  ),
                ),
              ],
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
                // validation check
                if (_formKey.currentState!.validate()) {
                  String result = await _userController.editUserProfile(
                      _usernameController.text.trim(),
                      _emailController.text.trim(),
                      _currentPassController.text.trim(),
                      _newPassController.text.trim());
                  if (result == "1")
                    showAlertDialog(context, "Update Success",
                        "Please re-login", sucessButton(context), "OK");
                  else if (result == "-1") {
                    Get.snackbar("Edit Failed",
                        "Please try again due to internal problem");
                  } else if (result.startsWith(tokenExpiredMsg)) {
                    showAlertDialog(
                        context,
                        tokenExpriedTitle,
                        tokenExpriedContent,
                        expiredToken(context),
                        tokenExpriedBtn);
                  } else {
                    Get.snackbar("Edit Failed", "$result");
                  }
                }
              },
              child: Text("Edit"),
            ),
          ],
        ),
      ),
    );
  }
}
