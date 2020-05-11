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
                self.resultString = word
                self.referenceWord = translation?.translation
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
            return "hu"
        case 9:
            return "pl"
        case 10:
            return "el"
        case 11:
            return "da"
        case 12:
            return "nl"
        case 13:
            return "hi"
        case 14:
            return "ja"
        case 15:
            return "pt"
        case 16:
            return "vi"
        case 17:
            return "sk"
        case 18:
            return "ga"
        case 19:
            return "he"
        case 20:
            return "cs"
        case 21:
            return "ro"
        case 22:
            return "id"
            
        default:
            return "en"
        }
    }
    
    func getFlagIcon(id:Int) -> UIBarButtonItem {
        let flags = ["🇪🇸","🇫🇷","🇧🇪","🇮🇹","🇸🇪","🇰🇷","🇷🇺","🇹🇷","🇭🇺","🇵🇱","🇬🇷","🇩🇰","🇳🇱","🇮🇳","🇯🇵","🇧🇷","🇻🇳","🇸🇰","🇮🇪","🇮🇱","🇨🇿","🇷🇴","🇮🇩"]
        return UIBarButtonItem(title: flags[id], style: .plain, target: self, action: nil)
    }
    
}
