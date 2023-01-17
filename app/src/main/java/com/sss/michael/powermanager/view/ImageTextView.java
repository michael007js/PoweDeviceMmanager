package com.sss.michael.powermanager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;


import com.blankj.utilcode.util.LogUtils;
import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.util.DensityUtil;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;


/**
 * 自定义的TextView，扩展了直接在XML中设置四个方向图片的宽高
 * app:drawableLeft_it="@mipmap/personal_center_icon_fe"
 * app:drawableRight_it="@mipmap/right_arrow_13_14"
 * app:drawableWidthLeft_it="8dp"
 * app:drawableHeightLeft_it="8dp"
 * app:drawableWidthRight_it="2dp"
 * app:drawableHeightRight_it="3dp"
 * ...
 * Created by Administrator on 2018/9/14.
 */

public class ImageTextView extends AppCompatTextView {
    private Drawable mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom;//设置的图片
    private int mScaleWidthLeft, mScaleWidthTop, mScaleWidthRight, mScaleWidthBottom; // 图片的宽度
    private int mScaleHeightLeft, mScaleHeightTop, mScaleHeightRight, mScaleHeightBottom;// 图片的高度
    private String mLeftTxt = "";//左边的文字
    private String mRightTxt = "";//右边的文字
    private String tag = " ";

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageTextView);
        mLeftTxt = typedArray.getString(R.styleable.ImageTextView_textLeft) == null ? mLeftTxt : typedArray.getString(R.styleable.ImageTextView_textLeft);
        mRightTxt = typedArray.getString(R.styleable.ImageTextView_textRight) == null ? mRightTxt : typedArray.getString(R.styleable.ImageTextView_textRight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawableLeft = typedArray.getDrawable(R.styleable.ImageTextView_drawableLeft_it);
            mDrawableTop = typedArray.getDrawable(R.styleable.ImageTextView_drawableTop_it);
            mDrawableRight = typedArray.getDrawable(R.styleable.ImageTextView_drawableRight_it);
            mDrawableBottom = typedArray.getDrawable(R.styleable.ImageTextView_drawableBottom_it);
        }else {
            int dlId = typedArray.getResourceId(R.styleable.ImageTextView_drawableLeft_it, -1);
            int dtId = typedArray.getResourceId(R.styleable.ImageTextView_drawableTop_it, -1);
            int drId = typedArray.getResourceId(R.styleable.ImageTextView_drawableRight_it, -1);
            int dbId = typedArray.getResourceId(R.styleable.ImageTextView_drawableBottom_it, -1);

            if (dlId != -1) {
                mDrawableLeft = ContextCompat.getDrawable(getContext(),dlId);
            }
            if (dtId != -1) {
                mDrawableTop = ContextCompat.getDrawable(getContext(),dtId);
            }
            if (drId != -1) {
                mDrawableRight = ContextCompat.getDrawable(getContext(),drId);
            }
            if (dbId != -1) {
                mDrawableBottom = ContextCompat.getDrawable(getContext(),dbId);
            }
        }
        mScaleWidthLeft = typedArray
                .getDimensionPixelOffset(
                        R.styleable.ImageTextView_drawableWidthLeft_it,
                        10);
        mScaleWidthTop = typedArray
                .getDimensionPixelOffset(
                        R.styleable.ImageTextView_drawableWidthTop_it,
                        10);
        mScaleWidthRight = typedArray
                .getDimensionPixelOffset(
                        R.styleable.ImageTextView_drawableWidthRight_it,
                        10);
        mScaleWidthBottom = typedArray
                .getDimensionPixelOffset(
                        R.styleable.ImageTextView_drawableWidthBottom_it,
                        10);
        mScaleHeightLeft = typedArray.getDimensionPixelOffset(
                R.styleable.ImageTextView_drawableHeightLeft_it,
                10);
        mScaleHeightTop = typedArray.getDimensionPixelOffset(
                R.styleable.ImageTextView_drawableHeightTop_it,
                10);
        mScaleHeightRight = typedArray.getDimensionPixelOffset(
                R.styleable.ImageTextView_drawableHeightRight_it,
                10);
        mScaleHeightBottom = typedArray.getDimensionPixelOffset(
                R.styleable.ImageTextView_drawableHeightBottom_it,
                10);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawableLeft != null) {
            mDrawableLeft.setBounds(0, 0, mScaleWidthLeft, mScaleHeightLeft);

        }

        if (mDrawableTop != null) {
            mDrawableTop.setBounds(0, 0, mScaleWidthTop, mScaleHeightTop);
        }

        if (mDrawableRight != null) {
            mDrawableRight.setBounds(0, 0, mScaleWidthRight, mScaleHeightRight);

        }

        if (mDrawableBottom != null) {
            mDrawableBottom.setBounds(0, 0, mScaleWidthBottom, mScaleHeightBottom);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setCompoundDrawables(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);
    }

    /**
     * 设置左侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableLeft(Drawable drawable) {
        this.mDrawableLeft = drawable;
    }

    /**
     * 设置左侧图片并重绘
     *
     * @param drawableRes
     * @param context
     */
    public void setDrawableLeft(int drawableRes, Context context) {
        this.mDrawableLeft = ContextCompat.getDrawable(getContext(),drawableRes);
    }

    /**
     * 设置右侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableRight(Drawable drawable) {
        this.mDrawableRight = drawable;
    }

    /**
     * 设置右侧图片并重绘
     *
     * @param drawableRes
     * @param context
     */
    public void setDrawableRight(int drawableRes, Context context) {
        this.mDrawableRight = ContextCompat.getDrawable(getContext(),drawableRes);
    }

    /**
     * 设置上侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableTop(Drawable drawable) {
        this.mDrawableTop = drawable;
    }

    /**
     * 设置上侧图片并重绘
     *
     * @param drawableRes
     * @param context
     */
    public void setDrawableTop(int drawableRes, Context context) {
        this.mDrawableTop = ContextCompat.getDrawable(getContext(),drawableRes);
    }

    /**
     * 设置下侧图片并重绘
     *
     * @param drawable
     */
    public void setmDrawableBottom(Drawable drawable) {
        this.mDrawableBottom = drawable;
    }

    /**
     * 设置下侧图片并重绘
     *
     * @param drawableRes
     * @param context
     */
    public void setDrawableBottom(int drawableRes, Context context) {
        this.mDrawableBottom = ContextCompat.getDrawable(getContext(),drawableRes);
    }

    /**
     * 设置左侧图片宽高并重绘
     *
     * @param widthLeft
     */
    public void setSizeLeft(int widthLeft, int heightLeft) {
        this.mScaleWidthLeft = DensityUtil.dp2px(widthLeft);
        this.mScaleHeightLeft = DensityUtil.dp2px(heightLeft);
        if (mDrawableLeft != null) {
            mDrawableLeft.setBounds(0, 0, mScaleWidthLeft, mScaleHeightLeft);

        }
    }

    /**
     * 设置上侧宽高并重绘
     *
     * @param widthTop
     * @param heightTop
     */
    public void setSizeTop(int widthTop, int heightTop) {
        this.mScaleWidthTop = DensityUtil.dp2px(widthTop);
        this.mScaleHeightTop = DensityUtil.dp2px(heightTop);
        if (mDrawableTop != null) {
            mDrawableTop.setBounds(0, 0, mScaleWidthTop, mScaleHeightTop);

        }
    }

    /**
     * 设置右侧宽高并重绘
     *
     * @param widthRight
     * @param heightRight
     */
    public void setSizeRight(int widthRight, int heightRight) {
        this.mScaleWidthRight = DensityUtil.dp2px(widthRight);
        this.mScaleHeightRight = DensityUtil.dp2px(heightRight);
        if (mDrawableRight != null) {
            mDrawableRight.setBounds(0, 0, mScaleWidthRight, mScaleHeightRight);

        }
    }

    /**
     * 设置下侧宽高并重绘
     *
     * @param widthBottom
     * @param heightBottom
     */
    public void setSizeBottom(int widthBottom, int heightBottom) {
        this.mScaleWidthBottom = DensityUtil.dp2px(widthBottom);
        this.mScaleHeightBottom = DensityUtil.dp2px(heightBottom);
        if (mDrawableBottom != null) {
            mDrawableBottom.setBounds(0, 0, mScaleWidthBottom, mScaleHeightBottom);

        }
    }


    /**
     * 设置标签左右端显示文字(尚未解决不同设备上空格宽度不一致的问题)
     * @param leftTxt
     * @param rightTxt
     */
    @Deprecated
    public void setText(String leftTxt, String rightTxt) {
        mLeftTxt = leftTxt == null ? mLeftTxt : leftTxt;
        mRightTxt = rightTxt == null ? mRightTxt : rightTxt;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextPaint paint = getPaint();
                int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
                float leftWidth = paint.measureText(mLeftTxt, 0, mLeftTxt.length());
                float rightWidth = paint.measureText(mRightTxt, 0, mRightTxt.length());
                float spaceWidth = paint.measureText(tag, 0, tag.length());
                int spaceCount = (int) ((width - leftWidth - rightWidth - DensityUtil.dp2px(mScaleWidthLeft) - DensityUtil.dp2px(mScaleWidthRight)  / spaceWidth));
                LogUtils.e(
                        "标签宽度：" + width + "\n" +
                                "左边padding宽度：" + getPaddingLeft() + "\n" +
                                "右边padding宽度：" + getPaddingRight() + "\n" +
                                "左边图标宽度：" + DensityUtil.dp2px(mScaleWidthLeft) + "\n" +
                                "右边图标宽度：" + DensityUtil.dp2px(mScaleWidthRight) + "\n" +
                                "左边文字宽度：" + leftWidth + "\n" +
                                "右边文字宽度：" + rightWidth + "\n" +
                                "空格宽度：" + spaceWidth + "\n" +
                                "添加空格的个数：" + spaceCount + "\n"
                );
                StringBuilder sb = new StringBuilder();
                sb.append(mLeftTxt);
                for (int i = 0; i < spaceCount; i++) {
                    sb.append(tag);
                }
                sb.append(mRightTxt);
                setText(sb.toString());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }


    /**
     * 清除图片
     */
    public void clearImages() {
        mDrawableLeft = null;
        mDrawableTop = null;
        mDrawableRight = null;
        mDrawableBottom = null;
    }
}
