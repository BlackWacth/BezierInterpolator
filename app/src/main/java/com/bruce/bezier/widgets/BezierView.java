package com.bruce.bezier.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bruce.bezier.R;

/**
 *
 * Created by huazh on 2017/6/12.
 */
public class BezierView extends View {

    private static final int DEFAULT_WIDTH = 840;
    private static final int DEFAULT_POINT_SIZE = 50;
    private static final int DEFAULT_LINE_WIDTH = 6;

    private Paint mPathPaint;
    private Paint mPointPaint0, mPointPaint1, mPointPaint2;
    //坐标
    private Paint mCoordinatePaint, mAssistLinePaint;

    private Path mPath;

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

    @SuppressLint("DrawAllocation")
    private void drawCoordinate(Canvas canvas) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //Y坐标
        canvas.drawLine(DEFAULT_LINE_WIDTH / 2, 0, DEFAULT_LINE_WIDTH / 2, DEFAULT_LINE_WIDTH / 2 + DEFAULT_WIDTH * 2, mCoordinatePaint);
        //X坐标
        canvas.drawLine(DEFAULT_LINE_WIDTH / 2, DEFAULT_WIDTH * 1.5f, DEFAULT_LINE_WIDTH / 2 + DEFAULT_WIDTH, DEFAULT_WIDTH * 1.5f, mCoordinatePaint);
        //辅助线
        canvas.drawLine(DEFAULT_LINE_WIDTH / 2, DEFAULT_WIDTH / 2, DEFAULT_LINE_WIDTH / 2 + DEFAULT_WIDTH, DEFAULT_WIDTH / 2, mAssistLinePaint);
    }

    private void drawPoint(Canvas canvas) {

    }

    private void drawPath(Canvas canvas) {
        if (mPath != null) {
            mPath.reset();
            mPath.moveTo(DEFAULT_LINE_WIDTH / 2, DEFAULT_WIDTH * 1.5f);
            mPath.lineTo(DEFAULT_LINE_WIDTH / 2 + DEFAULT_WIDTH, DEFAULT_WIDTH / 2);
            canvas.drawPath(mPath, mPathPaint);
        }
    }
}
