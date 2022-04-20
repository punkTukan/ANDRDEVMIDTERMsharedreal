package com.example.mobizstandrmid_termgame

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton


class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        initTexts()
    }

    fun initTexts() {
        val mPrefs = getSharedPreferences("label", 0)
        val mString = mPrefs.getString("PlayerName", "Player Name")
        findViewById<EditText>(R.id.editTextTextPersonName).setText(mString)
        val mString2 = mPrefs.getString("GreetingsMessage", "Hi, This is Your default greetings message! :)")
        findViewById<EditText>(R.id.editTextTextMultiLine2).setText(mString2)
    }

    fun save(v: View) {
        val mPrefs = getSharedPreferences("label", 0)
        val mEditor: SharedPreferences.Editor = mPrefs.edit()
        mEditor.putString("PlayerName", findViewById<EditText>(R.id.editTextTextPersonName).text.toString()).commit()
        mEditor.putString("GreetingsMessage", findViewById<EditText>(R.id.editTextTextMultiLine2).text.toString()).commit()
    }
}