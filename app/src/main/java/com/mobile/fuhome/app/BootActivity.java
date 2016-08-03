package com.mobile.fuhome.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.mobile.fuhome.app.application.BaseActivity;
import com.mobile.fuhome.app.utils.SharedPreferenceUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BootActivity extends BaseActivity implements Animation.AnimationListener{

    private static final boolean AUTO_JUMP = true;
    private boolean isLogined = false;

    private static final int AUTO_JUMP_DELAY_MILLIS = 1000;

    private static final int UI_ANIMATION_DELAY = 3000;
    private static final int FINISH_ACTIVITY = 0;
    private TextView mContentView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FINISH_ACTIVITY){
                if (isLogined){
                    jumpToActivity(SblistActivity.class);
                }else {
                    jumpToActivity(LoginActivity.class);
                }

                BootActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_boot);
        isLogined = SharedPreferenceUtils.getBoolean(this,"isLogined",false);
        mContentView = (TextView) findViewById(R.id.fullscreen_content);
        setAlphaAnimation(mContentView);

    }

    private void setAlphaAnimation(View view){
        //创建一个AnimationSet对象，参数为Boolean型，

        //true表示使用Animation的interpolator，false则是使用自己的

        AnimationSet animationSet = new AnimationSet(true);

        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

        //设置动画执行的时间

        alphaAnimation.setDuration(UI_ANIMATION_DELAY);

        //将alphaAnimation对象添加到AnimationSet当中

        animationSet.addAnimation(alphaAnimation);

        //使用ImageView的startAnimation方法执行动画

        view.startAnimation(animationSet);

        alphaAnimation.setAnimationListener(this);

    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (AUTO_JUMP){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(FINISH_ACTIVITY);
                }
            };
            timer.schedule(task,AUTO_JUMP_DELAY_MILLIS);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
