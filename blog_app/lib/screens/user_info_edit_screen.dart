import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:validators/validators.dart';

class UserInfoEditScreen extends StatelessWidget {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _emailController = TextEditingController();
  final _currentPassController = TextEditingController();
  final _newPassController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    _usernameController.text = "username";
    _emailController.text = "test@test.com";

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
                      if (!isAlphanumeric(value))
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
                      if (!isEmail(value))
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
                    textAlign: TextAlign.left,
                    controller: _currentPassController,
                    validator: (value) {
                      if (!isAlphanumeric(value))
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
                    textAlign: TextAlign.left,
                    controller: _newPassController,
                    validator: (value) {
                      if (isNull(value))
                        return null;
                      else if (!isAlphanumeric(value))
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
              onPressed: () {
                // validation check
                if (_formKey.currentState.validate()) {
                  Get.snackbar("Edit Profile", "Fail");
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
