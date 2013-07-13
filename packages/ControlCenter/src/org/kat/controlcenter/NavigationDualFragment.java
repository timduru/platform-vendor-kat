
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class NavigationDualFragment extends PreferenceFragment {

    public static NavigationDualFragment newInstance(Bundle args) {
        NavigationDualFragment frag = new NavigationDualFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, Utils.NAVBAR_SETTINGS_TITLE);
            args.putInt(Utils.FRAG_POSITION_KEY, 1);
        }
        frag.setArguments(args);
        return frag;
    }

    public static NavigationDualFragment newInstance() {
        NavigationDualFragment frag = new NavigationDualFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, Utils.NAVBAR_SETTINGS_TITLE);
        args.putInt(Utils.FRAG_POSITION_KEY, 1);
        frag.setArguments(args);
        return frag;
    }

    public NavigationDualFragment(Bundle args) {
        newInstance(args);
    }

    public NavigationDualFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_bar);
        new NavigationHandler(this.getPreferenceScreen(),
                (OnActivityRequestedListener) getActivity());
    }
}
