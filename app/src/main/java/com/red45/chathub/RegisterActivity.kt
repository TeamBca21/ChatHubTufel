package com.red45.chathub

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.red45.chathub.Classes.Users
import com.red45.chathub.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imgUri : Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var progreDialog = ProgressDialog(this)
        progreDialog.setTitle("Regisration")
        progreDialog.setMessage("We are creating your account .\npleas wait...")
        progreDialog.setIcon(R.drawable.chat_hub_logo)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()



        binding.selectImgBtn.setOnClickListener{
            selectImg()
        }

        binding.btnReg.setOnClickListener{
//            if (name.isEmpty()){
//                binding.etNameReg.error = "Name is required"
//                binding.etNameReg.requestFocus()
//                return@setOnClickListener
//            }
//            if (email.isEmpty()){
//                binding.etEmailReg.error = "E-mail is required"
//                binding.etEmailReg.requestFocus()
//                return@setOnClickListener
//            }
//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                binding.etEmailReg.error = "Enter valid E-mail"
//                binding.etEmailReg.requestFocus()
//                return@setOnClickListener
//            }
//            if (pass.isEmpty()){
//                binding.etPassReg.error = "Password is required"
//                binding.etPassReg.requestFocus()
//                return@setOnClickListener
//            }
//            if (pass.length < 8){
//                binding.etPassReg.error = "Password should be at least 8 characters"
//                binding.etPassReg.requestFocus()
//                return@setOnClickListener
//            }

                val name = binding.etNameReg.text.toString()
                val email = binding.etEmailReg.text.toString()
                val pass = binding.etPassReg.text.toString()
                progreDialog.show()

                auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            var userId : String = auth.uid.toString()
                            var storage = FirebaseStorage.getInstance().reference.child("Users/$userId")
                            storage.putFile(imgUri!!).addOnSuccessListener {
                                storage.downloadUrl.addOnSuccessListener { url->
                                    var users = Users(name,email,pass,url.toString())
                                    database.getReference("Users").child(auth.uid.toString()).setValue(users)
                                    Toast.makeText(this@RegisterActivity,"Registration Completed...",Toast.LENGTH_SHORT).show()
                                    progreDialog.dismiss()
                                    var intent = Intent(this@RegisterActivity,MainActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }else{
                            progreDialog.dismiss()

                            Toast.makeText(this@RegisterActivity,"Something Went Wrong !!!",Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        progreDialog.dismiss()
                        Toast.makeText(this@RegisterActivity,"Registration Faild.\nPleas try again leter.",Toast.LENGTH_SHORT).show()
                    }
            if(auth.currentUser != null){
                var intent = Intent(this@RegisterActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun  selectImg(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if(data.data != null){
                imgUri = data.data!!
                binding.userImg.setImageURI(imgUri)
            }
        }
    }
}
