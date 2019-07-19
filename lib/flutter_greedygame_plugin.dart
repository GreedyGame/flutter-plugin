import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class GreedyGame {
  // Events
  static const GG_INIT = "gg_init";
  static const GG_PATH = "gg_get_path";
  static const GG_REFRESH = "gg_refresh";
  static const GG_SHOW_UII = "gg_show_uii";

  //Data
  static const GAME_ID = "game_id";
  static const UNITS = "units";
  static const UNIT_ID = "unit_id";

  static const CAMPAIGN_AVAILABLE = "gg_campaign_available";
  static const CAMPAIGN_UNAVAILABLE = "gg_campaign_unavailable";
  static const CAMPAIGN_ERROR = "gg_campaign_error";

  Function onCampaignAvailable;
  Function onCampaignUnAvailable;
  Function onCampaignError;

  String gameID;
  List<String> units;
  bool admobEnabled;

  GreedyGame.private(MethodChannel channel) : _channel = channel {
    _channel.setMethodCallHandler(_handleMethod);
  }

  final MethodChannel _channel;

  static final GreedyGame _instance =
      GreedyGame.private(const MethodChannel('flutter_greedygame_plugin'));

  Future<dynamic> _handleMethod(MethodCall call) {
    switch (call.method) {
      case CAMPAIGN_AVAILABLE:
        if (onCampaignAvailable != null) {
          onCampaignAvailable();
        }
        break;
      case CAMPAIGN_UNAVAILABLE:
        if (onCampaignUnAvailable != null) {
          onCampaignUnAvailable();
        }
        break;
      case CAMPAIGN_ERROR:
        if (onCampaignError != null) {
          onCampaignError();
        }
        break;
    }
    return Future<dynamic>.value(null);
  }

  /// The single shared instance of this plugin.
  static GreedyGame get instance => _instance;

  GreedyGame setGameID(String gameID) {
    this.gameID = gameID;
    return this;
  }

  GreedyGame setUnits(List<String> units) {
    this.units = units;
    return this;
  }

  GreedyGame setAdmobEnabled(bool admobEnabled) {
    this.admobEnabled = admobEnabled;
    return this;
  }

  GreedyGame setCampaignAvailable(Function campaignAvailable) {
    onCampaignAvailable = campaignAvailable;
    return this;
  }

  GreedyGame setCampaignUnAvailable(Function campaignUnAvailable) {
    onCampaignUnAvailable = campaignUnAvailable;
    return this;
  }

  GreedyGame setCampaignError(Function campaignError) {
    onCampaignError = campaignError;
    return this;
  }

  void init() {
    Map<String, dynamic> args = <String, dynamic>{
      GAME_ID: gameID,
      UNITS: units,
    };
    _channel.invokeMethod(GG_INIT, args);
  }

  Future<String> getPath(String unitID) async {
    Map<String, dynamic> args = <String, dynamic>{
      UNIT_ID: unitID,
    };
    String result = await _channel.invokeMethod(GG_PATH, args);
    return result;
  }

  void refresh() {
    _channel.invokeMethod(GG_REFRESH, null);
  }

  void showUii(unitID) {
    Map<String, dynamic> args = <String, dynamic>{
      UNIT_ID: unitID,
    };
    _channel.invokeMethod(GG_SHOW_UII, args);
  }
}
