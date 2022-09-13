package com.lc.roomie


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


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


class SendOTPActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)

        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val phoneNumber: EditText = findViewById(R.id.inputMobile)
        val SendOTP: Button = findViewById(R.id.SendOtp)
        SendOTP.isEnabled = false


        phoneNumber.setOnKeyListener {
                v, keyCode, event ->
           if(phoneNumber.text.toString().length == 9){
               SendOTP.isEnabled = true
               SendOTP.background = getDrawable(R.drawable.enabled_button_background)
               return@setOnKeyListener false
           }else{
               SendOTP.isEnabled = false
               SendOTP.background = getDrawable(R.drawable.disabled_button_background)
               return@setOnKeyListener false
           }
        }

        auth = Firebase.auth
//        val ResendCode = findViewById<Button>(ResendCode)

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
                    Toast.makeText(this@SendOTPActivity, "Invalid phone number. ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    SendOtp.visibility = View.VISIBLE

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG, "SMS quota for the project has been exceeded ${e.message}")
                    progressBar.visibility = View.GONE
                    SendOtp.visibility = View.VISIBLE
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
                Toast.makeText(this@SendOTPActivity, "Verification Code Sent", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SendOTPActivity, VerifyOTPActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("phoneNumber", phoneNumber.text.toString())
                startActivity(intent)
            }
        }
        // [END phone_auth_callbacks]
        SendOTP.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            SendOtp.visibility = View.GONE
            val mobileNumber = phoneNumber.text.toString().trim()
            val pattern = "((?:(?:^(7|1)(?:(?:[01249][0-9])|(?:5[789])|(?:6[89])))|(?:1(?:[1][0-5])))[0-9]{6})\$".toRegex()
            if (!pattern.matches(mobileNumber)) {
                phoneNumber.error = "Enter a valid mobile"
                phoneNumber.requestFocus()
                progressBar.visibility = View.GONE
                SendOtp.visibility = View.VISIBLE
                SendOtp.isEnabled = false
                SendOTP.background = getDrawable(R.drawable.disabled_button_background)
                return@setOnClickListener
            }
            startPhoneNumberVerification(mobileNumber)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+254$phoneNumber")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

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
    companion object {
      private const val TAG = "SendOTPActivity"
    }
}