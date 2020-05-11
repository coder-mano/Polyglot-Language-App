//
//  Translation.swift
//  zct-assignment
//
//  Created by Tomáš Adam on 11/05/2020.
//  Copyright © 2020 Tomáš Adam. All rights reserved.
//

import Foundation
import Assistant
import LanguageTranslator

extension GameController  {
    
    public func translate(word:String) {
        let authenticator = WatsonIAMAuthenticator(apiKey: "lKP9iBAi-sJNW-Kwpg_3W9Dfkn93m5ClpSoL7Y782Jrl")
        let languageTranslator = LanguageTranslator(version: "2018-05-01", authenticator: authenticator)
        languageTranslator.serviceURL = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/788a2077-6f89-4c5c-9b3f-56501c57b512"
        languageTranslator.translate(text: [word], source: "en", target: locale) { text, error in
            let translation = text!.result?.translations[0]
            DispatchQueue.main.async {
                (self.view.viewWithTag(111) as? UILabel)?.text = translation?.translation
                self.wordForTranslate = translation?.translation
                self.readWord()
            }
        }
    }
    
    func getLocale(id:Int)->String{
        switch id {
        case 0:
            return "es"
        case 1:
            return "fr"
        case 2:
            return "de"
        case 3:
            return "it"
        case 4:
            return "sv"
        case 5:
            return "ko"
        case 6:
            return "ru"
        case 7:
            return "tr"
        case 8:
            return "pl"
       
        default:
            return "en"
        }
    }
          
    func getFlagIcon(id:Int) -> UIBarButtonItem {
        switch id {
        case 0:
            return UIBarButtonItem(title: "🇪🇸", style: .plain, target: self, action: nil)
        case 1:
            return UIBarButtonItem(title: "🇫🇷", style: .plain, target: self, action: nil)
        case 2:
            return UIBarButtonItem(title: "🇧🇪", style: .plain, target: self, action: nil)
        case 3:
            return UIBarButtonItem(title: "🇮🇪", style: .plain, target: self, action: nil)
        case 4:
            return UIBarButtonItem(title: "🇸🇪", style: .plain, target: self, action: nil)
        case 5:
            return UIBarButtonItem(title: "🇰🇷", style: .plain, target: self, action: nil)
        case 6:
            return UIBarButtonItem(title: "🇷🇺", style: .plain, target: self, action: nil)
        case 7:
            return UIBarButtonItem(title: "🇹🇷", style: .plain, target: self, action: nil)
        case 8:
            return UIBarButtonItem(title: "🇵🇱", style: .plain, target: self, action: nil)
            
        default:
            return UIBarButtonItem(title: "", style: .plain, target: self, action: nil)
        }
        
    }
    
}
