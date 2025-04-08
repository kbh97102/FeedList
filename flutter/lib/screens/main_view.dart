import 'dart:math';

import 'package:feed_list/screens/feed_item.dart';
import 'package:flutter/material.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';

class MainView extends StatelessWidget {
  MainView({super.key});

  List<String> testList = List.generate(30, (index) => "Index $index");
  Random random = Random();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Container(
        color: Colors.white,
        child: Scaffold(
          body: MasonryGridView.count(
            itemCount: testList.length,
            crossAxisSpacing: 10,
            mainAxisSpacing: 10,
            crossAxisCount: 3,
            itemBuilder: (context, index) {
              return Container(
                color: Colors.red,
                child: SizedBox(
                  height: 50.0 + random.nextInt(150),
                  child: FeedItem(
                    imageUrl: "http://via.placeholder.com/350x150",
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }
}
