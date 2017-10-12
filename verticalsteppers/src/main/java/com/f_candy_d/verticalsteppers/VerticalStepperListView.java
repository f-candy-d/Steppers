package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by daichi on 10/12/17.
 */

public class VerticalStepperListView extends RecyclerView {

    public VerticalStepperListView(Context context) {
        super(context);
    }

    public VerticalStepperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalStepperListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final VerticalStepperAdapter build(Context context, VerticalStepperManager stepperManager) {
        // This recycler-view should has vertical layout
        super.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        VerticalStepperAdapter adapter = new VerticalStepperAdapter(stepperManager);
        super.setAdapter(new VerticalStepperAdapter(stepperManager));
        return adapter;
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        throw new RuntimeException("Do not use this method");
    }

    @Override
    public final void setLayoutManager(LayoutManager layout) {
        throw new RuntimeException("Do not use this method");
    }
}
