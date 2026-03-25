package com.example.eventmanagementsystem.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.User
import com.example.eventmanagementsystem.database.EventDatabase
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        val db = EventDatabase.getDatabase(this)
        val userDao = db.userDao()

        btnRegister.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            lifecycleScope.launch {

                val existingUser = userDao.getUserByEmail(email)

                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "Email already exists", Toast.LENGTH_SHORT).show()
                } else {
                    userDao.insert(
                        User(
                            name = name,
                            email = email,
                            password = password,
                            role = "user"
                        )
                    )

                    Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}