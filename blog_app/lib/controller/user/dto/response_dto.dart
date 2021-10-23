class ResponseDto {
  int? code;
  String? msg;
  dynamic data;

  ResponseDto({this.code, this.msg, this.data});

  ResponseDto.fromJson(Map<dynamic, dynamic> json)
      : code = json["code"],
        msg = json["msg"],
        data = json["data"];
}
