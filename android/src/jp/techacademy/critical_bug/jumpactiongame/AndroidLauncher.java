package jp.techacademy.critical_bug.jumpactiongame;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication implements ActivityRequestHandler {
    static final int HIDE_ADS = 0;
    static final int SHOW_ADS = 1;

    private AdView mAdView;
    private Handler mHandler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final View gameView = initializeForView(new JumpActionGame(this), config);

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        mAdView.setVisibility(View.INVISIBLE);
        mAdView.setBackgroundColor(Color.BLACK);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mHandler = new ShowAdsHandler(mAdView);

        final RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(mAdView, params);

        setContentView(layout);
    }

    @Override
    public void showAds(final Boolean show) {
        if (show) {
            mHandler.sendEmptyMessage(SHOW_ADS);
        } else {
            mHandler.sendEmptyMessage(HIDE_ADS);
        }
    }
}
