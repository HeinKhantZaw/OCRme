package com.ashomok.ocrme.get_more_requests.row.free_options.option_delegates;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ashomok.ocrme.OcrRequestsCounter;
import com.ashomok.ocrme.R;
import com.ashomok.ocrme.get_more_requests.GetMoreRequestsActivity;
import com.ashomok.ocrme.get_more_requests.row.free_options.UiFreeOptionManagingDelegate;

import javax.inject.Inject;

import static com.ashomok.ocrme.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 3/6/18.
 */

public class RateAppDelegate extends UiFreeOptionManagingDelegate {
    public static final String TAG = DEV_TAG + RateAppDelegate.class.getSimpleName();
    public static final String ID = "rate_app";
    private static final String RATE_APP_DONE_TAG = "RATE_APP_DONE";
    private final GetMoreRequestsActivity activity;
    private final SharedPreferences sharedPreferences;
    private final OcrRequestsCounter ocrRequestsCounter;

    @Inject
    public RateAppDelegate(GetMoreRequestsActivity activity, OcrRequestsCounter ocrRequestsCounter, SharedPreferences sharedPreferences) {
        super(activity);
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.ocrRequestsCounter = ocrRequestsCounter;
    }

    @Override
    protected void startTask() {
        Log.d(TAG, "onStartTask");
        saveData();
        rate();

        onTaskDone(ocrRequestsCounter, activity);
    }

    @Override
    public boolean isTaskAvailable() {
        boolean isAlreadyDone = sharedPreferences.getBoolean(RATE_APP_DONE_TAG, false);
        return !isAlreadyDone;
    }

    private void rate() {
        Toast.makeText(activity, R.string.thank_you_for_your_support, Toast.LENGTH_SHORT).show();
        String appPackageName = activity.getPackageName();
        openPackageInMarket(appPackageName);
    }

    private void openPackageInMarket(String appPackageName) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appPackageName));
        try {
            activity.startActivity(marketIntent);
        } catch (ActivityNotFoundException exception) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void saveData() {
        sharedPreferences.edit().putBoolean(RATE_APP_DONE_TAG, true).apply();
    }
}