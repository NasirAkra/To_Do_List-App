package com.example.firebaseapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseapp.databinding.ActivityAllNotesBinding
import com.example.firebaseapp.databinding.UpdateNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotesActivity : AppCompatActivity(),NoteAdapter.OnItemClickListener {
    private lateinit var binding: ActivityAllNotesBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database Reference
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Reference to the current user's Notes node in Firebase
            val noteReference = databaseReference.child("users").child(user.uid).child("Notes")

            // Listen for changes in the notes
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItem>()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    noteList.reverse()

                    // Set up RecyclerView
                    val adapter = NoteAdapter(noteList,this@AllNotesActivity)
                    binding.noteRecycleView.layoutManager = LinearLayoutManager(this@AllNotesActivity)
                    binding.noteRecycleView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error or show a message to the user
                    Log.e("AllNotesActivity", "Database error: ${error.message}")
                }
            })
        }
    }

    override fun onDeleteClick(noteId: String?) {
       val currentUser=auth.currentUser
        currentUser?.let {
            user->
            val noteReference=databaseReference.child("users").child(user.uid).child("Notes")
                 noteReference.child(noteId.toString()).removeValue()
        }
    }

    override fun onUpdateClick(noteId: String?, title: String, description: String) {
        val dialogBinding=UpdateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog=AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){
                dialog,_->
                val newTitle=dialogBinding.titles.text.toString()
                val newDescription=dialogBinding.descriptions.text.toString()
                updateNotesDatabase(noteId,newTitle,newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Canceled"){  dialog,_->
                dialog.dismiss()

            }
            .create()
        dialogBinding.titles.setText(title)
        dialogBinding.descriptions.setText(description)
        dialog.show()



    }

    private fun updateNotesDatabase(noteId: String?, newTitle: String, newDescription: String) {
        val currentUser=auth.currentUser
        currentUser?.let {
                user->
            val noteReference=databaseReference.child("users").child(user.uid).child("Notes")
            noteReference.child(noteId.toString()).removeValue()
            val updateNote=NoteItem(newTitle,newDescription,noteId)
            noteReference.child(noteId.toString()).setValue(updateNote)
                .addOnCompleteListener {
                        task->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this,"Note Updated Successfully",Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(this,"Note Not Updated",Toast.LENGTH_LONG).show()

                    }
                }
        }

    }
}