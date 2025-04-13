import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

class FeedItem extends StatelessWidget {
  final String imageUrl;

  const FeedItem({super.key, required this.imageUrl});

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Align(
          alignment: Alignment.center,
          child: CachedNetworkImage(
            imageUrl: imageUrl,
            placeholder: (context, url) => CircularProgressIndicator(),
            errorWidget: (context, url, error) => Icon(Icons.error),
          ),
        ),
        Align(
          alignment: Alignment.topRight,
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Icon(Icons.favorite_border_outlined),
          ),
        ),
      ],
    );
  }
}
