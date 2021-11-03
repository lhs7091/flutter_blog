import 'package:blog_app/export.dart';
import 'package:flutter/material.dart';

class BoardCardWidget extends StatefulWidget {
  final Board board;

  const BoardCardWidget({Key? key, required this.board}) : super(key: key);

  @override
  _BoardCardWidgetState createState() => _BoardCardWidgetState();
}

class _BoardCardWidgetState extends State<BoardCardWidget> {
  String? firstHalf;
  String? secondHalf;

  bool flag = true;

  @override
  void initState() {
    super.initState();

    if (widget.board.content!.length > 50) {
      firstHalf = widget.board.content!.substring(0, 50);
      secondHalf =
          widget.board.content!.substring(50, widget.board.content!.length);
    } else {
      firstHalf =
          widget.board.content!.substring(0, widget.board.content!.length);
      secondHalf = "";
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        secondHalf!.isEmpty
            ? buildContent(firstHalf!)
            : Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                      flag ? (firstHalf! + "...") : (firstHalf! + secondHalf!)),
                  InkWell(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        Text(
                          flag ? "show more" : "show less",
                          style: TextStyle(
                            color: Colors.blue,
                          ),
                        ),
                      ],
                    ),
                    onTap: () {
                      setState(() {
                        flag = !flag;
                      });
                    },
                  ),
                ],
              ),
        SizedBox(height: 10),
        buildBoardInfo(),
      ],
    );
  }

  Widget buildContent(String content) {
    return Text(
      widget.board.content!,
      style: TextStyle(
        fontSize: 16,
        fontWeight: FontWeight.bold,
      ),
      maxLines: 2,
      overflow: TextOverflow.ellipsis,
    );
  }

  Widget buildBoardInfo() {
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
          "${widget.board.replys?.length ?? 0}",
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
