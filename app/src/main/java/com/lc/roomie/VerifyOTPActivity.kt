package com.lc.roomie


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_send_otp.*
import kotlinx.android.synthetic.main.activity_verify_otp.*
import java.util.concurrent.TimeUnit
import android.text.TextWatcher as TextWatcher


class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        auth = Firebase.auth

        val VerifyButton = findViewById<Button>(R.id.buttonVerify)
        val resendOTP = findViewById<TextView>(R.id.textResendOtp)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
        val inputCode1 = findViewById<EditText>(R.id.inputCode1)
        val inputCode2 = findViewById<EditText>(R.id.inputCode2)
        val inputCode3 = findViewById<EditText>(R.id.inputCode3)
        val inputCode4 = findViewById<EditText>(R.id.inputCode4)
        val inputCode5 = findViewById<EditText>(R.id.inputCode5)
        val inputCode6 = findViewById<EditText>(R.id.inputCode6)

        val phoneNumber = intent.getStringExtra("phoneNumber")

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(SendOTPActivity.TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(SendOTPActivity.TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(this@VerifyOTPActivity, "Invalid phone number. ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    SendOtp.visibility = View.VISIBLE

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(SendOTPActivity.TAG, "SMS quota for the project has been exceeded ${e.message}")
                    progressBar.visibility = View.GONE
                    SendOtp.visibility = View.VISIBLE
                }

                // Show a message and update the UI
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(SendOTPActivity.TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later

                storedVerificationId = verificationId
                Toast.makeText(this@VerifyOTPActivity, "Verification code sent to $phoneNumber", Toast.LENGTH_SHORT).show()
                resendOTP.isEnabled = true
                resendOTP.setTextColor(getColor(R.color.my_green))
            }
        }


        resendToken = intent.getStringExtra("resendToken") as PhoneAuthProvider.ForceResendingToken?

        val storedVerificationId = intent.getStringExtra("storedVerificationId")
        VerifyButton.isEnabled = false

//        inputCode1.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString().length == 1) {
//                    inputCode2.requestFocus()
//                }
//                else if(s.toString().length == 0) {
//                    return
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })
//        inputCode2.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString().length == 1) {
//                    inputCode3.requestFocus()
//                }
//                else if(s.toString().length == 0) {
//                    inputCode1.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })
//        inputCode3.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString().length == 1) {
//                    inputCode4.requestFocus()
//                }
//                else if(s.toString().length == 0) {
//                    inputCode2.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })
//        inputCode4.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString().length == 1) {
//                    inputCode5.requestFocus()
//                }
//                else if(s.toString().length == 0) {
//                    inputCode3.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })
//        inputCode5.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString().length == 1) {
//                    inputCode6.requestFocus()
//                }
//                else if(s.toString().length == 0) {
//                    inputCode4.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })
//        inputCode6.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if(s.toString().length == 0) {
//                    inputCode5.requestFocus()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
//                if (code.length == 6) {
//                    VerifyButton.isEnabled = true
//                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
//                    return
//                }else{
//                    VerifyButton.isEnabled = false
//                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
//                    return
//                }
//            }
//        })

        class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.inputCode1 && currentView.text.isEmpty()) {
                    //If current is empty then previous EditText's number will also be deleted
                    previousView!!.text = null
                    previousView.requestFocus()
                    return true
                }
                return false
            }


        }

        class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val text = editable.toString()
                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
                if (code.length == 6) {
                    VerifyButton.isEnabled = true
                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
                }else{
                    VerifyButton.isEnabled = false
                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
                }
                when (currentView.id) {
                    R.id.inputCode1 -> if (text.length == 1) nextView!!.requestFocus()
                    R.id.inputCode2 -> if (text.length == 1) nextView!!.requestFocus()
                    R.id.inputCode3 -> if (text.length == 1) nextView!!.requestFocus()
                    R.id.inputCode4 -> if (text.length == 1) nextView!!.requestFocus()
                    R.id.inputCode5 -> if (text.length == 1) nextView!!.requestFocus()
                    //You can use EditText4 same as above to hide the keyboard
                }
            }

        }

        inputCode1.addTextChangedListener(GenericTextWatcher(inputCode1, inputCode2))
        inputCode2.addTextChangedListener(GenericTextWatcher(inputCode2, inputCode3))
        inputCode3.addTextChangedListener(GenericTextWatcher(inputCode3, inputCode4))
        inputCode4.addTextChangedListener(GenericTextWatcher(inputCode4, inputCode5))
        inputCode5.addTextChangedListener(GenericTextWatcher(inputCode5, inputCode6))
        inputCode5.addTextChangedListener(GenericTextWatcher(inputCode6, null))


//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        inputCode1.setOnKeyListener(GenericKeyEvent(inputCode1, null))
        inputCode2.setOnKeyListener(GenericKeyEvent(inputCode2, inputCode1))
        inputCode3.setOnKeyListener(GenericKeyEvent(inputCode3, inputCode2))
        inputCode4.setOnKeyListener(GenericKeyEvent(inputCode4,inputCode3))
        inputCode5.setOnKeyListener(GenericKeyEvent(inputCode5,inputCode4))
        inputCode6.setOnKeyListener(GenericKeyEvent(inputCode6,inputCode5))



        VerifyButton.setOnClickListener(){
            progressBar2.visibility = View.VISIBLE
            buttonVerify.visibility = View.GONE
            val code : String = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString() + inputCode6.getText().toString()
            verifyPhoneNumberWithCode(storedVerificationId, code)
        }

        resendOTP.setOnClickListener(){
            if (phoneNumber != null) {
                resendOTP.isEnabled = false
                resendOTP.setTextColor(getColor(R.color.dark_grey))
                resendVerificationCode(phoneNumber, resendToken)
            }
        }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+254$phoneNumber")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
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
                    val user = task.result?.user
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Login Successful $phone", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerifyEmailActivity::class.java)
                    startActivity(intent)


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
        private const val TAG = "VerifyOTPActivity"
    }
}

