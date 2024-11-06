package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivityDatabaseBinding

class DatabaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDatabaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createNotebutton.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }
      binding.openNotebutton.setOnClickListener {
          startActivity(Intent(this,AllNotesActivity::class.java))
      }

    }
}