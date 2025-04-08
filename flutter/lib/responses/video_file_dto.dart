import 'package:json_annotation/json_annotation.dart';

part 'video_file_dto.g.dart';

@JsonSerializable()
class VideoFileDto {
  final int id;
  final String quality;

  @JsonKey(name: 'file_type')
  final String fileType;

  final int width;
  final int height;
  final double fps;
  final String link;

  VideoFileDto({
    required this.id,
    required this.quality,
    required this.fileType,
    required this.width,
    required this.height,
    required this.fps,
    required this.link,
  });

  factory VideoFileDto.fromJson(Map<String, dynamic> json) =>
      _$VideoFileDtoFromJson(json);
  Map<String, dynamic> toJson() => _$VideoFileDtoToJson(this);
}
