import 'package:blog_app/controller/board/board_controller.dart';
import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:validators/validators.dart';

// ignore: must_be_immutable
class BoardWriteScreen extends StatelessWidget {
  final _formKey = GlobalKey<FormState>();
  BoardController boardController = Get.find();
  UserController userController = Get.find();
  final _titleController = TextEditingController();
  final _contentController = TextEditingController();
  Board? board;

  BoardWriteScreen({this.board});

  @override
  Widget build(BuildContext context) {
    if (board != null) {
      _titleController.text = board!.title!;
      _contentController.text = board!.content!;
    }
    return Scaffold(
      appBar: AppBar(),
      body: SingleChildScrollView(
        child: Container(
          padding:
              const EdgeInsets.only(top: 30, bottom: 60, left: 10, right: 10),
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                _titleField(),
                SizedBox(
                  height: 20,
                ),
                _contentField(),
                SizedBox(height: 10),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    primary: Style.AccentBlue,
                    minimumSize: Size(double.infinity, 50),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                  onPressed: () async {
                    if (_formKey.currentState!.validate()) {
                      var result;
                      // insert
                      if (board == null) {
                        result = await boardController.saveBoard(
                            userController.token.value.userId!,
                            _titleController.text,
                            _contentController.text);

                        // update
                      } else {
                        board!.title = _titleController.text;
                        board!.content = _contentController.text;
                        result = await boardController.updateBoard(
                            board!.id.toString(), board!);
                      }

                      if (result == 1) {
                        Get.off(() => HomeScreen());
                      } else {
                        Get.snackbar("Failed", "$result");
                      }
                    }
                  },
                  child: Text("SAVE"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _contentField() {
    return TextFormField(
      maxLines: 10,
      controller: _contentController,
      decoration: InputDecoration(
        hintText: "contents",
        fillColor: Colors.black,
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide(
            color: Colors.black45,
          ),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide(
            color: Colors.black45,
          ),
        ),
        focusedErrorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide(
            color: Colors.red,
          ),
        ),
      ),
      validator: (value) {
        if (isNull(value))
          return "please input contents";
        else if (!isLength(value!, 1, 500))
          return "Maximum length exceeded(within 500)";
      },
    );
  }

  Widget _titleField() {
    return TextFormField(
      controller: _titleController,
      decoration: InputDecoration(hintText: "title"),
      validator: (value) {
        if (isNull(value))
          return "please input title";
        else if (!isLength(value!, 1, 30))
          return "Maximum length exceeded(within 30)";
      },
    );
  }
}
