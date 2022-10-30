package com.lc.roomie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lc.roomie.databinding.FragmentLandlordHomeBinding
import kotlinx.android.synthetic.main.fragment_landlord_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandlordHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandlordHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var user: FirebaseUser? = Firebase.auth.currentUser
    private var db = Firebase.firestore
    val userDocRef = db.collection("users").document(user?.uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        userDocRef.get().addOnSuccessListener { document ->
            if(document != null ) {
                if (document.data?.get("listings") != null) {
//                    not_found.visibility = View.GONE
                    val listings = document.data?.get("listings") as ArrayList<String>
                    for (listing in listings) {
                        val listingDocRef = db.collection("listings").document(listing)
                        listingDocRef.get().addOnSuccessListener { document ->
                            if (document != null) {
                                val listing = document.data
                            }
                        }
                    }
                }else{
//                    not_found.visibility = View.VISIBLE
                }
            }else{
                println("No such document")
            }
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_landlord_home, container, false) as View
        var addListing = view.findViewById<ExtendedFloatingActionButton>(R.id.add_listing)
        addListing.setOnClickListener {
            val intent = Intent(activity, AddListingActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LandlordHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandlordHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}