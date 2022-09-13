package com.lc.roomie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

class PhoneAuthActivity : Activity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phoneNumber = findViewById<EditText>(R.id.inputMobile)
        val SendOTP = findViewById<Button>(R.id.SendOtp)
//      val ResendCode = findViewById<Button>(ResendCode)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)

        val VerifyButton = findViewById<Button>(R.id.buttonVerify)

        val inputCode1 = findViewById<EditText>(R.id.inputCode1)
        val inputCode2 = findViewById<EditText>(R.id.inputCode2)
        val inputCode3 = findViewById<EditText>(R.id.inputCode3)
        val inputCode4 = findViewById<EditText>(R.id.inputCode4)
        val inputCode5 = findViewById<EditText>(R.id.inputCode5)
        val inputCode6 = findViewById<EditText>(R.id.inputCode6)


        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(this@PhoneAuthActivity, "Invalid phone number. ${e.message}", Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG, "SMS quota for the project has been exceeded ${e.message}")
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(this@PhoneAuthActivity, "Verification Code Sent", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@PhoneAuthActivity, VerifyOTPActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("phoneNumber", phoneNumber.text.toString())
                startActivity(intent)
            }
        }
        // [END phone_auth_callbacks]
        SendOTP.setOnClickListener() {
            val mobileNumber = phoneNumber.text.toString().trim()
            val pattern = "((?:(?:^(7|1)(?:(?:[01249][0-9])|(?:5[789])|(?:6[89])))|(?:1(?:[1][0-5])))[0-9]{6})\$".toRegex()
            if(mobileNumber.isEmpty()){
                phoneNumber.error = "Phone Number is required"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }
            if (mobileNumber.length < 9 || mobileNumber.length > 9 || !pattern.matches(mobileNumber)) {
                phoneNumber.error = "Kindly enter a valid phone number"
                phoneNumber.requestFocus()
                return@setOnClickListener
            }
            startPhoneNumberVerification(mobileNumber)
        }

//        ResendCode.setOnclickListener() {
//          val mobileNumber = phoneNumber.text.toString().trim()
//            val pattern = "((?:(?:^(7|1)(?:(?:[01249][0-9])|(?:5[789])|(?:6[89])))|(?:1(?:[1][0-5])))[0-9]{6})\$".toRegex()
//            if(mobileNumber.isEmpty()){
//                phoneNumber.error = "Phone Number is required"
//                phoneNumber.requestFocus()
//                return@setOnClickListener
//            }
//            if (mobileNumber.length < 9 || mobileNumber.length > 9 || !pattern.matches(mobileNumber)) {
//                phoneNumber.error = "Kindly enter a valid phone number"
//                phoneNumber.requestFocus()
//                return@setOnClickListener
//            }
//            resendVerificationCode(mobileNumber, resendToken)
//        }

        VerifyButton.setOnClickListener(){
            if(inputCode1.getText().toString().trim().isEmpty() || inputCode2.getText().toString().trim().isEmpty() || inputCode3.getText().toString().trim().isEmpty() || inputCode4.getText().toString().trim().isEmpty() || inputCode5.getText().toString().trim().isEmpty() || inputCode6.getText().toString().trim().isEmpty()){
                Toast.makeText(this@PhoneAuthActivity, "Please enter valid code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val code : String = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString() + inputCode6.getText().toString()
            verifyPhoneNumberWithCode(storedVerificationId, code)
        }
    }

    // [START on_start_check_user]
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
    // [END on_start_check_user]

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        progressBar.visibility = View.VISIBLE
        SendOtp.visibility = View.GONE
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        progressBar2.visibility = View.VISIBLE
        buttonVerify.visibility = View.GONE
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
        // [END verify_with_code]
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
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
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

    }

    companion object {
        const val TAG = "PhoneAuthActivity"
    }
}