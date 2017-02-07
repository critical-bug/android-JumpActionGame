package jp.techacademy.critical_bug.jumpactiongame;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.gms.ads.AdView;

public class ShowAdsHandler extends Handler {
    private final AdView mAdView;

    public ShowAdsHandler(final AdView adView) {
        this.mAdView = adView;
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.what) {
            case AndroidLauncher.SHOW_ADS:
                mAdView.setVisibility(View.VISIBLE);
                break;
            case AndroidLauncher.HIDE_ADS:
                mAdView.setVisibility(View.GONE);
                break;
        }
    }
}
