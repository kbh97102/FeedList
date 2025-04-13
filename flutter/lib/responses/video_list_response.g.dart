// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video_list_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VideoListResponse _$VideoListResponseFromJson(Map<String, dynamic> json) =>
    VideoListResponse(
      page: (json['page'] as num).toInt(),
      perPage: (json['per_page'] as num).toInt(),
      videos: (json['videos'] as List<dynamic>)
          .map((e) => VideoDto.fromJson(e as Map<String, dynamic>))
          .toList(),
      totalResults: (json['total_results'] as num).toInt(),
      nextPage: json['next_page'] as String,
      url: json['url'] as String,
    );

Map<String, dynamic> _$VideoListResponseToJson(VideoListResponse instance) =>
    <String, dynamic>{
      'page': instance.page,
      'per_page': instance.perPage,
      'videos': instance.videos.map((e) => e.toJson()).toList(),
      'total_results': instance.totalResults,
      'next_page': instance.nextPage,
      'url': instance.url,
    };
