package com.example.rate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.rate.R;

/**
 * Created by 阿龙 on 2017/6/6.
 */

public class Rate extends View {

    private static final String TAG = ">>>>>>>";
    private Context context;
    private int size = 5;//星评的个数
    private int activeSize = 3;//激活的星评个数
    private Bitmap activeBitmap = null;//激活的bitmap
    private Bitmap disactiveBitmap = null;//没有激活的bitmap
    private float rateWidth;//默认单个宽
    private float rateHeight;//默认单个高
    private int padding;//间距
    private boolean isTouch;//是否可以選擇

    private Paint mPaint;//图片的画笔
    private Paint activPaint;//选中星星的画笔
    private Paint disactivPaint;//未选中星星的画笔
//    private Path mPath ;

    private int width = 0;//view的宽度
    private int height = 0;//view的高度
    private float touchX;//触摸位置x坐标点
    private float touchY;//触摸位置y坐标点

    private RateChangeListener changeListener;//选中个数的监听

    public Rate(Context context) {
        super(context);
    }

    public Rate(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public Rate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Rate);
        int activeId = 0;
        int disactiveId = 0;
        if (array != null) {
            size = array.getInt(R.styleable.Rate_custom_rate_size, 5);
            activeSize = array.getInt(R.styleable.Rate_custom_rate_active_size, 3);
            rateWidth = array.getDimensionPixelOffset(R.styleable.Rate_custom_rate_width, 0);
            rateHeight = array.getDimensionPixelOffset(R.styleable.Rate_custom_rate_height, 0);
            activeId = array.getResourceId(R.styleable.Rate_custom_rate_active_drawable, 0);
            disactiveId = array.getResourceId(R.styleable.Rate_custom_rate_disactive_drawable, 0);
            padding = array.getDimensionPixelOffset(R.styleable.Rate_custom_rate_padding, 0);
            isTouch = array.getBoolean(R.styleable.Rate_custom_rate_touch, false);
            array.recycle();
        }
        //如果没有宽高就设置一个默认值
        if (rateHeight <= 0){
            rateHeight = 80;
        }
        if (rateWidth <= 0){
            rateWidth = 80;
        }

        if (activeId!=0){
            activeBitmap = BitmapFactory.decodeResource(getResources(), activeId);
            //如果没有设置宽高时候
            if (rateWidth <= 0) {
                rateWidth = activeBitmap.getWidth();
            }
            //把图片压缩到设置的宽高
            activeBitmap = Bitmap.createScaledBitmap(activeBitmap, (int) rateWidth, (int) rateHeight, false);
        }
        if (disactiveId != 0){
            disactiveBitmap = BitmapFactory.decodeResource(getResources(), disactiveId);
            if (rateHeight <= 0) {
                rateHeight = activeBitmap.getHeight();
            }
            disactiveBitmap = Bitmap.createScaledBitmap(disactiveBitmap, (int) rateWidth, (int) rateHeight, false);
        }

        mPaint = new Paint();//初始化bitmap的画笔
        mPaint.setAntiAlias(true);

        activPaint = new Paint();//初始化选中星星的画笔
        activPaint.setAntiAlias(true);
        activPaint.setColor(Color.YELLOW);

