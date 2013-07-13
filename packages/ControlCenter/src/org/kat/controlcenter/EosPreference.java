
package org.kat.controlcenter;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.preference.Preference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.teameos.jellybean.settings.EOSConstants;

import java.util.Arrays;
import java.util.List;

public class EosPreference extends Preference {
    private static final boolean DEBUG = false;
    private static final boolean NO_DIVIDER = true;

    private static final String TAG = "ECC_ColorPreference";

    private static final String ATTR_HEADER_TITLE = "headerTitle";
    private static final String ATTR_HEADER_TEXT = "headerText";
    private static final String ATTR_PREF_TITLE = "prefTitle";
    private static final String ATTR_URI_TARGET = "providerUri";
    private static final String ATTR_DEF_VAL = "defaultVal";
    private static final String ATTR_APPLY_ALL_LIST = "applyAllList";
    private static final String ATTR_CUSTOM_SUMMARY = "customSummary";
    private static final String ATTR_HAS_CHECKBOX = "hasCheckBox";

    Context mContext;
    ContentResolver mResolver;
    Resources mRes;

    // current color held in SettingsProvider
    int mCurrentColor;

    // set a generic, common android color to safely create the dialog
    // if we were unable to grab the target objects current color value
    // default color only refers to the dialog color selector circle
    // as opposed to defColor which will likely hold a -1 value
    int mDefaultColor;

    // holds current color while ColorPicker is open
    // use to restore in case user cancels dialog
    int mColorHolder;

    // preference views
    // header
    View headerGroup;
    TextView headerTitleView;
    TextView headerTextView;
    View headerDivider;
    
    // main preference
    View prefMain;
    TextView prefTitleView;
    TextView textButton1View;
    ImageView imageViewButton1;
    View button1Divider;
    TextView textButton2View;
    ImageView imageViewButton2;
    View button2Divider;
    TextView textButton3View;
    ImageView imageViewButton3;
    View button3Divider;
    TextView textButton4View;
    CheckBox mCheckBox4;


    String targetUri;
    String headerTitle;
    String headerText;
    String prefMainTitle;
    List<String> applyAllList;

    String key;

    boolean mApplyAllButton = false;
    boolean mHasCheckBox = false;

    public EosPreference(Context context) {
        this(context, null);
    }

    public EosPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EosPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mContext = context;
        mResolver = mContext.getContentResolver();
        mRes = mContext.getResources();
        mDefaultColor = mRes.getColor(com.android.internal.R.color.holo_blue_light);
        loadAttributes(attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.eos_preference, parent,
                false);

        View headerGroup = layout.findViewById(R.id.eos_pref_header_group);

        if (headerTitle == null) {
           layout.findViewById(R.id.eos_pref_header_title).setVisibility(View.GONE);
        } else {
            ((TextView)layout.findViewById(R.id.eos_pref_header_title)).setText(headerTitle);
        }
        
        if (headerText == null) {
            layout.findViewById(R.id.eos_pref_header_text).setVisibility(View.GONE);
        } else {
            ((TextView)layout.findViewById(R.id.eos_pref_header_text)).setText(headerText);
        }
        
        if (headerTitle == null && headerText == null) {
            layout.findViewById(R.id.eos_pref_header_divider).setVisibility(View.GONE);
            headerGroup.setVisibility(View.GONE);
        }
        
        if (prefMainTitle == null) {
            layout.findViewById(R.id.eos_pref_title).setVisibility(View.GONE);
        } else {
            ((TextView)layout.findViewById(R.id.eos_pref_title)).setText(prefMainTitle);
        }
        
        View buttonGroup = layout.findViewById(R.id.eos_pref_text_and_icon_group);

        View buttonGroup1 = buttonGroup.findViewById(R.id.icon_text_button1_group);
        textButton1View = (TextView) buttonGroup1.findViewById(R.id.eos_pref_text_button1);
        textButton1View.setText(R.string.color_preference_button_change);
        buttonGroup1.setOnClickListener(new ChangeColorListener());

        View buttonGroup2 = buttonGroup.findViewById(R.id.icon_text_button2_group);
        textButton2View = (TextView) buttonGroup2.findViewById(R.id.eos_pref_text_button2);
        textButton2View.setText(R.string.color_preference_button_restore);
        buttonGroup2.setOnClickListener(new RestoreColorListener());

        View buttonGroup3 = buttonGroup.findViewById(R.id.icon_text_button3_group);
        textButton3View = (TextView) buttonGroup3.findViewById(R.id.eos_pref_text_button3);

