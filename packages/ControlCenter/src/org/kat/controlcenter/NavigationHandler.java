
package org.kat.controlcenter;

import android.content.Intent;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.provider.Settings;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

public class NavigationHandler extends PreferenceScreenHandler {
    private static final String CATEGORY_UI_MODE_KEY = "eos_systemui_mode_category";
    private static final String CATEGORY_EXPANDED_DESKTOP = "eos_systemui_expanded_desktop_category";
    private static final String CATEGORY_CAPKEY = "eos_navbar_capkey_features";
    private static final String CATEGORY_STYLE_KEY = "eos_navbar_style";
    private static final String CATEGORY_ACTIONS_KEY = "eos_navbar_actions";
    private static final String STYLE_SIZE_KEY = "eos_interface_navbar_size";
    private static final String STYLE_NX_KEY = "eos_interface_navbar_nx_style";
    private static final String STYLE_GLASS_KEY = "eos_interface_navbar_glass_style";
    private static final String UI_MODE_KEY = "eos_systemui_mode";
    private static final String EXPANDED_STYLE_KEY = "eos_interface_enable_expanded_desktop_list";

    OnActivityRequestedListener mListener;

    PreferenceCategory pc_style;
    PreferenceCategory pc_action;
    PreferenceCategory pc_capkey;
    PreferenceCategory pc_ui;
    PreferenceCategory pc_expanded;
    ListPreference mExpandedStyle;
    ListPreference mLowProfileNavBar;
    CheckBoxPreference mNxStyleBar;
    CheckBoxPreference mGlassStyleBar;
    Preference mSoftKeyActions;
    Preference mSearchPanelActions;
    ListPreference mUiMode;
    PreferenceScreen mCapScreen;
    PreferenceScreen mNavbarScreen;
    PreferenceScreen mSystembarScreen;

    /*
     * is this a true capkey device? is not
     * give extra warning in UI mode summary
     */
    boolean mIsCapKey;

    public NavigationHandler(PreferenceScreen pref, OnActivityRequestedListener listener) {
        super(pref);
        mListener = listener;
        reloadUiModeScreen(mRoot);
        init();
    }

    protected void init() {
        pc_ui = (PreferenceCategory) mRoot.findPreference(CATEGORY_UI_MODE_KEY);
        pc_expanded = (PreferenceCategory) mRoot.findPreference(CATEGORY_EXPANDED_DESKTOP);
        pc_style = (PreferenceCategory) mRoot.findPreference(CATEGORY_STYLE_KEY);
        pc_action = (PreferenceCategory) mRoot.findPreference(CATEGORY_ACTIONS_KEY);
        pc_capkey = (PreferenceCategory) mRoot.findPreference(CATEGORY_CAPKEY);
        mIsCapKey = EOSUtils.isCapKeyDevice(mContext);

        if (pc_style != null)
            mLowProfileNavBar = (ListPreference) pc_style.findPreference(STYLE_SIZE_KEY);
        if (pc_style != null)
            mNxStyleBar = (CheckBoxPreference) pc_style.findPreference(STYLE_NX_KEY);
        if (pc_style != null)
            mGlassStyleBar = (CheckBoxPreference) pc_style.findPreference(STYLE_GLASS_KEY);
        if (pc_action != null)
            mSoftKeyActions = (Preference) pc_action.findPreference(Utils.SOFTKEY_FRAG_TAG);
        if (pc_action != null)
            mSearchPanelActions = (Preference) pc_action
                    .findPreference(Utils.SEARCH_PANEL_FRAG_TAG);
        if (pc_expanded != null) {
            mExpandedStyle = (ListPreference) pc_expanded.findPreference(EXPANDED_STYLE_KEY);
        }

        mUiMode = (ListPreference) pc_ui.findPreference(UI_MODE_KEY);
        int uiVal = Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_UI_MODE,
                EOSUtils.isCapKeyDevice(mContext) ? EOSConstants.SYSTEMUI_UI_MODE_NO_NAVBAR
                        : EOSConstants.SYSTEMUI_UI_MODE_NAVBAR);
        mUiMode.setDefaultValue(String.valueOf(uiVal));
        mUiMode.setValue(String.valueOf(uiVal));
        updateUiSummary();

        mUiMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int val = Integer.parseInt(((String) newValue).toString());
                Settings.System.putInt(mResolver,
                        EOSConstants.SYSTEMUI_UI_MODE,
                        (val));
                sendIntentToWindowManager(EOSConstants.INTENT_EOS_UI_CHANGED_KEY_MODE, true);
                reloadUiModeScreen(mRoot);
                init();
                return true;
            }
        });

        if (mExpandedStyle != null) {
            mExpandedStyle.setValue(String.valueOf(Settings.System.getInt(mResolver,
                    Settings.System.EXPANDED_DESKTOP_STYLE, 0)));
            mExpandedStyle
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            int val = Integer.parseInt(((String) newValue).toString());
                            Settings.System.putInt(mResolver,
                                    Settings.System.EXPANDED_DESKTOP_STYLE, val);
                            return true;
                        }
                    });
        }

        if (mLowProfileNavBar != null)
            mLowProfileNavBar
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Settings.System.putInt(mResolver,
                                    EOSConstants.SYSTEMUI_BAR_SIZE_MODE,
                                    Integer.parseInt(((String) newValue)));
                            sendIntentToWindowManager(
                                    EOSConstants.INTENT_EOS_UI_CHANGED_KEY_BAR_SIZE, false);
                            return true;
                        }
                    });

        if (mGlassStyleBar != null)
            mGlassStyleBar
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            boolean enabled = ((Boolean) newValue).booleanValue();
                            Settings.System.putInt(mResolver,
                                    EOSConstants.SYSTEMUI_USE_GLASS, enabled ? 1 : 0);
                            mGlassStyleBar.setChecked(enabled);
                            sendIntentToWindowManager(
                                    EOSConstants.INTENT_EOS_UI_CHANGED_KEY_GLASS_ENABLED, false);
                            return true;
                        }
                    });

        if (mNxStyleBar != null)
            mNxStyleBar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean enabled = ((Boolean) newValue).booleanValue();
                    Settings.System.putInt(mResolver,
                            EOSConstants.SYSTEMUI_USE_NX_NAVBAR, enabled ? 1 : 0);
                    mNxStyleBar.setChecked(enabled);
                    return true;
                }
            });

        if (mSoftKeyActions != null)
            mSoftKeyActions
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            mListener.onActivityRequested(Utils.SOFTKEY_FRAG_TAG);
                            return true;
                        }
                    });

        if (mSearchPanelActions != null)
            mSearchPanelActions
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            mListener.onActivityRequested(Utils.SEARCH_PANEL_FRAG_TAG);
                            return true;
                        }
                    });
    }

    private void reloadUiModeScreen(PreferenceScreen root) {
        int mode = Settings.System.getInt(mResolver,
                EOSConstants.SYSTEMUI_UI_MODE, EOSConstants.SYSTEMUI_UI_MODE_NAVBAR);
        int screenRes;

        switch (mode) {
            case EOSConstants.SYSTEMUI_UI_MODE_NO_NAVBAR:
                screenRes = R.xml.ui_mode_no_nav;
                break;
            case EOSConstants.SYSTEMUI_UI_MODE_NAVBAR:
                screenRes = R.xml.ui_mode_nav;
                break;
            case EOSConstants.SYSTEMUI_UI_MODE_NAVBAR_LEFT:
                screenRes = R.xml.ui_mode_nav_left;
                break;
            case EOSConstants.SYSTEMUI_UI_MODE_SYSTEMBAR:
                screenRes = R.xml.ui_mode_systembar;
                break;
            default:
                screenRes = R.xml.ui_mode_nav;
        }
        root.removeAll();
        root.getPreferenceManager().inflateFromResource(mContext, screenRes, root);
    }

    private void sendIntentToWindowManager(String key, boolean shouldRestartUI) {
        Intent intent = new Intent()
                .setAction(EOSConstants.INTENT_EOS_UI_CHANGED)
                .putExtra(EOSConstants.INTENT_EOS_UI_CHANGED_REASON, key)
                .putExtra(EOSConstants.INTENT_EOS_UI_CHANGED_KEY_RESTART_SYSTEMUI, shouldRestartUI);
        mContext.sendBroadcastAsUser(intent, new UserHandle(UserHandle.USER_ALL));
    }

    private void updateUiSummary() {
        String[] entries = mContext.getResources().getStringArray(R.array.systemui_mode_entries);
        String[] vals = mContext.getResources().getStringArray(R.array.systemui_mode_values);
        String currentVal = mUiMode.getValue();
        String newEntry = "";
        for (int i = 0; i < vals.length; i++) {
            if (vals[i].equals(currentVal)) {
                newEntry = entries[i];
                break;
            }
        }
        StringBuilder b = new StringBuilder()
                .append(mContext.getResources().getString(
                        R.string.eos_interface_systemui_mode_summary))
                .append(newEntry);
        if (!mIsCapKey && currentVal.equals("0")) {
            b.append(mContext.getResources().getString(
                    R.string.eos_interface_systemui_mode_capkey_warning));
        }
        mUiMode.setSummary(b.toString());
    }

}
