package com.example.mobizstandrmid_termgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGreetings()
    }

    fun initGreetings() {
        val mPrefs = getSharedPreferences("label", 0)
        val mString = mPrefs.getString("GreetingsMessage", "Hi, This is Your default greetings message! :)")
        Toast.makeText(this, mString, Toast.LENGTH_SHORT).show()
    }

    fun launchGame(v: View) {
        val i = Intent(this, GameActivity::class.java).putExtra(GameActivity.GAME_MODE, "PvP")
        startActivity(i)
    }

    fun launchGameVsBot(v: View) {
        val i = Intent(this, GameActivity::class.java).putExtra(GameActivity.GAME_MODE, "PvB")
        startActivity(i)
    }

    fun launchOptions(v: View) {
        val i = Intent(this, OptionsActivity::class.java)
        startActivity(i)
    }

}