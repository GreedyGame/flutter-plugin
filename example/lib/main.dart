import 'package:flutter/material.dart';
import 'dart:io';
import 'package:flutter_greedygame_plugin/flutter_greedygame_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // GreedyGame Ad local path will be updated in this path
    String adPath;

    void campaignAvailable() {
      print("Campaign available received");

    }

    void campaignUnAvailable() {
      print("Campaign unavailable received");
    }

    void campaignError() {
      print("Campaign error received");
    }

    void initializeGreedyGame() {
      var units = <String> {
        "float-4040",
        "float-4039"
      };

      GreedyGame.instance.init();
    }

    void refreshAd() {}

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Row(
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: RaisedButton(
                      onPressed: initializeGreedyGame,
                      child: Text("Init"),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: RaisedButton(
                      onPressed: refreshAd,
                      child: Text("Refresh"),
                    ),
                  )
                ],
              ),
              Container(
                  child: (adPath != null && adPath.isNotEmpty) ? Image.file(File(adPath)) : Image.asset("assets/image1.jpg")
              )
            ],
          ),
        ),
      ),
    );
  }
}
