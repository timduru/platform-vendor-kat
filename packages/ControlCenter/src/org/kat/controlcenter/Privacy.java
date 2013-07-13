
package org.kat.controlcenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

public class Privacy extends PreferenceFragment implements
        OnPreferenceChangeListener {
    
    public static Privacy newInstance(Bundle args) {
        Privacy frag = new Privacy();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Privacy");
        }
        frag.setArguments(args);
        return frag;
    }

    public static Privacy newInstance() {
        Privacy frag = new Privacy();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Privacy");
        frag.setArguments(args);
        return frag;
    }

    public Privacy(Bundle args) {
        newInstance(args);
    }

    public Privacy() {
    }

    private static final String LOGGER_PACKAGES = "eos_logger_packages";
    private static final String LOGGER_MODE = "eos_logger_mode";

    public static final String UIDS_MODE_FILE = "uids_mode";
    public static final String UIDS_LIST_FILE = "uids_list";

    public static final String UIDS_MODE_NORMAL = "none";
    public static final String UIDS_MODE_BLACKLIST = "blacklist";
    public static final String UIDS_MODE_WHITELIST = "whitelist";

    private Context mContext;
    private ListPreference mLoggerModePreference;
    private Preference mLoggerPackages;
    private OnActivityRequestedListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        try {
            mListener = ((OnActivityRequestedListener) getActivity());
        } catch (Exception e) {
            Log.i("Privacy Settings",
                    "Calling activity must implement OnActivityRequestedListener!");
        }

        addPreferencesFromResource(R.xml.privacy_settings);

        mLoggerModePreference = (ListPreference) findPreference(LOGGER_MODE);
        mLoggerModePreference.setOnPreferenceChangeListener(this);

        mLoggerPackages = findPreference(LOGGER_PACKAGES);
        mLoggerPackages.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mListener != null) {
                    mListener.onActivityRequested(Utils.PRIVACY_LOG_PACKAGES);
                    return true;
                }
                return false;
            }
        });

        File loggingMode = new File(mContext.getDir("eos", Context.MODE_PRIVATE), UIDS_MODE_FILE);
        String input = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(loggingMode));
            input = br.readLine();
            br.close();
        } catch (IOException e) {
        }

        if (input != null && input.equals(UIDS_MODE_NORMAL)) {
            mLoggerModePreference.setValueIndex(0);
            setLoggerListEnabled(UIDS_MODE_NORMAL);
        } else if (input != null && input.equals(UIDS_MODE_BLACKLIST)) {
            mLoggerModePreference.setValueIndex(1);
            setLoggerListEnabled(UIDS_MODE_BLACKLIST);
        } else if (input != null && input.equals(UIDS_MODE_WHITELIST)) {
            mLoggerModePreference.setValueIndex(2);
            setLoggerListEnabled(UIDS_MODE_WHITELIST);
        } else {
            mLoggerModePreference.setValueIndex(0);
            setLoggerListEnabled(UIDS_MODE_NORMAL);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.equals(mLoggerModePreference)) {
            String newMode = ((String) newValue);
            if (newMode.equals(UIDS_MODE_NORMAL)) {
                mLoggerModePreference.setValueIndex(0);
            } else if (newMode.equals(UIDS_MODE_BLACKLIST)) {
                mLoggerModePreference.setValueIndex(1);
            } else if (newMode.equals(UIDS_MODE_WHITELIST)) {
                mLoggerModePreference.setValueIndex(2);
            } else {
                mLoggerModePreference.setValueIndex(0);
            }
            setLoggerListEnabled(newMode);
            File loggingMode = new File(mContext.getDir("eos", Context.MODE_PRIVATE),
                    UIDS_MODE_FILE);
            try {
                FileWriter writer = new FileWriter(loggingMode);
                writer.write(newMode + "\n");
                writer.close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private void setLoggerListEnabled(String mode) {
        mLoggerPackages.setEnabled(!mode.equals(UIDS_MODE_NORMAL));
    }
}
