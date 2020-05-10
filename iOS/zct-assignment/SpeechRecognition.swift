//
//  SpeechRecognition.swift
//  zct-assignment
//
//  Created by Tomáš Adam on 10/05/2020.
//  Copyright © 2020 Tomáš Adam. All rights reserved.
//

import Foundation
import Speech


public class SpeechRecognition {
    
    let audioEngine = AVAudioEngine()
    var parrentController: GameController?
    private var player: AVAudioPlayer?
    var audioSession = AVAudioSession.sharedInstance()
    
    public func speecchRecognition(word: String){
        
        let speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: "en"))!
        
        if !audioEngine.isRunning {
            
            // req config
            let request = SFSpeechAudioBufferRecognitionRequest()
            
            do {
                try audioSession.setCategory(AVAudioSession.Category.record)
                try audioSession.setMode(AVAudioSession.Mode.measurement)
                try audioSession.setActive(true)
                
            }  catch let error as NSError  {
                print(error)
            }
            
            let node = audioEngine.inputNode
            
            let recordingFormat = node.outputFormat(forBus: 0)
            node.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat){
                buffer, _ in
                request.append(buffer)
            }
            
            var recognitionRequest: SFSpeechAudioBufferRecognitionRequest?
            
            
            // partial reports handling
            recognitionRequest?.shouldReportPartialResults = true
            request.requiresOnDeviceRecognition = false
            
            speechRecognizer.recognitionTask(with: (request)) { result, error in
                
                var isFinal = false
                
                if let result = result {
                    isFinal = result.isFinal
                    if result.bestTranscription.formattedString.lowercased().contains(word.lowercased()) {
                        // Sucess!
                        self.audioEngine.stop()
                        node.removeTap(onBus: 0)
                        
                        do{
                            try self.audioSession.setCategory(.soloAmbient)
                        }catch{}
                        
                        recognitionRequest = nil
                        self.parrentController?.succes()
                        AudioServicesPlaySystemSound(1054);
                    }
                }
                
                if error != nil || isFinal {
                    self.audioEngine.stop()
                    
                    node.removeTap(onBus: 0)
                    recognitionRequest = nil
                }
            }
            
            audioEngine.prepare()
            do {
                try audioEngine.start()
            }  catch let error as NSError  {
                print(error)
            }
        }
        
    }
    
    func stopRecognizing(){
        self.audioEngine.stop()
        let node = audioEngine.inputNode
        node.removeTap(onBus: 0)
        
        do{
            try audioSession.setCategory(.soloAmbient)
        }catch{}
    }
    
}