        disactivPaint = new Paint();//初始化未选中星星的画笔
        disactivPaint.setAntiAlias(true);
        disactivPaint.setColor(Color.GRAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //计算宽
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果view的宽度小于设置size*星星的宽度，就用size * (int) (padding + rateWidth)做为控件的宽度度
            if (widthSize < size * (int) (padding + rateWidth)) {
                width = size * (int) (padding + rateWidth);
            } else {
                width = widthSize;
            }
        } else {
            width = size * (int) (padding + rateWidth)-padding;
        }
        //计算高
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果view的高度小于设置星星的高度，就用星星高度做为控件的高度
            if (heightSize < rateHeight) {
                height = (int) rateHeight + 5;
            } else {
                height = heightSize;
            }
        } else {
            height = (int) rateHeight + 5;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //开始画active
        for (int i = 0; i < activeSize; i++) {
            if (activeBitmap != null){
                if (i == 0) {
                    canvas.drawBitmap(activeBitmap, rateWidth * i, (height - rateHeight) / 2, mPaint);
                } else {
                    canvas.drawBitmap(activeBitmap, (rateWidth + padding) * i, (height - rateHeight) / 2, mPaint);
                }
            }else {
                drawActivRate(i,canvas);
            }
        }
//        //开始画disactive
        for (int i = activeSize; i < size; i++) {
            if (disactiveBitmap != null){
                if (i == 0) {
                    canvas.drawBitmap(disactiveBitmap, rateWidth * i, (height - rateHeight) / 2, mPaint);
                } else {
                    canvas.drawBitmap(disactiveBitmap, (rateWidth + padding) * i, (height - rateHeight) / 2, mPaint);
                }
            }else {
                drawDisActivRate(i,canvas);
            }
        }
    }
    /**
     * 绘制黄色的五角星（在活动的）
     * */
    private void drawActivRate(int position,Canvas canvas){
        float radius = rateWidth/2;//根據每一個星星的位置繪製園，確定五角星五個點的位置
        float angle = 360/5;
        float centerX = (rateWidth+padding)*(position+1)-padding-radius;//獲取每一個星星空間的中心位置的X坐標
        float centerY =height/2;//獲取每一個星星空間的中心位置的y坐標
        Path mPath = new Path();
        mPath.moveTo(centerX,centerY-radius);
        mPath.lineTo(centerX+(float) Math.cos((angle*2-90)*Math.PI / 180)*radius,centerY+(float)Math.sin((angle*2-90)*Math.PI / 180)*radius);
        mPath.lineTo( centerX-(float)Math.sin(angle*Math.PI / 180)*radius,centerY-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( centerX+(float)Math.sin(angle*Math.PI / 180)*radius,centerY-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( centerX-(float)Math.sin((angle*3-180)*Math.PI / 180)*radius,centerY+(float)Math.cos((angle*3-180)*Math.PI / 180)*radius);
//        mPath.lineTo(centerX,centerY-radius);
        mPath.close();
        canvas.drawPath(mPath,activPaint);
    }

    /**
     * 绘制灰色的五角星
     * */
    private void drawDisActivRate(int position,Canvas canvas){
        float radius = rateWidth/2;
        float angle = 360/5;
        float centerX = (rateWidth+padding)*(position+1)-padding-radius;
        float centerY =height/2;
        Path mPath = new Path();
        mPath.moveTo(centerX,centerY-radius);
        mPath.lineTo(centerX+(float) Math.cos((angle*2-90)*Math.PI / 180)*radius,centerY+(float)Math.sin((angle*2-90)*Math.PI / 180)*radius);
        mPath.lineTo( centerX-(float)Math.sin(angle*Math.PI / 180)*radius,centerY-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( centerX+(float)Math.sin(angle*Math.PI / 180)*radius,centerY-(float)Math.cos(angle*Math.PI / 180)*radius);
        mPath.lineTo( centerX-(float)Math.sin((angle*3-180)*Math.PI / 180)*radius,centerY+(float)Math.cos((angle*3-180)*Math.PI / 180)*radius);
//        mPath.lineTo(centerX,centerY-radius);
        mPath.close();
        canvas.drawPath(mPath,disactivPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch){//如果不支持觸摸
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        if (0.0 < touchX && touchX < rateWidth+padding/2) {
                            activeSize = 1;
                        }
                    }else {
                        if ((rateWidth+padding)*i-padding/2<touchX&&touchX<(rateWidth+padding)*(i+1)-padding/2){
                            activeSize = i+1;
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if ( null!= changeListener){
                    changeListener.change(activeSize);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                touchY = event.getY();
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        if (0.0 < touchX && touchX < rateWidth+padding/2) {
                            activeSize = 1;
                        }
                    }else {
                        if ((rateWidth+padding)*i-padding/2<touchX&&touchX<(rateWidth+padding)*(i+1)-padding/2){
                            activeSize = i+1;
                        }
                    }
                }
                invalidate();
                if (touchX<=0){
                    activeSize = 0;
                }
                break;
        }
        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //动态评论控件的选中数目
    public void setActivRate(int size){
        activeSize = size;
        invalidate();
    }

    //选中改变的回调
    public void setRateChangeListener(RateChangeListener changeListener){
        this.changeListener = changeListener;
    }


    public interface RateChangeListener{
        void change(int size);
    }

}
