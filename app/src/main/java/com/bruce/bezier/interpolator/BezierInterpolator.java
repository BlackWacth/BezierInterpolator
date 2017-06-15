package com.bruce.bezier.interpolator;

import android.graphics.PointF;
import android.view.animation.Interpolator;

import com.bruce.bezier.utils.BezierUtils;

import java.util.ArrayList;

/**
 * 贝塞尔曲线插值器
 * Created by Bruce on 2017/6/15.
 */
public class BezierInterpolator implements Interpolator {

    private static final float PRECISION = 0.002f;

    private ArrayList<PointF> mBezierPoints;

    public BezierInterpolator(ArrayList<PointF> controlPoints) {
        initBezierPoint(controlPoints);
    }

    public BezierInterpolator(float cx, float cy) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(0, 0));
        points.add(new PointF(cx, cy));
        points.add(new PointF(1f, 1f));
        initBezierPoint(points);
    }

    public BezierInterpolator(float cx1, float cy1, float cx2, float cy2) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(0, 0));
        points.add(new PointF(cx1, cy1));
        points.add(new PointF(cx2, cy2));
        points.add(new PointF(1f, 1f));
        initBezierPoint(points);
    }

    public BezierInterpolator(float cx1, float cy1, float cx2, float cy2, float cx3, float cy3) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(0, 0));
        points.add(new PointF(cx1, cy1));
        points.add(new PointF(cx2, cy2));
        points.add(new PointF(cx3, cy3));
        points.add(new PointF(1f, 1f));
        initBezierPoint(points);
    }

    private void initBezierPoint(ArrayList<PointF> points) {
        if(points.get(0).x != 0 || points.get(0).y != 0 || points.get(points.size() - 1).x != 1f || points.get(points.size() - 1).y != 1f) {
            throw new IllegalArgumentException("控制点起始必须为(0,0)， 终点必须为(1,1)");
        }
        mBezierPoints = BezierUtils.getInstance().buildBezierPoints(points);
    }


    @Override
    public float getInterpolation(float t) {
        if (t <= 0) {
            return 0;
        } else if (t >= 1) {
            return 1;
        }

        int startIndex = 0;
        int endIndex = mBezierPoints.size() - 1;

        while (endIndex - startIndex > 1) {
            int midIndex = (startIndex + endIndex) / 2;
            if (t < mBezierPoints.get(midIndex).x) {
                endIndex = midIndex;
            } else {
                startIndex = midIndex;
            }
        }

        float xRange = mBezierPoints.get(endIndex).x - mBezierPoints.get(startIndex).x;
        if(xRange == 0) {
            return mBezierPoints.get(startIndex).x;
        }

        float tInRange = t - mBezierPoints.get(startIndex).x;
        float fraction = tInRange / xRange;

        float startY = mBezierPoints.get(startIndex).y;
        float endY = mBezierPoints.get(endIndex).y;
        return startY + (fraction * (endY - startY));
    }
}
