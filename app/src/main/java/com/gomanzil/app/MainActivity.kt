package com.gomanzil.app

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val blue = Color.rgb(9, 43, 70)
    private val green = Color.rgb(30, 125, 82)
    private val orange = Color.rgb(244, 135, 24)
    private val soft = Color.rgb(247, 249, 248)
    private val orangeSoft = Color.rgb(255, 245, 226)
    private val muted = Color.rgb(105, 112, 120)
    private var content: LinearLayout? = null
    private var pickup: EditText? = null
    private var destination: EditText? = null
    private var ride = 0
    private var driver = false
    private val names = listOf("Bike", "Auto", "Car")
    private val icons = listOf("🏍", "🛺", "🚗")
    private val fares = listOf("From ₹49", "From ₹69", "From ₹119")

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
    private fun bg(color: Int, radius: Int = 16): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }
    private fun text(s: String, size: Float, color: Int = blue, bold: Boolean = false): TextView = TextView(this).apply {
        this.text = s
        textSize = size
        setTextColor(color)
        typeface = Typeface.create("sans", if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        buildShell()
        renderHome()
    }

    private fun buildShell() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(10), dp(18), dp(14))
        }
        root.addView(ScrollView(this).apply {
            isFillViewport = true
            addView(content)
        }, LinearLayout.LayoutParams(-1, 0, 1f))
        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            elevation = dp(6).toFloat()
        }
        val labels = listOf("⌂\nHome", "▣\nTrips", "₹\nEarnings", "?\nHelp", "♙\nProfile")
        labels.forEachIndexed { index, label ->
            val item = text(label, 10.5f, if (index == 0) green else muted, index == 0).apply {
                gravity = Gravity.CENTER
                setOnClickListener {
                    when (index) {
                        0 -> renderHome()
                        1 -> trips()
                        2 -> earnings()
                        3 -> help()
                        4 -> profile()
                    }
                }
            }
            nav.addView(item, LinearLayout.LayoutParams(0, dp(58), 1f))
        }
        root.addView(nav, LinearLayout.LayoutParams(-1, dp(64)))
        setContentView(root)
    }

    private fun reset(title: String? = null) {
        content?.removeAllViews()
        if (title != null) {
            val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            row.addView(text("‹", 32f, blue, true).apply {
                gravity = Gravity.CENTER
                setOnClickListener { renderHome() }
            }, LinearLayout.LayoutParams(dp(42), dp(46)))
            row.addView(text(title, 21f, blue, true), LinearLayout.LayoutParams(0, dp(46), 1f))
            content?.addView(row)
        }
    }

    private fun header() {
        val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        row.addView(text("G", 23f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
            background = bg(blue, 15)
        }, LinearLayout.LayoutParams(dp(46), dp(46)))
        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(11), 0, 0, 0)
        }
        val name = LinearLayout(this)
        name.addView(text("Go", 23f, orange, true))
        name.addView(text("Manzil", 23f, blue, true))
        brand.addView(name)
        brand.addView(text("RIDE  •  TRAVEL  •  EXPLORE", 9.5f, green, true))
        row.addView(brand, LinearLayout.LayoutParams(0, dp(48), 1f))
        val mode = text(if (driver) "DRIVER\nON" else "PASSENGER\nON", 8.5f, blue, true).apply {
            gravity = Gravity.CENTER
            background = bg(if (driver) Color.rgb(235, 247, 241) else orangeSoft, 12)
            setOnClickListener {
                driver = !driver
                if (driver) driverHome() else renderHome()
            }
        }
        row.addView(mode, LinearLayout.LayoutParams(dp(86), dp(46)))
        content?.addView(row)
    }

    private fun renderHome() {
        if (driver) {
            driverHome()
            return
        }
        reset()
        header()
        content?.addView(text("Where do you want to go?", 29f, blue, true), LinearLayout.LayoutParams(-1, dp(40)).apply { topMargin = dp(22) })
        content?.addView(text("Your ride, tour and travel plans in one app.", 14f, muted), LinearLayout.LayoutParams(-1, dp(28)))

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
            background = bg(soft, 21)
        }
        pickup = field("Pickup location", true)
        destination = field("Where do you want to go?", false)
        box.addView(pickup, LinearLayout.LayoutParams(-1, dp(54)))
        box.addView(text("", 1f).apply { setBackgroundColor(Color.LTGRAY) }, LinearLayout.LayoutParams(-1, 1).apply {
            topMargin = dp(7); bottomMargin = dp(7); leftMargin = dp(18); rightMargin = dp(18)
        })
        box.addView(destination, LinearLayout.LayoutParams(-1, dp(54)))
        box.addView(text("◎  Use current location", 12.5f, green, true).apply {
            setPadding(dp(10), dp(8), 0, 0)
            setOnClickListener { pickup?.setText("Current location") }
        }, LinearLayout.LayoutParams(-1, dp(30)))
        content?.addView(box, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16) })

        content?.addView(text("Choose your ride", 19f, blue, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(18) })
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val cards = mutableListOf<LinearLayout>()
        names.forEachIndexed { index, name ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                background = bg(if (index == ride) orangeSoft else Color.WHITE, 17)
                setOnClickListener {
                    ride = index
                    cards.forEachIndexed { j, view -> view.background = bg(if (j == ride) orangeSoft else Color.WHITE, 17) }
                }
            }
            card.addView(text(icons[index], 25f))
            card.addView(text(name, 13.5f, blue, true))
            card.addView(text(fares[index], 10f, muted))
            cards.add(card)
            row.addView(card, LinearLayout.LayoutParams(0, dp(82), 1f).apply { marginEnd = if (index < 2) dp(8) else 0 })
        }
        content?.addView(row, LinearLayout.LayoutParams(-1, dp(82)).apply { topMargin = dp(7) })
        content?.addView(Button(this).apply {
            text = "Book ${names[ride]}  →"
            textSize = 17f
            isAllCaps = false
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(green, 17)
            setOnClickListener { confirmRide() }
        }, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(17) })

        content?.addView(text("✓ Safe rides     ✓ Easy booking     ✓ Live updates", 11.5f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(7) })
        content?.addView(text("Tour & Travel", 20f, blue, true), LinearLayout.LayoutParams(-1, dp(32)).apply { topMargin = dp(14) })
        val servicesRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        service(servicesRow, "🏔", "Tours", green) { services() }
        service(servicesRow, "🗺", "Outstation", blue) { services() }
        service(servicesRow, "✈", "Airport", orange) { services() }
        service(servicesRow, "🚌", "Group Travel", blue) { services() }
        content?.addView(servicesRow, LinearLayout.LayoutParams(-1, dp(86)).apply { topMargin = dp(6) })

        content?.addView(text("Popular in Himachal", 19f, blue, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(16) })
        val popular = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("Shimla", "Manali", "Dharamshala").forEachIndexed { index, place ->
            popular.addView(text(place, 12.5f, blue, index == 0).apply {
                gravity = Gravity.CENTER
                background = bg(if (index == 0) orangeSoft else soft, 14)
                setOnClickListener { destination?.setText(place) }
            }, LinearLayout.LayoutParams(0, dp(42), 1f).apply { marginEnd = if (index < 2) dp(7) else 0 })
        }
        content?.addView(popular, LinearLayout.LayoutParams(-1, dp(42)).apply { topMargin = dp(6) })
    }

    private fun field(hintText: String, first: Boolean): EditText = EditText(this).apply {
        hint = hintText
        textSize = 15.5f
        setTextColor(blue)
        setHintTextColor(muted)
        setSingleLine(true)
        setPadding(dp(12), 0, dp(10), 0)
        background = bg(if (first) Color.rgb(255, 252, 232) else Color.WHITE, 15)
        if (!first) setOnEditorActionListener { _, action, _ ->
            if (action == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                confirmRide(); true
            } else false
        }
    }

    private fun service(row: LinearLayout, icon: String, name: String, accent: Int, action: () -> Unit) {
        row.addView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = bg(if (accent == blue) soft else if (accent == green) Color.rgb(235, 247, 241) else orangeSoft, 15)
            setOnClickListener { action() }
            addView(text(icon, 23f))
            addView(text(name, 10.5f, blue, true))
        }, LinearLayout.LayoutParams(0, dp(86), 1f).apply { marginEnd = dp(7) })
    }

    private fun confirmRide() {
        val from = pickup?.text?.toString()?.trim().orEmpty()
        val to = destination?.text?.toString()?.trim().orEmpty()
        if (from.isBlank() || to.isBlank()) {
            Toast.makeText(this, "Pickup aur destination dono enter karo", Toast.LENGTH_SHORT).show()
            return
        }
        reset("Confirm Ride")
        content?.addView(text("Your trip is ready", 27f, blue, true), LinearLayout.LayoutParams(-1, dp(40)).apply { topMargin = dp(22) })
        content?.addView(text("${names[ride]}\n\n$from\n\n↓\n\n$to", 17f, blue, true).apply {
            gravity = Gravity.CENTER_VERTICAL
            background = bg(soft, 20)
            setPadding(dp(16), dp(15), dp(16), dp(15))
        }, LinearLayout.LayoutParams(-1, dp(190)).apply { topMargin = dp(16) })
        content?.addView(Button(this).apply {
            text = "Confirm & Find Driver  →"
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = bg(green, 17)
            setOnClickListener { Toast.makeText(this@MainActivity, "Ride request sent", Toast.LENGTH_SHORT).show() }
        }, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(16) })
    }

    private fun driverHome() {
        reset()
        header()
        content?.addView(text("Driver Dashboard", 28f, blue, true), LinearLayout.LayoutParams(-1, dp(40)).apply { topMargin = dp(22) })
        content?.addView(text("Manage your rides and earnings.", 14f, muted))
        val online = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
            background = bg(Color.rgb(235, 247, 241), 18)
            setPadding(dp(14), 0, dp(10), 0)
        }
        online.addView(text("Driver mode\nOnline & ready for rides", 15f, blue, true), LinearLayout.LayoutParams(0, dp(64), 1f))
        online.addView(Button(this).apply {
            text = "ONLINE"
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = bg(green, 14)
            setOnClickListener { driver = false; renderHome() }
        }, LinearLayout.LayoutParams(dp(105), dp(44)))
        content?.addView(online, LinearLayout.LayoutParams(-1, dp(70)).apply { topMargin = dp(15) })
        val stats = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        stat(stats, "Today", "₹1,280")
        stat(stats, "Trips", "12")
        stat(stats, "Rating", "4.9 ★")
        content?.addView(stats, LinearLayout.LayoutParams(-1, dp(82)).apply { topMargin = dp(14) })
        content?.addView(text("New ride request", 19f, blue, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(20) })
        val request = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = bg(orangeSoft, 18)
            setPadding(dp(14), dp(12), dp(14), dp(12))
        }
        request.addView(text("🏍  Bike ride", 16f, blue, true))
        request.addView(text("Bilaspur  →  Manali   •   ₹620 est.", 13f, muted))
        request.addView(Button(this).apply {
            text = "Accept Ride"
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = bg(green, 14)
            setOnClickListener { Toast.makeText(this@MainActivity, "Ride accepted", Toast.LENGTH_SHORT).show() }
        }, LinearLayout.LayoutParams(-1, dp(48)).apply { topMargin = dp(7) })
        content?.addView(request, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(7) })
    }

    private fun stat(row: LinearLayout, label: String, value: String) {
        row.addView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = bg(soft, 15)
            addView(text(value, 18f, blue, true))
            addView(text(label, 11f, muted))
        }, LinearLayout.LayoutParams(0, dp(82), 1f).apply { marginEnd = dp(7) })
    }

    private fun services() {
        reset("Tour & Travel")
        content?.addView(text("Travel services built for Himachal.", 25f, blue, true), LinearLayout.LayoutParams(-1, dp(55)).apply { topMargin = dp(20) })
        listOf("🏔  Himachal Tour Packages", "🗺  Outstation & Round Trips", "✈  Airport Transfers", "🚌  Group & Family Travel", "🏨  Stay + Travel Planning").forEach { item ->
            content?.addView(text("$item     ›", 15f, blue, true).apply {
                gravity = Gravity.CENTER_VERTICAL
                background = bg(soft, 16)
                setPadding(dp(14), 0, dp(8), 0)
            }, LinearLayout.LayoutParams(-1, dp(58)).apply { topMargin = dp(9) })
        }
    }

    private fun trips() {
        reset("My Trips")
        content?.addView(text("Your rides and travel bookings.", 25f, blue, true), LinearLayout.LayoutParams(-1, dp(45)).apply { topMargin = dp(20) })
        content?.addView(text("No trips yet. Upcoming and completed bookings will appear here.", 14f, muted), LinearLayout.LayoutParams(-1, dp(55)).apply { topMargin = dp(10) })
        content?.addView(Button(this).apply {
            text = "Book a ride"
            isAllCaps = false
            setTextColor(Color.WHITE)
            background = bg(green, 15)
            setOnClickListener { renderHome() }
        }, LinearLayout.LayoutParams(-1, dp(52)).apply { topMargin = dp(10) })
    }

    private fun earnings() {
        reset("Earnings")
        content?.addView(text("₹1,280", 34f, green, true), LinearLayout.LayoutParams(-1, dp(50)).apply { topMargin = dp(25) })
        content?.addView(text("Today • 12 trips • 4.9 ★", 13f, muted))
        listOf("Today" to "₹1,280", "This week" to "₹7,840", "This month" to "₹24,600").forEach { pair ->
            content?.addView(text("${pair.first}                         ${pair.second}", 15f, blue, true).apply {
                gravity = Gravity.CENTER_VERTICAL
                background = bg(soft, 15)
                setPadding(dp(15), 0, dp(8), 0)
            }, LinearLayout.LayoutParams(-1, dp(54)).apply { topMargin = dp(8) })
        }
    }

    private fun help() {
        reset("Help & Safety")
        content?.addView(text("Safety and support", 26f, blue, true), LinearLayout.LayoutParams(-1, dp(42)).apply { topMargin = dp(22) })
        listOf("🛡  Safety & emergency help", "☎  Contact support", "❓  FAQs", "⚠  Report a problem").forEach { item ->
            content?.addView(text(item, 15f, blue, true).apply {
                gravity = Gravity.CENTER_VERTICAL
                background = bg(soft, 15)
                setPadding(dp(15), 0, 0, 0)
            }, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(9) })
        }
    }

    private fun profile() {
        reset("Profile")
        content?.addView(text("GoManzil Account", 25f, blue, true), LinearLayout.LayoutParams(-1, dp(45)).apply { topMargin = dp(22) })
        listOf("Account & mobile number", "Saved places", "Payments", "Offers & coupons", "Safety settings", "Help & support").forEach { item ->
            content?.addView(text("$item     ›", 14f, blue, true).apply {
                gravity = Gravity.CENTER_VERTICAL
                background = bg(soft, 15)
                setPadding(dp(15), 0, 0, 0)
            }, LinearLayout.LayoutParams(-1, dp(54)).apply { topMargin = dp(8) })
        }
    }
}
