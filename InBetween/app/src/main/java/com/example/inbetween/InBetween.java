package com.example.inbetween;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Random;

public class InBetween extends AppCompatActivity {
    ImageButton btnMute;
    Button btnHigher,
            btnLower,
            btnShuffle,
            btnBet,
            btnFold;
    TextView txtBetMoney,
            txtCardOne,
            txtCardTwo,
            txtCardThree,
            txtMoney;
    ImageView imgCard3;
    SeekBar seekBar;

    int cardOne,
            cardTwo,
            cardThree,
            crntMoney;
    boolean higher = false,
            lower = false,
            mute = false;
    String text;
    String[] cardValues = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    Random rand = new Random();
    MediaPlayer ring;

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
        imgCard3 = findViewById(R.id.img_card3);
        seekBar = findViewById(R.id.betting);
        txtBetMoney = findViewById(R.id.txt_betMoney);
        btnMute = findViewById(R.id.btn_mute);

        text = txtMoney.getText().toString().substring(2);
        crntMoney = Integer.parseInt(text);

        ring= MediaPlayer.create(InBetween.this,R.raw.bg_music);
        ring.start();
        ring.isLooping();
        reset();
        setBetFoldEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress *= 10;
                txtBetMoney.setText("$ " + (progress));
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
                final int betMoney = getInt(txtBetMoney);
                crntMoney = getInt(txtMoney);

                if (betMoney > crntMoney) {
                    Toast.makeText(getApplicationContext(),"Insufficient Money",Toast.LENGTH_LONG).show();
                } else if(betMoney == 0) {
                    Toast.makeText(getApplicationContext(),"Assign Bet Amount",Toast.LENGTH_LONG).show();
                } else if(cardOne == cardTwo && !higher && !lower) {
                    Toast.makeText(getApplicationContext(),"Select Higher or Lower",Toast.LENGTH_LONG).show();
                } else {
                    reset();
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgCard3, "scaleX", 1f, 0f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgCard3, "scaleX", 0f, 1f);
                    final ObjectAnimator oa3 = ObjectAnimator.ofFloat(txtCardThree, "scaleX", 0f, 1f);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa3.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            imgCard3.setImageResource(R.drawable.img_card_front);
                            oa2.start();
                            crntMoney = getInt(txtMoney);

                            if ((cardOne == cardTwo && (cardThree > cardOne && higher || cardThree < cardOne && lower)) ||
                                    (cardOne != cardTwo && ((cardOne < cardThree && cardThree < cardTwo) || (cardOne > cardThree && cardThree > cardTwo)))) {
                                txtMoney.setText("$ " + (crntMoney + betMoney));
                            } else {
                                txtMoney.setText("$ " + (crntMoney - betMoney));
                            }
                            Log.e("MSG:                   ", "Lower: " + String.valueOf(lower));
                            Log.e("MSG:                   ", "Higher: " + String.valueOf(higher));

                            imgCard3.setImageResource(R.drawable.img_card_front);
                            txtCardThree.setText(cardValues[cardThree]);
                            crntMoney = getInt(txtMoney);

                            if (crntMoney == 0){
                                btnShuffle.setEnabled(false);
                                btnShuffle.setBackground(getDrawable(R.drawable.btn_disabled));
                                seekBar.setProgress(0);
                            }
                        }
                    });
                    oa1.start();
                    oa3.start();
                    oa3.setDuration(500);
                }
            }
        });
    }

    public void mute(View view){
        if(mute){
            btnMute.setImageResource(R.drawable.ic_volume_on);
            ring.start();
            ring.isLooping();
            mute = false;
        }
        else{
            btnMute.setImageResource(R.drawable.ic_volume_off);
            ring.pause();
            mute = true;
        }
    }

    public void shuffle(View view) {
        cardShuffle();
    }

    public void fold(View view) {
        txtCardOne.setText(R.string.card_default);
        txtCardTwo.setText(R.string.card_default);
        reset();
    }

    public void higher(View view) {
        if(lower) {
            lower = false;
        }
        btnLower.setBackground(getDrawable(R.drawable.btn_disabled));
        btnHigher.setBackground(getDrawable(R.drawable.btn_bet));
        btnFold.setBackground(getDrawable(R.drawable.btn_disabled));
        btnFold.setEnabled(false);
        higher = true;
    }

    public void lower(View view) {
        if(higher) {
            higher = false;
        }
        btnHigher.setBackground(getDrawable(R.drawable.btn_disabled));
        btnLower.setBackground(getDrawable(R.drawable.btn_bet));
        btnFold.setBackground(getDrawable(R.drawable.btn_disabled));
        btnFold.setEnabled(false);
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
            btnFold.setBackground(getDrawable(R.drawable.btn_fold));
        } else {
            btnBet.setBackground(getDrawable(R.drawable.btn_disabled));
            btnFold.setBackground(getDrawable(R.drawable.btn_disabled));
        }
    }

    public void reset() {
        seekBar.setProgress(0);
        seekBar.setEnabled(false);
        btnShuffle.setEnabled(true);
        btnShuffle.setBackground(getDrawable(R.drawable.btn_shuffle));
        setBetFoldEnabled(false);
        setHighLowVisibility(View.GONE);
    }

    public void cardShuffle() {
        btnShuffle.setEnabled(false);
        seekBar.setEnabled(true);
        btnShuffle.setBackground(getDrawable(R.drawable.btn_disabled));
        imgCard3.setImageResource(R.drawable.img_card_back);
        txtCardThree.setText("");
        higher = false;
        lower = false;
        btnHigher.setBackground(getDrawable(R.drawable.btn_lower));
        btnLower.setBackground(getDrawable(R.drawable.btn_lower));
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
                cardThree = rand.nextInt(13);
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

    public int getInt(TextView txt) {
        text = txt.getText().toString().substring(2);
        return Integer.parseInt(text);
    }

    @Override
    public void onPause() {
        super.onPause();
        ring.pause();
    }
    @Override
    public void onStart() {
        super.onStart();
        ring.start();
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