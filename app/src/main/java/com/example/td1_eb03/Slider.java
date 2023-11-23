package com.example.td1_eb03;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class Slider extends View {

    private float currentValue , minSlider, maxSlide ;
    private float cursorDiameter, barWidth, barLength;

    private int cursorColor, valueColor, barColor;

    public final static float DEFAULT_BAR_LENGHT = 160;
    public final static float DEFAULT_CURSOR_DIAMETER = 40;
    public final static float DEFAULT_BAR_WIDTH = 30;

    public final static int DEFAULT_CURSOR_COLOR = 15;
    public final static int DEFAULT_VALUE_COLOR = 10;
    public final static int DEFAULT_BAR_COLOR = 20;

    public Slider(Context context){
        super(context);
    };
    public Slider(Context context, AttributeSet attrs){
        super(context, attrs);

    };

    public float valueToRatio(currentValue minSlider, maxSlider){

        return outputValue};

    public float ratioToValue(currentValue minSlider, maxSlider){

        return outputValue};

}
