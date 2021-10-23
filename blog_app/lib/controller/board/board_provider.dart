import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class BoardProvider extends GetConnect {
  Future<Response> getAllBoards() => get("$host/api/v1/board",
      headers: {"Authorization": BEARER + "$accessKey"});
}
