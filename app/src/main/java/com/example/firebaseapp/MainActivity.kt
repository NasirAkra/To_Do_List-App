@file:Suppress("DEPRECATION")

package com.example.firebaseapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
  val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN ).requestIdToken(getString(R.string.web_client_id )).requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)

        auth = Firebase.auth
        binding.signup.setOnClickListener{
            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString() )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
                        auth.currentUser
                      Toast.makeText(this,task.result.toString(),Toast.LENGTH_LONG).show()
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                    }
                }
        }
        binding.google?.setOnClickListener {
            val signInClient=googleSignInClient.signInIntent
           luncher.launch(signInClient)

        }

    }
    private val luncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result ->
        if (result.resultCode== Activity.RESULT_OK)
        {
           val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful)
            {
                val account:GoogleSignInAccount?=task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        Toast.makeText(this,"Done",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this,DatabaseActivity::class.java))
                    }
                    else
                    {
                        Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
        else
        {
            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
        }

    }
}