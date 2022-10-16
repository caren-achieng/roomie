package com.lc.roomie


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_complete_profile.*
import java.util.*
import kotlin.collections.HashMap


class CompleteProfileActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val storage = Firebase.storage

    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null

    fun pickPhoto(view: View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            pickedPhoto = data.data
            var currentUser = FirebaseAuth.getInstance().getCurrentUser();
            val uploadTask = storage.reference.child("images/users/"+ currentUser?.uid.toString()).child("/profile.jpg").putFile(pickedPhoto!!)
            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                uploadTask.getResult().storage.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val user = hashMapOf(
                        "profilePhoto" to downloadUrl
                    )
                    db.collection("users").document("${intent.getStringExtra("docRef")}").update(user as Map<String, Any>)
                }
            }.addOnFailureListener(){
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }

            if(pickedPhoto != null){
                if(VERSION.SDK_INT >= 28)
                {
                    val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                    pickedBitMap = ImageDecoder.decodeBitmap(source)
                    iv_profile.setImageBitmap(pickedBitMap)
                }
                else
                {
                    pickedBitMap = getBitmap(this.contentResolver, pickedPhoto)
                    iv_profile.setImageBitmap(pickedBitMap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)




        birthday.calendarViewShown = false
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        birthday.setMaxDate(calendar.getTimeInMillis())

        val first_name = findViewById<TextInputEditText>(R.id.first_name)

        first_name.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val fname = first_name.text.toString()
                val pattern = "^[A-Za-z'][A-Za-z']{0,}\$".toRegex()
                if(fname.length < 3){
                    first_name.error = "First name must be at least 3 characters long"
                }
                if(!pattern.matches(fname)){
                    first_name.error = "First name must contain letters"
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val last_name = findViewById<TextInputEditText>(R.id.last_name)
        last_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val lname = last_name.text.toString()
                val pattern = "^[A-Za-z'][A-Za-z']{0,}\$".toRegex()
                if(lname.length < 3){
                    last_name.error = "First name must be at least 3 characters long"
                }
                if(!pattern.matches(lname)){
                    last_name.error = "First name must only contain letters"

                }
            }
        })

        change_profile_picture.setOnClickListener(){
            pickPhoto(it)
        }

        continueProfile.setOnClickListener{
//            if(first_name.text.toString() == ""){
//                first_name.error = "First name is required"
//                first_name.requestFocus()
//                return@setOnClickListener
//            }
//            if(last_name.text.toString() == ""){
//                last_name.error = "First name is required"
//                last_name.requestFocus()
//                return@setOnClickListener
//            }
//            if(radioGenders.checkedRadioButtonId == -1){
//                Toast.makeText(this, "Choose your gender", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val data = hashMapOf<String, String>(
//                "first_name" to first_name.text.toString(),
//                "last_name" to last_name.text.toString(),
//                "birthday" to birthday.dayOfMonth.toString()+"/"+birthday.month.toString() +"/"+birthday.year.toString(),
//                "gender" to findViewById<RadioButton>(radioGenders.checkedRadioButtonId).text.toString()
//                )
//
//            db.collection("users").document("${intent.getStringExtra("docRef")}").update(data as Map<String, Any>).addOnSuccessListener {
//                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
//                val reference = intent.getStringExtra("docRef")
//                val intent = Intent(this, PersonalityActivity::class.java)
//                intent.putExtra("docRef", reference)
//                startActivity(intent)
//            }.addOnFailureListener {
//                Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
//                Log.d("TAG", "onCreate: ${it.message}")
//
//            }
            val intent = Intent(this, PersonalityActivity::class.java)
            intent.putExtra("docRef", intent.getStringExtra("docRef"))
            startActivity(intent)

        }

    }

    fun onRadioButtonClicked(view: View) {}

    companion object{
        private const val TAG = "CompleteProfileActivity"
    }
}
