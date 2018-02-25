package com.awesprojects.innolib.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by ilya on 2/23/18.
 */

public class PinKeyboardView extends LinearLayout {

    public static final int ACTION_LEFT = 0;
    public static final int ACTION_RIGHT = 1;

    public interface PinKeyboardCallback{
        void onKeyboardAction(int action);
        void onKeyboardKey(int number);
    }

    View[][] mKeyboardButtons;
    ImageView mActionLeft;
    ImageView mActionRight;
    OnKeyboardClickListener mKCL;
    PinKeyboardCallback mCallback;

    public PinKeyboardView(Context context) {
        super(context);
        init();
    }

    public PinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PinKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setEnabled(boolean is){
        super.setEnabled(is);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                mKeyboardButtons[i][j].setEnabled(is);
            }
        }
    }

    public void setKeyboardCallback(PinKeyboardCallback callback){
        mCallback = callback;
    }

    public Button createKeyboardButton(int i){
        Button button = new Button(getContext());
        button.setText(i+"");
        if (Build.VERSION.SDK_INT>=23)
            button.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Medium);
        else
            button.setTextAppearance(getContext(),android.R.style.TextAppearance_DeviceDefault_Medium);
        return button;
    }

    public void setActionImage(int action,@DrawableRes int actionDrawable){
        ImageView button;
        if (action==ACTION_LEFT){
            button = mActionLeft;
        }else{
            button = mActionRight;
        }
        if (actionDrawable!=0) {
            button.setImageResource(actionDrawable);
        }else{
            button.setImageDrawable(null);
        }
    }

    @SuppressLint("ResourceType")
    public void init(){
        setOrientation(LinearLayout.VERTICAL);
        mKCL = new OnKeyboardClickListener(this);
        mKeyboardButtons = new View[4][3];
        for (int i = 0; i < 3; i++) {
            LinearLayout line = new LinearLayout(getContext());
            for (int j = 0; j < 3; j++) {
                Button button = createKeyboardButton(i*3+j+1);
                line.addView(button,new LinearLayout.LayoutParams(-1,-1,1f));
                mKeyboardButtons[i][j] = button;
            }
            addView(line,new LinearLayout.LayoutParams(-1,-1,1f));
        }
        LinearLayout line = new LinearLayout(getContext());
        mActionLeft = new ImageButton(getContext());
        mActionLeft.setScaleType(ImageView.ScaleType.CENTER);
        mActionLeft.setId(10000);
        line.addView(mActionLeft,new LinearLayout.LayoutParams(-1,-1,1f));
        mKeyboardButtons[3][0] = mActionLeft;
        Button button = createKeyboardButton(0);
        line.addView(button,new LinearLayout.LayoutParams(-1,-1,1f));
        mActionLeft.setStateListAnimator(button.getStateListAnimator().clone());
        mKeyboardButtons[3][1] = button;
        mActionRight = new ImageButton(getContext());
        mActionRight.setScaleType(ImageView.ScaleType.CENTER);
        mActionRight.setStateListAnimator(button.getStateListAnimator().clone());
        mActionRight.setId(10001);
        line.addView(mActionRight,new LinearLayout.LayoutParams(-1,-1,1f));
        mKeyboardButtons[3][2] = mActionRight;
        addView(line,new LinearLayout.LayoutParams(-1,-1,1f));
        for (View[] vv : mKeyboardButtons)
            for (View v : vv)
                v.setOnClickListener(mKCL);
    }

    private class OnKeyboardClickListener implements OnClickListener{

        final PinKeyboardView mRef;

        public OnKeyboardClickListener(PinKeyboardView pcv){
            mRef = pcv;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {
            if (view instanceof ImageView){
                if (mRef.mCallback!=null)
                    mRef.mCallback.onKeyboardAction(view.getId()-10000);
            }else if(view instanceof Button){
                if (mRef.mCallback!=null)
                    mRef.mCallback.onKeyboardKey(Integer.parseInt( ((Button) view).getText().toString() ));
            }
        }
    }
}
