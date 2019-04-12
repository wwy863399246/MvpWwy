package com.app.common.commonwidget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.app.common.R;

public class SquareLine extends View
{
    private Paint paint;
    private ValueAnimator valueA;
    private int delayTime = 500;
    private boolean initView = false;
    float f = 0;
    private final int paint_color;
    private final int paint_size;
    Runnable runnable = () ->
    {
        start();
        invalidate();
    };

    public SquareLine(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareLine);
        paint_color = array.getColor(R.styleable.SquareLine_paint_color, context.getResources().getColor(R.color.FF7ECE28));
        paint_size = array.getDimensionPixelSize(R.styleable.SquareLine_paint_size, context.getResources().getDimensionPixelSize(R.dimen.y6));
        init();
    }

    private void init()
    {
        paint = new Paint();
        paint.setColor(paint_color);
        paint.setStrokeWidth(paint_size);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (!initView)
        {
            initView = true;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int length = width / 12;
        if (null != valueA)
        {
            if (valueA.isRunning())
            {
                f = (float) valueA.getAnimatedValue();
            }
            canvas.drawLines(new float[]{yValue(f), height / 2, length * 4 + yValue(f), height / 2}, paint);
            canvas.drawLines(new float[]{yValue(f) - width / 2, height / 2, length * 4 + yValue(f) - width / 2, height / 2}, paint);
            canvas.drawLines(new float[]{yValue(f) - width, height / 2, length * 4 + yValue(f) - width, height / 2}, paint);
            if (valueA.isRunning())
            {
                invalidate();
            }
        }
    }

    public void start()
    {
        if (valueA == null)
        {
            valueA = getValueAnimator();
        }
        else
        {
            valueA.start();
        }
        invalidate();
        postDelayed(runnable, valueA.getDuration());
    }

    public void stop()
    {
        if (null != getHandler())
        {
            getHandler().removeCallbacks(runnable);
        }

        if (valueA != null)
        {
            valueA.cancel();
        }
    }

    private ValueAnimator getValueAnimator()
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(delayTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        invalidate();
        return valueAnimator;
    }

    private float yValue(float x)
    {
        float allLength = getMeasuredWidth();
        return allLength / 1 * x;
    }
}
