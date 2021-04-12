package com.example.inbetween;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;
import java.util.Timer;

public class InBetween extends AppCompatActivity {
    Button btnHigher,
            btnLower,
            btnShuffle,
            btnBet,
            btnFold;
    TextView txtProgress,
            txtCardOne,
            txtCardTwo,
            txtCardThree,
            txtMoney;
    ImageView cardThree;
    SeekBar seekBar;

    int cardOne,
            cardTwo,
            crntMoney;
    boolean higher = false,
            lower = false;
    String text;
    String[] cardValues = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    Timer timer = new Timer();
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_between);
        btnHigher = findViewById(R.id.btn_higher);
        btnLower = findViewById(R.id.btn_lower);
        btnShuffle = findViewById(R.id.btn_shuffle);
        btnBet = findViewById(R.id.btn_bet);
        btnFold = findViewById(R.id.btn_fold);
        txtCardOne = findViewById(R.id.txt_card1);
        txtCardTwo = findViewById(R.id.txt_card2);
        txtCardThree = findViewById(R.id.txt_card3);
        txtMoney = findViewById(R.id.txt_money);
        cardThree = findViewById(R.id.img_card3);
        seekBar = findViewById(R.id.betting);
        txtProgress = findViewById(R.id.txt_betMoney);

        reset();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress *= 10;
                txtProgress.setText("$ " + (String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = txtMoney.getText().toString().substring(2);
                crntMoney = Integer.parseInt(text);
                if (crntMoney == 0){
                    btnBet.setEnabled(false);
                    btnShuffle.setEnabled(false);
                }
                else {
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardThree, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardThree, "scaleX", 0f, 1f);
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(txtCardThree, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cardThree.setImageResource(R.drawable.img_card_front);
                            oa2.start();
                            int card3 = rand.nextInt(13);

                            if((cardOne == cardTwo && (card3 > cardOne && higher || card3 < cardOne && lower)) ||
                                    (cardOne != cardTwo && ((cardOne < card3 && card3 < cardTwo) || (cardOne > card3 && card3 > cardTwo)))) {
                                txtMoney.setText("$ " + (crntMoney + 50));
                            } else {
                                txtMoney.setText("$ " + (crntMoney - 50));
                            }
                            cardThree.setImageResource(R.drawable.img_card_front);
                            txtCardThree.setText(cardValues[card3]);
                            reset();
                        }
                    });
                    oa1.start();
                    oa3.start();
                }
            }
        });
    }

    public void btnShuffle(View view) throws InterruptedException {
        shuffle();
    }

    public void fold(View view) {
        reset();
    }

    public void higher(View view) {
        if(lower) {
            lower = false;
        }
        btnLower.setBackground(getDrawable(R.drawable.btn_disabled));
        btnHigher.setBackground(getDrawable(R.drawable.btn_lower));
        higher = true;
    }

    public void lower(View view) {
        if(higher) {
            higher = false;
        }
        btnHigher.setBackground(getDrawable(R.drawable.btn_disabled));
        btnLower.setBackground(getDrawable(R.drawable.btn_lower));
        lower = true;
    }

    public void setHighLowVisibility(int visibility) {
        btnHigher.setVisibility(visibility);
        btnLower.setVisibility(visibility);
    }

    public void setBetFoldEnabled(boolean status) {
        btnBet.setEnabled(status);
        btnFold.setEnabled(status);

        if(status) {
            btnBet.setBackground(getDrawable(R.drawable.btn_bet));
            btnFold.setBackground(getDrawable(R.drawable.btn_bet));
        } else {
            btnBet.setBackground(getDrawable(R.drawable.btn_disabled));
            btnFold.setBackground(getDrawable(R.drawable.btn_disabled));
        }
    }

    public void reset() {
        setHighLowVisibility(View.GONE);
        setBetFoldEnabled(false);
        higher = false;
        lower = false;
        btnHigher.setBackground(getDrawable(R.drawable.btn_lower));
        btnLower.setBackground(getDrawable(R.drawable.btn_lower));
        btnShuffle.setEnabled(true);
        btnShuffle.setBackground(getDrawable(R.drawable.btn_shuffle));
    }

    public void shuffle() {
        btnShuffle.setEnabled(false);
        btnShuffle.setBackground(getDrawable(R.drawable.btn_disabled));
        cardThree.setImageResource(R.drawable.img_card_back);
        txtCardThree.setText("");
        new CountDownTimer(1000, 50) {
            int i = 0;

            public void onTick(long millisUntilFinished) {
                txtCardOne.setText(cardValues[i]);
                txtCardTwo.setText(cardValues[i++]);
                if(i == 12) i = 0;
            }

            public void onFinish() {
                cardOne = rand.nextInt(13);
                cardTwo = rand.nextInt(13);
                while((cardOne - cardTwo) == 1 || (cardTwo - cardOne) == 1) {
                    cardOne = rand.nextInt(13);
                    cardTwo = rand.nextInt(13);
                }
                txtCardOne.setText(cardValues[cardOne]);
                txtCardTwo.setText(cardValues[cardTwo]);

                if(cardOne == cardTwo) setHighLowVisibility(View.VISIBLE);
                setBetFoldEnabled(true);
            }
        }.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}