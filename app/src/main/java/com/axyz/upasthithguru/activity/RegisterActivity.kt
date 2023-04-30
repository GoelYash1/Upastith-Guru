package com.axyz.upasthithguru.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.axyz.upasthithguru.R
import com.axyz.upasthithguru.apidata.SignupRequest
import com.axyz.upasthithguru.other.EventObserver
import com.axyz.upasthithguru.viewModels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
//    private lateinit var descriptionEditText: EditText
    private lateinit var signupViewModel: SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.input_username)
        emailEditText = findViewById(R.id.input_email)
        passwordEditText = findViewById(R.id.input_password)
        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        val alreadyhaveaccount = findViewById<TextView>(R.id.text_signIn)

        alreadyhaveaccount.setOnClickListener {
            navigateToLoginActivity()
        }

        val click_register = findViewById<Button>(R.id.btn_create)
        click_register.setOnClickListener {
            handleSignupFormSubmission()
        }

        nameEditText.setText("Yash")
        emailEditText.setText("ys@gmail.com")
        passwordEditText.setText("12345@Aa")

        subscribeToObservers()
    }


    private fun handleSignupFormSubmission() {
        // Get the values of the form fields

        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val description = "descriptionEditText.text.toString()"

        // Validate the form fields
        if (name.isEmpty()) {
            // Display an error message if the name field is empty
            nameEditText.error = "Please enter your name"
            return
        }
        if (email.isEmpty()) {
            // Display an error message if the email field is empty
            Toast.makeText(this,"Please enter your email", Toast.LENGTH_SHORT).show()
            emailEditText.error = "Please enter your email"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Display an error message if the email is not a valid email address
            Toast.makeText(this,"Please enter a valid email", Toast.LENGTH_SHORT).show()
            emailEditText.error = "Please enter a valid email"
            return
        }
        if (password.isEmpty()) {
            // Display an error message if the password field is empty
            Toast.makeText(this,"Please enter a password", Toast.LENGTH_SHORT).show()
            passwordEditText.error = "Please enter a password"
            return
        }
        if (!isValidPassword(password)) {
            Toast.makeText(this,"Password must include 1 number,1 Capital, 1 small & 1 special Character",
                Toast.LENGTH_SHORT).show()
            passwordEditText.error = "Password must include 1 number,1 Capital, 1 small & 1 special Character"
            return
        }

        // Clear any error messages
        nameEditText.error = null
        emailEditText.error = null
        passwordEditText.error = null
        Log.d("Signup Submit ", "Signup $name $email $password")
        val signupUser = SignupRequest( name,email,password,description )
        signupViewModel.signup(email,password)
    }

    private fun subscribeToObservers() {
        signupViewModel.signupStatus.observe(this, EventObserver(
            onError = {
                if(it.contains("Email already in use Loggin you In")){
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }else{
//                binding.name.isVisible = false
//                binding.email.isVisible = false
//                binding.password.isVisible = false
//                binding.description.isVisible = false
//                binding.signupButton.isVisible = false
//                binding.progressBar2.isVisible = false
//                binding.error.isVisible = true
                Log.d("LoginActivity", "Error: $it")
                }
            },
            onLoading = {

//                binding.name.isVisible = false
//                binding.email.isVisible = false
//                binding.password.isVisible = false
//                binding.description.isVisible = false
//                binding.signupButton.isVisible = false
//                binding.progressBar2.isVisible = true
                Log.d("LoginActivity", "Loading")
            }
        ) { user ->
//            binding.name.isVisible = false
//            binding.email.isVisible = false
//            binding.password.isVisible = false
//            binding.description.isVisible = false
//            binding.signupButton.isVisible = false
//            binding.progressBar2.isVisible = false
            Log.d("LoginActivity", "Success: $user")
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        })
    }

    private fun navigateToLoginActivity(){
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }
}


fun isValidPassword(password: String): Boolean {
//    val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
    val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
    val regex = Regex(pattern)
    return password.matches(regex)
}