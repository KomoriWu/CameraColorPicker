package com.komori.camera.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

public class ColorPickView extends View {
    private Context context;
    private int rudeRadius; // 可移动小球的半径
    private int centerColor; // 可移动小球的颜色
    private int scaleType;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap; // 背景图片
    private Paint mPaintBitmap; // 背景画笔
    private Paint mCenterPaint; // 可移动小球画笔
    private Point mRockPosition;// 小球当前位置
    private String bitmapPath;
    private Bitmap newBitmap;
    private boolean isFirst = true;
    private OnColorChangedListener listener; // 小球移动的监听


    public ColorPickView(Context context) {
        this(context, null);
    }

    public ColorPickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public ColorPickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.listener = listener;
    }


    private void init(AttributeSet attrs, int defStyleAttr) {
        // 获取自定义组件的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.color_picker,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.color_picker_center_color:
                    centerColor = a.getColor(R.styleable.color_picker_center_color,
                            Color.WHITE);
                    break;
                case R.styleable.color_picker_center_radius:
                    rudeRadius = a.getDimensionPixelOffset(R.styleable.
                            color_picker_center_radius, 10);
                    break;
                case R.styleable.color_picker_image:
//                    mBitmap = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, R.
//                            drawable.splash_background));
                    break;
                case R.styleable.color_picker_imageScaleType:
                    scaleType = a.getInt(attr, 0);
                    break;
            }
        }
        a.recycle();
        mBitmap = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/cameracolorpicker.komori.com.cameracolorpicker/files/Pictures/picture.jpg", new
                BitmapFactory.Options());
        // 中心位置坐标
        mRockPosition = new Point();
        // 初始化背景画笔和可移动小球的画笔
        mPaintBitmap = new Paint();
        mPaintBitmap.setAntiAlias(true);

        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setStrokeWidth(10);
        mCenterPaint.setStyle(Paint.Style.STROKE);
        mCenterPaint.setColor(centerColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景图片
        canvas.drawBitmap(newBitmap, 0, 0, mPaintBitmap);
        // 画中心小球
        canvas.drawCircle(mRockPosition.x, mRockPosition.y, rudeRadius,
                mCenterPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
        //设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            //bitmap 决定的宽
            mWidth = getPaddingLeft() + getPaddingRight() + mBitmap.getWidth();
        }

        //设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            //bitmap 决定的高
            mHeight = getPaddingTop() + getPaddingBottom() + mBitmap.getHeight();
        }
        if (isFirst) {
            Matrix matrix = new Matrix();
            float w = (float)mWidth /  mBitmap.getWidth();
            float h = (float)mHeight /  mBitmap.getHeight();
            matrix.postScale(w, h);//获取缩放比例
            matrix.postRotate(90);//旋转
            newBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), matrix, true); //根据缩放比例获取新的位图

            mRockPosition.set(newBitmap.getWidth() / 2, newBitmap.getHeight() / 2);

            int color = newBitmap.getPixel(mRockPosition.x, mRockPosition.y);
            mCenterPaint.setColor(color);
            listener.onColorChange(color);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isFirst = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
                break;
            case MotionEvent.ACTION_MOVE: // 移动
                if ((int) event.getX() < mWidth && (int) event.getY() < mHeight) {
                    mRockPosition.set((int) event.getX(), (int) event.getY());
                    int color = newBitmap.getPixel(mRockPosition.x, mRockPosition.y);
                    mCenterPaint.setColor(color);
                    listener.onColorChange(color);
                }
                break;
            case MotionEvent.ACTION_UP:// 抬起

                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    // 颜色发生变化的回调接口
    public interface OnColorChangedListener {
        void onColorChange(int color);
    }
}
