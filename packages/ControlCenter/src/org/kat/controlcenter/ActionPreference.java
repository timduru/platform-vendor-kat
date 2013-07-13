
package org.kat.controlcenter;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.teameos.jellybean.settings.EOSConstants;

public class ActionPreference extends Preference {
    private Context mContext;
    private ContentResolver mResolver;
    private Resources mRes;
    private String mTargetUri;
    private ImageView mWidget = null;
    private View.OnClickListener mListener;
    private String CUSTOM_SUMMARY;
    private String DEFAULT_SUMMARY;

    private static String REBOOT_VAL = EOSConstants.SYSTEMUI_TASK_POWER_MENU;
    private static String SCREENSHOT_VAL = EOSConstants.SYSTEMUI_TASK_SCREENSHOT;
    private static String KILLCURRENT_VAL = EOSConstants.SYSTEMUI_TASK_KILL_PROCESS;
    private static String SCREENOFF_VAL = EOSConstants.SYSTEMUI_TASK_SCREENOFF;
    private static String ASSIST_VAL = EOSConstants.SYSTEMUI_TASK_ASSIST;

    private static int REBOOT_TITLE = R.string.eos_interface_softkeys_reboot_title;
    private static int SCREENSHOT_TITLE = R.string.eos_interface_softkeys_screenshot_title;
    private static int KILLCURRENT_TITLE = R.string.eos_interface_softkeys_kill_process_title;
    private static int SCREENOFF_TITLE = R.string.eos_interface_softkeys_screenoff_title;
    private static int ASSIST_TITLE = R.string.eos_interface_navring_assist_title;

    private static int REBOOT_ICON = com.android.internal.R.drawable.ic_lock_reboot;
    private static int KILLCURRENT_ICON = com.android.internal.R.drawable.ic_dialog_alert;
    private static int SCREENSHOT_ICON = com.android.internal.R.drawable.ic_menu_gallery;
    private static int SCREENOFF_ICON = com.android.internal.R.drawable.ic_lock_power_off;
    private static int ASSIST_ICON = R.drawable.ic_action_assist_activated;

    private static int PACKAGE_NOT_FOUND_SUMMARY = R.string.eos_interface_softkey_package_removed;
    private static int DEFAULT_TITLE = R.string.eos_interface_softkeys_pref_default_title;
    private static int DEFAULT_ICON = com.android.internal.R.drawable.sym_def_app_icon;

    public ActionPreference(Context context) {
        this(context, null);
    }

    public ActionPreference(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public ActionPreference(Context context, AttributeSet attr, int defStyle) {
        super(context, attr);
        mContext = context;
        mRes = mContext.getResources();
        mResolver = mContext.getContentResolver();

        int customSummaryRes = attr.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                "customSummary", -1);
        if (customSummaryRes != -1)
            CUSTOM_SUMMARY = context.getString(customSummaryRes);
        DEFAULT_SUMMARY = String.valueOf(getSummary());

        int uriRes = attr.getAttributeResourceValue(Utils.CONTROLCENTERNS, "providerUri", -1);
        if (uriRes != -1)
            mTargetUri = context.getString(uriRes);
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
        setResources(getUriValue(mTargetUri));
    }

    public String getTargetUri() {
        return mTargetUri;
    }

    @Override
    public void onBindView(View v) {
        super.onBindView(v);
        mWidget = (ImageView) v.findViewById(R.id.eos_configure_settings);
        mWidget.setImageResource(R.drawable.ic_action_settings);
        mWidget.setOnClickListener(mListener);
        mWidget.setTag(mTargetUri);
    }

    private String getUriValue(String uri) {
        return Settings.System.getString(mResolver, uri);
    }

    public void setResources(String uriValue) {
        if (uriValue == null || uriValue.equals("") || uriValue.equals(" ")) {
            setTargetValue("none");
            uriValue = "none";
            setDefaultSettings(false);
        } else if (uriValue.startsWith("app:")) {
            setResourcesFromUri(uriValue);
        } else {
            loadCustomApp(uriValue);
        }
    }

