
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SystemDualFragment extends PreferenceFragment {

    public static SystemDualFragment newInstance(Bundle args) {
        SystemDualFragment frag = new SystemDualFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, Utils.SYSTEM_SETTINGS_TITLE);
            args.putInt(Utils.FRAG_POSITION_KEY, 3);
        }
        frag.setArguments(args);
        return frag;
    }

    public static SystemDualFragment newInstance() {
        SystemDualFragment frag = new SystemDualFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, Utils.SYSTEM_SETTINGS_TITLE);
        args.putInt(Utils.FRAG_POSITION_KEY, 3);
        frag.setArguments(args);
        return frag;
    }

    public SystemDualFragment(Bundle args) {
        newInstance(args);
    }

    public SystemDualFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.system_settings);
        new SystemHandler(this.getPreferenceScreen(),
                (OnActivityRequestedListener) getActivity());
    }
}
