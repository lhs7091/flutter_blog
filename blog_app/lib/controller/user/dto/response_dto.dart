class ResponseDto {
  final int code;
  final String msg;
  final dynamic data;

  ResponseDto({this.code, this.msg, this.data});

  ResponseDto.fromJson(Map<dynamic, dynamic> json)
      : code = json["code"],
        msg = json["msg"],
        data = json["data"];
}
