
package org.kat.controlcenter;

import android.os.Bundle;

public class SystemFragment extends PreferenceListFragment {
    
    public static SystemFragment newInstance(int xml) {
        SystemFragment frag = new SystemFragment(xml);
        Bundle b = new Bundle();
        b.putInt("xml", xml);
        frag.setArguments(b);
        return frag;
    }

    public SystemFragment(int xml) {
        super(xml);
    }

    public SystemFragment() {}

}
