package com.example.inbetween;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    TextView txtCardOne,
            txtCardTwo,
            txtCardThree,
            txtMoney;
    ImageView cardThree;
    int cardOne,
            cardTwo;
    boolean higher = false,
            lower = false;
    String[] cardValues = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    Timer timer = new Timer();
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_between);
        btnHigher = (Button) findViewById(R.id.btn_higher);
        btnLower = (Button) findViewById(R.id.btn_lower);
        btnShuffle = (Button) findViewById(R.id.btn_shuffle);
        btnBet = (Button) findViewById(R.id.btn_bet);
        btnFold = (Button) findViewById(R.id.btn_fold);
        txtCardOne = (TextView) findViewById(R.id.txt_card1);
        txtCardTwo = (TextView) findViewById(R.id.txt_card2);
        txtCardThree = (TextView) findViewById(R.id.txt_card3);
        txtMoney = (TextView) findViewById(R.id.txt_money);
        cardThree = (ImageView) findViewById(R.id.img_card3);

        reset();
    }

    public void btnShuffle(View view) throws InterruptedException {
        shuffle();
    }

    public void btnBet(View view) {
        check();
    }

    public void btnFold(View view) {
        reset();
    }

    public void btnHigher(View view) {
        if(lower) {
            lower = false;
        }
        btnLower.setBackground(getDrawable(R.drawable.btn_disabled));
        btnHigher.setBackground(getDrawable(R.drawable.btn_lower));
        higher = true;
    }

    public void btnLower(View view) {
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

    public void check() {
        int crntMoney;
        int card3 = rand.nextInt(13);

        String text = txtMoney.getText().toString().substring(2);
        crntMoney = new Integer(text);

        if((cardOne == cardTwo && (card3 > cardOne && higher || card3 < cardOne && lower)) ||
                (cardOne != cardTwo && ((cardOne < card3 && card3 < cardTwo) || (cardOne > card3 && card3 > cardTwo)))) {
            txtMoney.setText("$ " + String.valueOf(crntMoney+50));
        } else {
            txtMoney.setText("$ " + String.valueOf(crntMoney-50));
        }

        cardThree.setImageResource(R.drawable.img_card_front);
        txtCardThree.setText(cardValues[card3]);
        reset();
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