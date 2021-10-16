import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class HomeScreen extends StatelessWidget {
  final UserController _userController = Get.find();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          PopupMenuButton<String>(
            icon: Icon(Icons.filter_list),
            onSelected: (String result) {
              switch (result) {
                case 'PROFILE':
                  print('filter 1 clicked');
                  Get.to(() => UserInfoScreen());
                  break;
                case 'LOGOUT':
                  print('LOGOUT');
                  _userController.logout();
                  Get.off(() => LoginScreen());
                  break;
                default:
              }
            },
            itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
              const PopupMenuItem<String>(
                value: 'PROFILE',
                child: Text('PROFILE'),
              ),
              const PopupMenuItem<String>(
                value: 'LOGOUT',
                child: Text('LOG OUT'),
              ),
            ],
          ),
        ],
      ),
      body: Container(
        alignment: Alignment.center,
        padding: const EdgeInsets.only(top: 30, bottom: 60),
        child: Text("Home Screen"),
      ),
    );
  }
}
