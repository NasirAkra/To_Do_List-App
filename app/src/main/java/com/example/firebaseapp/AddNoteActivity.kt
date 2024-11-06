package com.example.firebaseapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()

        binding.saveNotebutton.setOnClickListener {


            val title=binding.title.text.toString()
            val description=binding.description.text.toString()
            if (title.isEmpty()&&description.isEmpty())
            {
                Toast.makeText(this,"Fill both fields",Toast.LENGTH_LONG).show()
            }
            else
            {
                val currentUser=auth.currentUser
                currentUser?.let {
                    user->
                    val noteKey=databaseReference.child("users").child(user.uid)
                        .child("Notes").push().key
                    val noteItem=NoteItem(title,description,noteKey?:"")
                    if(noteKey!=null)
                        databaseReference.child("users").child(user.uid).child("Notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener { task->
                                if (task.isSuccessful)
                                {
                                    Toast.makeText(this,"Note Save Successful",Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                else
                                {
                                    Toast.makeText(this,"Failed To Save Note",Toast.LENGTH_LONG).show()
                                }
                            }
                }
            }


        }

    }
}