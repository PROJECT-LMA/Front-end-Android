package com.laioffer.lma.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.Nullable;

import com.laioffer.lma.utils.DpOrPxUtils;

public class WaveProgressView extends View {

    private Paint wavePaint;
    private Path wavePath;

    private float waveWidth;
    private float waveHeight;

    private int waveNum;
    private int defaultSize;
    private int maxHeight;
    private int viewSize;

    private WaveProgressAnim waveProgressAnim;
    private float percent;
    private float progressNum;
    private float maxNum;
    private float waveMovingDistance;


    public WaveProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        waveWidth = DpOrPxUtils.dip2px(context,15);
        waveHeight = DpOrPxUtils.dip2px(context,20);

        wavePath = new Path();
        wavePaint = new Paint();

        wavePaint.setColor(Color.GREEN);
        wavePaint.setAntiAlias(true);

        waveWidth = DpOrPxUtils.dip2px(context,20);
        waveHeight = DpOrPxUtils.dip2px(context,10);
        defaultSize = DpOrPxUtils.dip2px(context,200);
        maxHeight = DpOrPxUtils.dip2px(context,250);
        waveNum = (int)Math.ceil(Double.parseDouble(String.valueOf(defaultSize / waveWidth / 2)));

        percent = 0;
        progressNum = 0;
        maxNum = 100;
        waveProgressAnim = new WaveProgressAnim();
        waveMovingDistance = 0;
    }

    private Path getWavePath() {
        wavePath.reset();

        wavePath.moveTo(viewSize, (1 - percent) * viewSize);
        wavePath.lineTo(viewSize, viewSize);
        wavePath.lineTo(0, viewSize);
        wavePath.lineTo(-waveMovingDistance, (1 - percent) * viewSize);

        for (int i = 0; i < waveNum; i++) {
            wavePath.rQuadTo(waveWidth/2, waveHeight, waveWidth, 0);
            wavePath.rQuadTo(waveWidth/2, -waveHeight, waveWidth, 0);
        }

        wavePath.close();
        return wavePath;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = measureSize(defaultSize, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        viewSize = min;
        waveNum =(int) Math.ceil(Double.parseDouble(String.valueOf(viewSize / waveWidth / 2)));
    }

    private int measureSize(int defaultSize,int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    public class WaveProgressAnim extends Animation {

        public WaveProgressAnim(){}

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (percent < progressNum / maxNum) {
                percent = interpolatedTime * progressNum / maxNum;
            }
            waveMovingDistance = interpolatedTime * waveNum * waveWidth * 2;
            postInvalidate();
        }
    }

    public void setProgressNum(float progressNum, int time) {
        this.progressNum = progressNum;

        percent = 0;
        waveProgressAnim.setDuration(time);
        waveProgressAnim.setRepeatCount(Animation.INFINITE);
        waveProgressAnim.setInterpolator(new LinearInterpolator());
        this.startAnimation(waveProgressAnim);
    }
}
