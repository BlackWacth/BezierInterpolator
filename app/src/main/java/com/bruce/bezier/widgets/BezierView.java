package com.bruce.bezier.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bruce.bezier.R;

import java.util.ArrayList;

/**
 *
 * Created by huazh on 2017/6/12.
 */
public class BezierView extends View {

    private static final String TAG = "BezierView";

    /**控制点的半径 */
    private static final int DEFAULT_POINT_RADIUS = 30;
    /**坐标线宽度 */
    private static final int DEFAULT_LINE_WIDTH = 6;
    /**触控合法区域宽度 */
    private static final int TOUCH_REGION_WIDTH = 50;
    /**手指矩形区域 */
    private static final int FINGER_RECT_SIZE = 40;

    /**贝塞尔曲线在单位1的时间下等分数 */
    public static final float UNIT_EQUAL_PARTS = 1000;

    /**画布宽度 */
    private float mWidth;
    /**画布高度 */
    private float mHeight;

    /**坐标系可用宽度 */
    private float mCoordinateWidth;

    /**坐标原点 */
    private PointF mOriginPoint = new PointF();

    /**横向辅助线，与X轴平行 */
    private float mHAssistLineY;
    /**纵向辅助线，与Y轴平行 */
    private float mVAssistLineX;

    /**路径画笔 */
    private Paint mPathPaint;
    /**控制点画笔 */
    private Paint mPointPaint1, mPointPaint2, mPointPaint3;
    /**坐标画笔 */
    private Paint mCoordinatePaint;
    /**坐标辅助线画笔 */
    private Paint mAssistLinePaint;
    /**控制点连接线线画笔 */
    private Paint mControlLinePaint;

    /**路径 */
    private Path mPath;

    /**控制点集合坐标 */
    private final ArrayList<PointF> mControlPoints = new ArrayList<>();;

    /**贝塞尔点集合坐标 */
    private ArrayList<PointF> mBezierPoints = null;

    /**当前触控点 */
    private PointF mCurPoint;


    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        mPathPaint.setColor(getResources().getColor(R.color.red_500));

        mPointPaint1 = new Paint();
        mPointPaint1.setAntiAlias(true);
        mPointPaint1.setStyle(Paint.Style.FILL);
        mPointPaint1.setColor(getResources().getColor(R.color.deep_purple_500));

        mPointPaint2 = new Paint();
        mPointPaint2.setAntiAlias(true);
        mPointPaint2.setStyle(Paint.Style.FILL);
        mPointPaint2.setColor(getResources().getColor(R.color.blue_500));

        mPointPaint3 = new Paint();
        mPointPaint3.setAntiAlias(true);
        mPointPaint3.setStyle(Paint.Style.FILL);
        mPointPaint3.setColor(getResources().getColor(R.color.light_green_500));

        mCoordinatePaint = new Paint();
        mCoordinatePaint.setAntiAlias(true);
        mCoordinatePaint.setStyle(Paint.Style.STROKE);
        mCoordinatePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        mCoordinatePaint.setColor(Color.BLACK);

        mAssistLinePaint = new Paint();
        mAssistLinePaint.setAntiAlias(true);
        mAssistLinePaint.setStyle(Paint.Style.STROKE);
        mAssistLinePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        mAssistLinePaint.setColor(Color.BLACK);
        mAssistLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

        mControlLinePaint = new Paint();
        mControlLinePaint.setAntiAlias(true);
        mControlLinePaint.setStyle(Paint.Style.STROKE);
        mControlLinePaint.setStrokeWidth(DEFAULT_LINE_WIDTH - 2);
        mControlLinePaint.setColor(getResources().getColor(R.color.md_teal_500));

        mPath = new Path();

