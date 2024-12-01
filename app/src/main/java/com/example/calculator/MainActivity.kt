package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentNumber = ""
    private var operator = ""
    private var firstNumber = ""
    private var isOperatorSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.tvDisplay)

        // Initialize buttons
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnDot, R.id.btnClear, R.id.btnPlusMinus,
            R.id.btnPercentage, R.id.btnDivide, R.id.btnMultiply,
            R.id.btnSubtract, R.id.btnAdd, R.id.btnEquals
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }
    }

    private fun onButtonClick(button: Button) {
        when (button.text.toString()) {
            "AC" -> reset()
            "+/-" -> toggleSign()
            "%" -> applyPercentage()
            "÷", "×", "-", "+" -> setOperator(button.text.toString())
            "=" -> calculateResult()
            "." -> appendToNumber(".")
            else -> appendToNumber(button.text.toString())
        }
    }

    private fun reset() {
        currentNumber = ""
        operator = ""
        firstNumber = ""
        isOperatorSet = false
        display.text = "0"
    }

    private fun toggleSign() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
            updateDisplay()
        }
    }

    private fun applyPercentage() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = (currentNumber.toDoubleOrNull()?.div(100)).toString()
            updateDisplay()
        }
    }

    private fun setOperator(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (isOperatorSet) {
                calculateResult() // Perform intermediate calculation
            }
            firstNumber = currentNumber
            operator = op
            isOperatorSet = true
            currentNumber = "" // Clear currentNumber for next input
            updateDisplay()
        }
    }

    private fun appendToNumber(value: String) {
        if (value == "." && currentNumber.contains(".")) return // Prevent multiple dots
        currentNumber += value
        updateDisplay()
    }

    private fun calculateResult() {
        if (operator.isNotEmpty() && currentNumber.isNotEmpty() && firstNumber.isNotEmpty()) {
            val result = when (operator) {
                "+" -> firstNumber.toDouble() + currentNumber.toDouble()
                "-" -> firstNumber.toDouble() - currentNumber.toDouble()
                "×" -> firstNumber.toDouble() * currentNumber.toDouble()
                "÷" -> {
                    if (currentNumber.toDouble() != 0.0) {
                        firstNumber.toDouble() / currentNumber.toDouble()
                    } else {
                        display.text = "Error"
                        reset()
                        return
                    }
                }
                else -> 0.0
            }
            display.text = "$firstNumber $operator $currentNumber = ${result.toString().removeSuffix(".0")}" // Full expression
            firstNumber = result.toString()
            operator = ""
            currentNumber = ""
            isOperatorSet = false
        }
    }

    private fun updateDisplay() {
        val displayText = if (isOperatorSet) {
            "$firstNumber $operator $currentNumber"
        } else {
            currentNumber
        }
        display.text = displayText
    }
}
