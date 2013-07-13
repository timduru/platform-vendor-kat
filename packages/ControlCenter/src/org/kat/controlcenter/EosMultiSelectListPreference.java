
package org.kat.controlcenter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class EosMultiSelectListPreference extends DialogPreference {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private LinkedHashSet<String> mValues;
    private LinkedHashSet<String> mNewValues;
    private LinkedHashMap<String, Boolean> mChangedValues;
    private boolean mPreferenceChanged;

    private boolean mReturnFullList = false;

    public EosMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mValues = new LinkedHashSet<String>();
        mNewValues = new LinkedHashSet<String>();
        mChangedValues = new LinkedHashMap<String, Boolean>();
    }

    public EosMultiSelectListPreference(Context context) {
        this(context, null);
    }

    public CharSequence[] getEntries() {
        return mEntries;
    }

    public void setEntries(CharSequence[] entries) {
        mEntries = entries;
    }

    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    public void setEntryValues(CharSequence[] values) {
        mEntryValues = values;
    }

    public HashSet<String> getValues() {
        return mValues;
    }

    public void setValues(HashSet<String> values) {
        mValues.clear();
        mValues.addAll(values);
    }

    public void removeValueEntry(String entry) {
        int location = -1;
        for (int i = 0; i < mEntryValues.length; i++) {
            if (mEntryValues[i].toString().equals(entry)) {
                location = i;
                break;
            }
        }

        if (location < 0)
            return;

        CharSequence[] newEntries = new CharSequence[mEntries.length - 1];
        CharSequence[] newEntryValues = new CharSequence[mEntryValues.length - 1];

        {
            int i = 0;
            int j = 0;
            if (i == location)
                i++;
            while (i < mEntryValues.length) {
                newEntries[j] = mEntries[i];
                newEntryValues[j] = mEntryValues[i];

                i++;
                j++;
                if (i == location)
                    i++;
            }
        }
        mEntries = newEntries;
        mEntryValues = newEntryValues;

        mValues.remove(entry);
    }

    public int findIndexOfValue(String value) {
        if (value != null && mEntryValues != null) {
            for (int i = mEntryValues.length - 1; i >= 0; i--) {
                if (mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setReturnFullList(boolean value) {
        mReturnFullList = value;
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);

        if (mEntries == null || mEntryValues == null) {
            throw new IllegalStateException(
                    "MultiSelectListPreference requires an entries array and "
                            + "an entryValues array.");
        }

        mNewValues.clear();
        mNewValues.addAll(mValues);
        mChangedValues.clear();

        boolean[] checkedItems = getSelectedItems();
        builder.setMultiChoiceItems(mEntries, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mPreferenceChanged |= mNewValues.add(mEntryValues[which].toString());
                            mChangedValues.put(mEntryValues[which].toString(), true);
                        } else {
                            mPreferenceChanged |= mNewValues.remove(mEntryValues[which].toString());
                            mChangedValues.put(mEntryValues[which].toString(), false);
                        }
                    }
                });
    }

    private boolean[] getSelectedItems() {
        final CharSequence[] entries = mEntryValues;
        final int entryCount = entries.length;
        final Set<String> values = mValues;
        boolean[] result = new boolean[entryCount];

        for (int i = 0; i < entryCount; i++) {
            result[i] = values.contains(entries[i].toString());
        }

        return result;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult && mPreferenceChanged) {
            final HashMap<String, Boolean> values;

            if (mReturnFullList) {
                values = new LinkedHashMap<String, Boolean>();
                for (String i : mNewValues)
                    values.put(i, true);
            } else {
                values = mChangedValues;
            }
            if (callChangeListener(values)) {
                setValues(mNewValues);
            }
        }
        mPreferenceChanged = false;
    }
}
