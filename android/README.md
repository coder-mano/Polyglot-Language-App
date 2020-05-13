
[![Screenshots](https://github.com/Coder-mano/Polyglot-Pronunciation-Training/blob/master/images/android.png)]()

# About

prototype Android implementation of the **Translation Game** containing Speech recognition, Firebase Realtime Database, and IBM Watson Language Translation example build in Java.

**Supported languages:** 🇪🇸 Spanish, 🇫🇷 French, 🇧🇪 German, 🇮🇹 Italian, 🇸🇪 Swedish, 🇰🇷 Korean, 🇷🇺 Russian, 🇹🇷 Turkish, 🇭🇺 Hungarian, 🇵🇱 Polish, 🇬🇷 Greek, 🇩🇰 Danish, 🇳🇱 Dutch, 🇮🇳 Hindi, 🇯🇵 Japanese, 🇧🇷 Portuguese, 🇻🇳 Vietnamese, 🇸🇰 Slovak, 🇮🇪 Irish, 🇮🇱 Hebrew, 🇨🇿 Czech, 🇷🇴 Romanian, 🇮🇩 Indonesian


## Implementation overview

The final implementation is divided into four main parts:

* **Random word generation -** fetches the word for translation from the database based on the maximum size.

* **Word translation -** translates the generated word through IBM Watson to the selected language

* **Text to speech -** Text to speech (TTS) is a native function by android which can transform the input text into playback. This function should support all languages which are included in class Locale.

* **Speech recognition -** Speech recognition is a native function provided by Android. As in the previous case, it should support all languages from the Local class. The output of this function is an ordered list of words in which every word has a confidence score.

### Random word generation 
```java
DatabaseReference ref = database.getReference(); 
ValueEventListener firebaseListener = new ValueEventListener() { 
    @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //check the size of db Integer 
        size = Integer.valueOf(dataSnapshot.child("db_size").getValue().toString()); //Generate random word from DB int random = ThreadLocalRandom.current().nextInt(1, size); 
        db_word = dataSnapshot.child(String.valueOf(random)).getValue().toString();
    }
}
``` 
### Word translation
```java
public String translate(String word, String targetLanguage){ 
    targetLanguage = helper.iso639Handler(targetLanguage); 
    //setup translator settings (options) 
    TranslateOptions options = new TranslateOptions.Builder().addText(word).modelId("en-"+targetLanguage).build(); 
    TranslationResult result = languageTranslator.translate(options).execute().getResult(); 
    //List of words from Wattson response 
    List<Translation> translations = result.getTranslations();
    //return best translation 
    return translations.get(0).getTranslation();
} 
Translator translator = new Translator(); 
word = translator.translate(word, country);
``` 

### Text to speech
```java
//Text to english speech - word Hello
String word = "Hello";
TextToSpeech repeatTTS = new TextToSpeech(context, listener);
repeatTTS.setLanguage(Locale locale = Locale.ENGLISH);
repeatTTS.speak(word,TextToSpeech.QUEUE_FLUSH, null, "identifier"));
``` 

### Speech recognition
```java
//This code fragment initialize speech recognizer
SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//Set language which recognizer should recognize
speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
//Set listener for speach recognizer. In this case we show just succesfull case. Whole functiun have lot of states (onError, onEndOfSpeech etc.)
speechRecognizer.setRecognitionListener(new RecognitionListener(){
    @Override
    public void onResults(Bundle results){
    //There u get Bundle of reults(words) ordered by probability (Highest to lowest)
    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    }
}
``` 

## User guide
1. Installation of the application is currently available only in the development version (build in Xcode Version 11.4.1 with Pixel 2  , Samsung A5, and Sony Xperia Z1 targeted to API 26 Oreo).

2. The main screen represents the List of all available languages. 
3. After selecting the language, the user is navigated to the second screen, containing the translated word with audio pronunciation.
4. The *Tap to speak* button handles the main gamification mechanism, which allows the user to train the pronunciation and translation of different words.  
5. In case of successful recognition, the app grants the user with completion popup and generates a new word.
