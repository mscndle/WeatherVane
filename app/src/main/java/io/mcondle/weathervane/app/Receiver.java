package io.mcondle.weathervane.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mscndle on 5/25/15.
 */
public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent Detected: " + intent.getAction(), Toast.LENGTH_SHORT).show();
    }
}
