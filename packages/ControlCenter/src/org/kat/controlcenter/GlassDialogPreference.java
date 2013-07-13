
package org.kat.controlcenter;

import android.content.ContentResolver;
import android.content.Context;
import android.preference.DialogPreference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import org.teameos.jellybean.settings.EOSConstants;
import org.teameos.jellybean.settings.EOSUtils;

public class GlassDialogPreference extends DialogPreference {
    Context mContext;
    ContentResolver mResolver;
    TextView mBottomBar_header;
    SeekBar mBottomBar_seek;
    TextView mBottombarDefTitle;
    CheckBox mBottomBar_checkbox;
    TextView mBottomBar_value;
    TextView mStatusbar_header;
    SeekBar mStatusbar_seek;
    TextView mStatusbarDefTitle;
    CheckBox mStatusbar_checkbox;
    TextView mStatusbar_value;

    int mBottombarValue;
    int mStatusbarValue;

    public GlassDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mResolver = context.getContentResolver();
    }

    @Override
    protected View onCreateDialogView() {
        final boolean hasSystemBar = EOSUtils.hasSystemBar(mContext);
        final boolean hasNavBar = EOSUtils.hasNavBar(mContext);
        final boolean statusbarOnly = !hasSystemBar && !hasNavBar;

        View root = LayoutInflater.from(mContext).inflate(R.layout.glass_dialog_preference, null);
        mStatusbar_header = (TextView) root.findViewById(R.id.statusbar_header);
        mStatusbar_seek = (SeekBar) root.findViewById(R.id.statusbar_seekbar);
        mStatusbarDefTitle = (TextView) root.findViewById(R.id.statusbar_default_title);
        mStatusbar_checkbox = (CheckBox) root.findViewById(R.id.statusbar_default_checkbox);
        mStatusbar_value = (TextView) root.findViewById(R.id.statusbar_progress_value);
        mBottomBar_header = (TextView) root.findViewById(R.id.navbar_header);
        mBottomBar_seek = (SeekBar) root.findViewById(R.id.navbar_seekbar);
        mBottombarDefTitle = (TextView) root.findViewById(R.id.navbar_default_title);
        mBottomBar_checkbox = (CheckBox) root.findViewById(R.id.navbar_default_checkbox);
        mBottomBar_value = (TextView) root.findViewById(R.id.navbar_progress_value);

        if (!statusbarOnly) {
            String titleText = "";
            if (hasSystemBar) {
                titleText = "System bar transparency";
            } else {
                titleText = "Navigation bar transparency";
            }
            mBottomBar_header.setText(titleText);

            mBottomBar_seek.setMax(255);
            mBottomBar_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mBottombarValue = progress;
                    mBottomBar_value.setText("Current value: " + String.valueOf(mBottombarValue));
                    Settings.System.putInt(mResolver, EOSConstants.SYSTEMUI_NAVBAR_GLASS_LEVEL,
                            255 - mBottombarValue);

                }
            });

            mBottombarValue = Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_NAVBAR_GLASS_LEVEL,
                    EOSConstants.SYSTEMUI_NAVBAR_GLASS_PRESET);
            mBottomBar_seek.setProgress(255 - mBottombarValue);

            mBottombarDefTitle.setText("Default launcher preset");

            mBottomBar_checkbox.setChecked(Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_NAVBAR_GLASS_DEFAULT_ENABLED, 0) == 1);
            mBottomBar_seek.setEnabled(!mBottomBar_checkbox.isChecked());
            mBottomBar_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBottomBar_seek.setEnabled(!isChecked);
                    Settings.System.putInt(mResolver,
                            EOSConstants.SYSTEMUI_NAVBAR_GLASS_DEFAULT_ENABLED, isChecked ? 1 : 0);
                }

            });
            mBottomBar_value.setText("Current value: " + String.valueOf(mBottombarValue));

        } else {
            mBottomBar_header.setVisibility(View.GONE);
            mBottomBar_seek.setVisibility(View.GONE);
            mBottombarDefTitle.setVisibility(View.GONE);
            mBottomBar_checkbox.setVisibility(View.GONE);
            mBottomBar_value.setVisibility(View.GONE);
        }

        if (!hasSystemBar) {
            mStatusbar_header.setText("Statusbar transparency");
            mStatusbar_seek.setMax(255);
            mStatusbar_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mStatusbarValue = progress;
                    mStatusbar_value.setText("Current value: " + String.valueOf(mStatusbarValue));
                    Settings.System.putInt(mResolver, EOSConstants.SYSTEMUI_STATUSBAR_GLASS_LEVEL,
                            255 - progress);

                }
            });
            mStatusbarValue = Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_STATUSBAR_GLASS_LEVEL,
                    EOSConstants.SYSTEMUI_STATUSBAR_GLASS_PRESET);

            mStatusbar_seek.setProgress(255 - mStatusbarValue);
            mStatusbarDefTitle.setText("Default launcher preset");

            mStatusbar_checkbox.setChecked(Settings.System.getInt(mResolver,
                    EOSConstants.SYSTEMUI_STATUSBAR_GLASS_DEFAULT_ENABLED, 0) == 1);
            mStatusbar_seek.setEnabled(!mStatusbar_checkbox.isChecked());
            mStatusbar_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mStatusbar_seek.setEnabled(!isChecked);
                    Settings.System.putInt(mResolver,
                            EOSConstants.SYSTEMUI_STATUSBAR_GLASS_DEFAULT_ENABLED, isChecked ? 1
                                    : 0);
                }

            });
            mStatusbar_value.setText("Current value: " + String.valueOf(mStatusbarValue));
        } else {
            mStatusbar_header.setVisibility(View.GONE);
            mStatusbar_seek.setVisibility(View.GONE);
            mStatusbarDefTitle.setVisibility(View.GONE);
            mStatusbar_checkbox.setVisibility(View.GONE);
            mStatusbar_value.setVisibility(View.GONE);
        }

        return root;
    }
}
