package com.example.soundboard

import android.content.Context
import android.util.Patterns
import android.widget.Toast

/*
*  Helpful publicly available functions for all classes.
*  Mainly for consistent validators across activities
* */

object Util {

    public val NICKNAME_MAX_LENGTH : Int = 24

    //if email contains an @
    public fun isEmailValid(email :String) : Boolean{
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    //at least 8 characters with 1 special char and one digit
    public fun isPasswordValid(password : String): Boolean{
        if(password.isNotBlank()) {
            val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$"
            val matcher = Regex(pattern)
            return matcher.find(password) != null
        }else{
            return false
        }

    }

    //nickname is less than certain number characters
    public fun isNicknameValid(nickname : String): Boolean{
        if(nickname.isNotBlank()) {
            return nickname.length < NICKNAME_MAX_LENGTH
        }else{
            return false
        }

    }

    public fun showToast(context: Context, message :String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}