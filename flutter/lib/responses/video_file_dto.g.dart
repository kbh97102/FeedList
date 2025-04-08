// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video_file_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VideoFileDto _$VideoFileDtoFromJson(Map<String, dynamic> json) => VideoFileDto(
      id: (json['id'] as num).toInt(),
      quality: json['quality'] as String,
      fileType: json['file_type'] as String,
      width: (json['width'] as num).toInt(),
      height: (json['height'] as num).toInt(),
      fps: (json['fps'] as num).toDouble(),
      link: json['link'] as String,
    );

Map<String, dynamic> _$VideoFileDtoToJson(VideoFileDto instance) =>
    <String, dynamic>{
      'id': instance.id,
      'quality': instance.quality,
      'file_type': instance.fileType,
      'width': instance.width,
      'height': instance.height,
      'fps': instance.fps,
      'link': instance.link,
    };
