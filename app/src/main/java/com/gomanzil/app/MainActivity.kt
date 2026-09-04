package com.gomanzil.app

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val dark = Color.rgb(25, 25, 25)
    private val muted = Color.rgb(105, 105, 105)
    private val accent = Color.rgb(255, 193, 7)

    private fun rounded(color: Int, radius: Float = 24f): GradientDrawable =
        GradientDrawable().apply { setColor(color); cornerRadius = radius }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val scroll = ScrollView(this).apply { setBackgroundColor(Color.WHITE) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(12), dp(20), dp(28))
        }
        scroll.addView(root)

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val mark = TextView(this).apply {
            text = "G"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            background = rounded(dark, dp(14).toFloat())
        }
        header.addView(mark, LinearLayout.LayoutParams(dp(48), dp(48)))
        val brand = TextView(this).apply {
            text = "GoManzil"
            textSize = 29f
            setTextColor(dark)
            setPadding(dp(12), 0, 0, 0)
        }
        header.addView(brand, LinearLayout.LayoutParams(0, dp(52), 1f))
        val menu = TextView(this).apply { text = "☰"; textSize = 25f; setTextColor(dark); gravity = Gravity.CENTER }
        header.addView(menu, LinearLayout.LayoutParams(dp(44), dp(48)))
        root.addView(header)

        val welcome = TextView(this).apply {
            text = "Apni ride\nbook karo"
            textSize = 32f
            setTextColor(dark)
            setPadding(0, dp(28), 0, dp(18))
        }
        root.addView(welcome)

        val bookingCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
            background = rounded(Color.rgb(248, 248, 248), dp(18).toFloat())
        }

        fun locationField(hintText: String, icon: String): EditText {
            return EditText(this).apply {
                hint = hintText
                textSize = 16f
                setTextColor(dark)
                setHintTextColor(muted)
                setSingleLine(true)
                setPadding(dp(12), 0, dp(12), 0)
                background = rounded(Color.WHITE, dp(14).toFloat())
                compoundDrawablePadding = dp(8)
                contentDescription = hintText
            }
        }

        val pickup = locationField("Pickup location", "●")
        val drop = locationField("Where to?", "●")
        bookingCard.addView(pickup, LinearLayout.LayoutParams(-1, dp(58)).apply { bottomMargin = dp(10) })
        bookingCard.addView(drop, LinearLayout.LayoutParams(-1, dp(58)))
        root.addView(bookingCard, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(18) })

        val vehicleTitle = TextView(this).apply {
            text = "Choose your ride"
            textSize = 19f
            setTextColor(dark)
            setPadding(0, dp(2), 0, dp(12))
        }
        root.addView(vehicleTitle)

        val vehicles = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val choices = listOf("🏍", "🛺", "🚗") to listOf("Bike", "Auto", "Car")
        choices.second.forEachIndexed { i, name ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(6), dp(10), dp(6), dp(10))
                background = rounded(if (i == 0) Color.rgb(255, 248, 225) else Color.WHITE, dp(15).toFloat())
            }
            val icon = TextView(this).apply { text = choices.first[i]; textSize = 28f; gravity = Gravity.CENTER }
            val label = TextView(this).apply { text = name; textSize = 14f; setTextColor(dark); gravity = Gravity.CENTER }
            card.addView(icon, LinearLayout.LayoutParams(-1, dp(38)))
            card.addView(label, LinearLayout.LayoutParams(-1, dp(28)))
            vehicles.addView(card, LinearLayout.LayoutParams(0, dp(82), 1f).apply { marginEnd = if (i < 2) dp(8) else 0 })
        }
        root.addView(vehicles)

        val book = Button(this).apply {
            text = "BOOK A RIDE"
            textSize = 16f
            setTextColor(Color.BLACK)
            isAllCaps = false
            background = rounded(accent, dp(16).toFloat())
            elevation = dp(2).toFloat()
            setOnClickListener {
                if (pickup.text.isNullOrBlank() || drop.text.isNullOrBlank()) {
                    Toast.makeText(context, "Pickup aur destination enter karo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ride request received", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.addView(book, LinearLayout.LayoutParams(-1, dp(58)).apply { topMargin = dp(18) })

        val safety = TextView(this).apply {
            text = "\n✓ Safe rides     ✓ Easy booking     ✓ Live trip updates"
            textSize = 14f
            setTextColor(muted)
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, 0)
        }
        root.addView(safety)

        setContentView(scroll)
    }
}
