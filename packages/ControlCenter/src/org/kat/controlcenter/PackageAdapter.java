
package org.kat.controlcenter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PackageAdapter extends BaseAdapter {

    List<ResolveInfo> mPackageList;
    Activity mContext;
    PackageManager mPm;

    public PackageAdapter(Activity context, List<ResolveInfo> packageList,
            PackageManager packageManager) {
        super();
        mContext = context;
        mPackageList = packageList;
        mPm = packageManager;
    }

    private class ViewHolder {
        TextView apkName;
        ImageView apkIcon;
    }

    public int getCount() {
        return mPackageList.size();
    }

    public Object getItem(int position) {
        return mPackageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = mContext.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_item, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView.findViewById(R.id.title);
            holder.apkIcon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ResolveInfo packageInfo = (ResolveInfo) getItem(position);
        Drawable appIcon = packageInfo.activityInfo.loadIcon(mPm);
        String appName = packageInfo.activityInfo.loadLabel(mPm).toString();
        holder.apkName.setText(appName);
        holder.apkIcon.setImageDrawable(appIcon);

        return convertView;
    }
}
