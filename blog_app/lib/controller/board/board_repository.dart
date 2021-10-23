import 'dart:convert';

import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class BoardRepository {
  final _boardProvider = BoardProvider();

  Future<List<Board>> getAllBoards() async {
    Response res = await _boardProvider.getAllBoards();

    final convertBody = json.decode(res.bodyString!);

    ResponseDto responseDto = ResponseDto.fromJson(convertBody);

    if (responseDto.code == 200) {
      List<dynamic> temp = responseDto.data;
      List<Board> boards = temp.map((board) => Board.fromJson(board)).toList();
      return boards;
    } else {
      return <Board>[];
    }
  }
}
