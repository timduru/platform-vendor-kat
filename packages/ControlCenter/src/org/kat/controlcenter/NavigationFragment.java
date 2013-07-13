package org.kat.controlcenter;

import android.os.Bundle;

public class NavigationFragment extends PreferenceListFragment {


    public static NavigationFragment newInstance(int xml) {
        NavigationFragment frag = new NavigationFragment(xml);
        Bundle b = new Bundle();
        b.putInt("xml", xml);
        frag.setArguments(b);
        return frag;
    }

    public NavigationFragment(int xmlId) {
        // TODO Auto-generated constructor stub
        super(xmlId);
    }

    public NavigationFragment() {}
}
