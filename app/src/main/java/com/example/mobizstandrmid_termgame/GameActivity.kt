package com.example.mobizstandrmid_termgame

import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.DrawableUtils
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    val fieldSize = 2;
    var playField = Array<Array<Int>>(3){ Array(3){-10} }
    var currentPlayer = "Player 1"
    var buttonBckRound: Drawable? = null;
    var buttonBckCross: Drawable? = null;
    var winMessage = "WON";
    var GAME_MODE = ""

    companion object {
        const val GAME_MODE = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initBg()
        GAME_MODE = intent.getStringExtra(GAME_MODE).toString()
    }

    fun onTurnMade(v: View) {
        val btn = findViewById<AppCompatButton>(v.id)
        btn.isEnabled = false
        placeSymbol(btn)
        updateField(btn)
        if (checkForWin() != 0) {
            displayWinMessage()
            disableField()
            showPlayAgainButton()
        }
        else {
            switchPlayer()
            if (GAME_MODE == "PvB") {
                botMakesTurn()
                if (checkForWin() != 0) {
                    displayWinMessage()
                    disableField()
                    showPlayAgainButton()
                }
                switchPlayer()
            }
        }
    }

    fun placeSymbol(bt: AppCompatButton) {
        if (currentPlayer == "Player 1") {
            bt.background = buttonBckCross
        }
        else if (currentPlayer == "Player 2") {
            bt.background = buttonBckRound
        }
    }

    fun switchPlayer() {
        if (GAME_MODE == "PvP") {
            if (currentPlayer == "Player 1") {
                currentPlayer = "Player 2"
            } else if (currentPlayer == "Player 2") {
                currentPlayer = "Player 1"
            }
        }
        else if (GAME_MODE == "PvB") {
            if (currentPlayer == "Player 1") {
                currentPlayer = "Bot"
            } else if (currentPlayer == "Bot") {
                currentPlayer = "Player 1"
            }
        }
    }

    fun updateField(bt: AppCompatButton) {
        Log.d("123", bt.id.toString())
        if (currentPlayer == "Player 1") {
            when (bt.id) {
                R.id.topLeft -> playField[0][0] = 1
                R.id.topMid -> playField[0][1] = 1
                R.id.topRight -> playField[0][2] = 1
                R.id.midLeft -> playField[1][0] = 1
                R.id.midMid -> playField[1][1] = 1
                R.id.midRight -> playField[1][2] = 1
                R.id.botLeft -> playField[2][0] = 1
                R.id.botMid -> playField[2][1] = 1
                R.id.botRight -> playField[2][2] = 1
            }
        }
        else if (currentPlayer == "Player 2") {
            when (bt.id) {
                R.id.topLeft -> playField[0][0] = 2
                R.id.topMid -> playField[0][1] = 2
                R.id.topRight -> playField[0][2] = 2
                R.id.midLeft -> playField[1][0] = 2
                R.id.midMid -> playField[1][1] = 2
                R.id.midRight -> playField[1][2] = 2
                R.id.botLeft -> playField[2][0] = 2
                R.id.botMid -> playField[2][1] = 2
                R.id.botRight -> playField[2][2] = 2
            }
        }

    }

    fun printField() {
        for(i in 0..fieldSize) {
            var prStr = ""
            for (j in 0..fieldSize) {
                prStr += playField[i][j].toString() + " ";
            }
            Log.d("print",  prStr+"\n")
        }
    }

    fun checkForWin(): Int {
        //row check
         var sum = 0
         var avg = 0.0

         for (i in playField) {
            if (i.average() == 1.0 || i.average() == 2.0) {
                Log.d("win", "row win")
                return 1
            }
         }
         //column check
         for (i in 0..fieldSize) {
             sum = 0
             avg = 0.0
             for (j in 0..fieldSize) {
                 sum += playField[j][i]
             }
             avg = sum.toDouble() / (fieldSize+1)
             if (avg == 1.0 || avg == 2.0) {
                 Log.d("win", "column win")
                 Log.d("win", avg.toString())
                 return 1
             }
         }
         //1 diagonal check
         sum = 0
         avg = 0.0
         for (i in 0..fieldSize) {
             for (j in 0..fieldSize) {
                 if (i == j) {
                     sum += playField[i][j]
                 }
             }
         }
         avg = sum.toDouble() / (fieldSize+1)
         if (avg == 1.0 || avg == 2.0) {
             Log.d("win", "1 diagonal win")
             return 1
         }
         //2 diagonal check
         sum = 0
         avg = 0.0
         for (i in 0..fieldSize) {
             for (j in 0..fieldSize) {
                 /*Log.d("win", "2 diagonal")
                 Log.d("win", i.toString() + " " + j.toString())*/
                 if (i + j == fieldSize) {
                     sum += playField[i][j]
                 }
             }
         }
         avg = sum.toDouble() / (fieldSize+1)
         if (avg == 1.0 || avg == 2.0) {
             Log.d("win", "2 diagonal win")
             return 1
         }

        //check for draw
        if (playField.all{ it.all { it != -10 }}) {
            return 2
        }


         return 0
    }

    fun displayWinMessage() {
        val mTVid = resources.getIdentifier("messageTextView","id", packageName)
        val mTV = findViewById<TextView>(mTVid)

        if (checkForWin()==2) {
            mTV.text = "DRAW!"
        }
        else {
            if (currentPlayer == "Player 1") {
                //winMessage = "X WON!"
                mTV.setCompoundDrawables(buttonBckCross, null, null, null)
            } else if (currentPlayer == "Player 2" || currentPlayer == "Bot") {
                //winMessage = "O WON!"
                mTV.setCompoundDrawables(buttonBckRound, null, null, null)
            }
        }

        mTV.visibility = View.VISIBLE
    }

    fun disableField () {
        var bt: AppCompatButton? = null
        bt = findViewById(resources.getIdentifier("topLeft", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("topMid", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("topRight", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("midLeft", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("midMid", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("midRight", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("botLeft", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("botMid", "id", packageName))
        bt.isEnabled = false;
        bt = findViewById(resources.getIdentifier("botRight", "id", packageName))
        bt.isEnabled = false;
    }

    fun botMakesTurn() {
        var possibleTurns: List<Pair<Int,Int>> = listOf()
        for (i in 0..fieldSize) {
            for (j in 0..fieldSize) {
                if (playField[i][j] == -10) {
                    possibleTurns += Pair(i,j)
                }
            }
        }

        for (i in possibleTurns) {
            Log.d("bot", i.toString())
        }


        val nextTurn = Random.nextInt(possibleTurns.count());
        val nexTurnCoords = possibleTurns[nextTurn]
        val x = nexTurnCoords.first
        val y = nexTurnCoords.second
        playField[x][y] = 2
        var buttonName = ""
        when (x) {
            0 -> when(y) {
                0 -> buttonName = "topLeft"
                1 -> buttonName = "topMid"
                2 -> buttonName = "topRight"
            }
            1 -> when(y) {
                0 -> buttonName = "midLeft"
                1 -> buttonName = "midMid"
                2 -> buttonName = "midRight"
            }
            2 -> when(y) {
                0 -> buttonName = "botLeft"
                1 -> buttonName = "botMid"
                2 -> buttonName = "botRight"
            }
        }

        var bt = findViewById<AppCompatButton>(resources.getIdentifier(buttonName, "id", packageName))
        bt.background = buttonBckRound
        bt.isEnabled = false
        Log.d("bot", buttonName)
    }

    fun showPlayAgainButton() {
        var bt = findViewById<AppCompatButton>(resources.getIdentifier("playAgainButton", "id", packageName))
        bt.visibility = View.VISIBLE
    }

    fun initBg() {
        val bt = resources.getIdentifier("topLeft", "id", packageName)
        val but = findViewById<AppCompatButton>(bt)
        buttonBckRound = but.background
        but.setBackgroundColor(0)
        val bt2 = resources.getIdentifier("topMid", "id", packageName)
        val but2 = findViewById<AppCompatButton>(bt2)
        buttonBckCross = but2.background
        but2.setBackgroundColor(0)
    }

    fun restartGame(v: View) {
        recreate()
    }
}