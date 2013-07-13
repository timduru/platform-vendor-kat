
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class InterfaceDualFragment extends PreferenceFragment {

    public static InterfaceDualFragment newInstance(Bundle args) {
        InterfaceDualFragment frag = new InterfaceDualFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, Utils.INTERFACE_SETTINGS_TITLE);
            args.putInt(Utils.FRAG_POSITION_KEY, 0);
        }
        frag.setArguments(args);
        return frag;
    }

    public static InterfaceDualFragment newInstance() {
        InterfaceDualFragment frag = new InterfaceDualFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, Utils.INTERFACE_SETTINGS_TITLE);
        args.putInt(Utils.FRAG_POSITION_KEY, 0);
        frag.setArguments(args);
        return frag;
    }

    public InterfaceDualFragment(Bundle args) {
        newInstance(args);
    }

    public InterfaceDualFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.interface_settings);
        new InterfaceHandler(this.getPreferenceScreen());
    }
}
