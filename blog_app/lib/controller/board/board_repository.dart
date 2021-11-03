import 'dart:convert';
import 'dart:developer';

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

  Future<dynamic> saveBoard(String userId, Board board) async {
    Response res = await _boardProvider.saveBoard(userId, board.toJson());

    final convertBody = json.decode(res.bodyString!);

    ResponseDto responseDto = ResponseDto.fromJson(convertBody);

    if (responseDto.code == 200) {
      return Board.fromJson(responseDto.data);
    } else if (res.statusCode == 403) {
      log(convertBody["error_message"]);
      return convertBody["error_message"];
    } else if (res.statusCode == 404) {
      log("404 not found");
      return "Illegal URL";
    } else {
      log("Internet Error");
      return "Internet Error";
    }
  }

  Future<dynamic> updateBoard(String boardId, Board board) async {
    Response res = await _boardProvider.updateBoard(boardId, board.toJson());

    final convertBody = json.decode(res.bodyString!);

    ResponseDto responseDto = ResponseDto.fromJson(convertBody);

    if (responseDto.code == 200) {
      return Board.fromJson(responseDto.data);
    } else if (res.statusCode == 403) {
      log(convertBody["error_message"]);
      return convertBody["error_message"];
    } else if (res.statusCode == 404) {
      log("404 not found");
      return "Illegal URL";
    } else {
      log("Internal Error");
      return "Internal Error";
    }
  }

  Future<dynamic> deleteBoard(int? id) async {
    Response res = await _boardProvider.deleteBoard(id);

    final convertBody = json.decode(res.bodyString!);

    ResponseDto responseDto = ResponseDto.fromJson(convertBody);

    if (responseDto.code == 200) {
      return responseDto.code;
    } else if (res.statusCode == 403) {
      log(convertBody["error_message"]);
      return convertBody["error_message"];
    } else if (res.statusCode == 404) {
      log("404 not found");
      return "Illegal URL";
    } else {
      log("Internal Error");
      return "Internal Error";
    }
  }
}
