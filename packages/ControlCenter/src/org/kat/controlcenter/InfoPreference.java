
package org.kat.controlcenter;

import java.io.InputStream;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoPreference extends Preference {

    private String mKey;

    public InfoPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        getKey(attrs);
    }

    public InfoPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        getKey(attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {

        FrameLayout mView = new FrameLayout(getContext());
        mView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                275));

        AvatarView avatar = new AvatarView(getContext());
        avatar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        InputStream inputStream = null;
        if (mKey.equals("Kevdliu")) {
            inputStream = getContext().getResources().openRawResource(R.raw.kevdliu);
        } else if (mKey.equals("Timduru")) {
            inputStream = getContext().getResources().openRawResource(R.raw.timduru);
        } else if (mKey.equals("RunAndHide05")) {
            inputStream = getContext().getResources().openRawResource(R.raw.runandhide05);
        } else if (mKey.equals("RaymanFX")) {
            inputStream = getContext().getResources().openRawResource(R.raw.raymanfx);
        } else if (mKey.equals("BigRushDog")) {
            inputStream = getContext().getResources().openRawResource(R.raw.bigrushdog);
        } else if (mKey.equals("Solarnz")) {
            inputStream = getContext().getResources().openRawResource(R.raw.solarnz);
        } else if (mKey.equals("K.Crudup")) {
            inputStream = getContext().getResources().openRawResource(R.raw.kcrudup);
        } else if (mKey.equals("Uber")) {
            inputStream = getContext().getResources().openRawResource(R.raw.uber);
        }

        avatar.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        mView.addView(avatar);

        LinearLayout titleLayer = new LinearLayout(getContext());
        titleLayer.setGravity(Gravity.BOTTOM);
        TextView title = new TextView(getContext());
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setText(mKey);
        title.setTextSize(18);
        title.setBackgroundColor(Color.argb(125, 0, 0, 0));
        titleLayer.addView(title);
        mView.addView(titleLayer);

        return mView;
    }

    private void getKey(AttributeSet attrs) {
        String value = attrs.getAttributeValue(Utils.ANDROIDNS, "key");
        if (value == null) {
            value = "unknown";
        }
        mKey = value;
    }
}
