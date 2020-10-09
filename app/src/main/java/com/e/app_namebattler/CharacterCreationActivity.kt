package com.e.app_namebattler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_character_creation.*

class CharacterCreationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_creation)

        create_character_back_button_id.setOnClickListener {
            finish()
        }

        create_character_button_id.setOnClickListener {
            val intent = Intent(this,CharacterCreated::class.java)
            startActivity(intent)
        }
    }
}