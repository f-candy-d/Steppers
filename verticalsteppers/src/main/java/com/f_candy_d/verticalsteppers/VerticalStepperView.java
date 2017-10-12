package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 *
 * Add the 'isChecked' attribute
 */
class VerticalStepperView extends RelativeLayout {

    private int mStepNumber;
    private String mTitle;
    private String mSubTitle;

    // Colors
    private int mStepperCircleActiveColor;
    private int mStepperCircleInactiveColor;
    private int mTitleActiveColor;
    private int mTitleInActiveColor;
    private int mSubTitleActiveColor;
    private int mSubTitleInactiveColor;

    // Dimens
    private int mStepperConnectionLineTopMargin;
    private int mStepperConnectionLineBottomMargin;

    // UI
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mStepperCircleView;
    private FrameLayout mContentViewContainer;
    private View mContentView;

    private Paint mStepperConnectionLinePaint;

    public VerticalStepperView(Context context) {
        super(context);
        init(null, 0);
    }

    public VerticalStepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VerticalStepperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // # Colors

        mStepperCircleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_active_color);
        mStepperCircleInactiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_inactive_color);
        mTitleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_title_active);
        mTitleInActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_title_inactive);
        mSubTitleActiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_sub_title_active);
        mSubTitleInactiveColor = ContextCompat.getColor(getContext(), R.color.vertical_stepper_sub_title_inactive);

        // # Dimens

        mStepperConnectionLineTopMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_top_margin);
        mStepperConnectionLineBottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_bottom_margin);

        // # Load Attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalStepperView, defStyle, 0);

        mTitle = a.getString(R.styleable.VerticalStepperView_title);
        mSubTitle = a.getString(R.styleable.VerticalStepperView_subTitle);
        mStepNumber = a.getInteger(R.styleable.VerticalStepperView_stepNumber, mStepNumber);
        // Load layout's resource id; -> https://stackoverflow.com/questions/25303979/custom-xml-attribute-to-a-layout-reference
        // And id = 0 is a invalid resource id; -> https://stackoverflow.com/questions/5130789/android-resource-ids
        final int contentViewResId = a.getResourceId(R.styleable.VerticalStepperView_contentViewLayout, 0);

        a.recycle();

        // # UI

        // Load the layout file
        inflate(getContext(), R.layout.vertical_stepper_view, this);

        mTitleView = findViewById(R.id.work_title);
        mSubTitleView = findViewById(R.id.work_sub_title);
        mStepperCircleView = findViewById(R.id.stepper_circle);
        mContentViewContainer = findViewById(R.id.content_view_container);

        // # Inflate a content view if an user specified it in a layout XML file

        if (contentViewResId != 0) {
            setContentView(inflate(getContext(), contentViewResId, null));
        }

        // # Add ripple-effect to myself

        // See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);

        // # Connection Line For Stepper Circle

        mStepperConnectionLinePaint = new Paint();
        mStepperConnectionLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.vertical_stepper_connector_line));
        mStepperConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width));

        // # Default values

        mTitleView.setText(mTitle);
        mSubTitleView.setText(mSubTitle);
        mStepperCircleView.setText(String.valueOf(mStepNumber));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

//        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // # Connection Line

        int connectionLineX = mStepperCircleView.getLeft() + mStepperCircleView.getWidth() / 2;
        int connectionLineStartY = mStepperCircleView.getBottom() + mStepperConnectionLineTopMargin;
        int connectionLineEndY = contentHeight - mStepperConnectionLineBottomMargin;

        canvas.drawLine(connectionLineX, connectionLineStartY, connectionLineX, connectionLineEndY, mStepperConnectionLinePaint);
    }
    
    public void setTitle(String title) {
        mTitle = title;
        mTitleView.setText(title);
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
        mSubTitleView.setText(subTitle);
    }

    public void setStepNumber(int stepNumber) {
        mStepNumber = stepNumber;
        mStepperCircleView.setText(String.valueOf(stepNumber));
    }

    public void setContentView(View view) {
        if (0 < mContentViewContainer.getChildCount()) {
            mContentViewContainer.removeAllViews();
        }

        mContentView = view;
        if (view != null) {
            mContentViewContainer.addView(view);
        }
    }

    public View getContentView() {
        return mContentView;
    }

    ViewGroup getContentViewContainer() {
        return mContentViewContainer;
    }

    // Default is TRUE
    private boolean mIsActiveUi = true;

    public void setIsActiveUi(boolean isActiveUi, boolean enforceUpdate) {
        if (isActiveUi == mIsActiveUi && !enforceUpdate) return;

        // # Stepper Circle Color

        Drawable bg = DrawableCompat.wrap(mStepperCircleView.getBackground());
        int color = (isActiveUi) ? mStepperCircleActiveColor : mStepperCircleInactiveColor;
        DrawableCompat.setTint(bg, color);
        DrawableCompat.setTintMode(bg, PorterDuff.Mode.SRC_IN);

        // # Title & Sub-Title Color

        color = (isActiveUi) ? mTitleActiveColor : mTitleInActiveColor;
        mTitleView.setTextColor(color);
        color = (isActiveUi) ? mSubTitleActiveColor : mSubTitleInactiveColor;
        mSubTitleView.setTextColor(color);

        // Update
        mIsActiveUi = isActiveUi;
    }

    // Default is FALSE
    private boolean mIsCompletedUi = false;

    public void setIsCompletedUi(boolean isCompletedUi, boolean enforceUpdate) {
        if (isCompletedUi == mIsCompletedUi && !enforceUpdate) return;



        // Update
        mIsCompletedUi = isCompletedUi;
    }
}