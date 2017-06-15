package com.bruce.bezier.fragment;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bruce.bezier.R;
import com.bruce.bezier.widgets.BezierView;

import java.util.ArrayList;

public class BezierFragment extends Fragment {

    private BezierView mBezierView;

    public BezierFragment() {

    }

    public static BezierFragment newInstance() {
        return new BezierFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bezier, container, false);
        mBezierView = (BezierView) view.findViewById(R.id.bv_bezier_view);
        return view;
    }

    public ArrayList<PointF> getControlPoints() {
        return mBezierView.getControlPoints();
    }
}
