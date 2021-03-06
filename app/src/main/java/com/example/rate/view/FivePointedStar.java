package com.example.rate.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 阿龙 on 2017/6/9.
 */

public class FivePointedStar extends View {

    private int widthSize;
    private Context context;
    public FivePointedStar(Context context) {
        super(context);
    }

    public FivePointedStar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FivePointedStar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         widthSize = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = widthSize/2;
        float angle = 360/5;
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(5);

        Path mPath = new Path();
        mPath.moveTo(radius,0);
        mPath.lineTo(radius+(float) Math.cos((angle*2-90)*Math.PI / 180)*radius,radius+(float)Math.sin((angle*2-90)*Math.PI / 180)*radius);
        mPath.lineTo( radius-(float)Math.sin(angle*Math.PI / 180)*radius,radius-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( radius+(float)Math.sin(angle*Math.PI / 180)*radius,radius-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( radius-(float)Math.sin((angle*3-180)*Math.PI / 180)*radius,radius+(float)Math.cos((angle*3-180)*Math.PI / 180)*radius);
//       float Y = 50+((radius+(float) Math.cos((angle*2-90)*Math.PI / 180)*radius+radius-(float)Math.sin(angle*Math.PI / 180)*radius)
//                -(radius+(float)Math.sin(angle*Math.PI / 180)*radius+radius-(float)Math.sin((angle*3-180)*Math.PI / 180)*radius));
//        float X= 50-((radius+(float)Math.sin((angle*2-90)*Math.PI / 180)*radius+radius-(float)Math.cos(angle*Math.PI / 180)*radius)
//                -(radius-(float)Math.cos(angle*Math.PI / 180)*radius+radius+(float)Math.cos((angle*3-180)*Math.PI / 180)*radius));
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }
}
