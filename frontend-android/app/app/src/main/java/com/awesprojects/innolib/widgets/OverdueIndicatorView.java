package com.awesprojects.innolib.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.awesprojects.innolib.R;

@SuppressLint("AppCompatCustomView")
public class OverdueIndicatorView extends ImageView {

    float pivotX = 0;
    float pivotY = 0;
    int mTintColor;
    public OverdueIndicatorView(Context context) {
        super(context);
        init();
    }

    public OverdueIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverdueIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public OverdueIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        pivotX = getResources().getDimension(R.dimen.home_profile_checkout_overdue_pivot_x);
        pivotY = getResources().getDimension(R.dimen.home_profile_checkout_overdue_pivot_y);
        if (Build.VERSION.SDK_INT>=23) {
            mTintColor = getResources().getColor(R.color.colorBrightRed, null);
        }else{
            mTintColor = getResources().getColor(R.color.colorBrightRed);
        }
        setImageResource(R.drawable.ic_schedule_black_18dp);
        setScaleType(ScaleType.CENTER);
        setColorFilter(mTintColor, PorterDuff.Mode.SRC_ATOP);
        setOverdue(true);
    }

    public void setOverdue(boolean overdue){
        if (overdue){
            RotateAnimation rotateAnimation = new RotateAnimation(0,360,pivotX,pivotY);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setDuration(1000);
            setAnimation(rotateAnimation);
        }else{
            setAnimation(null);
        }
    }
}
