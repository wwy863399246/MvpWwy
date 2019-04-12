package com.app.common.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.app.common.R;
import com.app.common.commonutils.LogUtils;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public class ProgressColorView extends View
{

    public ProgressColorView(Context context)
    {
        super(context);
    }

    /** 渐变颜色 */
    private int[] SECTION_COLORS;
    /** 进度条最大值 */
    private float maxCount=100f;
    /** 进度条当前值 */
    private float currentCount=0.1f;
    /** 画笔 */
    private Paint mPaint;
    private int mWidth, mHeight;//设置宽度和高度
    private int backColor;//进度条默认颜色
    private int startColor;//开始颜色
    private int endColor;//末尾颜色


    public ProgressColorView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ProgressColorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        // 获得我们所定义的自定义样式属性 
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressColorView, defStyleAttr, 0);
        backColor = typedArray.getColor(R.styleable.ProgressColorView_backColor, Color.GRAY);
        startColor = typedArray.getColor(R.styleable.ProgressColorView_startColor, Color.RED);
        endColor = typedArray.getColor(R.styleable.ProgressColorView_endColor, Color.RED);
        SECTION_COLORS = new int[]{startColor, endColor};
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int round = mHeight / 2;

        //绘制背景
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(backColor);
        RectF rectBlackBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);

        //绘制精度
        float section = currentCount / maxCount;
        RectF rectProgressBg = new RectF(0, 0, mWidth * section, mHeight);
        int[] colors = new int[SECTION_COLORS.length];
        System.arraycopy(SECTION_COLORS, 0, colors, 0, SECTION_COLORS.length);
        float[] positions = new float[SECTION_COLORS.length];
        positions[0] = 0.0f;
        positions[1] = 1.0f - positions[0];
        positions[positions.length - 1] = 1.0f;
        //线性渲染
        LinearGradient shader = new LinearGradient(0, 0, mWidth * section, mHeight, colors, null, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
    }

    /***
     * 设置最大的进度值
     * @param maxCount
     */
    public void setMaxCount(float maxCount)
    {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     * @param currentCount
     */
    public void setCurrentCount(float currentCount)
    {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        post(new Runnable()
        {
            @Override
            public void run()
            {
                invalidate();
            }
        });
    }

    //获取进度值
    public float getCurrentCount()
    {
        return currentCount;
    }

    /**
     * dp转换成像素
     *
     * @param dip
     * @return
     */
    private int dipToPx(int dip)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * UNSPECIFIED 父容器没有对当前View有任何限制，当前View可以任意取尺寸
     * EXACTLY 当前的尺寸就是当前View应该取的尺寸
     * AT_MOST 当前尺寸是当前View能取的最大尺寸
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST)
//        {
//            mWidth = widthSpecSize;
//        }
//        else
//        {
//            mWidth = 0;
//        }
//        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED)
//        {
//            mHeight = dipToPx(16);
//        }
//        else
//        {
//            mHeight = heightSpecSize;
//        }
//        setMeasuredDimension(mWidth, mHeight);
//    }
}