        if (!mApplyAllButton) {
            buttonGroup3.setVisibility(View.GONE);
            buttonGroup.findViewById(R.id.eos_pref_button3_divider).setVisibility(View.GONE);
        } else {
            textButton3View.setText(R.string.color_preference_button_apply_all);
            buttonGroup3.setOnClickListener(new ApplyAllListener());
        }
      
        View buttonGroup4 = buttonGroup.findViewById(R.id.icon_text_button4_group);
        textButton4View = (TextView) buttonGroup.findViewById(R.id.eos_pref_text_button4);
        mCheckBox4 = (CheckBox)buttonGroup4.findViewById(R.id.widget_checkbox_4);

        if (!mHasCheckBox) {
            buttonGroup4.setVisibility(View.GONE);
            buttonGroup.findViewById(R.id.eos_pref_button4_divider).setVisibility(View.GONE);
        } else {
            textButton4View.setText("Show/Hide");
        }

        if (DEBUG) {
            prefTitleView.setBackgroundResource(com.android.internal.R.color.holo_blue_light);
            buttonGroup1.setBackgroundResource(com.android.internal.R.color.holo_green_light);
            buttonGroup2.setBackgroundResource(com.android.internal.R.color.holo_orange_light);
            buttonGroup3.setBackgroundResource(com.android.internal.R.color.holo_red_light);
        }
        if (NO_DIVIDER) {
            buttonGroup.findViewById(R.id.eos_pref_button1_divider).setVisibility(View.GONE);
            buttonGroup.findViewById(R.id.eos_pref_button2_divider).setVisibility(View.GONE);
            buttonGroup.findViewById(R.id.eos_pref_button3_divider).setVisibility(View.GONE);
            buttonGroup.findViewById(R.id.eos_pref_button4_divider).setVisibility(View.GONE);
        }
        return layout;
    }

    private void loadAttributes(AttributeSet attrs) {
        key = this.getKey();
        
        int headerTitleResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_HEADER_TITLE, -1);
        int headerTextResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_HEADER_TEXT, -1);
        int prefTitleResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_PREF_TITLE, -1);
        int uriResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_URI_TARGET, -1);
        int defValResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_DEF_VAL, -2);
        int applyAllListResource = attrs.getAttributeResourceValue(Utils.CONTROLCENTERNS,
                ATTR_APPLY_ALL_LIST, -1);

        mHasCheckBox = attrs.getAttributeBooleanValue(Utils.CONTROLCENTERNS, ATTR_HAS_CHECKBOX, false);
        
        if (headerTitleResource == -1) {
            headerTitle = null;
        } else {
            headerTitle = mRes.getString(headerTitleResource);
        }

        if (headerTextResource == -1) {
            headerText = null;
        } else {
            headerText = mRes.getString(headerTextResource);
        }

        if (prefTitleResource == -1) {
            prefMainTitle = null;
        } else {
            prefMainTitle = mRes.getString(prefTitleResource);
        }

        if (uriResource == -1) {
            targetUri = Utils.URI_GRAVEYARD;
        } else {
            targetUri = mRes.getString(uriResource);
        }

        if (defValResource == -2) {
            mDefaultColor = -1;
        } else {
            mDefaultColor = mRes.getInteger(defValResource);
        }

        if (applyAllListResource != -1) {
            mApplyAllButton = true;
            applyAllList = Arrays.asList(mRes.getStringArray(
                    applyAllListResource));
        }
        
        mColorHolder = getColorSetting();
        mCurrentColor = mColorHolder;
    }

    private int getColorSetting() {
        int color = Settings.System.getInt(mResolver, targetUri, mDefaultColor);
        if (color == -1)
            color = mDefaultColor;
        return color;
    }

    private void writeColorSetting(int color) {
        Settings.System.putInt(mResolver, targetUri, color);
    }

    class ChangeColorListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mCurrentColor = getColorSetting();
            mColorHolder = mCurrentColor;
            ColorPickerDialog cpd = new ColorPickerDialog(mContext,
                    new ColorPickerDialog.OnColorChangedListener() {

                        @Override
                        public void colorUpdate(int color) {
                            mCurrentColor = color;
                            writeColorSetting(color);
                        }

                        @Override
                        public void colorChanged(int color) {
                        }
                    }, mCurrentColor);

            cpd.setOnCancelListener(new Dialog.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    mCurrentColor = mColorHolder;
                    writeColorSetting(mCurrentColor);
                }
            });
            cpd.setCanceledOnTouchOutside(true);
            cpd.show();
        }
    }

    class RestoreColorListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            writeColorSetting(mDefaultColor);
        }
    }

    class ApplyAllListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int currentColor = Settings.System.getInt(mResolver, targetUri, mDefaultColor);
            for (String uri : applyAllList) {
                Settings.System.putInt(mResolver, uri, currentColor);
            }
        }
    }
}
