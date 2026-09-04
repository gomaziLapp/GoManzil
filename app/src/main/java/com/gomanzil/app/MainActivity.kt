package com.gomanzil.app

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24,32,24,24); setBackgroundColor(Color.WHITE) }
        val logo = TextView(this).apply { text = "GoManzil"; textSize = 32f; setTextColor(Color.BLACK); gravity = Gravity.CENTER; setPadding(0,20,0,28) }
        root.addView(logo)
        val title = TextView(this).apply { text = "Apni ride book karo"; textSize = 24f; setTextColor(Color.DKGRAY); setPadding(0,8,0,18) }
        root.addView(title)
        val pickup = EditText(this).apply { hint = "Pickup location"; setSingleLine(true) }
        root.addView(pickup, LinearLayout.LayoutParams(-1,60))
        val drop = EditText(this).apply { hint = "Where to?"; setSingleLine(true) }
        root.addView(drop, LinearLayout.LayoutParams(-1,60))
        val book = Button(this).apply { text = "Book a Ride"; setOnClickListener { Toast.makeText(context,"Ride request received",Toast.LENGTH_SHORT).show() } }
        val lp = LinearLayout.LayoutParams(-1,64); lp.topMargin=24; root.addView(book,lp)
        val options = TextView(this).apply { text = "\nBike   •   Auto   •   Car\n\nSafe rides • Live trip updates • Easy booking"; textSize=17f; setTextColor(Color.DKGRAY); gravity=Gravity.CENTER; setPadding(0,24,0,0) }
        root.addView(options)
        setContentView(root)
    }
}
