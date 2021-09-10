 package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

 class ProfilePage : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        db.collection("users")
            .add("1" to 1)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Susses data base", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->

            }
    }
}