package com.example.myapplication.thirdTab

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.util.*
import kotlin.collections.ArrayList

class GameFragment : Fragment() {

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    var activeplayer = 1

    var roundCount = 0
    var player1point = 0
    var player2point = 0

    var gametype = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val btn1 = view.findViewById<View>(R.id.button_00) as Button
        btn1.setOnClickListener {
            playGame(1, btn1, view)
        }
        val btn2 = view.findViewById<View>(R.id.button_01) as Button
        btn2.setOnClickListener {
            playGame(2, btn2, view)
        }
        val btn3 = view.findViewById<View>(R.id.button_02) as Button
        btn3.setOnClickListener {
            playGame(3, btn3, view)
        }
        val btn4 = view.findViewById<View>(R.id.button_10) as Button
        btn4.setOnClickListener {
            playGame(4, btn4, view)
        }
        val btn5 = view.findViewById<View>(R.id.button_11) as Button
        btn5.setOnClickListener {
            playGame(5, btn5, view)
        }
        val btn6 = view.findViewById<View>(R.id.button_12) as Button
        btn6.setOnClickListener {
            playGame(6, btn6, view)
        }
        val btn7 = view.findViewById<View>(R.id.button_20) as Button
        btn7.setOnClickListener {
            playGame(7, btn7, view)
        }
        val btn8 = view.findViewById<View>(R.id.button_21) as Button
        btn8.setOnClickListener {
            playGame(8, btn8, view)
        }
        val btn9 = view.findViewById<View>(R.id.button_22) as Button
        btn9.setOnClickListener {
            playGame(9, btn9, view)
        }
        val btn10 = view.findViewById<View>(R.id.button_reset) as Button
        btn10.setOnClickListener {
            resetBoard(view)
            player2point = 0
            player1point = 0
            if(gametype == 0) {
                view.text_view_p1.setText("User: " + player1point)
                view.text_view_p2.setText("Computer: " + player2point)
            } else {
                view.text_view_p1.setText("Player 1: " + player1point)
                view.text_view_p2.setText("Player 2: " + player2point)
            }
        }
        val btnAI = view.findViewById<View>(R.id.button_AI) as Button
        btnAI.setOnClickListener {
            resetBoard(view)
            player1point = 0
            player2point = 0
            gametype = 0
            view.text_view_p1.setText("User: " + player1point)
            view.text_view_p2.setText("Computer: " + player2point)
        }
        val btnUser = view.findViewById<View>(R.id.button_Users) as Button
        btnUser.setOnClickListener {
            resetBoard(view)
            player1point = 0
            player2point = 0
            gametype = 1
            view.text_view_p1.setText("Player 1: " + player1point)
            view.text_view_p2.setText("Player 2: " + player2point)
        }
        return view
    }


    private fun playGame(cellID: Int, buSelected: Button, view: View) {

        if (activeplayer == 1) {
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.parseColor("#92CDED")) // @color/sky_blue
            player1.add(cellID)
            activeplayer = 2

            buSelected.isEnabled = false
            roundCount++
            checkWinner(view)

        } else {

            buSelected.text = "O"
            buSelected.setBackgroundColor(Color.parseColor("#F1C400")) // @color/sun_flower
            player2.add(cellID)
            activeplayer = 1

            buSelected.isEnabled = false
            roundCount++
            checkWinner(view)

        }
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            if (activeplayer == 2) {
//                AutoPlay(view)
//            }
//        }, 1000)
        if(gametype == 0) {
            if (activeplayer == 2) {
                AutoPlay(view)
            }
        }
    }


    private fun checkWinner(view: View) {

        var winner = -1
        //rows
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }

        //columns
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }

        if(winner != -1) {

            if(winner == 1) {
                player1point++
                if(gametype == 0) Toast.makeText(activity, "Player 1 won the game.", Toast.LENGTH_LONG).show()
                else Toast.makeText(activity, "User won the game.", Toast.LENGTH_LONG).show()
                updatePointsText(winner, view)
                resetBoard(view);
            } else {
                player2point++
                if(gametype == 0) Toast.makeText(activity, "Computer won the game.", Toast.LENGTH_LONG).show()
                else Toast.makeText(activity, "Player 2 won the game.", Toast.LENGTH_LONG).show()
                updatePointsText(winner, view)
                resetBoard(view)
            }
        } else if(roundCount == 9) {
            Toast.makeText(activity, "Draw game.", Toast.LENGTH_LONG).show()
            resetBoard(view)
        }
    }

    private fun updatePointsText(winner : Int, view: View) {
        if(winner == 1) {
            if(gametype == 0) {
                view.text_view_p1.setText("User: " + player1point)
            } else {
                view.text_view_p1.setText("Player 1: " + player1point)
            }
        } else {
            if(gametype == 0) {
                view.text_view_p2.setText("Computer: " + player2point)
            } else {
                view.text_view_p2.setText("Player 2: " + player2point)
            }
        }
    }

    private fun resetBoard(view: View) {
        view.button_00.setText("")
        view.button_00.setBackgroundColor(Color.parseColor("#676262"))
        view.button_00.isEnabled = true
        view.button_01.setText("")
        view.button_01.setBackgroundColor(Color.parseColor("#676262"))
        view.button_01.isEnabled = true
        view.button_02.setText("")
        view.button_02.setBackgroundColor(Color.parseColor("#676262"))
        view.button_02.isEnabled = true
        view.button_10.setText("")
        view.button_10.setBackgroundColor(Color.parseColor("#676262"))
        view.button_10.isEnabled = true
        view.button_11.setText("")
        view.button_11.setBackgroundColor(Color.parseColor("#676262"))
        view.button_11.isEnabled = true
        view.button_12.setText("")
        view.button_12.setBackgroundColor(Color.parseColor("#676262"))
        view.button_12.isEnabled = true
        view.button_20.setText("")
        view.button_20.setBackgroundColor(Color.parseColor("#676262"))
        view.button_20.isEnabled = true
        view.button_21.setText("")
        view.button_21.setBackgroundColor(Color.parseColor("#676262"))
        view.button_21.isEnabled = true
        view.button_22.setText("")
        view.button_22.setBackgroundColor(Color.parseColor("#676262"))
        view.button_22.isEnabled = true

        roundCount = 0
        activeplayer = 1
        player1 = ArrayList<Int>()
        player2 = ArrayList<Int>()

    }

    private fun AutoPlay(view: View) {
        var emptyCells = ArrayList<Int>()
        for(cellID in 1..9) {
            if(!(player1.contains(cellID) || player2.contains(cellID))) {
                emptyCells.add(cellID)
            }
        }
        val r = Random()
        val randIndex = r.nextInt(emptyCells.size - 0) + 0
        val cellID = emptyCells[randIndex]

        val buSelected:Button
        when(cellID) {
            1 -> buSelected = view.button_00
            2 -> buSelected = view.button_01
            3 -> buSelected = view.button_02
            4 -> buSelected = view.button_10
            5 -> buSelected = view.button_11
            6 -> buSelected = view.button_12
            7 -> buSelected = view.button_20
            8 -> buSelected = view.button_21
            9 -> buSelected = view.button_22
            else -> buSelected = view.button_00
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            playGame(cellID, buSelected, view)
        }, 1000)
    }



}