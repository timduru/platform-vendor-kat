
package org.kat.controlcenter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;
import android.view.View;

import org.teameos.jellybean.settings.EOSUtils;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Pair<Fragment, String>> mFragments;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<Pair<Fragment, String>>();
        mFragments.add(Pair.create(
                (Fragment) InterfaceFragment.newInstance(R.xml.interface_settings), "INTERFACE"));
        mFragments.add(Pair.create(
                (Fragment) NavigationFragment.newInstance(R.xml.navigation_bar), "NAVIGATION"));

        mFragments.add(Pair.create((Fragment) StatusbarFragment.newInstance(R.xml.statusbar),
                "STATUSBAR"));
        mFragments.add(Pair.create((Fragment) SystemFragment.newInstance(R.xml.system_settings),
                "SYSTEM"));
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragments.get(position).second;
    }

    @Override
    public int getCount()
    {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return mFragments.get(position).first;
    }

    @Override
    public Object instantiateItem(View pager, int position)
    {
        return null;
    }

    @Override
    public void destroyItem(View pager, int position, Object view)
    {
    }

    @Override
    public void finishUpdate(View view) {
    }

    @Override
    public void restoreState(Parcelable p, ClassLoader c) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View view) {
    }
}
