package com.awesprojects.innolib.widgets;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by ilya on 2/23/18.
 */

@SuppressLint("AppCompatCustomView")
public class PinFieldView extends TextView {

    public PinFieldView(Context context) {
        super(context);
        init();
    }

    public PinFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PinFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public String mCurrentPin = "";

    public void init(){
        setSingleLine(true);
        setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT>=23)
            setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Large);
        else
            setTextAppearance(getContext(),android.R.style.TextAppearance_DeviceDefault_Large);
        //setTextSize(getTextSize()*1.0f);
    }

    public void clearPin(){
        setText("");
        mCurrentPin = "";
    }

    public void addPinNumber(int pin){
        setText(getText().toString()+pin);
        mCurrentPin+=pin;
    }

    public void removeLastPinNumber(){
        if (getText().toString().length()>0)
            setText(getText().toString().substring(0,getPinLength()-1));
    }

    public void setPin(int pin){
        setText(pin);
        mCurrentPin = pin+"";
    }

    public int getPinLength(){
        return mCurrentPin.length();
    }

    public int[] getPin(){
        String pinStr = mCurrentPin;
        char[] chars = pinStr.toCharArray();
        int[] pin = new int[chars.length];
        for (int i = 0; i < chars.length; i++)
            pin[i] = Integer.parseInt(chars[i]+"");
        return pin;
    }

    public void startWrongTypeAnimation(){
        ValueAnimator backgroundAnimator = ValueAnimator.ofArgb(
                Color.TRANSPARENT,Color.argb(80,255,0,0),Color.TRANSPARENT);
        backgroundAnimator.addUpdateListener((valueAnimator) -> { setBackgroundColor((int)valueAnimator.getAnimatedValue());});
        backgroundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        backgroundAnimator.start();
    }

    public static class WrongPinBackgroundAnimator extends ValueAnimator{
        public WrongPinBackgroundAnimator(){
            addUpdateListener((valueAnimator) ->{

            });
        }
    }

}
