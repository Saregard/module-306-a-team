package se.linerotech.quizkampen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CreateAccount : AppCompatActivity() {
    private var mySignUpButton: Button? = null
    private var myPassword: EditText? = null
    private var myEmail: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mySignUpButton = findViewById(R.id.buttonSignUp)
        myPassword = findViewById(R.id.editTextSignUpPassword)
        myEmail = findViewById(R.id.editTextSignUpEmail)

    }
}