
package org.kat.controlcenter;

import android.os.Bundle;
import android.provider.Settings;

import org.teameos.jellybean.settings.EOSConstants;

public class SearchPanelActions extends ActionFragment {

    public static SearchPanelActions newInstance(Bundle args) {
        SearchPanelActions frag = new SearchPanelActions();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "SearchPanel Actions");
        }
        frag.setArguments(args);
        return frag;
    }

    public static SearchPanelActions newInstance() {
        SearchPanelActions frag = new SearchPanelActions();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "SearchPanel Actions");
        frag.setArguments(args);
        return frag;
    }

    public SearchPanelActions(Bundle args) {
        newInstance(args);
    }

    public SearchPanelActions() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.search_panel_settings);

        addActionPreference((ActionPreference) findPreference("eos_interface_navring_1_release"));
        addActionPreference((ActionPreference) findPreference("eos_interface_navring_2_release"));
        addActionPreference((ActionPreference) findPreference("eos_interface_navring_3_release"));

        String target2 = Settings.System.getString(mContext.getContentResolver(),
                EOSConstants.SYSTEMUI_NAVRING_2);
        if (target2 == null || target2.equals("")) {
            Settings.System.putString(mContext.getContentResolver(),
                    EOSConstants.SYSTEMUI_NAVRING_2, "assist");
        }
    }
}
