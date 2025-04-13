import 'package:json_annotation/json_annotation.dart';

part 'video_picture_dto.g.dart';

@JsonSerializable()
class VideoPictureDto {
  final int id;
  final String picture;
  final int nr;

  VideoPictureDto({required this.id, required this.picture, required this.nr});

  factory VideoPictureDto.fromJson(Map<String, dynamic> json) =>
      _$VideoPictureDtoFromJson(json);
  Map<String, dynamic> toJson() => _$VideoPictureDtoToJson(this);
}
