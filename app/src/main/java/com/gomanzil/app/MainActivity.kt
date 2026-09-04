package com.gomanzil.app

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val ink = Color.rgb(18, 24, 32)
    private val muted = Color.rgb(105, 112, 120)
    private val line = Color.rgb(232, 235, 238)
    private val soft = Color.rgb(248, 249, 250)
    private val yellow = Color.rgb(255, 193, 7)
    private val orange = Color.rgb(246, 143, 30)

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun bg(color: Int, radius: Int = 18) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }

    private fun label(value: String, size: Float, color: Int = ink, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        typeface = Typeface.create("sans", if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun divider() = View(this).apply { setBackgroundColor(line) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val scroll = ScrollView(this).apply { setBackgroundColor(Color.WHITE); isFillViewport = true }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(10), dp(18), dp(24))
        }
        scroll.addView(root)

        // Header
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        val mark = TextView(this).apply {
            text = "G"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(ink, 15)
        }
        header.addView(mark, LinearLayout.LayoutParams(dp(46), dp(46)))
        val brandBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(11), 0, 0, 0) }
        brandBox.addView(label("GoManzil", 23f, ink, true))
        brandBox.addView(label("RIDE • TRAVEL • EXPLORE", 9.5f, muted, true), LinearLayout.LayoutParams(-1, dp(18)))
        header.addView(brandBox, LinearLayout.LayoutParams(0, dp(48), 1f))
        val profile = TextView(this).apply {
            text = "●"
            textSize = 23f
            gravity = Gravity.CENTER
            setTextColor(orange)
            contentDescription = "Profile"
        }
        header.addView(profile, LinearLayout.LayoutParams(dp(44), dp(46)))
        root.addView(header)

        root.addView(label("Where do you want to go?", 29f, ink, true), LinearLayout.LayoutParams(-1, dp(40)).apply { topMargin = dp(24) })
        root.addView(label("Book a ride across Himachal, simply.", 14f, muted), LinearLayout.LayoutParams(-1, dp(25)))

        // Location card
        val locationCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
            background = bg(soft, 20)
        }
        fun locationField(hint: String, icon: String): EditText = EditText(this).apply {
            this.hint = hint
            textSize = 15.5f
            setTextColor(ink)
            setHintTextColor(muted)
            setSingleLine(true)
            setPadding(dp(12), 0, dp(10), 0)
            background = bg(Color.WHITE, 15)
            compoundDrawablePadding = dp(8)
            contentDescription = hint
        }
        val pickup = locationField("Pickup location", "●")
        val drop = locationField("Where to?", "●")
        locationCard.addView(pickup, LinearLayout.LayoutParams(-1, dp(54)))
        locationCard.addView(divider(), LinearLayout.LayoutParams(-1, 1).apply { topMargin = dp(8); bottomMargin = dp(8); leftMargin = dp(18); rightMargin = dp(18) })
        locationCard.addView(drop, LinearLayout.LayoutParams(-1, dp(54)))
        root.addView(locationCard, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18) })

        // Ride choices
        root.addView(label("Choose your ride", 19f, ink, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(22) })
        val rides = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val names = listOf("Bike", "Auto", "Car")
        val icons = listOf("🏍", "🛺", "🚗")
        val cards = mutableListOf<LinearLayout>()
        var selected = 0
        names.forEachIndexed { i, name ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(4), dp(7), dp(4), dp(6))
                background = bg(if (i == 0) Color.rgb(255, 248, 225) else Color.WHITE, 17)
                elevation = dp(1).toFloat()
                isClickable = true
            }
            card.addView(label(icons[i], 26f).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(35)))
            card.addView(label(name, 13.5f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(23)))
            card.setOnClickListener {
                selected = i
                cards.forEachIndexed { index, c -> c.background = bg(if (index == selected) Color.rgb(255, 248, 225) else Color.WHITE, 17) }
            }
            cards.add(card)
            rides.addView(card, LinearLayout.LayoutParams(0, dp(72), 1f).apply { marginEnd = if (i < 2) dp(8) else 0 })
        }
        root.addView(rides, LinearLayout.LayoutParams(-1, dp(72)).apply { topMargin = dp(8) })

        // Booking button
        val book = Button(this).apply {
            text = "Book a ride  →"
            textSize = 17f
            isAllCaps = false
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(yellow, 17)
            elevation = dp(2).toFloat()
            setOnClickListener {
                if (pickup.text.isNullOrBlank() || drop.text.isNullOrBlank()) {
                    Toast.makeText(context, "Pickup aur destination enter karo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "${names[selected]} ride request ready", Toast.LENGTH_SHORT).show()
                }
            }
        }
        root.addView(book, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(18) })

        // Trust strip
        val trust = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, dp(10), 0, 0)
        }
        listOf("✓ Safe rides", "✓ Easy booking", "✓ Live updates").forEach { item ->
            trust.addView(label(item, 11.5f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(0, dp(26), 1f))
        }
        root.addView(trust)

        // Popular places
        root.addView(label("Popular in Himachal", 18f, ink, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(18) })
        val places = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("Shimla", "Manali", "Dharamshala").forEachIndexed { i, place ->
            val p = TextView(this).apply {
                text = place
                textSize = 12.5f
                gravity = Gravity.CENTER
                setTextColor(ink)
                background = bg(if (i == 0) Color.rgb(255, 248, 225) else soft, 14)
                setPadding(dp(8), 0, dp(8), 0)
                isClickable = true
                setOnClickListener { drop.setText(place) }
            }
            places.addView(p, LinearLayout.LayoutParams(0, dp(42), 1f).apply { marginEnd = if (i < 2) dp(7) else 0 })
        }
        root.addView(places, LinearLayout.LayoutParams(-1, dp(42)).apply { topMargin = dp(8) })

        root.addView(label("Your next trip starts here.", 13f, muted), LinearLayout.LayoutParams(-1, dp(24)).apply { topMargin = dp(18) })
        setContentView(scroll)
    }
}
