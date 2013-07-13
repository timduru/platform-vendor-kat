
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class InfoDualFragment extends PreferenceFragment {

    public static InfoDualFragment newInstance(Bundle args) {
        InfoDualFragment frag = new InfoDualFragment();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Info");
        }
        frag.setArguments(args);
        return frag;
    }

    public static InfoDualFragment newInstance() {
        InfoDualFragment frag = new InfoDualFragment();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Info");
        frag.setArguments(args);
        return frag;
    }

    public InfoDualFragment(Bundle args) {
        newInstance(args);
    }

    public InfoDualFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.info);
        new InfoHandler(this.getPreferenceScreen());
    }
}
