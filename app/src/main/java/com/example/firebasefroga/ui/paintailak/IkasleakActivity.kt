package com.example.firebasefroga.ui.paintailak

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebasefroga.data.Ikaslea
import com.example.firebasefroga.ui.theme.FireBaseFrogaTheme
import com.google.firebase.firestore.FirebaseFirestore

class IkasleakActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FireBaseFrogaTheme {
                IkasleakScreen()
            }
        }
    }
}

@Composable
fun IkasleakScreen() {

    val activity = LocalActivity.current
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val purpleColor = Color(0xFF5369A0)

    var nan by remember { mutableStateOf("") }
    var izena by remember { mutableStateOf("") }
    var abizena by remember { mutableStateOf("") }

    var text by remember {
        mutableStateOf("Oraindik ez dago daturik")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(purpleColor)
                .padding(
                    top = 48.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Text(
                text = "IKASLEAK",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nan,
                onValueChange = { nan = it },
                singleLine = true,
                label = { Text("NANa") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = izena,
                onValueChange = { izena = it },
                singleLine = true,
                label = { Text("Izena") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = abizena,
                onValueChange = { abizena = it },
                singleLine = true,
                label = { Text("Abizena") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    val ikaslea = Ikaslea(
                        nan = nan,
                        izena = izena,
                        abizena = abizena
                    )

                    db.collection("ikaslea")
                        .add(ikaslea)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(
                                context,
                                "Dokumentua ondo gorde da. ID: ${documentReference.id}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Errorea dokumentua gehitzean",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(40.dp)
            ) {
                Text("Gorde")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    db.collection("ikaslea")
                        .whereEqualTo("nan", nan)
                        .get()
                        .addOnSuccessListener { documents ->

                            if (!documents.isEmpty) {

                                val document = documents.documents.first()

                                document.reference.delete()
                                    .addOnSuccessListener {

                                    }
                            }
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(40.dp)
            ) {
                Text("Ezabatu")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    val eguneraketa = hashMapOf<String, Any>(
                        "izena" to izena,
                        "abizena" to abizena
                    )

                    db.collection("ikaslea")
                        .whereEqualTo("nan", nan)
                        .get()
                        .addOnSuccessListener { documents ->

                            if (!documents.isEmpty) {

                                val document = documents.documents.first()

                                document.reference.update(eguneraketa)
                                    .addOnSuccessListener {

                                        Toast.makeText(
                                            context,
                                            "Ikaslea ondo eguneratu da",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {

                                        Toast.makeText(
                                            context,
                                            "Errorea ikaslea eguneratzean",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                            } else {

                                Toast.makeText(
                                    context,
                                    "Ez da ikaslea aurkitu",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                context,
                                "Errorea ikaslea bilatzean",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(40.dp)
            ) {
                Text("Update")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    db.collection("ikaslea")
                        .get()
                        .addOnSuccessListener { ikasleak ->

                            var datuak = ""

                            for (ikaslea in ikasleak) {
                                datuak += "${ikaslea.id}: ${ikaslea.data}\n"
                            }

                            text = if (datuak.isEmpty()) {
                                "Ez dago dokumenturik"
                            } else {
                                datuak
                            }
                        }
                        .addOnFailureListener { e ->
                            text = "Errorea: ${e.message}"
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(40.dp)
            ) {
                Text("Erakutsi")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(40.dp)
            ) {
                Text("Itzuli")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}