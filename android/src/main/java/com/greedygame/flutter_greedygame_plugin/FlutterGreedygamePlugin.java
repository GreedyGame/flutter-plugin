package com.greedygame.flutter_greedygame_plugin;

import android.util.Log;

import com.greedygame.android.core.campaign.CampaignStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterGreedygamePlugin
 */
public class FlutterGreedygamePlugin implements MethodCallHandler, CampaignStateListener {

    enum DartMethods {

        GG_CAMPAIGN_AVAILABLE("gg_campaign_available"),
        GG_CAMPAIGN_UNAVAILABLE("gg_campaign_unavailable"),
        GG_CAMPAIGN_ERROR("gg_campaign_error");

        private final String mValue;

        private DartMethods(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public static final String GG_INIT = "gg_init";
    public static final String GG_REFRESH = "gg_refresh";
    public static final String GG_SHOW_UII = "gg_show_uii";
    public static final String GG_GET_PATH = "gg_get_path";

    // Argument params received from Dart
    public static final String GAME_ID = "game_id";
    public static final String UNITS = "units";
    public static final String UNIT_ID = "unit_id";

    private static final String TAG = "FlutterGreedygamePlugin";

    private Registrar mRegistrar;
    private MethodChannel mMethodChannel;
    private GreedyGameWrapper mGreedyGameWrapper;

    private FlutterGreedygamePlugin(Registrar registrar, MethodChannel methodChannel) {
        mRegistrar = registrar;
        mMethodChannel = methodChannel;
        mGreedyGameWrapper = new GreedyGameWrapper(registrar.activity(), this);
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_greedygame_plugin");
        channel.setMethodCallHandler(new FlutterGreedygamePlugin(registrar, channel));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (mRegistrar.activity() == null) {
            Log.d(TAG, "GreedyGame Plugin needs foreground activity");
            return;
        }
        switch (call.method) {
            case GG_INIT:
                List<String> units = getUnits(call.argument(UNITS));
                if (units == null) {
                    result.error("init failed", "units not received", null);
                    return;
                }
                if (mGreedyGameWrapper.init(call.argument(GAME_ID).toString(), units)) {
                    result.success(Boolean.TRUE);
                } else {
                    result.error("Greedygame not initialized", "empty game id or units provided", null);
                }
                return;
            case GG_GET_PATH:
                String unitId = call.argument(UNIT_ID);
                if(unitId == null) {
                    result.error("get_path_failed", "unit id not passed", null);
                    return;
                }
                result.success(mGreedyGameWrapper.getPath(unitId));
                return;
            case GG_REFRESH:
                mGreedyGameWrapper.refresh();
                result.success(Boolean.TRUE);
                return;
            case GG_SHOW_UII:
                String unitId2 = call.argument(UNIT_ID);
                if(unitId2 == null) {
                    result.error("show_uii_failed", "unit id not passed", null);
                    return;
                }
                mGreedyGameWrapper.showUii(unitId2);
                result.success(Boolean.TRUE);
                return;
            default:
                result.notImplemented();
        }
    }

    private List<String> getUnits(Object value) {
        if (value == null) return null;
        if (!(value instanceof ArrayList)) {
            return null;
        }
        return (List<String>) value;
    }

    @Override
    public void onUnavailable() {
        Log.d(TAG, "Campaign unavailable native plugin");
        mMethodChannel.invokeMethod(DartMethods.GG_CAMPAIGN_UNAVAILABLE.toString(), null);
    }

    @Override
    public void onAvailable(String s) {
        Log.d(TAG, "Campaign available native plugin" + DartMethods.GG_CAMPAIGN_AVAILABLE.toString());
        mMethodChannel.invokeMethod(DartMethods.GG_CAMPAIGN_AVAILABLE.toString(), argumentsMap("value", s));
    }

    @Override
    public void onError(String s) {
        Log.d(TAG, "Campaign error native plugin");
        mMethodChannel.invokeMethod(DartMethods.GG_CAMPAIGN_ERROR.toString(), argumentsMap("value",s));
    }

    private Map<String, Object> argumentsMap(Object... args) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i += 2) arguments.put(args[i].toString(), args[i + 1]);
        return arguments;
    }
}