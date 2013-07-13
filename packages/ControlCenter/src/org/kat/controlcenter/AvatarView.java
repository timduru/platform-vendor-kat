
package org.kat.controlcenter;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AvatarView extends ImageView {

    public AvatarView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {
        float frameWidth = frameRight - frameLeft;
        float frameHeight = frameBottom - frameTop;

        float originalImageWidth = (float) getDrawable().getIntrinsicWidth();
        float originalImageHeight = (float) getDrawable().getIntrinsicHeight();

        float usedScaleFactor = 1;

        if ((frameWidth > originalImageWidth) || (frameHeight > originalImageHeight)) {
            float fitHorizontallyScaleFactor = frameWidth / originalImageWidth;
            float fitVerticallyScaleFactor = frameHeight / originalImageHeight;
            usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor);
        }

        Matrix matrix = getImageMatrix();
        matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0);
        setImageMatrix(matrix);
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}
