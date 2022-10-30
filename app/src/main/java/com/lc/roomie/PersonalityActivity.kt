package com.lc.roomie


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.generateViewId
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.toColor
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_personality.*



class PersonalityActivity : AppCompatActivity() {

    private val personalities = arrayOf(
        "Student",
        "Early Bird",
        "Night Owl",
        "Sports",
        "Gym Rat",
        "Pets",
        "Full Time Worker",
        "Going Out",
        "Gamer",
        "Clean",
        "Organized",
        "Handyman",
        "Non Smoker",
        "Foodie",
        "Musician",
        "Creative",
        "Introvert",
        "Extrovert"
    )

    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personality)




        val db = Firebase.firestore

        for(personality in personalities){
            val chip = layoutInflater.inflate(R.layout.personality_chip, chipGroup, false) as Chip
            chip.text = personality
            chip.id = generateViewId()
            chipGroup.addView(chip)
        }

        continueButton.setOnClickListener{
            db.collection("users").document(user?.uid.toString()).update("traits", getTraits()).addOnSuccessListener {
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LandlordMainActivity::class.java)
                startActivity(intent)
            }
//            val intent = Intent(this, LandlordMainActivity::class.java)
//            startActivity(intent)

        }

    }

    fun getTraits(): MutableList<String> {
       val checkedTraits = chipGroup.checkedChipIds
        val traits = ArrayList<String>()
        for(checkedTrait in checkedTraits){
            val trait = findViewById<Chip>(checkedTrait).text
            traits.add(trait.toString())
        }
        return traits
    }

    companion object {
        private const val TAG = "PersonalityActivity"
    }

}