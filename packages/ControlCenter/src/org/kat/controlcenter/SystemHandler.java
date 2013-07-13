
package org.kat.controlcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.TextUtils;

import org.teameos.jellybean.settings.EOSConstants;

public class SystemHandler extends PreferenceScreenHandler {
    private static final String EOS_DEVICE_SETTINGS = "eos_device_settings";
    private static final String EOS_PERFORMANCE_SETTINGS = "eos_performance_settings";
    private static final String PRIVACY = "eos_privacy_settings";
    private static final String KEY_SCREENSHOT_FACTOR = "screenshot_scaling";
    private static final String WIFI_IDLE_MS = "wifi_idle_ms";

    private boolean isCrtOffChecked = false;
    private Preference mPrivacy;
    private Preference mPerformance;
    private CheckBoxPreference mVolumeKeysSwitch;
    private CheckBoxPreference mVolumeKeysMusicControl;
    private ListPreference mVolumePanelStyle;
    private CheckBoxPreference mVolumeKeySounds;
    private CheckBoxPreference mVolumeNotificationLink;
    private CheckBoxPreference mBatteryWarning;
    private CheckBoxPreference mScreenCharging;
    private CheckBoxPreference mCrtOff;
    private CheckBoxPreference mCrtOn;
    private ListPreference mWifiChannelsPreference;
    private ListPreference mDefaultVolumeStreamPreference;
    private ListPreference mScreenshotFactor;
    private ListPreference mWifiIdleMs;
    private EditTextPreference mHostnamePreference;

    OnActivityRequestedListener mListener;

    public SystemHandler(PreferenceScreen pref, OnActivityRequestedListener listener) {
        super(pref);
        mListener = listener;
        init();
    }

