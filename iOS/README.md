[![Screenshots](https://github.com/Coder-mano/Polyglot-Pronunciation-Training/blob/master/images/ios.png)]()

## About

prototype iOS implementation of the Translation Game containing Speech recognition, Firebase Realtime Database, and IBM Watson Language Translation example build in Swift 5.

**Supported languages:** 🇪🇸 Spanish, 🇫🇷 French, 🇧🇪 German, 🇮🇹 Italian, 🇸🇪 Swedish, 🇰🇷 Korean, 🇷🇺 Russian, 🇹🇷 Turkish, 🇭🇺 Hungarian, 🇵🇱 Polish, 🇬🇷 Greek, 🇩🇰 Danish, 🇳🇱 Dutch, 🇮🇳 Hindi, 🇯🇵 Japanese, 🇧🇷 Portuguese, 🇻🇳 Vietnamese, 🇸🇰 Slovak, 🇮🇪 Irish, 🇮🇱 Hebrew, 🇨🇿 Czech, 🇷🇴 Romanian, 🇮🇩 Indonesian


## Implementation overview

The final implementation is divided into three main parts:

* **Random word generation -** fetches the word for translation from the database based on the maximum size.
* **Word translation -** translates the generated word through IBM Watson to the selected language
* **Speech recognition -** handles the voice to string transformation and equality with generated word

### Random word generation

```Swift
// Check db size
let ref = Database.database().reference()
       let dbSizeRef = ref.child("db_size")
       dbSizeRef.observeSingleEvent(of: .value, with: { (snapshot) in
           self.db_size = snapshot.value as? Int
       })
```
```Swift
// Random word generator
   func getRandomWord(){
       let randomRef = Int.random(in: 1 ... (db_size!))
       let dbRef = Database.database().reference().child(String(randomRef))
       dbRef.observeSingleEvent(of: .value, with: { (snapshot) in
           self.firCompletion(snapshot.value as? String ?? "Error")
       }) { (error) in
           self.firCompletion(error.localizedDescription)
       }
   }
```

### Word translation
```Swift
languageTranslator.translate(text: [word], source: "en", target: selectedLanguage) { text, error in
    let translation = text!.result?.translations[0]
    DispatchQueue.main.async {
        completion()
    }
}
```

### Speech recognition
```Swift
speechRecognizer.recognitionTask(with: (request)) { result, error in
        
    if let result = result {
        if result.bestTranscription.formattedString.lowercased().contains(word.lowercased()) {
            // Sucess!
            self.audioEngine.stop()
            node.removeTap(onBus: 0)
            
            do{
                try self.audioSession.setCategory(.soloAmbient)
            }catch{}
            
            AudioServicesPlaySystemSound(1054);
            self.parrentController?.succes()
        }
    }
}
```

## User guide
1. Installation of the application is currently available only in the development version (build in Xcode Version 11.4.1 with iPhone 7 Plus on iOS 13.4).

2. The main screen represents the List of all available languages. 
3. After selecting the language, the user is navigated to the second screen, containing the translated word with audio pronunciation.
4. The *Tap to speak* button handles the main gamification mechanism, which allows the user to train the pronunciation and translation of different words.  
5. In case of successful recognition, the app grants the user with completion popup and generates a new word.
