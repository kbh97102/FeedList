// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video_picture_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VideoPictureDto _$VideoPictureDtoFromJson(Map<String, dynamic> json) =>
    VideoPictureDto(
      id: (json['id'] as num).toInt(),
      picture: json['picture'] as String,
      nr: (json['nr'] as num).toInt(),
    );

Map<String, dynamic> _$VideoPictureDtoToJson(VideoPictureDto instance) =>
    <String, dynamic>{
      'id': instance.id,
      'picture': instance.picture,
      'nr': instance.nr,
    };
