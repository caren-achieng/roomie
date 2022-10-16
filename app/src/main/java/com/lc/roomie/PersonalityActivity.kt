package com.lc.roomie


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.generateViewId
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.toColor
import com.google.android.material.chip.Chip
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
//            db.collection("users").document("${intent.getStringExtra("docRef")}").update("traits", getTraits()).addOnSuccessListener {
//
//            }

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