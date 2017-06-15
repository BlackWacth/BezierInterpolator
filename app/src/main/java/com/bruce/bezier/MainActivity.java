package com.bruce.bezier;

import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.bruce.bezier.fragment.AnimatorFragment;
import com.bruce.bezier.fragment.BezierFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    private BezierFragment mBezierFragment;
    private AnimatorFragment mAnimatorFragment;
    private ArrayList<PointF> mControlPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_view_pager);
        mList.clear();
        mBezierFragment = BezierFragment.newInstance();
        mList.add(mBezierFragment);
        mAnimatorFragment = AnimatorFragment.newInstance();
        mList.add(mAnimatorFragment);
        mViewPager.setAdapter(new BezierPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                } else if (position == 1) {
                    mAnimatorFragment.setControlPoints(mBezierFragment.getControlPoints());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class BezierPagerAdapter extends FragmentStatePagerAdapter {

        public BezierPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }
}
