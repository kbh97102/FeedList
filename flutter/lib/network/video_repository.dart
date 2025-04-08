import 'package:dio/dio.dart';
import 'package:feed_list/responses/video_list_response.dart';
import 'package:retrofit/error_logger.dart';
import 'package:retrofit/http.dart';

part 'video_repository.g.dart';

@RestApi(baseUrl: "https://api.pexels.com/")
abstract class VideoRepository {
  factory VideoRepository(Dio dio, {String? baseUrl}) = _VideoRepository;

  @GET("videos/popular")
  Future<VideoListResponse> getPopularVideos();
}
