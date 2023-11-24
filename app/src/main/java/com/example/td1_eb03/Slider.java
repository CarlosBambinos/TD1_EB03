package com.example.td1_eb03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

public class Slider extends View {

    private float currentValue , minSlider = 0, maxSlide = 100 ;


    private float cursorDiameter, barWidth, barLength, ratio, value;

    private int cursorColor, valueColor, barColor, disabledColor;

    private Paint cursorPaint, valueBarPaint, barPaint;

    private boolean mEnabled = true ;

    public final static float DEFAULT_BAR_LENGTH = 160;
    public final static float DEFAULT_CURSOR_DIAMETER = 40;
    public final static float DEFAULT_BAR_WIDTH = 20;

    public final static float MIN_BAR_LENGTH = 160;

    public final static float MIN_CURSOR_DIAMETER = 30;

    public final static int DEFAULT_CURSOR_COLOR = 15;
    public final static int DEFAULT_VALUE_COLOR = 10;
    public final static int DEFAULT_BAR_COLOR = 20;

    public Slider(Context context){
        super(context);
        init(context,null);
    };
    public Slider(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context,attrs);

    };

    private float valueToRatio(float value){
            ratio = (value - minSlider / maxSlide - minSlider) ;
        return ratio;
    }

    private float ratioToValue(float ratio){
            value = (ratio*(maxSlide-minSlider)+minSlider);
            return value;
    }

    private Point toPos(float value){
        int x,y;
        x = (int)Math.max(cursorDiameter,barWidth)/2+getPaddingLeft();
        y = (int)((1-valueToRatio(value))*(barLength)+(cursorDiameter/2))+getPaddingTop();

        return new Point(x,y);
    }

    private float toValue(Point point){
        float ratio = 1-(point.y-getPaddingTop()-cursorDiameter/2)/barLength;
        if(ratio>1) ratio = 1;
        if(ratio<0) ratio = 0;
        return ratioToValue(ratio);
    }

    private void init(Context context, AttributeSet attrs){
        barPaint = new Paint();
        cursorPaint = new Paint();
        valueBarPaint = new Paint();

        barColor = ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary);
        cursorColor = ContextCompat.getColor(context, R.color.black);
        valueColor = ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary);
        disabledColor = ContextCompat.getColor(context, R.color.grey);

        // Application des constantes
        barLength = dpToPixel(DEFAULT_BAR_LENGTH);
        cursorDiameter = dpToPixel(DEFAULT_CURSOR_DIAMETER);
        barWidth = dpToPixel(DEFAULT_BAR_WIDTH);

        // Définiton des graphiques
        barPaint.setStrokeWidth(barWidth);
        valueBarPaint.setStrokeWidth(barWidth);
        cursorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        barPaint.setStrokeCap(Paint.Cap.ROUND);
        valueBarPaint.setStrokeCap(Paint.Cap.ROUND);

        if (mEnabled) {
            cursorPaint.setColor(cursorColor);
            barPaint.setColor(barColor);
            valueBarPaint.setColor(valueColor);
        }else {
            cursorPaint.setColor(disabledColor);
            barPaint.setColor(disabledColor);
            valueBarPaint.setColor(disabledColor);
        }

        setMinimumHeight((int)dpToPixel(MIN_BAR_LENGTH+MIN_CURSOR_DIAMETER)+getPaddingTop()+getPaddingBottom());
        setMinimumWidth((int)dpToPixel(MIN_CURSOR_DIAMETER)+getPaddingLeft()+getPaddingRight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point p1, p2, pc;

        p1 = toPos(minSlider);
        p2 = toPos(maxSlide);
        pc = toPos(value);

        canvas.drawLine(p1.x,p1.y,p2.x, p2.y,barPaint);
        canvas.drawLine(p1.x,p1.y,p2.x, p2.y,valueBarPaint);
        canvas.drawCircle(pc.x, pc.y, cursorDiameter/2, cursorPaint);
    }

    private float dpToPixel(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
