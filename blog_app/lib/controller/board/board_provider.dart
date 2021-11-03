import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class BoardProvider extends GetConnect {
  Future<Response> getAllBoards() => get("$host/api/v1/board",
      headers: {"Authorization": BEARER + "$accessKey"});

  Future<Response> saveBoard(String userId, Map data) =>
      post("$host/api/v1/board/$userId", data,
          headers: {"Authorization": BEARER + "$accessKey"});

  Future<Response> updateBoard(String boardId, Map data) =>
      put("$host/api/v1/board/$boardId", data,
          headers: {"Authorization": BEARER + "$accessKey"});

  Future<Response> deleteBoard(int? id) => delete("$host/api/v1/board/$id",
      headers: {"Authorization": BEARER + "$accessKey"});
}
