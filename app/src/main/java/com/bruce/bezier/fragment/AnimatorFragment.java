package com.bruce.bezier.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.bruce.bezier.R;
import com.bruce.bezier.interpolator.BezierInterpolator;

import java.util.ArrayList;

public class AnimatorFragment extends Fragment implements View.OnClickListener{

    private ArrayList<PointF> mControlPoints;
    private View mYellowView, mRedView;
    private Button mButton;

    public AnimatorFragment() {

    }

    public static AnimatorFragment newInstance() {
        return new AnimatorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animator, container, false);
        mYellowView = view.findViewById(R.id.view_yellow);
        mRedView = view.findViewById(R.id.view_red);
        mButton = (Button) view.findViewById(R.id.btn_start_animator);
        mButton.setOnClickListener(this);
        return view;
    }

    public void setControlPoints(ArrayList<PointF> controlPoints) {
        mControlPoints = controlPoints;
    }

    @Override
    public void onClick(View v) {
        if(mControlPoints == null) {
            return;
        }
        ObjectAnimator yellowAnimator = ObjectAnimator.ofFloat(mYellowView, View.TRANSLATION_Y, 0, 1300);
        yellowAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator redAnimator = ObjectAnimator.ofFloat(mRedView, View.TRANSLATION_Y, 0, 1300);
        redAnimator.setInterpolator(new BezierInterpolator(mControlPoints));

        AnimatorSet set = new AnimatorSet();
        set.play(yellowAnimator).with(redAnimator);
        set.setDuration(1000);
        set.start();
    }
}
