package com.greedygame.flutter_greedygame_plugin;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignStateListener;

import java.util.List;

/**
 * @author Mathan on 2019-07-18
 */
public class GreedyGameWrapper  {

    private static final String TAG = "GreedyGameWrapper";

    private GreedyGameAgent mGreedyGameAgent;
    private Activity mActivity;
    private CampaignStateListener mCampaignStateListener;

    GreedyGameWrapper(Activity activity, CampaignStateListener campaignStateListener) {
        mActivity = activity;
        mCampaignStateListener = campaignStateListener;
    }

    Boolean init(String gameID, List<String> units) {
        if (TextUtils.isEmpty(gameID)) {
            Log.e(TAG, "Game id not provided");
            return false;
        }
        if (units != null && units.isEmpty()) {
            Log.e(TAG, "Units not provided");
            return false;
        }

        mGreedyGameAgent = new GreedyGameAgent.Builder(mActivity)
                .enableAdmob(true) // Enable Admob
                .withAgentListener(mCampaignStateListener) // For campaign changes
                .addUnitList(units) // units
                .setGameId(gameID) // 8 digit gameid
                .build();
        mGreedyGameAgent.init();
        return true;
    }

    String getPath(String unitID) {
        if(mGreedyGameAgent == null) {
            Log.d(TAG, "GreedyGame not initialized");
            return null;
        }
        return mGreedyGameAgent.getPath(unitID);
    }

    void refresh() {
        if(mGreedyGameAgent == null) {
            Log.d(TAG, "GreedyGame not initialized");
            return;
        }
        mGreedyGameAgent.startEventRefresh();
    }

    void showUii(String unitID) {
        if(mGreedyGameAgent == null) {
            Log.d(TAG, "GreedyGame not initialized");
            return;
        }
        mGreedyGameAgent.showUII(unitID);
    }

}