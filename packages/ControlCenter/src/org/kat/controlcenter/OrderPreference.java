
package org.kat.controlcenter;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderPreference extends Preference {
    public static final int UP = 1;
    public static final int DOWN = 2;

    Context mContext;
    OnPositionChangedListener mListener;
    TextView mTitle;
    CheckBox mSwitch;
    String titleText;
    boolean mEnabled = false;

    public interface OnPositionChangedListener {
        public void onStateChanged();

        public void onPositionChanged(OrderPreference pref, int order);
    }

    public OrderPreference(Context context) {
        this(context, null);
    }

    public OrderPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.order_preference, parent,
                false);

        mSwitch = (CheckBox) layout.findViewById(R.id.on_off_switch);
        mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEnabled = isChecked;
                mListener.onStateChanged();
            }
        });
        mSwitch.setChecked(mEnabled);

        mTitle = (TextView) layout.findViewById(R.id.orderRowTextView);
        mTitle.setText(titleText);

        ImageView top_button = (ImageView) layout.findViewById(R.id.btnOrderUp);
        top_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositionChanged(OrderPreference.this, UP);
            }
        });

        ImageView bottom_button = (ImageView) layout.findViewById(R.id.btnOrderDown);
        bottom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositionChanged(OrderPreference.this, DOWN);
            }
        });

        return layout;
    }

    public void setOnOrderChangedListener(OnPositionChangedListener listener) {
        mListener = listener;
    }

    public void setValues(String title, String value) {
        titleText = title;
        if (mTitle != null) {
            mTitle.setText(titleText);
        }
        setKey(value);
    }

    public void setToggleEnabled(boolean enabled) {
        mEnabled = enabled;
        if (mSwitch != null) {
            mSwitch.setChecked(mEnabled);
        }
    }

    public boolean isToggleEnabled() {
        return mEnabled;
    }
}
