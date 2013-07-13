package org.kat.controlcenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceScreen;

public abstract class PreferenceScreenHandler {
    protected Context mContext;
    protected ContentResolver mResolver;
    protected Resources mRes;
    protected PreferenceScreen mRoot;
    
    public PreferenceScreenHandler(PreferenceScreen pref) {
        mRoot = pref;
        mContext = pref.getContext();
        mResolver = pref.getContext().getContentResolver();
        mRes = pref.getContext().getResources();
    }
    
    protected abstract void init();
}
