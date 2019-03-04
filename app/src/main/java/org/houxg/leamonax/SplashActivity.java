package org.houxg.leamonax;

import android.content.Intent;
import org.houxg.leamonax.ui.LaunchActivity;

import co.bxvip.sdk.ui.BxStartActivityImpl;

public class SplashActivity extends BxStartActivityImpl {
    @Override
    public void toYourMainActivity() {
        startActivity(new Intent(this, LaunchActivity.class));
    }
}
