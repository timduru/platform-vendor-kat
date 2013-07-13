
package org.kat.controlcenter;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Activity;

import java.util.ArrayList;

public class SingleFragmentActivity extends Activity implements OnActivityRequestedListener {
    ImageView mBackIndicator;
    ImageView mIcon;
    TextView mTitle;
    ActionBar mBar;

    ArrayList<String> mFragmentsTitleList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBar = getActionBar();
        mBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        String newFrag = (String) b.get(Utils.INCOMING_FRAG_KEY);
        if (newFrag == null) {
            finish();
            return;
        }
        Fragment f;
        if (newFrag.equals(Utils.SOFTKEY_FRAG_TAG)) {
            f = (Fragment) SoftKeyActions.newInstance();
        } else if (newFrag.equals(Utils.SEARCH_PANEL_FRAG_TAG)) {
            f = (Fragment) SearchPanelActions.newInstance();
        } else if (newFrag.equals(Utils.PERFORMANCE_FRAG_TAG)) {
            f = (Fragment) Performance.newInstance();
        } else if (newFrag.equals(Utils.PRIVACY_FRAG_TAG)) {
            f = (Fragment) Privacy.newInstance();
        } else if (newFrag.equals(Utils.INFO_TITLE)) {
            f = (Fragment) InfoDualFragment.newInstance();
        } else if (newFrag.equals(Utils.LEGACY_TOGGLES_FRAGMENT_TAG)) {
            f = (Fragment) LegacyTogglesFragment.newInstance();
        } else if (newFrag.equals(Utils.QUICK_SETTINGS_FRAGMENT_TAG)) {
            f = (Fragment) SettingsPanelFragment.newInstance();
        } else {
            finish();
            return;
        }
        showFragment(f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SingleFragmentActivity.this.onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            mFragmentsTitleList.remove(mFragmentsTitleList.size() - 1);
            updateActionBarTitle();
        } else {
            finish();
        }
    }

    @Override
    public void onActivityRequested(String tag) {
        Fragment f;
        if (Utils.PRIVACY_LOG_PACKAGES.equals(tag)) {
            f = LoggerPackages.newInstance();
        } else {
            return;
        }
        replaceFragment(f);
    }

    private void showFragment(Fragment f) {
        getFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .commit();
        String title = f.getArguments().getString(Utils.FRAG_TITLE_KEY, Utils.DEFAULT_TITLE);
        mFragmentsTitleList.add(title);
        updateActionBarTitle();
    }

    private void replaceFragment(Fragment f) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, f)
                .addToBackStack(null)
                .commit();
        String title = f.getArguments().getString(Utils.FRAG_TITLE_KEY, Utils.DEFAULT_TITLE);
        mFragmentsTitleList.add(title);
        updateActionBarTitle();
    }

    private void updateActionBarTitle() {
        if (mFragmentsTitleList.size() > 0) {
            mBar.setTitle(mFragmentsTitleList.get(mFragmentsTitleList.size() - 1));
        } else {
            mBar.setTitle(Utils.DEFAULT_TITLE);
        }
    }
}
