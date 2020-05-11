//
//  GameController.swift
//  zct-assignment
//
//  Created by Tomáš Adam on 10/05/2020.
//  Copyright © 2020 Tomáš Adam. All rights reserved.
//

import UIKit
import Firebase

class GameController: UIViewController {

    public var languageId: Int?
   
    var db_size: Int?
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
    }
    
    func getRandomWord(){
        let randomRef = Int.random(in: 1 ..< (db_size ?? 5))
        let dbRef = Database.database().reference().child(String(randomRef))
        dbRef.observeSingleEvent(of: .value, with: { (snapshot) in
            self.firaCompletion(snapshot.value as? String ?? "Error")
        }) { (error) in
            self.firaCompletion(error.localizedDescription)
           }
    }
    
    // tmp completion
    func firaCompletion(_ completion:String) {
        print("Random word \(completion)")
    }

}
