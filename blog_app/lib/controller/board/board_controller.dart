import 'package:blog_app/export.dart';
import 'package:get/get.dart';

class BoardController extends GetxController {
  final _boardRepository = BoardRepository();
  final boards = <Board>[].obs;
  final loading = true.obs;

  onInit() {
    super.onInit();
    getAllBoards();
    this.loading.value = false;
  }

  Future<void> getAllBoards() async {
    this.boards.value = await _boardRepository.getAllBoards();
  }

  Future<dynamic> saveBoard(
      String userId, String title, String contents) async {
    var board = Board();
    board.title = title;
    board.content = contents;
    var resultBoard = await _boardRepository.saveBoard(userId, board);

    if (resultBoard.runtimeType == Board && resultBoard != null) {
      this.boards.add(resultBoard);
      return 1;
    } else if (resultBoard.runtimeType == String || resultBoard == null) {
      return resultBoard;
    } else {
      return "-1";
    }
  }

  Future<dynamic> updateBoard(String boardId, Board board) async {
    var resultBoard = await _boardRepository.updateBoard(boardId, board);

    if (resultBoard.runtimeType != Board || resultBoard == null) {
      return resultBoard;
    } else {
      int index =
          this.boards.indexWhere((element) => element.id == resultBoard.id);
      if (index == -1) {
        return "Internal Error";
      }
      this.boards[index] = resultBoard;
      return 1;
    }
  }

  Future<dynamic> deleteBoard(int? id) async {
    var result = await _boardRepository.deleteBoard(id);

    if (result.runtimeType == int && result != null) {
      int index = this.boards.indexWhere((element) => element.id == id);
      if (index == -1) {
        return "Internal Error";
      }
      this.boards.removeAt(index);
      return 1;
    } else {
      return result;
    }
  }
}
