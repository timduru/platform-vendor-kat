
package org.kat.controlcenter;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

import java.util.ArrayList;

public class LegacyTogglesFragment extends ItemOrderFragment {

    public static LegacyTogglesFragment newInstance(Bundle args) {
        LegacyTogglesFragment frag = new LegacyTogglesFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Legacy Toggles");
        }
        frag.setArguments(args);
        return frag;
    }

    public static LegacyTogglesFragment newInstance() {
        LegacyTogglesFragment frag = new LegacyTogglesFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Legacy Toggles");
        frag.setArguments(args);
        return frag;
    }

    public LegacyTogglesFragment(Bundle args) {
        newInstance(args);
    }

    public LegacyTogglesFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateFragment();
    }

    @Override
    protected String getArrayListUri() {
        return EOSConstants.SYSTEMUI_SETTINGS_ENABLED_CONTROLS;
    }

    @Override
    protected String getPrefsKey() {
        return Utils.LEGACY_TOGGLES_PREFS_KEY;
    }

    @Override
    protected String[] getEnabledValues() {
        String enabledControls = Settings.System.getString(mResolver,
                EOSConstants.SYSTEMUI_SETTINGS_ENABLED_CONTROLS);
        if (enabledControls != null) {
            String[] controls = enabledControls.split("\\|");
            return controls;
        } else {
            return EOSConstants.SYSTEMUI_SETTINGS_DEFAULTS;
        }
    }

    @Override
    protected ArrayList<Pair<String, String>> getEntryMap() {
        boolean hasData = EOSUtils.hasData(mContext);
        boolean isCdmaLte = EOSUtils.isCdmaLTE(mContext);
        boolean hasTorch = EOSUtils.hasTorch();
        ArrayList<Pair<String, String>> map = new ArrayList<Pair<String, String>>();
        String[] entries = mContext.getResources().getStringArray(
                R.array.eos_quick_enabled_names);
        String[] values = mContext.getResources().getStringArray(
                R.array.eos_quick_enabled_preferences);
        for (int i = 0; i < entries.length; i++) {
            if (!hasData &&
                    (values[i].equals(EOSConstants.SYSTEMUI_SETTINGS_MOBILEDATA)
                            || values[i].equals(EOSConstants.SYSTEMUI_SETTINGS_WIFITETHER)
                            || values[i].equals(EOSConstants.SYSTEMUI_SETTINGS_USBTETHER))) {
                continue;
            }
            if (!isCdmaLte && values[i].equals(EOSConstants.SYSTEMUI_SETTINGS_LTE)) {
                continue;
            }
            if (!hasTorch && values[i].equals(EOSConstants.SYSTEMUI_SETTINGS_TORCH)) {
                continue;
            }
            map.add(Pair.create(entries[i], values[i]));
        }
        return map;
    }
}
