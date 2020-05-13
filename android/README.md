
# Android

![Android](https://upload.wikimedia.org/wikipedia/commons/8/82/Android_logo_2019.svg)

## About

This application represent android solution of problem mentioned [before](https://github.com/Coder-mano/Assignment/blob/master/README.md). For this purpose we use native android technologies like *Text to speech, Speech recognition* and external technologies like *IBM Wattson translator* and *Firebase*. Application main goal is improve user skills with foreign languages not just by translating and extending his vocabulary but can help with his speech skill. 

## Features
* Text to speech 
* Translating words from database
* Speech recognition
* Matching words 

## User guide
1. Installation of application is currently available only in developer version (from IDE).
2. First screen is list of languages. You can choose which language you want to learn or get translations.
3. After selecting the language second screen will appear. This screen contains main functionality of application. You can see word image under which is translation of word.
4. By tapping on nation flag you can play how you should spell the word.
5. Button *Tap to speak* serves on recording your speech and subsequently evaluate your speech. If both words match u can proceed to next word.

## Screenshots

## Text to speech
Text to speech (TTS) is native function by android which is able to transform text to playback. This function should support all languages which are included in class Locale.

```java
//Text to english speech - word Hello
String word = "Hello";
TextToSpeech repeatTTS = new TextToSpeech(context, listener);
repeatTTS.setLanguage(Locale locale = Locale.ENGLISH);
repeatTTS.speak(word,TextToSpeech.QUEUE_FLUSH, null, "identifier"));
``` 

## Speech recognition
Speech recognition is native function provided by android, too. As in the previous case it should support all languages from Local class. Output of this function is ordered list of words which every word have confidence score.
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
