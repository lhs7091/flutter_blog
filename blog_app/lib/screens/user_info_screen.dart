import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class UserInfoScreen extends StatelessWidget {
  final UserController _userController = Get.find();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            icon: Icon(Icons.edit),
            onPressed: () {
              Navigator.pop(context);
              Get.to(() => UserInfoEditScreen());
            },
          ),
        ],
      ),
      body: Container(
        alignment: Alignment.centerLeft,
        padding: const EdgeInsets.only(left: 30, top: 30, bottom: 60),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "${_userController.token.value.username}",
              style: TextStyle(
                fontSize: 35,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 10),
            Text(
              "${_userController.token.value.userEmail}",
              style: TextStyle(
                fontSize: 20,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
