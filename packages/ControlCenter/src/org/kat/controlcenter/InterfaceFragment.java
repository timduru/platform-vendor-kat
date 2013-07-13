package org.kat.controlcenter;

import android.os.Bundle;

public class InterfaceFragment extends PreferenceListFragment {


    public static InterfaceFragment newInstance(int xml) {
        InterfaceFragment frag = new InterfaceFragment(xml);
        Bundle b = new Bundle();
        b.putInt("xml", xml);
        frag.setArguments(b);
        return frag;
    }

    public InterfaceFragment(int xmlId) {
        // TODO Auto-generated constructor stub
        super(xmlId);
    }

    public InterfaceFragment() {}
}
