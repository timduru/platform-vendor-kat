
package org.kat.controlcenter;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;

public class ExpandedDesktopActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentState = Settings.System.getInt(getContentResolver(),
                Settings.System.EXPANDED_DESKTOP_STATE, 0);
        Settings.System.putInt(getContentResolver(),
                Settings.System.EXPANDED_DESKTOP_STATE, currentState == 0 ? 1 : 0);
        finish();
    }
}
