package com.bruce.bezier.utils;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * 贝塞尔曲线工具
 * Created by Bruce on 2017/6/15.
 */
public class BezierUtils {

    private static final int UNIT_EQUAL_PARTS = 1000;
    private ArrayList<PointF> mControlPoints;

    private static final BezierUtils ourInstance = new BezierUtils();


    public static BezierUtils getInstance() {
        return ourInstance;
    }

    private BezierUtils() {

    }

    /**
     * 贝塞尔曲线上t时间上点的Y坐标
     * @param i 阶数
     * @param j 控制点的下标
     * @param t 时间
     * @return 曲线在t时间上点的X坐标
     */
    private float bezierPointX(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).x + t * mControlPoints.get(j + 1).x;
        }
        return (1 - t) * bezierPointX(i - 1, j, t) + t * bezierPointX(i - 1, j + 1, t);
    }

    /**
     * 贝塞尔曲线上t时间上点的Y坐标
     * @param i 阶数
     * @param j 控制点的下标
     * @param t 时间
     * @return 曲线在t时间上点的Y坐标
     */
    private float bezierPointY(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).y + t * mControlPoints.get(j + 1).y;
        }
        return (1 - t) * bezierPointY(i - 1, j, t) + t * bezierPointY(i - 1, j + 1, t);
    }

    /**
     * 创建Bezier点集， 控制点集合长度必须大于2
     * 第一个控制点为起点，最后一个控制点为终点
     * @param controlPoints 控制点集合
     * @return 贝塞尔曲线点集合
     */
    public ArrayList<PointF> buildBezierPoints(ArrayList<PointF> controlPoints) {
        if(controlPoints == null || controlPoints.size() <= 2) {
            throw new IllegalArgumentException("控制点集合不能为空，且控制点个数必须大于2");
        }
        mControlPoints = controlPoints;
        ArrayList<PointF> points = new ArrayList<>();
        //阶数
        int order = mControlPoints.size() - 1;
        float delta = 1.0f / UNIT_EQUAL_PARTS;
        for (float t = 0; t <= 1; t += delta) {
            // Bezier点集
            points.add(new PointF(bezierPointX(order, 0, t), bezierPointY(order, 0, t)));
        }
        return points;
    }
}
