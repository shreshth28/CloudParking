package com.example.shreshth.cloudparking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardingActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;
    private Button mNextBtn;
    private Button mBackBtn;
    private ImageButton play;

    private int mCurrentPage;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);


        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this);
        mNextBtn=(Button)findViewById(R.id.nextBtn);
        mBackBtn=(Button)findViewById(R.id.prevBtn);
        play=(ImageButton)findViewById(R.id.play);
        mBackBtn.setTextColor(R.color.colorPrimaryDark);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(BoardingActivity.this,LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }


    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotsLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage=i;
            if(i==0){
               mNextBtn.setEnabled(true);
               mBackBtn.setEnabled(false);
               mBackBtn.setVisibility(View.INVISIBLE);
               play.setVisibility(View.INVISIBLE);
               mNextBtn.setText("NEXT");
               mNextBtn.setVisibility(View.INVISIBLE);
               mBackBtn.setText("");

            }
            else if(i==mDots.length-1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setText("FINSIH");
                play.setVisibility(View.VISIBLE);
                mBackBtn.setText("BACK");

            }
            else{

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mNextBtn.setVisibility(View.INVISIBLE);
                mBackBtn.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                mNextBtn.setText("NEXT");
                mBackBtn.setText("BACK");

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
