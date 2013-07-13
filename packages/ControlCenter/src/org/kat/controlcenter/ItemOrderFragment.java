
package org.kat.controlcenter;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.util.Pair;

import org.kat.controlcenter.OrderPreference.OnPositionChangedListener;

import java.util.ArrayList;

public abstract class ItemOrderFragment extends PreferenceFragment implements
        OnPositionChangedListener {

    private static final String TOGGLE_CATEGORY = "eos_item_order_enabled";

    protected Context mContext;
    protected ContentResolver mResolver;

    private ArrayList<Pair<String, String>> mEntryMap;
    private String[] mEntries;;
    private String[] mEntryValues;
    private String[] mEnabledValues;
    private String mTargetUri;

    private PreferenceCategory mToggleCategory;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (Context) getActivity();
        mResolver = mContext.getContentResolver();

        addPreferencesFromResource(R.xml.item_order_fragment);
        mToggleCategory = (PreferenceCategory) findPreference(TOGGLE_CATEGORY);
    }

    public void populateFragment() {
        // get required values from subclass
        mEntryMap = (ArrayList<Pair<String, String>>) getEntryMap().clone();
        mEnabledValues = getEnabledValues();
        mTargetUri = getArrayListUri();

        // parse entry/value map from subclass
        mEntries = new String[mEntryMap.size()];
        mEntryValues = new String[mEntryMap.size()];
        int i = 0;
        for (Pair<String, String> pair : mEntryMap) {
            mEntries[i] = pair.first;
            mEntryValues[i] = pair.second;
            i++;
        }

        // next populate a list of all possible prefs
        ArrayList<OrderPreference> holder = new ArrayList<OrderPreference>();
        for (int j = 0; j < mEntryValues.length; j++) {
            OrderPreference pref = new OrderPreference(mContext);
            pref.setValues(mEntries[j], mEntryValues[j]);
            pref.setOnOrderChangedListener((OnPositionChangedListener) this);
            holder.add(pref);
        }

        // resequence based on shared prefs
        String rawList = Utils.getToggleOrderFromPrefs(mContext, getPrefsKey());
        if (!rawList.equals(Utils.URI_GRAVEYARD)) {
            String[] splitList = rawList.split("\\|");
            for (OrderPreference pref : holder) {
                for (int j = 0; j < splitList.length; j++) {
                    if (pref.getKey().equals(splitList[j])) {
                        pref.setOrder(j);
                        break;
                    }
                }
            }
        }

        // now set enabled or disabled accordingly
        for (int k = 0; k < mEnabledValues.length; k++) {
            for (OrderPreference pref : holder) {
                if (pref.getKey().equals(mEnabledValues[k])) {
                    pref.setToggleEnabled(true);
                    break;
                }
            }
        }

        // in case preference fragment is recreated
        mToggleCategory.removeAll();

        // prefs are now processed and can be added to category
        for (OrderPreference pref : holder) {
            mToggleCategory.addPreference(pref);
        }

        // cleanup
        holder.clear();
        holder = null;
    }

    protected abstract String getArrayListUri();

    protected abstract String getPrefsKey();

    protected abstract String[] getEnabledValues();

    protected abstract ArrayList<Pair<String, String>> getEntryMap();

    private void writeToUri() {
        StringBuilder enabledOrder = new StringBuilder();
        StringBuilder allOrder = new StringBuilder();
        for (int i = 0; i < mToggleCategory.getPreferenceCount(); i++) {
            OrderPreference pref = (OrderPreference) mToggleCategory.getPreference(i);
            StringBuilder aKey = new StringBuilder();
            aKey.append(pref.getKey()).append("|");
            allOrder.append(aKey.toString());
            if (pref.isToggleEnabled()) {
                enabledOrder.append(aKey.toString());
            }
        }
        Settings.System.putString(mResolver, mTargetUri, enabledOrder.toString());
        Utils.commitToggleOrder(mContext, getPrefsKey(), allOrder.toString());
    }

    @Override
    public void onStateChanged() {
        writeToUri();
    }

    @Override
    public void onPositionChanged(OrderPreference pref, int order) {
        int count = mToggleCategory.getPreferenceCount();
        int index = pref.getOrder();
        if (index == 0 && order == OrderPreference.UP)
            return;
        if (index == count - 1 && order == OrderPreference.DOWN)
            return;
        if (order == OrderPreference.UP) {
            ((OrderPreference) mToggleCategory.getPreference(index - 1)).setOrder(index);
            index--;
        } else {
            ((OrderPreference) mToggleCategory.getPreference(index + 1)).setOrder(index);
            index++;
        }
        pref.setOrder(index);
        writeToUri();
    }
}
