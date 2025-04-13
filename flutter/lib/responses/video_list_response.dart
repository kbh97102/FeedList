import 'package:json_annotation/json_annotation.dart';

import 'video_dto.dart';

part 'video_list_response.g.dart';

@JsonSerializable(explicitToJson: true)
class VideoListResponse {
  final int page;

  @JsonKey(name: 'per_page')
  final int perPage;

  final List<VideoDto> videos;

  @JsonKey(name: 'total_results')
  final int totalResults;

  @JsonKey(name: 'next_page')
  final String nextPage;

  final String url;

  VideoListResponse({
    required this.page,
    required this.perPage,
    required this.videos,
    required this.totalResults,
    required this.nextPage,
    required this.url,
  });

  factory VideoListResponse.fromJson(Map<String, dynamic> json) =>
      _$VideoListResponseFromJson(json);

  Map<String, dynamic> toJson() => _$VideoListResponseToJson(this);

  @override
  String toString() {
    return 'VideoListResponse(page: $page, perPage: $perPage, totalResults: $totalResults, nextPage: $nextPage, url: $url, videos: $videos)';
  }
}
