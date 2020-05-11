//
//  GameController.swift
//  zct-assignment
//
//  Created by Tomáš Adam on 10/05/2020.
//  Copyright © 2020 Tomáš Adam. All rights reserved.
//

import UIKit
import Firebase
import AVFoundation

class GameController: UIViewController {
    
    public var languageId: Int?
    var speechRecognizer = SpeechRecognition()

    var db_size: Int?
    var wordForTranslate: String?
    var wordImage: String?
    var peopleAssets = ["🧕","👮‍♀️","👷‍♀️","🕵️‍♀️","👩‍⚕️","👩‍🌾","👩‍🍳","👩‍🎓","👩‍🏫","👩‍💻","👩‍💼","👩‍🎨","👩‍🚒","👩‍✈️","👩‍🚀","👩‍⚖️","🧙‍♀️","🧛‍♀️"]
    var locale = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("LanguageId: \(languageId ?? 0)")
        
        let ref = Database.database().reference()
        let dbSizeRef = ref.child("db_size")
        dbSizeRef.observeSingleEvent(of: .value, with: { (snapshot) in
            print("Db_size: \(snapshot.value ?? 0)")
            self.db_size = snapshot.value as? Int
        })
        
        getRandomWord()
        generateUI()
        locale = getLocale(id: languageId!)
        navigationItem.rightBarButtonItem = getFlagIcon(id: languageId!)
        navigationItem.rightBarButtonItem?.imageInsets = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
    }
    
    var background: UIView!
    func loadSuccesView(){
        if background == nil {
            background = UIView(frame: CGRect(x: 0, y: UIScreen.main.bounds.height, width: UIScreen.main.bounds.width, height: 125))
            background.backgroundColor = .green
            view.addSubview(background)
            
            let title = UILabel(frame: CGRect(x: 10, y: 5, width: 300, height: 50))
            title.text = "Translation: \(wordForTranslate ?? "")"
            title.font = UIFont.boldSystemFont(ofSize: 20)
            title.tag = 111
            background.addSubview(title)
            
            let continueBtn = UIButton(frame: CGRect(x: 10, y: 55, width: UIScreen.main.bounds.width-20, height: 50))
            continueBtn.backgroundColor = .systemGreen
            continueBtn.setTitle("Continue", for: .normal)
            continueBtn.setTitleColor(.white, for: .normal)
            continueBtn.setTitleColor(.gray, for: .highlighted)
            continueBtn.layer.cornerRadius = 10
            continueBtn.addTarget(self, action: #selector(reloadView), for: .touchUpInside)
            background.addSubview(continueBtn)
        }else{
            (background.viewWithTag(111) as! UILabel).text = "Translation: \(wordForTranslate ?? "")"
        }
        background.isHidden = false
        UIView.animate(withDuration: 0.5, animations: {
            self.background.frame.origin.y = UIScreen.main.bounds.height - 125
        })
    }
    
    @objc func reloadView(){
        getRandomWord()
        UIView.animate(withDuration: 0.5, animations: {
            self.background!.frame.origin.y = UIScreen.main.bounds.height
        },completion: {
            _ in
            self.background.isHidden = true
        })
    }
    
    func getRandomWord(){
        let randomRef = Int.random(in: 1 ..< (db_size ?? 5))
        let dbRef = Database.database().reference().child(String(randomRef))
        dbRef.observeSingleEvent(of: .value, with: { (snapshot) in
            self.firCompletion(snapshot.value as? String ?? "Error")
        }) { (error) in
            self.firCompletion(error.localizedDescription)
        }
    }
    
    func firCompletion(_ completion:String) {
    
        print("Random word \(completion.prefix(1))")
        wordImage = String(completion.prefix(1))
        print("Random word \(wordImage)")
        
        translate(word: String(completion.dropFirst()))

        //readWord()
        //(view.viewWithTag(111) as? UILabel)?.text = wordForTranslate
        (view.viewWithTag(112) as? UILabel)?.text = wordImage
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    @objc func readWord(){
        let utterance = AVSpeechUtterance(string: wordForTranslate ?? "")
        utterance.voice = AVSpeechSynthesisVoice(language: locale)
        let synth = AVSpeechSynthesizer()
        synth.speak(utterance)
    }
    
    var tapToSpeak:UIButton!
    
    func generateUI(){
        
        tapToSpeak = UIButton(type: .custom)
        tapToSpeak.setTitle(" Tap to speak", for: .normal)
        tapToSpeak.frame.origin.y = 250
        tapToSpeak.setImage(UIImage(systemName: "mic.fill"), for: .normal)
        tapToSpeak.setTitleColor(.white, for: .normal)
        tapToSpeak.setTitleColor(.gray, for: .highlighted)
        tapToSpeak.tintColor = .white
        tapToSpeak.backgroundColor = .blue
        tapToSpeak.titleLabel?.font = UIFont.systemFont(ofSize: 18.0)
        tapToSpeak.layer.cornerRadius = 10.0
        tapToSpeak.addTarget(self, action: #selector(speechBtnTap(_:)), for: .touchUpInside)
        
        view.addSubview(tapToSpeak)
        tapToSpeak.translatesAutoresizingMaskIntoConstraints = false
        tapToSpeak.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -15).isActive = true
        tapToSpeak.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 15).isActive = true
        tapToSpeak.bottomAnchor.constraint(equalTo: view.centerYAnchor, constant: 0).isActive = true
        tapToSpeak.heightAnchor.constraint(equalToConstant: 70).isActive = true
        
        
        let person = UILabel()
        person.text = peopleAssets[Int.random(in: 0 ..< peopleAssets.count)]
        person.font = UIFont.systemFont(ofSize: 150)
        
        view.addSubview(person)
        person.translatesAutoresizingMaskIntoConstraints = false
        person.bottomAnchor.constraint(equalTo: tapToSpeak.topAnchor, constant: 15).isActive = true
        person.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 10).isActive = true
        
        
        let wordIImageView = UILabel()
        wordIImageView.text = wordImage
        wordIImageView.font = UIFont.systemFont(ofSize: 100)
        wordIImageView.tag = 112

        view.addSubview(wordIImageView)
        wordIImageView.translatesAutoresizingMaskIntoConstraints = false
        wordIImageView.leftAnchor.constraint(equalTo: tapToSpeak.centerXAnchor, constant: 0).isActive = true
        wordIImageView.centerYAnchor.constraint(equalTo: person.centerYAnchor, constant: -15).isActive = true
        
        
        let bubble = UILabel()
        bubble.text = self.wordForTranslate ?? ""
        bubble.sizeToFit()
        bubble.tag = 111
        bubble.font = UIFont.boldSystemFont(ofSize: 18)
        bubble.numberOfLines = 0
        bubble.textAlignment = .center
        
        self.view.addSubview(bubble)
        bubble.translatesAutoresizingMaskIntoConstraints = false
        bubble.topAnchor.constraint(equalTo: wordIImageView.bottomAnchor, constant: 0).isActive = true
        bubble.centerXAnchor.constraint(equalTo: wordIImageView.centerXAnchor, constant: 0).isActive = true
        
        
        let soundBtn = UIButton()
        soundBtn.setTitle("👂", for: .normal)
        soundBtn.titleLabel?.font = UIFont.systemFont(ofSize: 35)
        
        view.addSubview(soundBtn)
        soundBtn.translatesAutoresizingMaskIntoConstraints = false
        soundBtn.rightAnchor.constraint(equalTo: view.rightAnchor, constant: -20).isActive = true
        soundBtn.bottomAnchor.constraint(equalTo: tapToSpeak.topAnchor, constant: 0).isActive = true
        soundBtn.addTarget(self, action: #selector(readWord), for: .touchUpInside)
    }
    
    public func succes(){
        tapToSpeak.backgroundColor = .blue
        loadSuccesView()
    }
    
    @objc func speechBtnTap(_ sender:UIButton){
        if let back = background {
            if !back.isHidden {
                return
            }
        }
        
        if tapToSpeak.backgroundColor == .blue {
            tapToSpeak.backgroundColor = .red
            speechRecognizer = SpeechRecognition()
            speechRecognizer.parrentController = self
            speechRecognizer.speecchRecognition(word: wordForTranslate!,locale: locale)
            
        }else{
            tapToSpeak.backgroundColor = .blue
            speechRecognizer.stopRecognizing()
        }
    }
    
}
