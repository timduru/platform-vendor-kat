/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kat.controlcenter;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

public class Performance extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static Performance newInstance(Bundle args) {
        Performance frag = new Performance();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Performance");
        }
        frag.setArguments(args);
        return frag;
    }

    public static Performance newInstance() {
        Performance frag = new Performance();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Performance");
        frag.setArguments(args);
        return frag;
    }

    public Performance(Bundle args) {
        newInstance(args);
    }

    public Performance() {
    }

    private static final String FC_CATEGORY = "eos_settings_fast_charge";
    private static final String S2W_CATEGORY = "eos_settings_sweep2wake";
    private static final String ZRAM_CATEGORY = "eos_settings_zram";

    private CheckBoxPreference mClocksOnBootPreference;
    private CheckBoxPreference mIoSchedOnBootPreference;
    private CheckBoxPreference mZramPref;

    private ListPreference mClocksMinPreference;
    private ListPreference mClocksMaxPreference;
    private ListPreference mClocksGovPreference;
    private ListPreference mIoSchedPreference;
    private CheckBoxPreference mFastChargePreference;
    private CheckBoxPreference mEnableSweep2Wake;
    private CheckBoxPreference mSweep2WakeOnBoot;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.performance_settings);

        mContext = (Context) getActivity();

        mClocksOnBootPreference = (CheckBoxPreference) findPreference("eos_performance_cpu_set_on_boot");
        mClocksOnBootPreference.setOnPreferenceChangeListener(this);
        mClocksOnBootPreference.setChecked(Utils
                .prefFlagExists(mContext, Utils.CLOCKS_ON_BOOT_PREF));

        mClocksMinPreference = (ListPreference) findPreference("eos_performance_cpu_min");
        mClocksMaxPreference = (ListPreference) findPreference("eos_performance_cpu_max");
        mClocksMinPreference.setOnPreferenceChangeListener(this);
        mClocksMaxPreference.setOnPreferenceChangeListener(this);

        mIoSchedPreference = (ListPreference) findPreference("eos_performance_iosched");
        mIoSchedPreference.setOnPreferenceChangeListener(this);

        mIoSchedOnBootPreference = (CheckBoxPreference) findPreference("eos_performance_iosched_on_boot");
        mIoSchedOnBootPreference.setChecked(Utils.prefFlagExists(mContext, Utils.IOSCHED_PREF));
        mIoSchedOnBootPreference.setOnPreferenceChangeListener(this);

        String[] frequencies = Utils.readKernelList(Utils.CPU_AVAIL_FREQ);

        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = Utils.appendClockSuffix(frequencies[i]);
        }
        mClocksMinPreference.setEntries(frequencies);
        mClocksMinPreference.setEntryValues(frequencies);
        mClocksMaxPreference.setEntries(frequencies);
        mClocksMaxPreference.setEntryValues(frequencies);

        mClocksGovPreference = (ListPreference) findPreference("eos_performance_cpu_governor");
        mClocksGovPreference.setOnPreferenceChangeListener(this);

        String[] governors = Utils.readKernelList(Utils.CPU_AVAIL_GOV);
        mClocksGovPreference.setEntries(governors);
        mClocksGovPreference.setEntryValues(governors);

        if (!Utils.hasKernelFeature(Utils.FFC_PATH)) {
            final PreferenceCategory fc = (PreferenceCategory) getPreferenceScreen()
                    .findPreference(FC_CATEGORY);
            getPreferenceScreen().removePreference(fc);
        } else {
            mFastChargePreference = (CheckBoxPreference) findPreference("eos_performance_fast_charge");
            mFastChargePreference.setOnPreferenceChangeListener(this);
        }

        if (!Utils.hasKernelFeature(Utils.S2W_PATH)) {
            final PreferenceCategory s2w = (PreferenceCategory) getPreferenceScreen()
                    .findPreference(S2W_CATEGORY);
            getPreferenceScreen().removePreference(s2w);
        } else {
            mEnableSweep2Wake = (CheckBoxPreference) findPreference("eos_performance_sweep2wake_enable");
            mEnableSweep2Wake.setOnPreferenceChangeListener(this);

            mSweep2WakeOnBoot = (CheckBoxPreference) findPreference("eos_performance_sweep2wake_set_on_boot");
            mSweep2WakeOnBoot.setChecked(Utils.prefFlagExists(mContext, Utils.S2W_PREF));
            mSweep2WakeOnBoot.setOnPreferenceChangeListener(this);
        }

        if (!Utils.hasKernelFeature(Utils.ZRAM)) {
            final PreferenceCategory zram = (PreferenceCategory) getPreferenceScreen()
                    .findPreference(ZRAM_CATEGORY);
            getPreferenceScreen().removePreference(zram);
        } else {
            mZramPref = (CheckBoxPreference) findPreference("eos_performance_zram");
            mZramPref.setChecked(Utils.prefFlagExists(mContext, Utils.ZRAM_PREF));
            mZramPref.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Overclocking - setup the selected values */
        try {
            updateCpuPreferenceValues(mClocksMinPreference, CLOCK_TYPE.MIN);
            updateCpuPreferenceValues(mClocksMaxPreference, CLOCK_TYPE.MAX);
            updateCpuPreferenceValues(mClocksGovPreference, CLOCK_TYPE.GOV);
        } catch (IOException e) {
            Log.w("Settings", e.toString());
        }
        updateSchedulerPrefs();
        if (mFastChargePreference != null)
            mFastChargePreference.setChecked(Utils.isKernelFeatureEnabled(Utils.FFC_PATH));
        if (mEnableSweep2Wake != null)
            mEnableSweep2Wake.setChecked(Utils.isKernelFeatureEnabled(Utils.S2W_PATH));
    }

    private void updateSchedulerPrefs() {
        String[] schedulers = Utils.getSchedulers();
        String defSched = Utils.getCurrentScheduler();
        mIoSchedPreference.setEntries(schedulers);
        mIoSchedPreference.setEntryValues(schedulers);
        mIoSchedPreference.setSummary("Current value: " + defSched);
        mIoSchedPreference.setValue(defSched);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference.equals(mClocksOnBootPreference)) {
            if (((Boolean) objValue).booleanValue()) {
                if (!Utils.createPrefFlag(mContext, Utils.CLOCKS_ON_BOOT_PREF))
                    return false;

            } else {
                Utils.deletePrefFlag(mContext, Utils.CLOCKS_ON_BOOT_PREF);
                Utils.deletePrefFlag(mContext, Utils.MIN_PREF);
                Utils.deletePrefFlag(mContext, Utils.MAX_PREF);
                Utils.deletePrefFlag(mContext, Utils.GOV_PREF);
            }

            try {
                updateCpuPreferenceValues(mClocksMinPreference, CLOCK_TYPE.MIN);
            } catch (Exception e) {
            }
            try {
                updateCpuPreferenceValues(mClocksMaxPreference, CLOCK_TYPE.MAX);
            } catch (Exception e) {
            }
            try {
                updateCpuPreferenceValues(mClocksGovPreference, CLOCK_TYPE.GOV);
            } catch (Exception e) {
            }
        } else if (preference.equals(mClocksMinPreference) ||
                preference.equals(mClocksMaxPreference)) {
            try {
                CLOCK_TYPE clockType = null;
                String bootFile = null;
                if (preference.equals(mClocksMinPreference)) {
                    clockType = CLOCK_TYPE.MIN;
                    bootFile = Utils.MIN_PREF;
                }
                else {
                    clockType = CLOCK_TYPE.MAX;
                    bootFile = Utils.MAX_PREF;
                }

                String output = Utils.removeClockSuffix((String) objValue);

                Utils.writePrefValue(mContext, bootFile, output);

                if (!writeToCpuFiles(clockType, output))
                    return false; // Writing the files failed, so don't proceed
                                  // further.

                updateCpuPreferenceValues((ListPreference) preference, clockType);
            } catch (IOException e) {
                Log.d("Settings", e.toString());
                return false;
            }
        } else if (preference.equals(mClocksGovPreference)) {
            try {
                String newValue = (String) objValue;
                Utils.writePrefValue(mContext, Utils.GOV_PREF, newValue);
                if (!writeToCpuFiles(CLOCK_TYPE.GOV, newValue))
                    return false; // Writing the files failed, so don't proceed
                                  // further.

                updateCpuPreferenceValues(mClocksGovPreference, CLOCK_TYPE.GOV);
            } catch (IOException e) {
                Log.d("Settings", e.toString());
                return false;
            }
        } else if (preference.equals(mIoSchedPreference)) {
            Utils.writeKernelValue(Utils.IO_SCHED, (String) objValue);
            updateSchedulerPrefs();
            if (mIoSchedOnBootPreference.isChecked()) {
                Utils.writePrefValue(mContext, Utils.IOSCHED_PREF, ((String) objValue));
            }
        } else if (preference.equals(mIoSchedOnBootPreference)) {
            if (((Boolean) objValue).booleanValue()) {
                return Utils.createPrefFlag(mContext, Utils.IOSCHED_PREF)
                        && Utils.writePrefValue(mContext, Utils.IOSCHED_PREF,
                                mIoSchedPreference.getValue());
            } else {
                Utils.deletePrefFlag(mContext, Utils.IOSCHED_PREF);
            }
        } else if (preference.equals(mZramPref)) {
            if (((Boolean) objValue).booleanValue()) {
                return Utils.createPrefFlag(mContext, Utils.ZRAM_PREF);
            } else {
                Utils.deletePrefFlag(mContext, Utils.ZRAM_PREF);
            }
        } else if (preference.equals(mEnableSweep2Wake)) {
            Utils.setKernelFeatureEnabled(Utils.S2W_PATH, ((Boolean) objValue).booleanValue());
        } else if (preference.equals(mSweep2WakeOnBoot)) {
            if (((Boolean) objValue).booleanValue()) {
                return Utils.createPrefFlag(mContext, Utils.S2W_PREF);
            } else {
                Utils.deletePrefFlag(mContext, Utils.S2W_PREF);
            }
        } else if (preference.equals(mFastChargePreference)) {
            Utils.setKernelFeatureEnabled(Utils.FFC_PATH, ((Boolean) objValue).booleanValue());
        }
        return true;
    }

    /**
     * Dynamically write the values to the specific cpufrequency files according
     * to the number of cpu's available at run time.
     * 
     * @param fileName String of the file in the cpufrequency directory that you
     *            want to write to.
     * @param contents String of the contents you want to write.
     * @return boolean of whether the writes completed or failed miserably.
     */
    private boolean writeToCpuFiles(CLOCK_TYPE clockType, String contents) {
        String outputFile = null;

        switch (clockType) {
            case MIN:
                outputFile = Utils.CPU_MIN_SCALE;
                break;
            case MAX:
                outputFile = Utils.CPU_MAX_SCALE;
                break;
            case GOV:
                outputFile = Utils.CPU_GOV;
                break;
        }

        return Utils.writeKernelValue(outputFile, contents);
    }

    private void updatePreferenceSummary(Preference preference, String currentValue,
            String bootValue) {
        if (preference == null || currentValue == null)
            throw new IllegalArgumentException(
                    "Preference and currentValue variables cannot be null.");

        StringBuilder newSummary = new StringBuilder();
        newSummary.append(getResources().getString(R.string.eos_performance_current_value));
        newSummary.append(" ");
        newSummary.append(currentValue);

        if (bootValue != null) {
            newSummary.append("\n");
            newSummary.append(getResources().getString(R.string.eos_performance_boot_value));
            newSummary.append("      ");
            newSummary.append(bootValue);
        }

        preference.setSummary(newSummary.toString());
    }

    private void updateCpuPreferenceValues(ListPreference preference, CLOCK_TYPE clockType)
            throws IOException {
        String input = null, currentValue = null, bootValue = null, currentClockFile = null, bootClockFile = null;

        switch (clockType) {
            case MIN:
                currentClockFile = Utils.CPU_MIN_SCALE;
                bootClockFile = Utils.MIN_PREF;
                break;
            case MAX:
                currentClockFile = Utils.CPU_MAX_SCALE;
                bootClockFile = Utils.MAX_PREF;
                break;
            case GOV:
                currentClockFile = Utils.CPU_GOV;
                bootClockFile = Utils.GOV_PREF;
                break;
        }

        input = Utils.readKernelValue(mContext, currentClockFile);
        if (clockType == CLOCK_TYPE.MIN || clockType == CLOCK_TYPE.MAX)
            currentValue = Utils.appendClockSuffix(input);
        else
            currentValue = input;
        preference.setValue(currentValue);

        if (Utils.prefFlagExists(mContext, bootClockFile)
                && Utils.prefFlagExists(mContext, Utils.CLOCKS_ON_BOOT_PREF)) {
            bootValue = Utils.readPrefValue(mContext, bootClockFile);
            if (bootValue != null && (clockType == CLOCK_TYPE.MIN || clockType == CLOCK_TYPE.MAX)) {
                try {
                    bootValue = Utils.appendClockSuffix(bootValue);
                } catch (Exception e) {
                    Utils.deletePrefFlag(mContext, bootClockFile);
                    bootValue = null;
                }
            }
        }
        updatePreferenceSummary(preference, currentValue, bootValue);
    }

    private enum CLOCK_TYPE {
        MIN, MAX, GOV;
    }
}
