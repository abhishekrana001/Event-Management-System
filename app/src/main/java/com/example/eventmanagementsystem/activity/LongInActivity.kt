package com.example.eventmanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementsystem.R
import com.example.eventmanagementsystem.data.User
import com.example.eventmanagementsystem.database.EventDatabase
import kotlinx.coroutines.launch

class LongInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_in) // ✅ FIXED

        val db = EventDatabase.getDatabase(this)
        val userDao = db.userDao()

        lifecycleScope.launch {
            val existingAdmin = userDao.getUserByEmail("admin@gmail.com")
            if (existingAdmin == null) {
                userDao.insert(
                    User(
                        name = "Admin",
                        email = "admin@gmail.com",
                        password = "1234",
                        role = "admin"
                    )
                )
            }
        }

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)


        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            lifecycleScope.launch {
                val user = userDao.login(email, password)

                if (user != null) {
                    if(user.role == "admin"){
                        val intent = Intent(this@LongInActivity, AdminActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@LongInActivity, "Admin Login Success", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@LongInActivity, UserActivity::class.java)
                        intent.putExtra("userId", user.id)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@LongInActivity, "User Login Success", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@LongInActivity, "Invalid Credentials", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}