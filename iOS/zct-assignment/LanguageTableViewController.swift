//
//  LanguageTableViewController.swift
//  zct-assignment
//
//  Created by Tomáš Adam on 10/05/2020.
//  Copyright © 2020 Tomáš Adam. All rights reserved.
//

import UIKit

class LanguageTableViewController: UITableViewController {
    
    // tmp test values
    let flags = ["🇪🇸","🇫🇷","🇧🇪","🇮🇹","🇸🇪","🇰🇷","🇷🇺","🇹🇷","🇭🇺","🇵🇱","🇬🇷","🇩🇰","🇳🇱","🇮🇳","🇯🇵","🇧🇷","🇻🇳","🇸🇰","🇮🇪","🇮🇱","🇨🇿","🇷🇴","🇮🇩"]
    let languages = ["Spanish","French","German","Italian","Swedish","Korean","Russian","Turkish","Hungarian","Polish","Greek","Danish", "Dutch","Hindi","Japanese","Portuguese","Vietnamese","Slovak","Irish", "Hebrew","Czech","Romanian","Indonesian"]
       

    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.separatorStyle = .none
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 20, right: 0)
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return languages.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...
        cell.textLabel?.text = languages[indexPath.row]
        cell.imageView?.image = flags[indexPath.row].image()
        cell.accessoryType = .disclosureIndicator
        
        return cell
    }
    

    // Handle cell tap
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "showGame", sender: self)
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showGame"{
            let destination = segue.destination as! GameController
            destination.languageId = tableView.indexPathForSelectedRow?.row
        }
    }
    
}

extension String {
    // From String emoji to UIImage
    func image() -> UIImage? {
        let size = CGSize(width: 60, height: 60)
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        UIColor.white.set()
        let origin = CGPoint(x: 0, y: -5)
        let rect = CGRect(origin: origin, size: size)
        UIRectFill(CGRect(origin: .zero, size: size))
        (self as AnyObject).draw(in: rect, withAttributes: [.font: UIFont.systemFont(ofSize: 60)])
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image
    }
}
