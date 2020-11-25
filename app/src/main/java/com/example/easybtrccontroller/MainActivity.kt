package com.example.easybtrccontroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var m_address: String

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // m_address = intent.getStringExtra(BtControlActivity.EXTRA_ADDRESS)

//        val intent = Intent(this, BtControlActivity::class.java)
//        //intent.putExtra(EXTRA_ADDRESS, address)
//        startActivity(intent)

        val intent = Intent(this, BtControlActivity::class.java)
        val setBt=findViewById<Button>(R.id.set_bt)
        setBt?.setOnClickListener {
            startActivity(intent)
            finish()
        }

    }
}