    public void loadCustomApp(String uriValue) {
        if (uriValue.equals(REBOOT_VAL)) {
            setTitle(mRes.getString(REBOOT_TITLE));
            setIcon(mRes.getDrawable(REBOOT_ICON));
            StringBuilder builder = new StringBuilder();
            builder.append(CUSTOM_SUMMARY)
                    .append(" ")
                    .append(mRes.getString(REBOOT_TITLE));
            setSummary(builder.toString());
        } else if (uriValue.equals(SCREENSHOT_VAL)) {
            setTitle(mRes.getString(SCREENSHOT_TITLE));
            setIcon(mRes.getDrawable(SCREENSHOT_ICON));
            StringBuilder builder = new StringBuilder();
            builder.append(CUSTOM_SUMMARY)
                    .append(" ")
                    .append(mRes.getString(SCREENSHOT_TITLE));
            setSummary(builder.toString());
        } else if (uriValue.equals(KILLCURRENT_VAL)) {
            setTitle(mRes.getString(KILLCURRENT_TITLE));
            setIcon(mRes.getDrawable(KILLCURRENT_ICON));
            StringBuilder builder = new StringBuilder();
            builder.append(CUSTOM_SUMMARY)
                    .append(" ")
                    .append(mRes.getString(KILLCURRENT_TITLE));
            setSummary(builder.toString());
        } else if (uriValue.equals(SCREENOFF_VAL)) {
            setTitle(mRes.getString(SCREENOFF_TITLE));
            setIcon(mRes.getDrawable(SCREENOFF_ICON));
            StringBuilder builder = new StringBuilder();
            builder.append(CUSTOM_SUMMARY)
                    .append(" ")
                    .append(mRes.getString(SCREENOFF_TITLE));
            setSummary(builder.toString());
        } else if (uriValue.equals(ASSIST_VAL)) {
            setTitle(mRes.getString(ASSIST_TITLE));
            setIcon(mRes.getDrawable(ASSIST_ICON));
            StringBuilder builder = new StringBuilder();
            builder.append(CUSTOM_SUMMARY)
                    .append(" ")
                    .append(mRes.getString(ASSIST_TITLE));
            setSummary(builder.toString());
        } else if (uriValue.equals("none")) {
            setDefaultSettings(false);
        } else {
            setDefaultSettings(false);
        }
    }

    public void setTargetValue(String uriValue) {
        Settings.System.putString(mResolver, mTargetUri, uriValue);
    }

    private void setDefaultSettings(boolean packageNotFound) {
        setTitle(mRes.getString(DEFAULT_TITLE));
        setIcon(mRes.getDrawable(DEFAULT_ICON));
        setSummary(packageNotFound
                ? mRes.getString(PACKAGE_NOT_FOUND_SUMMARY)
                : DEFAULT_SUMMARY);
    }

    private void setResourcesFromUri(String uri) {
        if (uri.startsWith("app:")) {
            String activity = uri.substring(4);
            PackageManager pm = mContext.getPackageManager();
            ComponentName component = ComponentName.unflattenFromString(activity);
            ActivityInfo activityInfo = null;
            Boolean noError = false;
            try {
                activityInfo = pm.getActivityInfo(component, PackageManager.GET_RECEIVERS);
                noError = true;
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                noError = false;
                setDefaultSettings(true);
                Toast.makeText(mContext, "The selected application could not be found",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            if (noError) {
                setIcon(activityInfo.loadIcon(pm));
                String title = activityInfo.loadLabel(pm).toString();
                setTitle(title);
                StringBuilder builder = new StringBuilder();
                builder.append(CUSTOM_SUMMARY)
                        .append(" ")
                        .append(title);
                setSummary(builder.toString());
            }
        } else {
            setDefaultSettings(false);
        }
    }

    public void setResourcesFromPackage(String name, String component, Drawable d) {
        setTitle(name);
        setIcon(d);
        StringBuilder builder = new StringBuilder();
        builder.append(CUSTOM_SUMMARY)
                .append(" ")
                .append(name);
        setSummary(builder.toString());
        String tmp = "app:" + component;
        setTargetValue(tmp);
    }
}
