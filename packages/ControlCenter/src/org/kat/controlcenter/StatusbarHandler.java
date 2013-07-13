
package org.kat.controlcenter;

import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

public class StatusbarHandler extends PreferenceScreenHandler {
    private static final String APPEARANCE_CATEGORY = "statusbar_appearance_category";
    private static final String TILEPICKER = "eos_interface_panel_tile_chooser";
    private static final String COLUMNS = "eos_interface_panel_columns";
    private static final String TOGGLES = "eos_interface_settings";

    Preference mStatusBarColor;
    CheckBoxPreference mBatteryIcon;
    CheckBoxPreference mBatteryText;
    CheckBoxPreference mBatteryPercent;
    ListPreference mStatusbarClock;
    ListPreference mAmPmStyle;
    Preference mQuickSettingsOrder;
    ListPreference mColumns;
    CheckBoxPreference mEosTogglesEnabled;
    CheckBoxPreference mHideIndicator;
    Preference mLegacyTogglesOrder;

    PreferenceCategory mToggles;

    boolean mEosSettingsEnabled = false;
    OnActivityRequestedListener mListener;

    public StatusbarHandler(PreferenceScreen root, OnActivityRequestedListener listener) {
        super(root);
        mListener = listener;
        init();
    }

    protected void init() {
        PreferenceCategory mAppearance = (PreferenceCategory) mRoot.findPreference(APPEARANCE_CATEGORY);
        if (EOSUtils.hasSystemBar(mContext)) {
            mRoot.removePreference(mAppearance );
        }

        /* disable just for now */
        mToggles = (PreferenceCategory) mRoot.findPreference(TOGGLES);
        mRoot.removePreference(mToggles);
        mToggles = null;

        mBatteryIcon = (CheckBoxPreference) mRoot
                .findPreference("eos_interface_statusbar_battery_icon");
        mBatteryIcon.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE,
                EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE_DEF) == 1);
        mBatteryIcon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_BATTERY_ICON_VISIBLE,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        mBatteryText = (CheckBoxPreference) mRoot
                .findPreference("eos_interface_statusbar_battery_text");
        mBatteryText.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE,
                EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE_DEF) == 1);
        mBatteryText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_BATTERY_TEXT_VISIBLE,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        mBatteryPercent = (CheckBoxPreference) mRoot
                .findPreference("eos_interface_statusbar_battery_text_percent");
        mBatteryPercent.setChecked(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_BATTERY_PERCENT_VISIBLE,
                EOSConstants.SYSTEMUI_BATTERY_PERCENT_VISIBLE_DEF) == 1);

        mBatteryPercent.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_BATTERY_PERCENT_VISIBLE,
                        ((Boolean) newValue).booleanValue() ? 1 : 0);
                return true;
            }
        });

        mStatusbarClock = (ListPreference) mRoot
                .findPreference("eos_interface_statusbar_clock_state");

        mStatusbarClock.setDefaultValue(String.valueOf(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_CLOCK_VISIBLE,
                EOSConstants.SYSTEMUI_CLOCK_CLUSTER)));

        mStatusbarClock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int val = Integer.parseInt(((String) newValue).toString());
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_CLOCK_VISIBLE,
                        (val));
                return true;
            }
        });

        mAmPmStyle = (ListPreference) mRoot.findPreference("eos_interface_statusbar_clock_am_pm");

        String defValue = String.valueOf(Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_CLOCK_AMPM,
                EOSConstants.SYSTEMUI_CLOCK_AMPM_DEF));

        mAmPmStyle.setDefaultValue(defValue);
        mAmPmStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int val = Integer.parseInt(((String) newValue).toString());
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_CLOCK_AMPM,
                        (val));
                return true;
            }
        });

        mQuickSettingsOrder = (Preference) mRoot.findPreference(TILEPICKER);
        mQuickSettingsOrder
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mListener.onActivityRequested(Utils.QUICK_SETTINGS_FRAGMENT_TAG);
                        return true;
                    }
                });

        mColumns = (ListPreference) mRoot.findPreference(COLUMNS);
        mColumns.setDefaultValue(String.valueOf((Settings.System.getInt(
                mResolver,
                EOSConstants.SYSTEMUI_PANEL_COLUMN_COUNT,
                EOSConstants.SYSTEMUI_PANEL_COLUMNS_DEF))));
        mColumns.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int val = Integer.parseInt(((String) newValue).toString());
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_PANEL_COLUMN_COUNT,
                        (val));
                return true;
            }

        });

        if (mToggles != null) {
            mEosTogglesEnabled = (CheckBoxPreference) mRoot
                    .findPreference("eos_interface_settings_eos_settings_enabled");
            mEosTogglesEnabled.setChecked(Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_SETTINGS_ENABLED,
                    EOSConstants.SYSTEMUI_SETTINGS_ENABLED_DEF) == 1);
            mEosTogglesEnabled
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Settings.System.putInt(mResolver,
                                    EOSConstants.SYSTEMUI_SETTINGS_ENABLED,
                                    ((Boolean) newValue).booleanValue() ? 1 : 0);
                            enableAllCategoryChilds(mToggles,
                                    "eos_interface_settings_eos_settings_enabled",
                                    ((Boolean) newValue).booleanValue());
                            return true;
                        }
                    });

            mLegacyTogglesOrder = (Preference) mRoot
                    .findPreference("eos_interface_legacy_toggles_order");
            mLegacyTogglesOrder
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            mListener.onActivityRequested(Utils.LEGACY_TOGGLES_FRAGMENT_TAG);
                            return true;
                        }
                    });

            mHideIndicator = (CheckBoxPreference) mRoot
                    .findPreference("eos_interface_settings_indicator_visibility");
            mHideIndicator.setChecked(Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_SETTINGS_INDICATOR_HIDDEN,
                    EOSConstants.SYSTEMUI_SETTINGS_INDICATOR_HIDDEN_DEF) == 1);
            mHideIndicator
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Settings.System.putInt(mResolver,
                                    EOSConstants.SYSTEMUI_SETTINGS_INDICATOR_HIDDEN,
                                    ((Boolean) newValue).booleanValue() ? 1 : 0);
                            return true;
                        }
                    });

            enableAllCategoryChilds(mToggles, "eos_interface_settings_eos_settings_enabled",
                    mEosTogglesEnabled.isChecked());
        }
    }

    private void enableAllCategoryChilds(PreferenceCategory pc, String keyToExclude, boolean enabled) {
        int nbPrefs = pc.getPreferenceCount();
        for (int pref = 0; pref < nbPrefs; pref++)
            if (!pc.getPreference(pref).getKey().equals(keyToExclude))
                pc.getPreference(pref).setEnabled(enabled);
    }
}
