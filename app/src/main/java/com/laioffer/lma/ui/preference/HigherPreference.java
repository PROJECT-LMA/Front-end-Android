package com.laioffer.lma.ui.preference;

import android.content.Context;
import android.content.res.Resources;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.laioffer.lma.R;

public class HigherPreference extends Preference {
    public HigherPreference(Context context) {
        super(context);
    }
    public HigherPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected View onCreateView(ViewGroup parent) {
        Resources res=getContext().getResources();
        View v=super.onCreateView(parent);
        v.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, res.getDimensionPixelSize(R.dimen.preferenceHeight)));
        return v;
    }
}