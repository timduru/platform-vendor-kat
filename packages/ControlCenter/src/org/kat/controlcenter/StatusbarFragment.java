package org.kat.controlcenter;

import android.os.Bundle;

public class StatusbarFragment extends PreferenceListFragment {


    public static StatusbarFragment newInstance(int xml) {
        StatusbarFragment frag = new StatusbarFragment(xml);
        Bundle b = new Bundle();
        b.putInt("xml", xml);
        frag.setArguments(b);
        return frag;
    }

    public StatusbarFragment(int xmlId) {
        // TODO Auto-generated constructor stub
        super(xmlId);
    }

    public StatusbarFragment() {}
}
