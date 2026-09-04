package com.gomanzil.app

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val ink = Color.rgb(18, 25, 32)
    private val green = Color.rgb(22, 120, 82)
    private val orange = Color.rgb(242, 132, 25)
    private val orangeSoft = Color.rgb(255, 244, 228)
    private val greenSoft = Color.rgb(235, 247, 241)
    private val muted = Color.rgb(105, 112, 120)
    private val soft = Color.rgb(247, 249, 248)
    private val line = Color.rgb(228, 233, 230)

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun bg(color: Int, radius: Int = 18) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
    }
    private fun txt(value: String, size: Float, color: Int = ink, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        typeface = Typeface.create("sans", if (bold) Typeface.BOLD else Typeface.NORMAL)
    }
    private fun divider() = View(this).apply { setBackgroundColor(line) }

    private lateinit var content: LinearLayout
    private var pickup: EditText? = null
    private var drop: EditText? = null
    private var selectedRide = 0
    private val rideNames = listOf("Bike", "Auto", "Car")
    private val rideIcons = listOf("🏍", "🛺", "🚗")
    private val rideFares = listOf("From ₹49", "From ₹69", "From ₹119")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        buildShell()
        showHome()
    }

    private fun buildShell() {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(8), dp(18), dp(12))
        }
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(content)
        }
        outer.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(7), dp(8), dp(8))
            background = bg(Color.WHITE, 0)
            elevation = dp(10).toFloat()
        }
        listOf("⌂\nHome", "✦\nServices", "▣\nTrips", "♙\nProfile").forEachIndexed { i, item ->
            val b = txt(item, 11f, if (i == 0) green else muted, i == 0).apply {
                gravity = Gravity.CENTER
                setPadding(0, dp(3), 0, dp(3))
                isClickable = true
                setOnClickListener {
                    when (i) {
                        0 -> showHome()
                        1 -> showServices()
                        2 -> showTrips()
                        3 -> showProfile()
                    }
                }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, dp(50), 1f))
        }
        outer.addView(nav, LinearLayout.LayoutParams(-1, dp(64)))
        setContentView(outer)
    }

    private fun reset(title: String? = null) {
        content.removeAllViews()
        if (title != null) {
            val top = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
            val back = txt("‹", 34f, ink, true).apply {
                gravity = Gravity.CENTER
                setOnClickListener { showHome() }
            }
            top.addView(back, LinearLayout.LayoutParams(dp(42), dp(46)))
            top.addView(txt(title, 21f, ink, true), LinearLayout.LayoutParams(0, dp(46), 1f))
            top.addView(txt("GoManzil", 12f, green, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(dp(70), dp(46)))
            content.addView(top)
        }
    }

    private fun brandHeader() {
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        val mark = TextView(this).apply {
            text = "G"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(green, 15)
        }
        header.addView(mark, LinearLayout.LayoutParams(dp(46), dp(46)))
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(11), 0, 0, 0) }
        box.addView(txt("GoManzil", 23f, ink, true))
        box.addView(txt("RIDE  •  TRAVEL  •  EXPLORE", 9.5f, orange, true), LinearLayout.LayoutParams(-1, dp(18)))
        header.addView(box, LinearLayout.LayoutParams(0, dp(48), 1f))
        val menu = txt("☰", 22f, ink).apply { gravity = Gravity.CENTER; isClickable = true; setOnClickListener { showProfile() } }
        header.addView(menu, LinearLayout.LayoutParams(dp(44), dp(46)))
        content.addView(header)
    }

    private fun showHome() {
        reset()
        brandHeader()
        content.addView(txt("Where do you want to go?", 29f, ink, true), LinearLayout.LayoutParams(-1, dp(40)).apply { topMargin = dp(24) })
        content.addView(txt("Book rides, trips and travel services across Himachal.", 14f, muted), LinearLayout.LayoutParams(-1, dp(28)))

        val booking = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
            background = bg(soft, 21)
        }
        pickup = locationField("Pickup location", true)
        drop = locationField("Where to?", false)
        booking.addView(pickup, LinearLayout.LayoutParams(-1, dp(54)))
        booking.addView(divider(), LinearLayout.LayoutParams(-1, 1).apply { topMargin = dp(7); bottomMargin = dp(7); leftMargin = dp(18); rightMargin = dp(18) })
        booking.addView(drop, LinearLayout.LayoutParams(-1, dp(54)))
        val current = txt("◎  Use current location", 12.5f, green, true).apply {
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(10), dp(8), 0, 0)
            isClickable = true
            setOnClickListener { pickup?.setText("Current location") }
        }
        booking.addView(current, LinearLayout.LayoutParams(-1, dp(30)))
        content.addView(booking, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18) })

        content.addView(txt("Choose your ride", 19f, ink, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(20) })
        val rides = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val cards = mutableListOf<LinearLayout>()
        rideNames.forEachIndexed { i, name ->
            val c = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                background = bg(if (i == selectedRide) orangeSoft else Color.WHITE, 17)
                setPadding(dp(3), dp(6), dp(3), dp(5))
                elevation = dp(1).toFloat()
                isClickable = true
                setOnClickListener {
                    selectedRide = i
                    cards.forEachIndexed { n, v -> v.background = bg(if (n == selectedRide) orangeSoft else Color.WHITE, 17) }
                    Toast.makeText(this@MainActivity, "$name selected • ${rideFares[i]}", Toast.LENGTH_SHORT).show()
                }
            }
            c.addView(txt(rideIcons[i], 25f).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(34)))
            c.addView(txt(name, 13.5f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(22)))
            c.addView(txt(rideFares[i], 10f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(16)))
            cards.add(c)
            rides.addView(c, LinearLayout.LayoutParams(0, dp(78), 1f).apply { marginEnd = if (i < 2) dp(8) else 0 })
        }
        content.addView(rides, LinearLayout.LayoutParams(-1, dp(78)).apply { topMargin = dp(7) })

        val book = Button(this).apply {
            text = "Book ${rideNames[selectedRide]}  →"
            textSize = 17f
            isAllCaps = false
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(green, 17)
            elevation = dp(2).toFloat()
            setOnClickListener { confirmRide() }
        }
        content.addView(book, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(18) })
        drop?.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE || action == EditorInfo.IME_ACTION_GO || action == EditorInfo.IME_ACTION_NEXT) {
                confirmRide(); true
            } else false
        }

        val trust = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
        listOf("✓ Safe rides", "✓ Easy booking", "✓ Live updates").forEach { s -> trust.addView(txt(s, 11.5f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(0, dp(28), 1f)) }
        content.addView(trust, LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(7) })

        content.addView(txt("Everything you can book", 19f, ink, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(17) })
        val serviceRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        serviceCard(serviceRow, "🏍", "Local\nRide", orangeSoft) { confirmRide() }
        serviceCard(serviceRow, "🗺", "Outstation\nTrip", soft) { showServices() }
        serviceCard(serviceRow, "🏔", "Tour\nPackages", greenSoft) { showServices() }
        content.addView(serviceRow, LinearLayout.LayoutParams(-1, dp(94)).apply { topMargin = dp(7) })

        content.addView(txt("Popular in Himachal", 19f, ink, true), LinearLayout.LayoutParams(-1, dp(30)).apply { topMargin = dp(18) })
        val places = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("Shimla", "Manali", "Dharamshala").forEachIndexed { i, p ->
            val v = txt(p, 12.5f, ink, i == 0).apply {
                gravity = Gravity.CENTER
                background = bg(if (i == 0) orangeSoft else soft, 14)
                isClickable = true
                setOnClickListener { drop?.setText(p) }
            }
            places.addView(v, LinearLayout.LayoutParams(0, dp(42), 1f).apply { marginEnd = if (i < 2) dp(7) else 0 })
        }
        content.addView(places, LinearLayout.LayoutParams(-1, dp(42)).apply { topMargin = dp(7) })

        content.addView(txt("Travel made for Himachal — rides, routes and trips in one place.", 12.5f, muted), LinearLayout.LayoutParams(-1, dp(38)).apply { topMargin = dp(17) })
    }

    private fun locationField(hint: String, first: Boolean): EditText = EditText(this).apply {
        this.hint = hint
        textSize = 15.5f
        setTextColor(ink)
        setHintTextColor(muted)
        setSingleLine(true)
        imeOptions = if (first) EditorInfo.IME_ACTION_NEXT else EditorInfo.IME_ACTION_DONE
        setPadding(dp(12), 0, dp(10), 0)
        background = bg(if (first) Color.rgb(255, 252, 232) else Color.WHITE, 15)
        contentDescription = hint
    }

    private fun serviceCard(row: LinearLayout, icon: String, name: String, color: Int, action: () -> Unit) {
        val c = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = bg(color, 16)
            isClickable = true
            setOnClickListener { action() }
        }
        c.addView(txt(icon, 25f).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(42)))
        c.addView(txt(name, 11.5f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(34)))
        row.addView(c, LinearLayout.LayoutParams(0, dp(94), 1f).apply { marginEnd = dp(7) })
    }

    private fun confirmRide() {
        val from = pickup?.text?.toString()?.trim().orEmpty()
        val to = drop?.text?.toString()?.trim().orEmpty()
        if (from.isBlank() || to.isBlank()) {
            Toast.makeText(this, "Pickup aur destination dono enter karo", Toast.LENGTH_SHORT).show()
            if (from.isBlank()) pickup?.requestFocus() else drop?.requestFocus()
            return
        }
        reset("Confirm your ride")
        content.addView(txt("Your trip is ready", 27f, ink, true), LinearLayout.LayoutParams(-1, dp(38)).apply { topMargin = dp(22) })
        val route = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(15), dp(16), dp(15)); background = bg(soft, 20) }
        route.addView(txt("PICKUP", 10f, green, true))
        route.addView(txt(from, 17f, ink, true), LinearLayout.LayoutParams(-1, dp(30)))
        route.addView(txt("↓", 22f, orange, true), LinearLayout.LayoutParams(-1, dp(25)))
        route.addView(txt("DESTINATION", 10f, green, true))
        route.addView(txt(to, 17f, ink, true), LinearLayout.LayoutParams(-1, dp(30)))
        content.addView(route, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16) })

        val fare = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; background = bg(orangeSoft, 17); setPadding(dp(14), 0, dp(14), 0) }
        fare.addView(txt(rideIcons[selectedRide] + "  " + rideNames[selectedRide], 16f, ink, true), LinearLayout.LayoutParams(0, dp(56), 1f))
        fare.addView(txt(rideFares[selectedRide], 16f, green, true))
        content.addView(fare, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(14) })

        val confirm = Button(this).apply {
            text = "Confirm & Find Driver  →"
            textSize = 16f
            isAllCaps = false
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
            background = bg(green, 17)
            setOnClickListener { showSearching(from, to) }
        }
        content.addView(confirm, LinearLayout.LayoutParams(-1, dp(56)).apply { topMargin = dp(18) })
        content.addView(txt("No payment is taken in this demo build. Driver matching and live tracking will connect to the backend in the production phase.", 11.5f, muted), LinearLayout.LayoutParams(-1, dp(48)).apply { topMargin = dp(12) })
    }

    private fun showSearching(from: String, to: String) {
        reset("Finding your driver")
        val icon = txt("✓", 44f, green, true).apply { gravity = Gravity.CENTER; background = bg(greenSoft, 50) }
        content.addView(icon, LinearLayout.LayoutParams(dp(92), dp(92)).apply { gravity = Gravity.CENTER; topMargin = dp(45) })
        content.addView(txt("Ride request sent", 24f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(38)).apply { topMargin = dp(20) })
        content.addView(txt("Looking for a nearby ${rideNames[selectedRide].lowercase()} driver", 14f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(28)))
        val trip = txt("$from  →  $to", 14f, ink, true).apply { gravity = Gravity.CENTER; background = bg(soft, 16); setPadding(dp(10), 0, dp(10), 0) }
        content.addView(trip, LinearLayout.LayoutParams(-1, dp(52)).apply { topMargin = dp(24) })
        val cancel = Button(this).apply { text = "Back to home"; isAllCaps = false; setTextColor(green); background = bg(greenSoft, 17); setOnClickListener { showHome() } }
        content.addView(cancel, LinearLayout.LayoutParams(-1, dp(52)).apply { topMargin = dp(16) })
    }

    private fun showServices() {
        reset("Services")
        content.addView(txt("One app for your ride + travel plans.", 26f, ink, true), LinearLayout.LayoutParams(-1, dp(70)).apply { topMargin = dp(20) })
        content.addView(txt("Choose what you need — local rides, intercity travel or a complete Himachal trip.", 13.5f, muted), LinearLayout.LayoutParams(-1, dp(48)))

        val services = listOf(
            Triple("🏍", "Local Rides", "Bike • Auto • Car"),
            Triple("🛣", "Outstation", "One-way & round trip"),
            Triple("🏔", "Tour Packages", "Himachal sightseeing"),
            Triple("✈", "Airport Transfer", "Pickup & drop"),
            Triple("🚌", "Group Travel", "Tempo & larger groups"),
            Triple("🏨", "Stay + Travel", "Trip planning in one place")
        )
        services.forEach { s ->
            val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; background = bg(if (s.second == "Tour Packages") greenSoft else soft, 17); setPadding(dp(14), 0, dp(14), 0); isClickable = true }
            row.addView(txt(s.first, 28f).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(dp(52), dp(62)))
            val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(8), 0, 0, 0) }
            box.addView(txt(s.second, 16f, ink, true))
            box.addView(txt(s.third, 12f, muted), LinearLayout.LayoutParams(-1, dp(24)))
            row.addView(box, LinearLayout.LayoutParams(0, dp(62), 1f))
            row.addView(txt("›", 28f, orange, true).apply { gravity = Gravity.CENTER })
            row.setOnClickListener { Toast.makeText(this, "${s.second} selected", Toast.LENGTH_SHORT).show() }
            content.addView(row, LinearLayout.LayoutParams(-1, dp(70)).apply { topMargin = dp(9) })
        }
    }

    private fun showTrips() {
        reset("My Trips")
        content.addView(txt("Your journeys in one place.", 25f, ink, true), LinearLayout.LayoutParams(-1, dp(45)).apply { topMargin = dp(22) })
        val empty = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER; background = bg(soft, 22); setPadding(dp(20), dp(28), dp(20), dp(28)) }
        empty.addView(txt("▣", 42f, green, true).apply { gravity = Gravity.CENTER })
        empty.addView(txt("No trips yet", 19f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(34)))
        empty.addView(txt("Your completed and upcoming rides will appear here.", 13f, muted).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(48)))
        val b = Button(this).apply { text = "Book your first ride"; isAllCaps = false; setTextColor(Color.WHITE); background = bg(green, 15); setOnClickListener { showHome() } }
        empty.addView(b, LinearLayout.LayoutParams(-1, dp(50)).apply { topMargin = dp(8) })
        content.addView(empty, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(20) })
    }

    private fun showProfile() {
        reset("Profile")
        val avatar = txt("G", 28f, Color.WHITE, true).apply { gravity = Gravity.CENTER; background = bg(green, 50) }
        content.addView(avatar, LinearLayout.LayoutParams(dp(82), dp(82)).apply { gravity = Gravity.CENTER; topMargin = dp(25) })
        content.addView(txt("GoManzil Rider", 21f, ink, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(34)).apply { topMargin = dp(12) })
        content.addView(txt("Ride • Travel • Explore", 12.5f, orange, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(28)))
        listOf("Account & mobile number", "Saved places", "Safety & emergency help", "Payments", "Offers & coupons", "Help & support").forEach { item ->
            val row = txt("$item                                      ›", 14f, ink, true).apply { gravity = Gravity.CENTER_VERTICAL; background = bg(soft, 15); setPadding(dp(15), 0, dp(10), 0); isClickable = true; setOnClickListener { Toast.makeText(this, item, Toast.LENGTH_SHORT).show() } }
            content.addView(row, LinearLayout.LayoutParams(-1, dp(54)).apply { topMargin = dp(8) })
        }
    }
}