        mControlPoints.clear();
        for (int i = 0; i < 5; i++) {
            mControlPoints.add(new PointF());
        }
    }

    /**
     * Y坐标转换，从自定义Y轴坐标转换为canvas的Y轴坐标
     * 需要在onMeasure后使用
     * @param y 自定义X坐标
     * @return canvas Y坐标
     */
    private float parseY(float y) {
        return mCoordinateWidth + mHAssistLineY - y;
    }

    /**
     * X坐标转换，从自定义X轴坐标转换为canvas的X轴坐标
     * 需要在onMeasure后使用
     * @param x 自定义X坐标
     * @return canvas X坐标
     */
    private float parseX(float x) {
        return DEFAULT_POINT_RADIUS + x;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCoordinateWidth = mWidth - DEFAULT_POINT_RADIUS * 2;
        mHAssistLineY = (mHeight - mCoordinateWidth) / 2.0f;
        mVAssistLineX = mWidth - DEFAULT_POINT_RADIUS;
        mOriginPoint.x = parseX(0);
        mOriginPoint.y = parseY(0);

        //贝塞尔曲线起点
        mControlPoints.get(0).x = mOriginPoint.x;
        mControlPoints.get(0).y = mOriginPoint.y;

        mControlPoints.get(1).x = parseX(100f);
        mControlPoints.get(1).y = parseY(800f);

        mControlPoints.get(2).x = parseX(300f);
        mControlPoints.get(2).y = parseY(100f);

        mControlPoints.get(3).x = parseX(800f);
        mControlPoints.get(3).y = parseY(-200f);

        //贝塞尔曲线终点
        mControlPoints.get(4).x = parseX(mCoordinateWidth);
        mControlPoints.get(4).y = parseY(mCoordinateWidth);

        mBezierPoints = buildBezierPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(getResources().getColor(R.color.grey));
        drawCoordinate(canvas);
        drawPoint(canvas);
        drawPointCoordinate(canvas);
        drawPath(canvas);
    }

    /**
     * 绘制坐标系
     * @param canvas canvas
     */
    private void drawCoordinate(Canvas canvas) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //Y坐标
        canvas.drawLine(mOriginPoint.x, 0, mOriginPoint.x, mHeight, mCoordinatePaint);
        //X坐标
        canvas.drawLine(0, mOriginPoint.y, mWidth, mOriginPoint.y, mCoordinatePaint);

        //横向辅助线
        canvas.drawLine(0, mHAssistLineY, mWidth, mHAssistLineY, mAssistLinePaint);
        //纵向辅助线
        canvas.drawLine(mVAssistLineX, 0, mVAssistLineX, mHeight, mAssistLinePaint);
    }

    /**
     * 绘制控制点
     * @param canvas canvas
     */
    private void drawPoint(Canvas canvas) {
        //绘制控制点连接线
        for (int i = 0; i < mControlPoints.size() - 1; i++) {
            canvas.drawLine(mControlPoints.get(i).x, mControlPoints.get(i).y, mControlPoints.get(i + 1).x, mControlPoints.get(i + 1).y, mControlLinePaint);
        }
        //绘制控制点
        canvas.drawCircle(mControlPoints.get(1).x, mControlPoints.get(1).y, DEFAULT_POINT_RADIUS, mPointPaint1);
        canvas.drawCircle(mControlPoints.get(2).x, mControlPoints.get(2).y, DEFAULT_POINT_RADIUS, mPointPaint2);
        canvas.drawCircle(mControlPoints.get(3).x, mControlPoints.get(3).y, DEFAULT_POINT_RADIUS, mPointPaint3);
    }

    /**
     * 绘制控制点坐标
     * @param canvas canvas
     */
    private void drawPointCoordinate(Canvas canvas) {

        canvas.drawText(getPointCoordinateText(mControlPoints.get(1)), 50, 50, mPointPaint1);
        canvas.drawText(getPointCoordinateText(mControlPoints.get(2)), 50, 100, mPointPaint2);
        canvas.drawText(getPointCoordinateText(mControlPoints.get(3)), 50, 150, mPointPaint3);
    }

    private String getPointCoordinateText(PointF point) {
        return "x:" + point.x + "-y:" + point.y;
    }

    /**
     * 绘制路径
     * @param canvas canvas
     */
    private void drawPath(Canvas canvas) {
        if(mPath == null) {
            return;
        }
        mPath.reset();
        mPath.moveTo(mControlPoints.get(0).x, mControlPoints.get(0).y);
        for (PointF point : mBezierPoints) {
            mPath.lineTo(point.x, point.y);
        }
        canvas.drawPath(mPath, mPathPaint);
    }

    /**
     * 根据触摸时的x,y，获取View中的控制点。
     * 起点和终点除外
     * @param x x
     * @param y y
     * @return point
     */
    private PointF getLegalControlPoint(float x, float y) {
        RectF rect = new RectF();
        PointF point;
        for (int i = 1; i < mControlPoints.size() - 1; i++) {
            point = mControlPoints.get(i);

            //增加触控区域
            if(point.x < DEFAULT_POINT_RADIUS * 2) {//左边靠边
                rect.set(point.x, point.y - TOUCH_REGION_WIDTH * 2, point.x + TOUCH_REGION_WIDTH * 2, point.y + TOUCH_REGION_WIDTH * 2);
            } else if ((mWidth - point.x) < DEFAULT_POINT_RADIUS * 2) { //右边靠边
                rect.set(point.x - TOUCH_REGION_WIDTH * 2, point.y - TOUCH_REGION_WIDTH * 2, point.x, point.y + TOUCH_REGION_WIDTH * 2);
            } else if(point.y < DEFAULT_POINT_RADIUS * 2) { //上边靠边
                rect.set(point.x - TOUCH_REGION_WIDTH * 2, point.y, point.x + TOUCH_REGION_WIDTH * 2, point.y + TOUCH_REGION_WIDTH * 2);
            } else if ((mHeight - point.y) < DEFAULT_POINT_RADIUS * 2) { //下边靠边
                rect.set(point.x - TOUCH_REGION_WIDTH * 2, point.y, point.x + TOUCH_REGION_WIDTH * 2, point.y + TOUCH_REGION_WIDTH * 2);
            } else { //正常情况
                rect.set(point.x - TOUCH_REGION_WIDTH, point.y - TOUCH_REGION_WIDTH, point.x + TOUCH_REGION_WIDTH, point.y + TOUCH_REGION_WIDTH);
            }
            if(rect.contains(x, y)) {
                return point;
            }
        }
        return null;
    }

    /**
     *
     * 判断坐标是否在合法触摸区域中
     * @param x x
     * @param y y
     * @return 合法区域 : true
     */
    private boolean isLegalTouchRegion(float x, float y) {
        if (x < DEFAULT_POINT_RADIUS || x > mWidth - DEFAULT_POINT_RADIUS || y < DEFAULT_POINT_RADIUS || y > mHeight - DEFAULT_POINT_RADIUS) {
            return false;
        }
        RectF rectF = new RectF();
        PointF point;
        for (int i = 1; i < mControlPoints.size() - 1; i++) {
            point = mControlPoints.get(i);
            if(mCurPoint != null && mCurPoint.equals(point)) {
                continue;
            }
            rectF.set(point.x - TOUCH_REGION_WIDTH, point.y - TOUCH_REGION_WIDTH, point.x + TOUCH_REGION_WIDTH, point.y + TOUCH_REGION_WIDTH);
            if (rectF.contains(x, y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断手指坐标是否在合法区域中
     * @param x
     * @param y
     * @return
     */
    private boolean isLegalFingerRegion(float x, float y) {
        if(mCurPoint != null) {
            RectF rectF = new RectF(mCurPoint.x - FINGER_RECT_SIZE / 2,
                    mCurPoint.y - FINGER_RECT_SIZE / 2,
                    mCurPoint.x + FINGER_RECT_SIZE / 2,
                    mCurPoint.y + FINGER_RECT_SIZE / 2);
            if(rectF.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    float x, y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                if(mCurPoint == null) {
                    mCurPoint = getLegalControlPoint(x, y);
                }

                if(mCurPoint != null && isLegalTouchRegion(x, y)) {
                    mCurPoint.x = x;
                    mCurPoint.y = y;
                    mBezierPoints = buildBezierPoints();
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                mCurPoint = null;
                break;
        }
        return true;
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
     * 创建Bezier点集
     *
     * @return Bezier曲线点集
     */
    private ArrayList<PointF> buildBezierPoints() {
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
