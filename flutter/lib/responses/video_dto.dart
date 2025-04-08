import 'package:feed_list/responses/user_dto.dart';
import 'package:feed_list/responses/video_file_dto.dart';
import 'package:feed_list/responses/video_picture_dto.dart';
import 'package:json_annotation/json_annotation.dart';

part 'video_dto.g.dart';

@JsonSerializable()
class VideoDto {
  final int id;
  final int width;
  final int height;
  final String url;
  final String image;

  @JsonKey(name: 'full_res')
  final String? fullRes;

  final List<String> tags;
  final int duration;
  final UserDto user;

  @JsonKey(name: 'video_files')
  final List<VideoFileDto> videoFiles;

  @JsonKey(name: 'video_pictures')
  final List<VideoPictureDto> videoPictures;

  VideoDto({
    required this.id,
    required this.width,
    required this.height,
    required this.url,
    required this.image,
    this.fullRes,
    required this.tags,
    required this.duration,
    required this.user,
    required this.videoFiles,
    required this.videoPictures,
  });

  factory VideoDto.fromJson(Map<String, dynamic> json) =>
      _$VideoDtoFromJson(json);
  Map<String, dynamic> toJson() => _$VideoDtoToJson(this);

  factory VideoDto.empty() => VideoDto(
    id: 0,
    width: 0,
    height: 0,
    url: '',
    image: '',
    fullRes: null,
    tags: [],
    duration: 0,
    user: UserDto.empty(),
    videoFiles: [],
    videoPictures: [],
  );
}
