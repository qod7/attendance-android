package com.passion.attendance;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class TriangleView extends View {

    int mColor;

    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriangleView,
                0, 0);

        try {
            mColor = a.getColor(R.styleable.TriangleView_shape_color, Color.rgb(255,165,0));
        } finally {
            a.recycle();
        }
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriangleView,
                0, 0);

        try {
            mColor = a.getColor(R.styleable.TriangleView_shape_color, Color.rgb(255, 165, 0));
        } finally {
            a.recycle();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth() / 2;

        Path path = new Path();
        path.moveTo(0, w);
        path.lineTo( 0 , w * 2 );
        path.lineTo(w , w * 2 );
        path.lineTo(0, w);
        path.close();

        Paint p = new Paint();
        p.setColor(mColor);

        canvas.drawPath(path, p);
    }
}