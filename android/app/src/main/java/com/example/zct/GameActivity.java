package com.example.zct;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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


    //Listener for firebase database
    ValueEventListener firebaseListener = new ValueEventListener() {
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
        checkPermission();
        setContentView(R.layout.activity_game);
        myRef.addListenerForSingleValueEvent(firebaseListener);
        tapToSpeak = findViewById(R.id.tap_speak);
        tapToSpeak.setOnClickListener(recordVoice);
        myRef.addListenerForSingleValueEvent(firebaseListener);
        wordImageTextView = findViewById(R.id.emoji_word);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        flagTextView = findViewById(R.id.flag_play);
        country = getIntent().getStringExtra("COUNTRY");
        selectedLocale = stringToLocale(country);
        flagTextView.setText(localeToEmoji(selectedLocale));
        translateWord = findViewById(R.id.translate_word);
        flagTextView.setOnClickListener(sayWord);
        repeatTTS = new TextToSpeech(this, this);
        personTextView = findViewById(R.id.person_emoji);
        personTextView.setText(getEmoji());
        statusTextView = findViewById(R.id.status_text);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //Get random emoji for person picture (left side screen)
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
    //Play word speech
    View.OnClickListener sayWord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        repeatTTS.setLanguage(stringToLocale(helper.iso639Handler(country)));
            Log.d("selected locale", stringToLocale(helper.iso639Handler(country)).toString());
            if (country.equals("cn")){
                repeatTTS.setLanguage(Locale.CHINESE);
            } else if (country.equals("kr")) {
                repeatTTS.setLanguage(Locale.KOREA);
            } else if (country.equals("se")) {
                repeatTTS.setLanguage(new Locale("sv","SE"));}
            Log.d("Language", repeatTTS.getLanguage().toString());
            repeatTTS.speak(word,
                    TextToSpeech.QUEUE_FLUSH, null, "idk");
        }
    };
    //Back button
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    //Active microphone for speech recognition
    View.OnClickListener recordVoice = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try{
            speechRecognizer.startListening(speechRecognizerIntent);
            tapToSpeak.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F23E22")));}
            catch (Exception ex){
                System.out.println(ex);
            }
        }
    };
    //Get nation flag from locale
    public String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
    //Get locale from string shortcut nation
    public Locale stringToLocale(String s) {
        StringTokenizer tempStringTokenizer = new StringTokenizer(s,",");
        if(tempStringTokenizer.hasMoreTokens())
            l = tempStringTokenizer.nextElement().toString();
        return new Locale(l.toLowerCase(),l);
    }

    private void checkPermission(){
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(GameActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},1 );
        } else {
            initSpeechRecognizer();
        }
        //Internet permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GameActivity.this, new String[]{Manifest.permission.INTERNET},2 );
        }
    }
    public void initSpeechRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, helper.iso639Handler(country));
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
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
                AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
                fadeIn.setDuration(2000);
                fadeIn.setFillAfter(true);
                fadeOut.setDuration(2000);
                fadeOut.setFillAfter(true);
                fadeOut.setStartOffset(4200+fadeIn.getStartOffset());
                if (matches != null){
                    Log.d("u said", matches.get(0));
                   String spokenWord = matches.get(0);

                    statusTextView.setVisibility(View.VISIBLE);
                    //If recognized word match given word
                    if (spokenWord.equals(word.toLowerCase())) {
                       statusTextView.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                       statusTextView.setText("Correct 👩");
                       statusTextView.startAnimation(fadeIn);
                        myRef.addListenerForSingleValueEvent(firebaseListener);
                    //If doesnt recognize
                   } else {
                       statusTextView.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        statusTextView.setText("Incorrect \uD83D\uDE25 (Try again)");
                        statusTextView.startAnimation(fadeIn);
                    }
                    statusTextView.startAnimation(fadeOut);
                } else {
                    statusTextView.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    statusTextView.setText("Try again!");
                    statusTextView.startAnimation(fadeIn);
                }
                statusTextView.startAnimation(fadeOut);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    initSpeechRecognizer();
                } else {
                    //If user presses deny
                    Toast.makeText(GameActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                } else {
                    //If user presses deny
                    Toast.makeText(GameActivity.this, "Internet permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
