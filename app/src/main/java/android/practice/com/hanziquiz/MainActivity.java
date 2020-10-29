package android.practice.com.hanziquiz;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* This app creates a fill in the blank quiz.
 * The file data.txt holds the content used on the quiz.
 * There is one phrase per line and each phrase comprises one question of the quiz.
 *
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    InputStream file;
    BufferedReader bufferedReader;
    List<String> phrases = new ArrayList<>();
    String quizQuestion;
    String quizQuestionWithUnderscore;
    String choices;
    char correctAnswer;
    TextView textViewQuizQuestion, textViewAnswerMessage;
    Button buttonQuizChoice0, buttonQuizChoice1, buttonQuizChoice2, buttonQuizChoice3, buttonQuizChoice4;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuizQuestion = findViewById(R.id.textViewQuizQuestion);
        textViewAnswerMessage = findViewById(R.id.textViewAnswerMessage);
        buttonQuizChoice0 = findViewById(R.id.buttonAnswer0);
        buttonQuizChoice0.setOnClickListener(this);
        buttonQuizChoice1 = findViewById(R.id.buttonAnswer1);
        buttonQuizChoice1.setOnClickListener(this);
        buttonQuizChoice2 = findViewById(R.id.buttonAnswer2);
        buttonQuizChoice2.setOnClickListener(this);
        buttonQuizChoice3 = findViewById(R.id.buttonAnswer3);
        buttonQuizChoice3.setOnClickListener(this);
        buttonQuizChoice4 = findViewById(R.id.buttonAnswer4);
        buttonQuizChoice4.setOnClickListener(this);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);

        loadData();

        setQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            file.close();
            bufferedReader.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    boolean isValidChar(char c){
        return  (c >= '\u4E00' && c <= '\u9FA5');
    }

    // Loads the phrases to be used on the quiz from a txt file into a variable called phrases
    void loadData(){
        String line;
        try{
            file = getAssets().open("data.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(file));
            while ((line = bufferedReader.readLine()) != null){
                if (line.length() > 1)
                    phrases.add(line);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    String getRandomQuizQuestion(){

        Random randomNumber = new Random();
        int randomLineNumber = randomNumber.nextInt(phrases.size());
        return phrases.get(randomLineNumber);
    }

    int getRandomCharPosition(){
        int randomCharPosition;
        char randomChar;
        Random randomNumber = new Random();

        do{
            randomCharPosition = randomNumber.nextInt(quizQuestion.length());
            randomChar = quizQuestion.charAt(randomCharPosition);
        }while (!isValidChar(randomChar));
        return randomCharPosition;
    }

    // gets a random chinese character. Avoiding invalid characters like numbers and punctuation marks
    char getRandomCharacter(){
        int randomLine, randomPosition;
        Random randomNumber = new Random();

        randomLine = randomNumber.nextInt(phrases.size());

        char validRandomChar;
        do{
            randomPosition = randomNumber.nextInt(phrases.get(randomLine).length());
            validRandomChar = phrases.get(randomLine).charAt(randomPosition);
        }while (!isValidChar(validRandomChar));

        return validRandomChar;
    }

    String getFiveChoices(){
        String fiveChoices;
        fiveChoices = Character.toString(correctAnswer);
        char tempChar;

        for (int x = 0; x < 4; x++){
            tempChar = getRandomCharacter();
            while (fiveChoices.contains(Character.toString(tempChar)))
                tempChar = getRandomCharacter();
            fiveChoices += tempChar;
        }
        return fiveChoices;
    }

    void setQuestion(){

        quizQuestion = getRandomQuizQuestion();

        int validCharPosition = getRandomCharPosition();
        correctAnswer = quizQuestion.charAt(validCharPosition);

        quizQuestionWithUnderscore = quizQuestion.substring(0,validCharPosition)+"___"+ quizQuestion.substring(validCharPosition+1);

        choices = getFiveChoices();
        List<Character> shuffledChoices = new ArrayList<>();
        for (char c : choices.toCharArray()){
            shuffledChoices.add(c);
        }
        Collections.shuffle(shuffledChoices);

        textViewQuizQuestion.setText(quizQuestionWithUnderscore);
        buttonQuizChoice0.setText(Character.toString(shuffledChoices.get(0)));
        buttonQuizChoice1.setText(Character.toString(shuffledChoices.get(1)));
        buttonQuizChoice2.setText(Character.toString(shuffledChoices.get(2)));
        buttonQuizChoice3.setText(Character.toString(shuffledChoices.get(3)));
        buttonQuizChoice4.setText(Character.toString(shuffledChoices.get(4)));
        buttonNext.setEnabled(false);
        buttonQuizChoice0.setEnabled(true);
        buttonQuizChoice1.setEnabled(true);
        buttonQuizChoice2.setEnabled(true);
        buttonQuizChoice3.setEnabled(true);
        buttonQuizChoice4.setEnabled(true);
        textViewAnswerMessage.setText("");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttonAnswer0:
                if (buttonQuizChoice0.getText().equals(Character.toString(correctAnswer))){
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.correctMessageGreen));
                    textViewAnswerMessage.setText(R.string.correct);
                    buttonNext.setEnabled(true);
                    buttonQuizChoice1.setEnabled(false);
                    buttonQuizChoice2.setEnabled(false);
                    buttonQuizChoice3.setEnabled(false);
                    buttonQuizChoice4.setEnabled(false);
                }
                else{
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.wrongMessageRed));
                    textViewAnswerMessage.setText(R.string.wrong0);
                    buttonQuizChoice0.setEnabled(false);
                }
                break;
            case R.id.buttonAnswer1:
                if (buttonQuizChoice1.getText().equals(Character.toString(correctAnswer))){
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.correctMessageGreen));
                    textViewAnswerMessage.setText(R.string.correct);
                    buttonNext.setEnabled(true);
                    buttonQuizChoice0.setEnabled(false);
                    buttonQuizChoice2.setEnabled(false);
                    buttonQuizChoice3.setEnabled(false);
                    buttonQuizChoice4.setEnabled(false);
                }
                else{
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.wrongMessageRed));
                    textViewAnswerMessage.setText(R.string.wrong1);
                    buttonQuizChoice1.setEnabled(false);
                }

                break;
            case R.id.buttonAnswer2:
                if (buttonQuizChoice2.getText().equals(Character.toString(correctAnswer))){
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.correctMessageGreen));
                    textViewAnswerMessage.setText(R.string.correct);
                    buttonNext.setEnabled(true);
                    buttonQuizChoice0.setEnabled(false);
                    buttonQuizChoice1.setEnabled(false);
                    buttonQuizChoice3.setEnabled(false);
                    buttonQuizChoice4.setEnabled(false);
                }
                else{
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.wrongMessageRed));
                    textViewAnswerMessage.setText(R.string.wrong2);
                    buttonQuizChoice2.setEnabled(false);
                }
                break;
            case R.id.buttonAnswer3:
                if (buttonQuizChoice3.getText().equals(Character.toString(correctAnswer))){
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.correctMessageGreen));
                    textViewAnswerMessage.setText(R.string.correct);
                    buttonNext.setEnabled(true);
                    buttonQuizChoice0.setEnabled(false);
                    buttonQuizChoice1.setEnabled(false);
                    buttonQuizChoice2.setEnabled(false);
                    buttonQuizChoice4.setEnabled(false);
                }
                else{
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.wrongMessageRed));
                    textViewAnswerMessage.setText(R.string.wrong3);
                    buttonQuizChoice3.setEnabled(false);
                }

                break;
            case R.id.buttonAnswer4:
                if (buttonQuizChoice4.getText().equals(Character.toString(correctAnswer))){
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.correctMessageGreen));
                    textViewAnswerMessage.setText(R.string.correct);
                    buttonNext.setEnabled(true);
                    buttonQuizChoice0.setEnabled(false);
                    buttonQuizChoice1.setEnabled(false);
                    buttonQuizChoice2.setEnabled(false);
                    buttonQuizChoice3.setEnabled(false);
                }
                else{
                    textViewAnswerMessage.setTextColor(ContextCompat.getColor(this, R.color.wrongMessageRed));
                    textViewAnswerMessage.setText(R.string.wrong4);
                    buttonQuizChoice4.setEnabled(false);
                }
                break;
            case R.id.buttonNext:
                setQuestion();
                break;
        }
    }
}
