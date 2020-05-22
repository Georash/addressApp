//
//  ViewController.swift
//  Grocery List
//
//  Created by Certified Kennedy on 11/07/2019.
//  Copyright © 2019 Certified Addison. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextViewDelegate {
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var groceryTextView: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        groceryTextView.delegate = self
    }

    @IBAction func addItem(_ sender: Any) {
        
        if let text = textField.text, text != "" {
            
            groceryTextView.text.append("\(text)\n")
            textField.text = ""
            textField.resignFirstResponder()
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        textField.resignFirstResponder()
    }
    
    func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
        textView.resignFirstResponder()
        
        return false
    }
    
}

