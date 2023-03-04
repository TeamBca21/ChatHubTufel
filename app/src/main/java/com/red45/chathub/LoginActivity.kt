package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.red45.chathub.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    lateinit var database : FirebaseDatabase
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var progreDialog = ProgressDialog(this)
        progreDialog.setTitle("Login")
        progreDialog.setMessage("Login pleas wait...")
        progreDialog.setIcon(R.drawable.chat_hub_logo)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()



        binding.btnLogin.setOnClickListener {
            var email = binding.etEmailLogin.text.toString()
            var pass = binding.etPassLogin.text.toString()

           if(email.isEmpty() && pass.isEmpty()) {
               binding.etEmailLogin.error= "Email Address"
               binding.etPassLogin.error ="Passwrod"
           }else{
               progreDialog.show()
               loginUser(email,pass,progreDialog)
               }
           }
    }


    private fun loginUser(email: String, pass: String, progreDialog: ProgressDialog) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    progreDialog.dismiss()
                    Toast.makeText(this@LoginActivity, "Login Successfully...", Toast.LENGTH_SHORT)
                        .show()
                    var i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                } else {
                    progreDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "You don't have account !\nPleas SignUp first.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@LoginActivity,
                    "Somthing Went Wrong !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}