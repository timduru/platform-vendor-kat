
package org.kat.controlcenter;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

import java.util.ArrayList;

public class SettingsPanelFragment extends ItemOrderFragment {

    public static SettingsPanelFragment newInstance(Bundle args) {
        SettingsPanelFragment frag = new SettingsPanelFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Quick Settings");
        }
        frag.setArguments(args);
        return frag;
    }

    public static SettingsPanelFragment newInstance() {
        SettingsPanelFragment frag = new SettingsPanelFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Quick Settings");
        frag.setArguments(args);
        return frag;
    }

    public SettingsPanelFragment(Bundle args) {
        newInstance(args);
    }

    public SettingsPanelFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateFragment();
    }

    @Override
    protected String getArrayListUri() {
        return EOSConstants.SYSTEMUI_PANEL_ENABLED_TILES;
    }

    @Override
    protected String getPrefsKey() {
        return Utils.QS_PREFS_KEY;
    }

    @Override
    protected String[] getEnabledValues() {
        String enabledControls = Settings.System.getString(mResolver,
                EOSConstants.SYSTEMUI_PANEL_ENABLED_TILES);
        if (enabledControls != null) {
            String[] controls = enabledControls.split("\\|");
            return controls;
        } else {
            return EOSConstants.SYSTEMUI_PANEL_DEFAULTS;
        }
    }

    @Override
    protected ArrayList<Pair<String, String>> getEntryMap() {
        boolean hasData = EOSUtils.hasData(mContext);
        boolean isCdmaLte = EOSUtils.isCdmaLTE(mContext);
        boolean hasTorch = EOSUtils.hasTorch();
        boolean hasGSM = EOSUtils.isGSM(mContext);

        ArrayList<Pair<String, String>> map = new ArrayList<Pair<String, String>>();
        String[] entries = mContext.getResources().getStringArray(
                R.array.eos_panel_enabled_names);
        String[] values = mContext.getResources().getStringArray(
                R.array.eos_panel_enabled_preferences);
        for (int i = 0; i < entries.length; i++) {
            if (!hasData &&
                    (values[i].equals(EOSConstants.SYSTEMUI_PANEL_DATA_TILE)
                            || values[i].equals(EOSConstants.SYSTEMUI_PANEL_WIFIAP_TILE)
                            || values[i].equals(EOSConstants.SYSTEMUI_PANEL_2G3G_TILE))) {
                continue;
            }
            if (!isCdmaLte && values[i].equals(EOSConstants.SYSTEMUI_PANEL_LTE_TILE)) {
                continue;
            }
            if (!hasTorch && values[i].equals(EOSConstants.SYSTEMUI_PANEL_TORCH_TILE)) {
                continue;
            }
            if (!hasGSM && values[i].equals(EOSConstants.SYSTEMUI_PANEL_2G3G_TILE)) {
                continue;
            }
            map.add(Pair.create(entries[i], values[i]));
        }
        return map;
    }
}
