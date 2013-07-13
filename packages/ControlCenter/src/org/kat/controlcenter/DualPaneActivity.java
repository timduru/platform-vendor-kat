
package org.kat.controlcenter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

import java.util.ArrayList;

public class DualPaneActivity extends Activity implements OnActivityRequestedListener {
    ActionBar mBar;
    ArrayList<String> mFragmentsTitleList = new ArrayList<String>();

    IntentFilter filter;
    BroadcastReceiver mEosUiReceiver;
    ArrayList<BroadcastReceiver> mReceivers = new ArrayList<BroadcastReceiver>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if this is grouper or any large screen, and we're in portrait, go
        // back to view pager
        if (EOSUtils.isLargeScreen() && !EOSUtils.isLandscape(DualPaneActivity.this)) {
            Intent intent = new Intent(this, Main.class);
            intent.putExtra(Utils.INCOMING_LAST_FRAG_VIEWED, Utils.LAST_FRAG_VIEWED);
            startActivity(intent);
            finish();
            return;
        }

        // our content fragment, left pane
        setContentView(R.layout.main_twopane);

        mBar = getActionBar();
        updateActionBarTitle();
        mBar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .add(R.id.container, ContentFragment.newInstance())
                .commit();

        // our details pane, right fragment
        // maybe we got a request from the pager view
        Fragment newFragment;
        int position = getIntent().getIntExtra(Utils.INCOMING_LAST_FRAG_VIEWED, 0);
        if (position == 0) {
            newFragment = InterfaceDualFragment.newInstance();
        } else if (position == 1) {
            newFragment = NavigationDualFragment.newInstance();
        } else if (position == 2) {
            newFragment = StatusbarDualFragment.newInstance();
        } else if (position == 3) {
            newFragment = SystemDualFragment.newInstance();
        } else {
            newFragment = InterfaceDualFragment.newInstance();
        }
        replaceFragmentFromContainer(newFragment);
    }

    private void updateActionBarTitle() {
        if (mFragmentsTitleList.size() > 0) {
            mBar.setTitle(mFragmentsTitleList.get(mFragmentsTitleList.size() - 1));
        } else {
            mBar.setTitle(Utils.DEFAULT_TITLE);
        }
    }

    void registerReceivers() {
        unregisterReceivers();
        this.registerReceiver(mEosUiReceiver, filter);
        mReceivers.add(mEosUiReceiver);
    }

    void unregisterReceivers() {
        for (BroadcastReceiver r : mReceivers) {
            this.unregisterReceiver(r);
        }
        mReceivers.clear();
    }

    private void startTextDialogFragment(Bundle b) {
        TextInfoFragment textFragment = TextInfoFragment.newInstance(b);
        textFragment.show(getFragmentManager(), b.getString(Utils.TEXT_FRAGMENT_TITLE_KEY));        
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceivers();
        // Utils.turnOffEosUI(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // we don't call onBackPressed(); we only want to
                // pop the stack, not exit with this
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    mFragmentsTitleList.remove(mFragmentsTitleList.size() - 1);
                    updateActionBarTitle();
                } else {
                    Intent settingsIntent = new Intent()
                            .setAction(android.provider.Settings.ACTION_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityAsUser(settingsIntent, new UserHandle(UserHandle.USER_CURRENT));
                }
                break;
            case R.id.action_themes:
                Intent intent = new Intent()
                        .setAction(Intent.ACTION_MAIN)
                        .setClassName("com.tmobile.themechooser",
                                "com.tmobile.themechooser.ThemeChooser");
                startActivity(intent);
                break;
/*            case R.id.action_menu_roster:
                replaceFragment(InfoDualFragment.newInstance());
                break;
            case R.id.action_menu_xda:
                Intent xda_thread = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Utils.getXdaUrl(DualPaneActivity.this,
                                EOSUtils.getDevice())));
                startActivity(xda_thread);
                break;
*/
           case R.id.action_menu_changelog:
                Bundle b = new Bundle();
                b.putString(Utils.TEXT_FRAGMENT_TITLE_KEY, "Changelog");
                b.putInt(Utils.TEXT_FRAGMENT_TEXT_RES_KEY, R.raw.change_log);
                startTextDialogFragment(b);
                break;
            default:
                break;
        }
        return true;
    }

    private void clearBackStack() {
        getFragmentManager()
                .popBackStack(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        if (Utils.PRIVACY_LOG_PACKAGES.equals(tag)) {
            replaceFragment(LoggerPackages.newInstance());
        } else if (Utils.PRIVACY_FRAG_TAG.equals(tag)) {
            replaceFragment(Privacy.newInstance());
        } else if (Utils.PERFORMANCE_FRAG_TAG.equals(tag)) {
            replaceFragment(Performance.newInstance());
        } else if (Utils.SOFTKEY_FRAG_TAG.equals(tag)) {
            replaceFragment(SoftKeyActions.newInstance());
        } else if (Utils.SEARCH_PANEL_FRAG_TAG.equals(tag)) {
            replaceFragment(SearchPanelActions.newInstance());
        } else if (Utils.LEGACY_TOGGLES_FRAGMENT_TAG.equals(tag)) {
            replaceFragment(LegacyTogglesFragment.newInstance());
        } else if (Utils.QUICK_SETTINGS_FRAGMENT_TAG.equals(tag)) {
            replaceFragment(SettingsPanelFragment.newInstance());
        } else if (getString(R.string.eos_interface).equals(tag)) {
            replaceFragmentFromContainer(InterfaceDualFragment.newInstance());
        } else if (getString(R.string.eos_interface_navigation).equals(tag)) {
            replaceFragmentFromContainer(NavigationDualFragment.newInstance());
        } else if (getString(R.string.eos_interface_statusbar).equals(tag)) {
            replaceFragmentFromContainer(StatusbarDualFragment.newInstance());
        } else if (getString(R.string.eos_system).equals(tag)) {
            replaceFragmentFromContainer(SystemDualFragment.newInstance());
        } else {
            return;
        }
    }

    private void replaceFragment(Fragment f) {
        getFragmentManager().beginTransaction()
                .replace(R.id.detail_container, f)
                .addToBackStack(null)
                .commit();
        mFragmentsTitleList.add(f.getArguments()
                .getString(Utils.FRAG_TITLE_KEY,
                        Utils.DEFAULT_TITLE));
        updateActionBarTitle();
    }

    private void getFragmentPosition(Fragment f) {
        Utils.LAST_FRAG_VIEWED = f.getArguments()
                .getInt(Utils.FRAG_POSITION_KEY, 0);
    }

    private void replaceFragmentFromContainer(Fragment f) {
        clearBackStack();
        getFragmentPosition(f);
        getFragmentManager().beginTransaction()
                .replace(R.id.detail_container, f)
                .commit();
        mFragmentsTitleList.clear();
        mFragmentsTitleList.add(f.getArguments()
                .getString(Utils.FRAG_TITLE_KEY,
                        Utils.DEFAULT_TITLE));
        updateActionBarTitle();
    }
}
