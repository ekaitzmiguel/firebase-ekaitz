package com.example.firebasefroga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firebasefroga.ui.paintailak.OngiEtorriActivity
import com.example.firebasefroga.ui.theme.FireBaseFrogaTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // Authentication erabiltzeko
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        setContent {
            FireBaseFrogaTheme {
                LoginScreen(auth)
            }
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            singleLine = true,
            label = { Text("Pasahitza") }
        )

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            Toast.makeText(context, "Authentication successful.", Toast.LENGTH_SHORT).show()


                            val intent = Intent(context, OngiEtorriActivity::class.java)
                            intent.putExtra("user", email)
                            context.startActivity(intent)
                        } else {

                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Text("Saioa hasi")
        }
    }
}
