package com.example.zct;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Button tapToSpeak;
    String db_word, emoji, word, country;
    TextView wordImageTextView;
    TextView translateWord;
    TextView flagTextView;
    String l;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    Context context = this;
    Locale selectedLocale;
    String[] femaleAssets = {"🧕","👮‍♀️","👷‍♀️","🕵️‍♀️","👩‍⚕️","👩‍🌾","👩‍🍳","👩‍🎓","👩‍🏫","👩‍💻","👩‍💼","👩‍🎨","👩‍🚒","👩‍✈️","👩‍🚀","👩‍⚖️","🧙‍♀️","🧛‍♀️"},
            maleAssets = {"👷‍♂️","👨‍🌾","👨‍🌾","👨‍🍳","👨‍🏫","👨‍💻","👨‍🔧","👨‍🚒","👨‍🚀","👨‍🎨"};
    TextView personTextView, statusTextView;
    Helper helper = new Helper();
    private TextToSpeech repeatTTS;


    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            int random = ThreadLocalRandom.current().nextInt(1, (int) (dataSnapshot.getChildrenCount()-1));
            Log.d("number", String.valueOf(random));
            db_word = dataSnapshot.child(String.valueOf(random)).getValue().toString();
            word = db_word.substring(2);
            emoji = db_word.substring(0,2);
            wordImageTextView.setText(emoji);
            Translator translator = new Translator();
            word = translator.translate(word, country);
            translateWord.setText(word);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("tag","connection failed");
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        myRef.addValueEventListener(listener);
        tapToSpeak = findViewById(R.id.tap_speak);
        tapToSpeak.setOnClickListener(recordVoice);
        myRef.addValueEventListener(listener);
        wordImageTextView = findViewById(R.id.emoji_word);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        flagTextView = findViewById(R.id.flag_play);
        country = getIntent().getStringExtra("COUNTRY");
        selectedLocale = stringToLocale(country);
        flagTextView.setText(localeToEmoji(selectedLocale));
        translateWord = findViewById(R.id.translate_word);
        checkPermission();
        flagTextView.setOnClickListener(sayWord);
        repeatTTS = new TextToSpeech(this, this);
        personTextView = findViewById(R.id.person_emoji);
        personTextView.setText(getEmoji());
        statusTextView = findViewById(R.id.status_text);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String getEmoji(){
        String personEmoji;
        int male = new Random().nextInt(1);
        if (male == 1) {
            personEmoji = femaleAssets[new Random().nextInt(femaleAssets.length)];
        } else {
            personEmoji = maleAssets[new Random().nextInt(maleAssets.length)];
        }
        return personEmoji;
    }
    View.OnClickListener sayWord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repeatTTS.setLanguage(stringToLocale(helper.iso639Handler(country)));
            if (country.equals("cn")){
                repeatTTS.setLanguage(Locale.CHINA);
            } else if (country.equals("kr")) {
                repeatTTS.setLanguage(Locale.KOREA);
            }
            repeatTTS.speak(word,
                    TextToSpeech.QUEUE_FLUSH, null, "idk");
        }
    };

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    View.OnClickListener recordVoice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            speechRecognizer.startListening(speechRecognizerIntent);
            tapToSpeak.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F23E22")));
        }
    };

    public String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    public Locale stringToLocale(String s) {
        StringTokenizer tempStringTokenizer = new StringTokenizer(s,",");
        if(tempStringTokenizer.hasMoreTokens())
            l = tempStringTokenizer.nextElement().toString();
        return new Locale(l,l.toLowerCase());
    }

    private void checkPermission(){
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package: " + getPackageName()));
            Log.d("error", "cant load speech");
            startActivity(intent);
        } else {
            initSpeechRecognizer();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package: " + getPackageName()));
            Log.d("error", "cant load speech");
            startActivity(intent);
        }
    }

    public void initSpeechRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLocale);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null){
                   String spokenWord = matches.get(0);
                   AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
                   AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
                   fadeIn.setDuration(2000);
                   fadeIn.setFillAfter(true);
                   fadeOut.setDuration(2000);
                   fadeOut.setFillAfter(true);
                   fadeOut.setStartOffset(4200+fadeIn.getStartOffset());

                    statusTextView.setVisibility(View.VISIBLE);
                    if (spokenWord.equals(word.toLowerCase())) {
                       statusTextView.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                       statusTextView.setText("Correct");
                       statusTextView.startAnimation(fadeIn); ;
                        myRef.addValueEventListener(listener);

                   } else {
                       statusTextView.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                       statusTextView.startAnimation(fadeIn); ;
                    }
                    statusTextView.startAnimation(fadeOut);
                }
                speechRecognizer.stopListening();
                tapToSpeak.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2243F6")));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    @Override
    public void onInit(int status) {

    }
}
