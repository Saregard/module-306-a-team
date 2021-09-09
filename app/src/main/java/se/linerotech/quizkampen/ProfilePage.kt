 package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

 class ProfilePage : AppCompatActivity() {
    val db = Firebase.firestore
     val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        db.collection("users")
            .add(auth.currentUser!!.uid)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }
}