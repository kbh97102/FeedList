import 'package:dio/dio.dart';
import 'package:feed_list/network/video_repository.dart';
import 'package:feed_list/screens/main_view.dart';
import 'package:flutter/material.dart';

void main() {
  final dio =
      Dio()
        ..interceptors.add(
          LogInterceptor(
            request: true,
            requestHeader: true,
            requestBody: true,
            responseHeader: true,
            responseBody: true,
            error: true,
            logPrint: print, // 필요 시 custom print 함수로 교체 가능
          ),
        );
  dio.options.headers['Authorization'] =
      '3jIoUzX5NHTqXH84M41UumY676KIrp05LN4itIMRTOBferYjzF2zo8iE'; // config your dio headers globally
  final client = VideoRepository(dio);

  client.getPopularVideos().then((value) => {print("Result $value")});

  runApp(MainView());
}
