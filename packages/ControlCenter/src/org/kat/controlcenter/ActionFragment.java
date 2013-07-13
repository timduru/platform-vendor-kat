
package org.kat.controlcenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ActionFragment extends PreferenceFragment {

    protected Context mContext;
    protected ContentResolver mResolver;
    private PackageManager mPm;
    private PackageAdapter mAdapter;
    private ArrayList<ResolveInfo> mPackageList;
    private ListView mListView;
    protected Resources mRes;
    private ArrayList<ActionPreference> mActionPreferenceList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (Context) getActivity();
        mRes = mContext.getResources();
        mResolver = mContext.getContentResolver();
        mPm = mContext.getPackageManager();
        mActionPreferenceList = new ArrayList<ActionPreference>();
        View dialog = View.inflate(mContext, R.layout.activity_dialog, null);
        mListView = (ListView) dialog.findViewById(R.id.eos_dialog_list);
        mPackageList = new ArrayList<ResolveInfo>();
        mAdapter = new PackageAdapter(getActivity(), mPackageList, mPm);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    class WidgetListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String tag = (String) v.getTag();
            if (tag != null) {
                for (ActionPreference ap : mActionPreferenceList) {
                    if (tag.equals(ap.getTargetUri())) {
                        callInitDialog(ap);
                        break;
                    }
                }
            }
        }
    }

    protected void addActionPreference(ActionPreference pref) {
        pref.setListener(new WidgetListener());
        mActionPreferenceList.add(pref);
    }

    private void callInitDialog(final ActionPreference preference) {
        final ActionPreference pref = (ActionPreference) preference;
        final CharSequence[] item_entries = mRes
                .getStringArray(R.array.eos_action_dialog_entries);
        final CharSequence[] item_values = mRes
                .getStringArray(R.array.eos_action_dialog_values);
        mPackageList.clear();
        new PackageLoader().execute(mPackageList);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(
                mRes.getString(R.string.eos_interface_activity_dialog_title))
                .setNegativeButton(mRes.getString(com.android.internal.R.string.cancel),
                        new Dialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        })
                .setItems(item_entries, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String pressed = (String) item_values[which];
                        // be sure to keep applications menu as the last
                        // item
                        if (pressed.equals(item_values[item_values.length - 1])) {
                            callActivityDialog(pref);
                        } else {
                            pref.loadCustomApp(pressed);
                            pref.setTargetValue(pressed);

                        }
                    }
                }).create().show();
    }

    private void callActivityDialog(final ActionPreference caller) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setAdapter(mAdapter, new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ResolveInfo app = (ResolveInfo) mListView.getAdapter().getItem(which);
                String name = app.activityInfo.loadLabel(mPm).toString();
                String component = new ComponentName(app.activityInfo.packageName,
                        app.activityInfo.name).flattenToString();
                Drawable d = app.activityInfo.loadIcon(mPm);
                caller.setResourcesFromPackage(name, component, d);
            }
        })
                .setTitle(
                        mRes.getString(R.string.eos_interface_activity_dialog_title))
                .setNegativeButton(mRes.getString(com.android.internal.R.string.cancel),
                        new Dialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        })
                .create().show();
    }

    private class PackageLoader extends
            AsyncTask<ArrayList<ResolveInfo>, ArrayList<ResolveInfo>, ArrayList<ResolveInfo>> {

        @Override
        protected ArrayList<ResolveInfo> doInBackground(ArrayList<ResolveInfo>... list) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            for (ResolveInfo info : mPm.queryIntentActivities(intent, 0)) {
                list[0].add(info);
                publishProgress(list[0]);
                // sort the app packages by simple name
                Collections.sort(list[0], new Comparator<ResolveInfo>() {
                    @Override
                    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                        return lhs.activityInfo.loadLabel(mPm).toString()
                                .compareToIgnoreCase(rhs.activityInfo.loadLabel(mPm).toString());
                    }
                });
                publishProgress(list[0]);

            }
            return list[0];
        }

        @Override
        protected void onProgressUpdate(ArrayList<ResolveInfo>... list) {
            super.onProgressUpdate(list);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(ArrayList<ResolveInfo> list) {
            super.onPostExecute(list);
        }
    }
}