    @Override
    protected void init() {

        if (!mRes.getBoolean(R.bool.config_hasDeviceSettings)) {
            Preference ps = (Preference) mRoot.findPreference(EOS_DEVICE_SETTINGS);
            if (ps != null) {
                mRoot.removePreference(ps);
            }
        }

        mPrivacy = mRoot.findPreference(PRIVACY);
        mPrivacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                mListener.onActivityRequested(Utils.PRIVACY_FRAG_TAG);
                return true;
            }
        });

        mPerformance = mRoot.findPreference(EOS_PERFORMANCE_SETTINGS);
        mPerformance.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                mListener.onActivityRequested(Utils.PERFORMANCE_FRAG_TAG);
                return true;
            }
        });

        mVolumeKeysSwitch = (CheckBoxPreference) mRoot
                .findPreference("eos_system_volume_keys_rotate");
        mVolumeKeysSwitch.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_VOLUME_KEYS_SWITCH_ON_ROTATION,
                EOSConstants.SYSTEM_VOLUME_KEYS_SWITCH_ON_ROTATION_DEF) == 1);
        mVolumeKeysSwitch
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Settings.System.putInt(mResolver,
                                EOSConstants.SYSTEM_VOLUME_KEYS_SWITCH_ON_ROTATION,
                                ((Boolean) newValue).booleanValue() ? 1 : 0);
                        return true;
                    }
                });

        mVolumeKeysMusicControl = (CheckBoxPreference) mRoot
                .findPreference("eos_system_volume_keys_music_control");
        mVolumeKeysMusicControl.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_VOLUME_KEYS_MUSIC_CONTROL, 1) == 1);
        mVolumeKeysMusicControl
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Settings.System.putInt(mResolver,
                                EOSConstants.SYSTEM_VOLUME_KEYS_MUSIC_CONTROL,
                                ((Boolean) newValue).booleanValue() ? 1 : 0);
                        return true;
                    }
                });

        mVolumePanelStyle = (ListPreference) mRoot.findPreference("eos_system_volume_panel_controls_key");
        mVolumePanelStyle.setValue(String.valueOf(Settings.System.getInt(mResolver,
                Settings.System.MODE_VOLUME_OVERLAY, Settings.System.VOLUME_OVERLAY_EXPANDABLE)));
        mVolumePanelStyle
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int val = Integer.parseInt((String) newValue);
                        Settings.System.putInt(mResolver,
                                Settings.System.MODE_VOLUME_OVERLAY, val);
                        return true;
                    }
                });

        mVolumeNotificationLink = (CheckBoxPreference) mRoot
                .findPreference("eos_system_volume_link_notification_key");
        mVolumeNotificationLink.setChecked(Settings.System.getInt(mResolver,
                Settings.System.VOLUME_LINK_NOTIFICATION, 1) == 1);
        mVolumeNotificationLink
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Settings.System.putInt(mResolver,
                                Settings.System.VOLUME_LINK_NOTIFICATION,
                                ((Boolean) newValue).booleanValue() ? 1 : 0);
                        return true;
                    }
                });

        mVolumeKeySounds = (CheckBoxPreference) mRoot
                .findPreference("eos_system_volume_hard_button_sounds");
        mVolumeKeySounds.setChecked(Settings.System.getInt(mResolver,
                Settings.System.VOLUME_ADJUST_SOUNDS_ENABLED, 1) == 1);
        mVolumeKeySounds.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        Settings.System.VOLUME_ADJUST_SOUNDS_ENABLED,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        boolean unplugTurnsOnScreen = mRes
                .getBoolean(com.android.internal.R.bool.config_unplugTurnsOnScreen);
        mScreenCharging = (CheckBoxPreference) mRoot
                .findPreference("eos_system_power_unplug_screen_off");
        mScreenCharging.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_POWER_DONT_WAKE_DEVICE_PLUGGED,
                unplugTurnsOnScreen ? 0 : 1) == 1);
        mScreenCharging.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEM_POWER_DONT_WAKE_DEVICE_PLUGGED,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        // respect device default configuration
        // true fades while false animates
        boolean electronBeamFadesConfig = mRes.getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);

        // use this to enable/disable crt on feature
        // crt only works if crt off is enabled
        // total system failure if only crt on is enabled
        isCrtOffChecked = Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_POWER_ENABLE_CRT_OFF,
                electronBeamFadesConfig ? 0 : 1) == 1;

        mCrtOff = (CheckBoxPreference) mRoot.findPreference("eos_system_power_crt_screen_off");
        mCrtOff.setChecked(isCrtOffChecked);
        mCrtOff.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                isCrtOffChecked = ((Boolean) newValue).booleanValue();
                Settings.System.putInt(mResolver, EOSConstants.SYSTEM_POWER_ENABLE_CRT_OFF,
                        (isCrtOffChecked ? 1 : 0));
                // if crt off gets turned off, crt on gets turned off and
                // disabled
                if (!isCrtOffChecked) {
                    Settings.System.putInt(mResolver, EOSConstants.SYSTEM_POWER_ENABLE_CRT_ON, 0);
                    mCrtOn.setChecked(false);
                }
                mCrtOn.setEnabled(isCrtOffChecked);
                return true;
            }
        });

        mCrtOn = (CheckBoxPreference) mRoot.findPreference("eos_system_power_crt_screen_on");
        mCrtOn.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_POWER_ENABLE_CRT_ON, 0) == 1);
        mCrtOn.setEnabled(isCrtOffChecked);
        mCrtOn.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver, EOSConstants.SYSTEM_POWER_ENABLE_CRT_ON,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        mWifiChannelsPreference = (ListPreference) mRoot
                .findPreference("eos_wifi_regulatory_domain_selector");
        mWifiChannelsPreference
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        final String newCountryCode = (String) newValue;
                        String currentCountryCode = Settings.Secure.getString(mResolver,
                                Settings.Global.WIFI_COUNTRY_CODE);
                        if (newCountryCode.equals(currentCountryCode)) {
                            return false;
                        } else {
                            handleWifiState(newCountryCode);
                            return true;
                        }
                    }
                });
        mWifiChannelsPreference.setValue(Settings.Secure.getString(mResolver,
                                Settings.Global.WIFI_COUNTRY_CODE));

        mDefaultVolumeStreamPreference = (ListPreference) mRoot
                .findPreference("eos_system_default_volume_stream");
        mDefaultVolumeStreamPreference
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Settings.System.putString(mResolver,
                                EOSConstants.SYSTEM_DEFAULT_VOLUME_STREAM,
                                (String) newValue);
                        return true;
                    }
                });
        String currentStreamValue = Settings.System.getString(mResolver,
                EOSConstants.SYSTEM_DEFAULT_VOLUME_STREAM);
        if (!"media".equals(currentStreamValue)) {
            currentStreamValue = "default";
        }
        mDefaultVolumeStreamPreference.setValue(currentStreamValue);

        mWifiIdleMs = (ListPreference) mRoot.findPreference("eos_wifi_idle_ms");
        mWifiIdleMs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.Secure.putLong(mResolver, WIFI_IDLE_MS,
                        Long.valueOf((String) newValue));
                return true;
            }
        });
        mWifiIdleMs.setValue(String.valueOf(Settings.Secure.getLong(mResolver,
                WIFI_IDLE_MS, 900000)));

        mScreenshotFactor = (ListPreference) mRoot.findPreference(KEY_SCREENSHOT_FACTOR);
        mScreenshotFactor
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int val = Integer.parseInt((String) newValue);
                        Settings.System.putInt(mResolver,
                                EOSConstants.SYSTEMUI_SCREENSHOT_SCALE_INDEX, val);
                        updateScreenshotFactorSummary(val);
                        return true;
                    }
                });
        int currentVal = Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_SCREENSHOT_SCALE_INDEX, 0);
        mScreenshotFactor.setValue(String.valueOf(currentVal));
        updateScreenshotFactorSummary(currentVal);

        mBatteryWarning = (CheckBoxPreference) mRoot
                .findPreference("eos_system_disable_battery_warning");
        mBatteryWarning.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEM_DISABLE_LOW_BATTERY_WARNING,
                EOSConstants.SYSTEM_DISABLE_LOW_BATTERY_WARNING_DEF)
                == EOSConstants.SYSTEM_DISABLE_LOW_BATTERY_WARNING_DEF ? false : true);
        mBatteryWarning.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEM_DISABLE_LOW_BATTERY_WARNING,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        mHostnamePreference = (EditTextPreference) mRoot.findPreference("eos_net_hostname");
        String hostname = Settings.System.getString(mResolver, EOSConstants.NET_HOSTNAME);
        if (TextUtils.isEmpty(hostname))
            hostname = SystemProperties.get("net.hostname");
        mHostnamePreference.setText(hostname);
        mHostnamePreference.setSummary(hostname);
        mHostnamePreference
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String value = (String) newValue;
                        if (!value
                                .matches("^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$")) {
                            return false;
                        }
                        Settings.System.putString(mResolver, EOSConstants.NET_HOSTNAME, value);
                        SystemProperties.set("net.hostname", value);
                        mHostnamePreference.setSummary(value);
                        return true;
                    }
                });
    }

    private void updateScreenshotFactorSummary(int val) {
        // little cheat as the percent sign used in the pref entries
        // does not play nice with getStringArray
        String[] mScreenshotEntries = {
                "1.0", "0.75", "0.50", "0.25"
        };
        StringBuilder b = new StringBuilder()
                .append(mRes.getString(R.string.eos_screenshot_scaling_summary))
                .append(mScreenshotEntries[val]);
        mScreenshotFactor.setSummary(b.toString());
    }

    private void handleWifiState(String countryCode) {
        Settings.Secure.putString(mResolver, Settings.Global.WIFI_COUNTRY_CODE,
                countryCode);
        mWifiChannelsPreference.setValue(String.valueOf(countryCode));
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            int delay = 5000;
            final ProgressDialog dialog = ProgressDialog.show(mContext,
                    mRes.getString(R.string.eos_wifi_regulatory_domain_changed),
                    mRes.getString(R.string.eos_wifi_restarting), true);
            dialog.show();
            wifi.setWifiEnabled(false);
            wifi.setWifiEnabled(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, delay);
        }
    }
}
