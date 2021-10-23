import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class BoardController extends GetxController {
  final _boardRepository = BoardRepository();
  final boards = <Board>[].obs;

  onInit() {
    super.onInit();
    getAllBoards();
  }

  Future<void> getAllBoards() async {
    this.boards.value = await _boardRepository.getAllBoards();
  }
}
