package com.gomanzil.app

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val dark = Color.rgb(20, 20, 20)
    private val muted = Color.rgb(105, 105, 105)
    private val soft = Color.rgb(247, 247, 245)
    private val accent = Color.rgb(255, 193, 7)

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun card(background: Int, radius: Float = 22f) = android.graphics.drawable.GradientDrawable().apply {
        setColor(background)
        cornerRadius = dp(radius.toInt()).toFloat()
    }

    private fun text(value: String, size: Float, color: Int = dark, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        typeface = if (bold) Typeface.create("sans", Typeface.BOLD) else Typeface.create("sans", Typeface.NORMAL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val scroll = ScrollView(this).apply { setBackgroundColor(Color.WHITE) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(10), dp(20), dp(30))
        }
        scroll.addView(root)

        // Brand header
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val logo = TextView(this).apply {
            text = "G"
            textSize = 23f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = card(dark, 16f)
        }
        header.addView(logo, LinearLayout.LayoutParams(dp(48), dp(48)))
        val brand = text("GoManzil", 28f, dark, true).apply { setPadding(dp(12), 0, 0, 0) }
        header.addView(brand, LinearLayout.LayoutParams(0, dp(50), 1f))
        val menu = text("☰", 25f, dark).apply { gravity = Gravity.CENTER }
        header.addView(menu, LinearLayout.LayoutParams(dp(44), dp(48)))
        root.addView(header)

        val welcome = text("Apni ride\nbook karo", 34f, dark, true).apply { setPadding(0, dp(26), 0, dp(8)) }
        root.addView(welcome)
        root.addView(text("Fast, safe aur simple rides — bas destination batao.", 15f, muted), LinearLayout.LayoutParams(-1, dp(34)))

        // Booking panel
        val booking = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            background = card(soft, 22f)
        }
        fun field(hint: String, marker: String): EditText = EditText(this).apply {
            this.hint = hint
            textSize = 16f
            setTextColor(dark)
            setHintTextColor(muted)
            setSingleLine(true)
            setPadding(dp(14), 0, dp(10), 0)
            background = card(Color.WHITE, 16f)
            compoundDrawablePadding = dp(8)
            contentDescription = hint
        }
        val pickup = field("Pickup location", "●")
        val drop = field("Where to?", "●")
        booking.addView(pickup, LinearLayout.LayoutParams(-1, dp(58)).apply { bottomMargin = dp(10) })
        booking.addView(drop, LinearLayout.LayoutParams(-1, dp(58)))
        root.addView(booking, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18); bottomMargin = dp(22) })

        root.addView(text("Choose your ride", 20f, dark, true), LinearLayout.LayoutParams(-1, dp(36)))

        val vehicles = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val names = listOf("Bike", "Auto", "Car")
        val icons = listOf("🏍️", "🛺", "🚗")
        names.forEachIndexed { i, name ->
            val v = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(4), dp(8), dp(4), dp(7))
                background = card(if (i == 0) Color.rgb(255, 248, 225) else Color.WHITE, 18f)
                elevation = dp(1).toFloat()
            }
            v.addView(text(icons[i], 27f).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(38)))
            v.addView(text(name, 14f, dark, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(27)))
            vehicles.addView(v, LinearLayout.LayoutParams(0, dp(78), 1f).apply { marginEnd = if (i < 2) dp(8) else 0 })
        }
        root.addView(vehicles)

        val book = Button(this).apply {
            text = "Book a ride  →"
            textSize = 17f
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT_BOLD
            isAllCaps = false
            background = card(accent, 18f)
            elevation = dp(2).toFloat()
            setOnClickListener {
                if (pickup.text.isNullOrBlank() || drop.text.isNullOrBlank()) {
                    Toast.makeText(context, "Pickup aur destination enter karo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ride request received", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.addView(book, LinearLayout.LayoutParams(-1, dp(58)).apply { topMargin = dp(20); bottomMargin = dp(18) })

        val features = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(4), dp(8), dp(4), 0)
        }
        listOf("✓ Safe rides", "✓ Live updates", "✓ Easy booking").forEachIndexed { i, item ->
            features.addView(text(item, 12.5f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(0, dp(30), 1f))
        }
        root.addView(features)

        setContentView(scroll)
    }
}
