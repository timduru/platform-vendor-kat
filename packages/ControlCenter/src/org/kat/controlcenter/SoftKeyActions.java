
package org.kat.controlcenter;

import android.os.Bundle;

public class SoftKeyActions extends ActionFragment {

    public static SoftKeyActions newInstance(Bundle args) {
        SoftKeyActions frag = new SoftKeyActions();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Softkey Actions");
        }
        frag.setArguments(args);
        return frag;
    }

    public static SoftKeyActions newInstance() {
        SoftKeyActions frag = new SoftKeyActions();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Softkey Actions");
        frag.setArguments(args);
        return frag;
    }

    public SoftKeyActions(Bundle args) {
        newInstance(args);
    }

    public SoftKeyActions() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.softkey_settings);

        addActionPreference((ActionPreference) findPreference("eos_interface_softkey_back_longpress"));
        addActionPreference((ActionPreference) findPreference("eos_interface_softkey_recent_longpress"));
        addActionPreference((ActionPreference) findPreference("eos_interface_softkey_menu_longpress"));
    }
}
