@file:Suppress("DEPRECATION")

package com.example.firebaseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.databinding.ActivitySignOutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Suppress("DEPRECATION")
class SignOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignOutBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signout.setOnClickListener {
            val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN ).requestIdToken(getString(R.string.web_client_id )).requestEmail().build()
         val go=   GoogleSignIn.getClient(this,gso)
             go.signOut()
//            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))

        }

    }
}