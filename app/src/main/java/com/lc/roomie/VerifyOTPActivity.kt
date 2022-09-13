package com.lc.roomie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_send_otp.*
import kotlinx.android.synthetic.main.activity_verify_otp.*
import kotlinx.android.synthetic.main.activity_verify_otp.view.*
import java.util.concurrent.TimeUnit

class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        auth = Firebase.auth

        val VerifyButton = findViewById<Button>(R.id.buttonVerify)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
        val inputCode1 = findViewById<EditText>(R.id.inputCode1)
        val inputCode2 = findViewById<EditText>(R.id.inputCode2)
        val inputCode3 = findViewById<EditText>(R.id.inputCode3)
        val inputCode4 = findViewById<EditText>(R.id.inputCode4)
        val inputCode5 = findViewById<EditText>(R.id.inputCode5)
        val inputCode6 = findViewById<EditText>(R.id.inputCode6)

        val storedVerificationId = intent.getStringExtra("storedVerificationId")
        VerifyButton.isEnabled = false

        VerifyButton.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
            if (code.length == 6) {
                VerifyButton.isEnabled = true
                return@OnKeyListener false
            }else{
                VerifyButton.isEnabled = false
                return@OnKeyListener false
            }

        })

        VerifyButton.setOnClickListener(){
            progressBar2.visibility = View.VISIBLE
            buttonVerify.visibility = View.GONE
            val code : String = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString() + inputCode6.getText().toString()
            verifyPhoneNumberWithCode(storedVerificationId, code)
        }
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
        // [END verify_with_code]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //intent to change page
                    val phone = auth.currentUser?.phoneNumber
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Login Successful $phone", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    progressBar2.visibility = View.GONE
                    buttonVerify.visibility = View.VISIBLE
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show()
                        progressBar2.visibility = View.GONE
                        buttonVerify.visibility = View.VISIBLE
                    }
                    // Update UI
                }
            }
    }
    companion object {
        private const val TAG = "SendOTPActivity"
    }
}