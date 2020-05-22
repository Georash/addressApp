//
//  ViewController.swift
//  My First Project
//
//  Created by Certified Kennedy on 11/07/2019.
//  Copyright © 2019 Certified Addison. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var label: UILabel!
    
    @IBOutlet weak var textfield: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        label.text = "Hello from code!"
        textfield.becomeFirstResponder()
    }

    @IBAction func buttonWasTapped(_ sender: Any) {
        
        label.text = "Hello \(textfield.text!)"
        
        textfield.resignFirstResponder()
    }
    
}

