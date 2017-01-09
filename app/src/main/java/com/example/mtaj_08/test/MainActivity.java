package com.example.mtaj_08.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Button btnstart;

    Button btnred,btnblue,btnyellow,btngreen;

    RelativeLayout bgViewGroup;

    private static final int DELAY = 100;

    Interpolator interpolator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAnimation();

        btnstart=(Button)findViewById(R.id.button);

        btnyellow=(Button)findViewById(R.id.btnyellow);
        btngreen=(Button)findViewById(R.id.btngreen);
        btnred=(Button)findViewById(R.id.btnred);
        btnblue=(Button)findViewById(R.id.btnblue);

        bgViewGroup=(RelativeLayout)findViewById(R.id.activity_main);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,AnotherActivity.class);
                ActivityOptions transitionActivity =
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(i, transitionActivity.toBundle());

            }
        });

        btngreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealGreen();

            }
        });

        btnblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealBlue();

            }
        });

       btnyellow.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {

               if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   if (v.getId() == R.id.btnyellow) {
                       revealYellow(event.getRawX(), event.getRawY());
                   }
               }

               return false;
           }
       });

        btnred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealRed();
            }
        });


    }

    public void setupAnimation()
    {

        /*Slide slidedown=new Slide();
        slidedown.setDuration(1000);
        slidedown.setSlideEdge(Gravity.START);
        getWindow().setEnterTransition(slidedown);*/

        Transition slide=TransitionInflater.from(MainActivity.this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }


    private void revealYellow(float x, float y) {
        animateRevealColorFromCoordinates(bgViewGroup, R.color.sample_yellow, (int) x, (int) y);

    }

    private void revealGreen() {
        animateRevealColor(bgViewGroup, R.color.sample_green);

    }

    private void revealBlue() {
        animateButtonsOut();
        Animator anim = animateRevealColorFromCoordinates(bgViewGroup, R.color.sample_blue, bgViewGroup.getWidth() / 2, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animateButtonsIn();
            }
        });

    }

    private void revealRed() {
        final ViewGroup.LayoutParams originalParams = btnred.getLayoutParams();
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealColor(bgViewGroup, R.color.sample_red);
                btnred.setLayoutParams(originalParams);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(bgViewGroup, transition);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        btnred.setLayoutParams(layoutParams);
    }

    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color));
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }

    private void animateRevealColor(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy);
    }


    private void animateButtonsIn() {
        for (int i = 0; i < bgViewGroup.getChildCount(); i++) {
            View child = bgViewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(100 + i * DELAY)
                    .setInterpolator(interpolator)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }
    }

    private void animateButtonsOut() {
        for (int i = 0; i < bgViewGroup.getChildCount(); i++) {
            View child = bgViewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(i)
                    .setInterpolator(interpolator)
                    .alpha(0)
                    .scaleX(0f)
                    .scaleY(0f);
        }
    }

}
