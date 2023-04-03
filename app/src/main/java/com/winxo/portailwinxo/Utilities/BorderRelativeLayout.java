package com.winxo.portailwinxo.Utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.winxo.portailwinxo.R;

public class BorderRelativeLayout extends RelativeLayout {

    private float borderThickness;
    private int borderColor;

    public BorderRelativeLayout(Context context) {
        this(context, null, 0);
    }

    public BorderRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderThickness);
        getLocalVisibleRect(rect);
        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BorderRelativeLayout);
        borderThickness = array.getDimension(R.styleable.BorderRelativeLayout_borderThickness, 0.5f);
        borderColor = array.getColor(R.styleable.BorderRelativeLayout_borderColor, ContextCompat.getColor(context, R.color.colorBlack));
    }
}
