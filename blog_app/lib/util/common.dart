import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

const String tokenExpiredMsg = "The Token has expired";
const String tokenExpriedTitle = "Authentication Expired";
const String tokenExpriedContent = "Please renew Authentication";
const String tokenExpriedBtn = "RENEW";

showAlertDialog(BuildContext context, String title, String content,
    dynamic onPressed, String ok) {
  Widget okButton = TextButton(
    onPressed: onPressed,
    child: Text("${ok}"),
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

sucessSignUp(BuildContext context) => () {
      Get.offAll(() => LoginScreen());
    };

expiredToken(BuildContext context) => () async {
      final UserController _uc = Get.find();
      int result = await _uc.refreshToken();
      if (result == 1) {
        Navigator.pop(context);
        Get.snackbar("Authentication Success", "");
      } else {
        showAlertDialog(context, "Authentication Failed", "please login again",
            sucessSignUp(context), "OK");
      }
    };
