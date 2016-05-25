package com.uniquedu.cemetery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.uniquedu.cemetery.R;

/**
 * Created by ZhongHang on 2016/3/29.
 */
public class MyEditTextView extends EditText {
    private float width;
    private float height;
    private Paint mPaint;
    private int mMaxLength = 100;
    private int textSize;

    public MyEditTextView(Context context) {
        super(context);
    }

    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextView);
        mMaxLength = a.getInt(R.styleable.MyEditTextView_maxLength, 0);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
        textSize = a.getDimensionPixelSize(R.styleable.MyEditTextView_textSize, 15);
    }

    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMaxLength != 0) {
            height = getHeight();
            canvas.drawText(getText().toString().length() + "/" + mMaxLength, width - mPaint.measureText("50/200"), height - textSize / 2, mPaint);
        }
    }
}
