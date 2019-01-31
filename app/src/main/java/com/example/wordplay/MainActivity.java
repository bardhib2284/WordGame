package com.example.wordplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //game properties
    public int difficulty;
    public int scoreForDiff;
    public int timeLeft = 11;
    Timer timer = new Timer();
    ArrayList<String> words = new ArrayList<>();

    //HighScore
    public final String HIGH_SCORE= "HighScore";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public int highScore ;
    public int score=0;

    LinearLayout playGameView;
    LinearLayout gameOverView;
    TextView currentScoreTextView;
    TextView endGameScoreTextView;
    TextView wordTextView;
    TextView endGameTextView;
    TextView playGameTextView;
    TextView timeTextView;
    EditText editTextView;
    String currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scoreForDiff = score;
        sharedPreferences = getSharedPreferences(HIGH_SCORE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        highScore = sharedPreferences.getInt(HIGH_SCORE,0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        PopulateArray();
        GenerateNewWord();
        editTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sizeMatters())
                {
                    if(GuessedRight())
                    {
                        GenerateNewWord();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean sizeMatters()
    {
        if(editTextView.getText().length() != currentWord.length())
        {
            return false;
        }
        return true;
    }

    public boolean GuessedRight()
    {
        if(editTextView.getText().toString().equalsIgnoreCase(currentWord))
        {
            score += currentWord.length() * timeLeft / 1.5;
            scoreForDiff += currentWord.length() * timeLeft / 1.5;
            return true;
        }
        else
        {
            Lose();
        }
        return false;
    }
    public void Lose()
    {
        scoreForDiff = 0;
        difficulty = 0;
        if(highScore < score)
        {
            highScore = score;
            editor.putInt(HIGH_SCORE,highScore);
            editor.commit();
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editTextView.setText("");
        timeLeft = 1111;
        endGameScoreTextView.setText("Score : " + score + "\nHighScore : " + highScore);
        gameOverView.setVisibility(View.VISIBLE);
    }
    public void GenerateNewWord()
    {
        currentWord = getWordBasedOnDiff(difficulty);
        wordTextView.setText(currentWord);
        UpdateScore();
        timeLeft = 11;
        UpdateTime();
    }
    public void Init()
    {
        playGameView = findViewById(R.id.start_game_layout);
        gameOverView = findViewById(R.id.game_over_layout);
        currentScoreTextView = findViewById(R.id.currentScore_textView);
        endGameScoreTextView = findViewById(R.id.score_over_textView);
        endGameTextView = findViewById(R.id.game_over_textView);
        playGameTextView = findViewById(R.id.start_game_textView);
        timeTextView = findViewById(R.id.time_textView);
        wordTextView = findViewById(R.id.word_textView);
        editTextView = findViewById(R.id.word_editText);
        editTextView.requestFocus();
        gameOverView.setVisibility(View.GONE);
        playGameView.setVisibility(View.VISIBLE);
        playGameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = 0;
                playGameView.setVisibility(View.GONE);
                timeLeft = 11;
                highScore = sharedPreferences.getInt(HIGH_SCORE,0);
                Timer();
            }
        });
        endGameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGameView.setVisibility(View.VISIBLE);
                gameOverView.setVisibility(View.GONE);
                score = 0;
                UpdateScore();
            }
        });
    }

    public void UpdateScore()
    {
        currentScoreTextView.setText("Score : " + score);
    }
    public void PopulateArray()
    {
        words.add("Boss");
        words.add("Brryl");
        words.add("Minc");
        words.add("Tahir");
        words.add("Xythi");
        words.add("Majmun");
        words.add("GjaEGjall");
        words.add("LopEGjall");
        words.add("Cigare");
        words.add("Peva");
        words.add("Hupe");
        words.add("SunIBonNiMij");
        words.add("Shpi");
        words.add("MiKulm");
        words.add("NerKulm");
        words.add("Shqipni");
        words.add("Kosove");
    }

    public void Timer()
    {
        if(timer != null)
        {
            timer.cancel();
            timer = null;
            timer = new Timer();
        }
        else
        {
            timer = new Timer();
        }
        timeLeft = 11;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateTime();
                        System.out.println("Score Difficulty : " + scoreForDiff);
                    }
                });
            }
        }, 0, 1000);
    }

    public void UpdateTime()
    {
        timeLeft -=1;
        timeTextView.setText("Time Left " + timeLeft);
        if(timeLeft <= 0)
        {
            Lose();
        }
    }

    public int getDifficulty()
    {
        if(scoreForDiff / 250 >= 1)
        {
            scoreForDiff = 0;
            difficulty++;
        }
        return difficulty;
    }

    public String getWordBasedOnDiff(int diff)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();
        wordTextView.setText("");
        editTextView.setText("");
        if(getDifficulty() == 0)
        {
            r = new Random();
            stringBuilder.append(words.get(r.nextInt(words.size())));
        }
        else if(getDifficulty() == 1)
        {
            currentWord = words.get(r.nextInt(words.size()));
            stringBuilder.append(currentWord);
            stringBuilder.append(currentWord);
        }
        else if(getDifficulty() == 2)
        {
            stringBuilder.append(words.get(r.nextInt(words.size())));
            stringBuilder.append(words.get(r.nextInt(words.size())));
        }
        else if(getDifficulty() == 3)
        {
            String word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));

        }
        else if(getDifficulty() > 3)
        {
            String word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),1)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),4)));
        }
        else if(getDifficulty() == 4)
        {
            String word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),1)));
        }
        else if(getDifficulty() == 5)
        {
            String word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
        }
        else if(getDifficulty() >= 6)
        {
            stringBuilder.setLength(23);
            String word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),2)));
            word = words.get(r.nextInt(words.size()));
            stringBuilder.append(word.substring(0,Math.min(word.length(),3)));

        }
        return stringBuilder.toString();
    }
}
