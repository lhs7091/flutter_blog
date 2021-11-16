import 'package:flutter/material.dart';
import 'package:blog_app/export.dart';
import 'package:get/get.dart';

// ignore: must_be_immutable
class HomeScreen extends StatelessWidget {
  UserController _userController = Get.find();
  BoardController _boardController = Get.put(BoardController());
  var refreshKey = GlobalKey<RefreshIndicatorState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_userController.token.value.username.toString()),
        titleTextStyle: TextStyle(
          color: Colors.black,
          fontSize: 30,
          fontWeight: FontWeight.w600,
        ),
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
                  Get.offAll(LoginScreen());
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
      body: Obx(
        () => RefreshIndicator(
          child: _boardController.loading.value
              ? Center(child: CircularProgressIndicator())
              : ListView.builder(
                  padding:
                      const EdgeInsets.only(bottom: 80, left: 20, right: 20),
                  itemCount: _boardController.boards.length,
                  itemBuilder: (context, index) {
                    return buildBoardCard(
                        context,
                        _boardController.boards[
                            _boardController.boards.length - index - 1]);
                  },
                ),
          onRefresh: () async {
            await _boardController.getAllBoards();
          },
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Get.to(() => BoardWriteScreen());
        },
        child: Icon(Icons.add),
      ),
    );
  }

  Widget buildBoardCard(context, Board board) {
    return Container(
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
      decoration: BoxDecoration(
          color: Style.LightGrey,
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withOpacity(0.5),
              offset: Offset(0, 3),
            )
          ]),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Expanded(
                child: Text(
                  "${board.title!}",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 20,
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
              _userController.token.value.userId ==
                      board.user!.userid.toString()
                  ? IconButton(
                      onPressed: () {
                        Get.to(() => BoardWriteScreen(board: board));
                      },
                      icon: Icon(
                        Icons.edit,
                        size: 20,
                      ),
                    )
                  : Container(),
              _userController.token.value.userId ==
                      board.user!.userid.toString()
                  ? IconButton(
                      onPressed: () async {
                        var deleteOrNot = await displayDialogOKCallBack(
                          context,
                          "Delete Board",
                          "Do you want to delete?",
                        );
                        if (deleteOrNot) {
                          var result =
                              await _boardController.deleteBoard(board.id);
                          if (result != 1) {
                            Get.snackbar("Failed", result);
                          }
                        }
                      },
                      icon: Icon(
                        Icons.delete,
                        color: Style.AccentRed,
                        size: 20,
                      ),
                    )
                  : Container(),
            ],
          ),
          SizedBox(height: 15),
          Row(
            children: [
              Text(
                "${board.user!.username}",
                style: TextStyle(
                  fontSize: 15,
                  fontWeight: FontWeight.w500,
                ),
              ),
              SizedBox(width: 40),
              Flexible(
                child: BoardCardWidget(board: board),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget buildBoardInfo(Board board) {
    return Row(
      children: [
        Text(
          '1.1k',
          style: TextStyle(
            color: Colors.grey,
          ),
        ),
        SizedBox(width: 5),
        Icon(
          Icons.remove_red_eye_sharp,
          size: 14,
          color: Colors.grey,
        ),
        Text(
          ' / ',
          style: TextStyle(
            color: Colors.grey,
          ),
        ),
        Text(
          "${board.replys?.length ?? 0}",
          style: TextStyle(
            color: Colors.grey,
          ),
        ),
        SizedBox(width: 5),
        Icon(
          Icons.chat,
          size: 14,
          color: Colors.grey,
        ),
      ],
    );
  }
}
