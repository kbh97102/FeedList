// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VideoDto _$VideoDtoFromJson(Map<String, dynamic> json) => VideoDto(
      id: (json['id'] as num).toInt(),
      width: (json['width'] as num).toInt(),
      height: (json['height'] as num).toInt(),
      url: json['url'] as String,
      image: json['image'] as String,
      fullRes: json['full_res'] as String?,
      tags: (json['tags'] as List<dynamic>).map((e) => e as String).toList(),
      duration: (json['duration'] as num).toInt(),
      user: UserDto.fromJson(json['user'] as Map<String, dynamic>),
      videoFiles: (json['video_files'] as List<dynamic>)
          .map((e) => VideoFileDto.fromJson(e as Map<String, dynamic>))
          .toList(),
      videoPictures: (json['video_pictures'] as List<dynamic>)
          .map((e) => VideoPictureDto.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$VideoDtoToJson(VideoDto instance) => <String, dynamic>{
      'id': instance.id,
      'width': instance.width,
      'height': instance.height,
      'url': instance.url,
      'image': instance.image,
      'full_res': instance.fullRes,
      'tags': instance.tags,
      'duration': instance.duration,
      'user': instance.user,
      'video_files': instance.videoFiles,
      'video_pictures': instance.videoPictures,
    };
