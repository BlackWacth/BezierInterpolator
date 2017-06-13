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

    /**坐标宽度，也是整个View的宽度 */
    private static final int DEFAULT_WIDTH = 840;
    /**控制点的半径 */
    private static final int DEFAULT_POINT_RADIUS = 20;
    /**坐标线宽度 */
    private static final int DEFAULT_LINE_WIDTH = 6;
    /**触控合法区域宽度 */
    private static final int REGION_WIDTH = 20;
    /**手指矩形区域 */
    private static final int FINGER_RECT_SIZE = 40;

    /**画布宽度 */
    private final int mWidth = DEFAULT_WIDTH + DEFAULT_LINE_WIDTH / 2;
    /**画布高度 */
    private final int mHeight = DEFAULT_WIDTH * 2;
    /**画布X坐标轴起始坐标 */
    private final int mStartX = DEFAULT_LINE_WIDTH / 2;

    /**路径画笔 */
    private Paint mPathPaint;
    /**控制点画笔 */
    private Paint mPointPaint0, mPointPaint1, mPointPaint2;
    /**坐标画笔 */
    private Paint mCoordinatePaint;
    /**坐标辅助线画笔 */
    private Paint mAssistLinePaint;

    /**路径 */
    private Path mPath;

    /**控制点集合坐标 */
    private ArrayList<PointF> mControlPoints = null;

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

        mControlPoints = new ArrayList<>();
        mControlPoints.add(new PointF(DEFAULT_WIDTH / 4.0f, parseY(DEFAULT_WIDTH * 3.0f / 4)));
        mControlPoints.add(new PointF(DEFAULT_WIDTH / 2.0f, parseY(DEFAULT_WIDTH / 2.0f)));
        mControlPoints.add(new PointF(DEFAULT_WIDTH * 3.0f / 4, parseY(-DEFAULT_WIDTH / 4.0f)));

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        mPathPaint.setColor(getResources().getColor(R.color.red_500));

        mPointPaint0 = new Paint();
        mPointPaint0.setAntiAlias(true);
        mPointPaint0.setStyle(Paint.Style.FILL);
        mPointPaint0.setColor(getResources().getColor(R.color.deep_purple_500));

        mPointPaint1 = new Paint();
        mPointPaint1.setAntiAlias(true);
        mPointPaint1.setStyle(Paint.Style.FILL);
        mPointPaint1.setColor(getResources().getColor(R.color.blue_500));

        mPointPaint2 = new Paint();
        mPointPaint2.setAntiAlias(true);
        mPointPaint2.setStyle(Paint.Style.FILL);
        mPointPaint2.setColor(getResources().getColor(R.color.light_green_500));

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

        mPath = new Path();

    }

    /**
     * 坐标转换，从自定义坐标系转为canvas坐标系。
     * 当前坐标系，仅Y轴发生变化。
     * @param y 自定义坐标系y坐标
     * @return canvas坐标系y坐标
     */
    private float parseY(float y) {
        return (1 + 0.5f) * DEFAULT_WIDTH - y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DEFAULT_WIDTH + DEFAULT_LINE_WIDTH / 2, DEFAULT_WIDTH * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(getResources().getColor(R.color.grey));
        drawCoordinate(canvas);
        drawPoint(canvas);
        drawPath(canvas);
    }

    /**
     * 绘制坐标系
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //Y坐标
        canvas.drawLine(mStartX, 0, mStartX, mHeight, mCoordinatePaint);
        //X坐标
        canvas.drawLine(0, parseY(0), mWidth, parseY(0), mCoordinatePaint);
        //辅助线
        canvas.drawLine(0, parseY(DEFAULT_WIDTH), mWidth, parseY(DEFAULT_WIDTH), mAssistLinePaint);
    }

    /**
     * 绘制控制点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        canvas.drawCircle(mControlPoints.get(0).x, mControlPoints.get(0).y, DEFAULT_POINT_RADIUS, mPointPaint0);
        canvas.drawCircle(mControlPoints.get(1).x, mControlPoints.get(1).y, DEFAULT_POINT_RADIUS, mPointPaint1);
        canvas.drawCircle(mControlPoints.get(2).x, mControlPoints.get(2).y, DEFAULT_POINT_RADIUS, mPointPaint2);
    }

    /**
     * 绘制路径
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        if (mPath != null) {
            mPath.reset();
            mPath.moveTo(DEFAULT_LINE_WIDTH / 2, DEFAULT_WIDTH * 1.5f);
            mPath.lineTo(DEFAULT_LINE_WIDTH / 2 + DEFAULT_WIDTH, DEFAULT_WIDTH / 2);
            canvas.drawPath(mPath, mPathPaint);
        }
    }

    /**
     * 回去合法控制点
     * @param x
     * @param y
     * @return
     */
    private PointF getLegalControlPoint(float x, float y) {
        RectF rect = new RectF();
        for (PointF point : mControlPoints) {
            rect.set(point.x - REGION_WIDTH, point.y - REGION_WIDTH, point.x + REGION_WIDTH, point.y + REGION_WIDTH);
            if(rect.contains(x, y)) {
                return point;
            }
        }
        return null;
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

    /**
     *
     * 判断坐标是否在合法触摸区域中
     * @param x
     * @param y
     * @return
     */
    private boolean isLegalTouchRegion(float x, float y) {
        if (x <= REGION_WIDTH || x >= mWidth - REGION_WIDTH || y <= REGION_WIDTH || y >= mHeight - REGION_WIDTH) {
            return false;
        }
        RectF rectF = new RectF();
        for (PointF point : mControlPoints) {
            if(mCurPoint != null && mCurPoint.equals(point)) {
                continue;
            }
            rectF.set(point.x - REGION_WIDTH, point.y - REGION_WIDTH, point.x + REGION_WIDTH, point.y + REGION_WIDTH);
            if (rectF.contains(x, y)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if(mCurPoint == null) {
                    mCurPoint = getLegalControlPoint(x, y);
                }
                if(mCurPoint != null && isLegalTouchRegion(x, y)) {
                    if(isLegalFingerRegion(x, y)) {
                        mCurPoint.x = x;
                        mCurPoint.y = y;
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                mCurPoint = null;
                break;
        }

        return true;
    }
}
