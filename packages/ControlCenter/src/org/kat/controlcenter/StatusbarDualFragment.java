
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class StatusbarDualFragment extends PreferenceFragment {

    public static StatusbarDualFragment newInstance(Bundle args) {
        StatusbarDualFragment frag = new StatusbarDualFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, Utils.STATUSBAR_SETTINGS_TITLE);
            args.putInt(Utils.FRAG_POSITION_KEY, 2);
        }
        frag.setArguments(args);
        return frag;
    }

    public static StatusbarDualFragment newInstance() {
        StatusbarDualFragment frag = new StatusbarDualFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, Utils.STATUSBAR_SETTINGS_TITLE);
        args.putInt(Utils.FRAG_POSITION_KEY, 2);
        frag.setArguments(args);
        return frag;
    }

    public StatusbarDualFragment(Bundle args) {
        newInstance(args);
    }

    public StatusbarDualFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar);
        new StatusbarHandler(this.getPreferenceScreen(),
                (OnActivityRequestedListener) getActivity());
    }
}
