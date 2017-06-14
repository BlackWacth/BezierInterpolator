package com.bruce.bezier.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Android大小单位转换工具类
 */
public class DisplayUtil {
		
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue);
	}

	public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取屏幕高宽
	 * 
	 * @param mContext
	 *            index_0为宽 index_1为高
	 */

	@SuppressWarnings("deprecation")
	public static int[] getAspect(Context mContext) {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return new int[] { width, height };
	}
	
	/**
	 * 在RelativeLayout中的view的摆布控制
	 * 
	 * @param mView
	 *            需要排布的控件
	 * @param per_x
	 *            布局中左边距占的百分比，为0则居中显示，-1则靠右显示
	 * @param per_y
	 *            布局中上间距占的百分比，为0则居中显示，-1则在底部
	 */

	public static void putViewPosition(View mView, float per_x, float per_y) {
		int[] aspect = getViewWH(mView);
		int viewLeft = (int) (aspect[0] * per_x);
		int viewTop = (int) (aspect[1] * per_y);
		LayoutParams layParams = (LayoutParams) mView.getLayoutParams();
		if (per_y == -1) {
			layParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}
		if (per_y != 0) {
			layParams.topMargin = viewTop;
		} else {
			layParams.addRule(RelativeLayout.CENTER_VERTICAL);
		}
		if (per_x == -1) {
			layParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		}
		if (per_x != 0) {
			layParams.leftMargin = viewLeft;
		} else {
			layParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		mView.setLayoutParams(layParams);
	}
	
	
	private static int[] getViewWH(View mView){
		RelativeLayout reLay=(RelativeLayout) mView.getParent();
		return new int[]{reLay.getWidth(),reLay.getHeight()};
		
	}
	
	
}