package com.example.sergio.miapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    Boolean firstTime;
    private ViewPager mSliderViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapter sliderAdapter;
    //botones next y prev
    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            mCurrentPage = i;

            if (i == 0) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Siguiente");
                mBackBtn.setText("");
            } else if (i == mDots.length - 1) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Hecho");
                mBackBtn.setText("Atrás");
            } else {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Siguiente");
                mBackBtn.setText("Atrás");
            }
        }

        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime", true);

        if (firstTime) {
            mSliderViewPager = (ViewPager) findViewById(R.id.slideViewPager);
            mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

            mNextBtn = (Button) findViewById(R.id.nextBtn);
            mBackBtn = (Button) findViewById(R.id.prevBtn);

            sliderAdapter = new SliderAdapter(this);

            mSliderViewPager.setAdapter(sliderAdapter);

            addDotsIndicator(0);

            mSliderViewPager.addOnPageChangeListener(viewListener);

            mNextBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int current = getItem(+1);

                    if (current < mDots.length) {
                        mSliderViewPager.setCurrentItem(current);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        firstTime = false;
                        editor.putBoolean("firstTime", firstTime);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            mBackBtn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    mSliderViewPager.setCurrentItem(mCurrentPage - 1);
                }
            });

        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private int getItem(int i) {
        return mSliderViewPager.getCurrentItem() + 1;
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }
}

