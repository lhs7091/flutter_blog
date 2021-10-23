// "id": 2,
//         "title": "title test",
//         "content": "content test",
//         "user": {
//             "id": 1,
//             "username": "test",
//             "email": "test@test.com",
//             "roles": "ROLE_USER",
//             "created": "2021-10-22T20:22:00.512966",
//             "updated": "2021-10-22T20:22:00.512966"
//         },
//         "replys": [],
//         "created": "2021-10-22T20:22:16.060461",
//         "updated": "2021-10-22T20:22:16.060461"

import 'package:blog_app/export.dart';
import 'package:intl/intl.dart';

class Board {
  int? id;
  String? title;
  String? content;
  User? user;
  List<String>? replys;
  DateTime? created;
  DateTime? updated;

  Board(this.id, this.title, this.content, this.user, this.replys, this.created,
      this.updated);

  Board.fromJson(Map<dynamic, dynamic> json)
      : id = json["id"],
        title = json["title"],
        content = json["content"],
        user = User.fromJson(json["user"]),
        replys = ["test1", 'test2'],
        created = DateFormat('yyyy-mm-dd').parse(json["created"]),
        updated = DateFormat('yyyy-mm-dd').parse(json["updated"]);
}
