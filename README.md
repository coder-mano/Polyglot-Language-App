# Polyglot - Pronunciation Training

This repository represents the solution to the assignment. The primary goal was to create two mobile applications, as an example representing the usage of multiple cloud services for translation game, with an evaluation of spoken language based on a dynamic cloud dictionary database.


* **<a href="https://github.com/Coder-mano/Polyglot-Pronunciation-Training/tree/master/iOS">iOS -</a>**  represents the **iOS** part of the **Translation Game** & contains the commented Swift implementation solution of the mentioned assignment.

* **<a href="https://github.com/Coder-mano/Polyglot-Pronunciation-Training/tree/master/android">Android -</a>** represents the **Android** part of the **Translation Game** & contains the commented Java implementation solution of the mentioned assignment.

* **<a href="https://github.com/Coder-mano/Polyglot-Pronunciation-Training/tree/master/web">Web -</a>** 
    represents the administrator's access to the **Real-Time Firebase Database** & contains a **React** web application for performing basic CRUD operations like Create, Read, Update, and Delete with Login and Logout.
    
[![Scheme](https://github.com/Coder-mano/Polyglot-Pronunciation-Training/blob/master/images/Scheme.png)]()
## Implementation overview

In this part, we will introduce the implementation of used technologies with a small theoretical overview. The main use case represents the improvement of the user's pronunciation ability with a simple gamification aspect and image examples. The images and words for translation are stored on the cloud database, which ensures a clear way for improving the capacity of available words through web administrator without an update or any change in implementation. 

The main user flow begins with language selection and random word fetch from the database. The next step contains word translation through IBM Watson and image content presentation for the user. After successful speech recognition of word pronunciation, the app grants the user with tone and "Continue" popup.


## IBM Watson

IBM Watson™ Language Translator service grants multiple IBM provided translation models and offers 1,000,000 characters translation per month for free. 

**Supported languages:** Arabic, Bulgarian, Catalan, Chinese (Simplified & Traditional), Croatian, Czech, Danish, Dutch, English, Estonian, Finnish, French, German, Greek, Hebrew, Hindi, Hungarian, Irish (Gaelic), Italian, Indonesian, Japanese, Korean, Latvian, Lithuanian, Malay, Norwegian, Polish, Portuguese (Brazil), Romanian, Russian, Slovak, Slovenian, Spanish, Swedish, Thai, Turkish, Urdu and Vietnamese

### Installation
##### Cocoapods
```bash
pod 'IBMWatsonLanguageTranslatorV3', '~> 3.4.0'
```
##### Gradle
```gradle
implementation 'com.ibm.watson:ibm-watson:8.4.0'
```

### Authentication
Watson services are using token-based Identity and Access Management (IAM) authentication.

#####  Swift
```swift
let authenticator = WatsonIAMAuthenticator(apiKey: "{apikey}")
let languageTranslator = LanguageTranslator(version: "{version}", authenticator: authenticator)
languageTranslator.serviceURL = "{url}"
```
##### Java
```java
IamAuthenticator authenticator = new IamAuthenticator("{apikey}");
LanguageTranslator languageTranslator = new LanguageTranslator("{version}", authenticator);
languageTranslator.setServiceUrl("{url}");
```

### Translate
Translates the input text from the source language to the target language. A target language or translation model ID is required. 

#####  Swift
```swift
func translate(text: [String],
modelID: String? = nil,
source: String? = nil,
target: String? = nil,
headers: [String: String]? = nil,
completionHandler: @escaping (WatsonResponse<TranslationResult>?, WatsonError?) -> Void)
```
##### Java
```java
ServiceCall<TranslationResult> translate(TranslateOptions translateOptions)
```

## Firebase
The Firebase Realtime Database is a cloud-hosted database. Data is stored as JSON and synchronized in realtime to every connected client.

### Installation, Set Up & Usage
##### Cocoapods & Swift
```bash
pod 'Firebase/Database'
```
```swift
import Firebase
FirebaseApp.configure()

var ref: DatabaseReference!
ref = Database.database().reference()

// Get the first word from a database
ref.child("1").observeSingleEvent(of: .value, with: { (snapshot) in
    // Get value
    let value = snapshot.value as? String
  }) { (error) in
    print(error.localizedDescription)
}
```
##### Gradle & Java 
```gradle
implementation 'com.google.firebase:firebase-database:19.3.0'
```
```java
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference ref = database.getReference();

//Get the first word from a database
ref.child("1").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
            // Get value
            String value = dataSnapshot.getValue()
        }
    }
});
```

## Speech recognition
The speech framework recognizes spoken words in recorded or live audio. The service relies on Apple’s and Google's servers for speech recognition and requires a network connection. 

</br>

## Resources

* **<a href="https://cloud.ibm.com/apidocs/language-translator?code=java">IBM Watson Language Translator Documentation </a>** 

* **<a href="https://firebase.google.com/docs/ios/setup">Firebase Documentation </a>** 

* **<a href="https://developer.apple.com/documentation/speech">Apple Speech framework </a>** 

* **<a href="https://developer.android.com/reference/android/speech/package-summary">Android Speech package </a>** 



##  ✍️ Authors

- [Tomáš Adam @coder-mano](https://github.com/Coder-mano) 
- [Roman Varga @vargaroman](https://github.com/vargaroman) 
- [Filip Reichl @fire2124](https://github.com/fire2124) 
- [Lucia Szalonová @do-re-mi-fa](https://github.com/do-re-mi-fa) 
