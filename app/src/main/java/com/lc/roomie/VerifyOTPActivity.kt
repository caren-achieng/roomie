package com.lc.roomie


import SMSReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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
    //Used to read sms to get OPT code
    private var intentFilter: IntentFilter? = null
    private var smsReceiver: SMSReceiver? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = Firebase.auth
        //Initialize SMS listener
        initSmsListener()
        initBroadCast()

        val VerifyButton = findViewById<Button>(R.id.buttonVerify)
        val resendOTP = findViewById<TextView>(R.id.textResendOtp)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
        val inputCode1 = findViewById<EditText>(R.id.inputCode1)
        val inputCode2 = findViewById<EditText>(R.id.inputCode2)
        val inputCode3 = findViewById<EditText>(R.id.inputCode3)
        val inputCode4 = findViewById<EditText>(R.id.inputCode4)
        val inputCode5 = findViewById<EditText>(R.id.inputCode5)
        val inputCode6 = findViewById<EditText>(R.id.inputCode6)
        val textMobile = findViewById<TextView>(R.id.textMobile)

        textMobile.setText("+254"+intent.getStringExtra("phoneNumber").toString())

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
//        VerifyButton.isEnabled = false

        class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                val code = inputCode1.text.toString() + inputCode2.text.toString() + inputCode3.text.toString() + inputCode4.text.toString() + inputCode5.text.toString() + inputCode6.text.toString()
                if (code.length == 6) {
                    VerifyButton.isEnabled = true
                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
                }else{
                    VerifyButton.isEnabled = false
                    VerifyButton.background = getDrawable(R.drawable.disabled_button_background)
                }
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
//                    VerifyButton.isEnabled = true
                    VerifyButton.background = getDrawable(R.drawable.enabled_button_background)
                }else{
//                    VerifyButton.isEnabled = false
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
            //Comment this out
//            progressBar2.visibility = View.VISIBLE
//            buttonVerify.visibility = View.GONE
//            val code : String = inputCode1.getText().toString() + inputCode2.getText().toString() + inputCode3.getText().toString() + inputCode4.getText().toString() + inputCode5.getText().toString() + inputCode6.getText().toString()
//            verifyPhoneNumberWithCode(storedVerificationId, code)
            val intent = Intent(this, VerifyEmailActivity::class.java)
            startActivity(intent)
            finish()
        }

        resendOTP.setOnClickListener(){
            if (phoneNumber != null) {
                resendOTP.isEnabled = false
                resendOTP.setTextColor(getColor(R.color.dark_grey))
                resendVerificationCode(phoneNumber, resendToken)
            }
        }
    }


    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsReceiver = SMSReceiver()
        smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                showToast("OTP Received: $otp")
            }
        })
    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(this@VerifyOTPActivity)
        client.startSmsRetriever()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(smsReceiver, intentFilter)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver = null
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
                    val data = hashMapOf(
                        "uid" to auth.currentUser?.uid,
                        "phone" to phone
                    )

                    val db = Firebase.firestore
                    var reference: String? = null
                    db.collection("users")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            reference = documentReference.id
                            Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                            Log.d(TAG, "signInWithCredential:success")
                            Toast.makeText(this, "Login Successful $phone", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, VerifyEmailActivity::class.java)
                            intent.putExtra("docRef", reference.toString())
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error adding document", e)
                        }



                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Invalid Verification Code", Toast.LENGTH_SHORT).show()
                    }
                    progressBar2.visibility = View.GONE
                    buttonVerify.visibility = View.VISIBLE
                    // Update UI
                }
            }
    }
    companion object {
        private const val TAG = "VerifyOTPActivity"
    }
}

