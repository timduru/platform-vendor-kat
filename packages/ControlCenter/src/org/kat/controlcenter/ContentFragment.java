
package org.kat.controlcenter;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class ContentFragment extends PreferenceFragment {
    OnActivityRequestedListener mListener;

    public static ContentFragment newInstance(Bundle args) {
        ContentFragment frag = new ContentFragment();
        frag.setArguments(args);
        return frag;
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    public ContentFragment(Bundle args) {
        newInstance(args);
    }

    public ContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.dual_pane_content);
        mListener = (OnActivityRequestedListener) getActivity();

        findPreference(getString(R.string.eos_interface)).setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mListener.onActivityRequested(getString(R.string.eos_interface));
                        return true;
                    }
                });
        findPreference(getString(R.string.eos_interface_navigation)).setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mListener.onActivityRequested(getString(R.string.eos_interface_navigation));
                        return true;
                    }
                });
        findPreference(getString(R.string.eos_interface_statusbar)).setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mListener.onActivityRequested(getString(R.string.eos_interface_statusbar));
                        return true;
                    }
                });
        findPreference(getString(R.string.eos_system)).setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        mListener.onActivityRequested(getString(R.string.eos_system));
                        return true;
                    }
                });
    }
}
