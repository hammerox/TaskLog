package com.mcustodio.tasklog.utils

import android.app.Activity
import android.graphics.*
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.DatePicker
import android.widget.Toast
//import com.bumptech.glide.Glide
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.salesforce.androidsdk.app.SalesforceSDKManager
//import com.salesforce.androidsdk.rest.RestClient
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by mcustodio on 30/11/2017.
 */

var sdf = SimpleDateFormat()


//fun LatLng.distanceTo(coord: LatLng) : Float {
//    val first = Location("first").apply {
//        latitude = this@distanceTo.latitude
//        longitude = this@distanceTo.longitude
//    }
//    return first.distanceTo(coord)
//}


//fun Location.distanceTo(coord: LatLng) : Float {
//    val second = Location("second").apply {
//        latitude = coord.latitude
//        longitude = coord.longitude
//    }
//    return this.distanceTo(second)
//}


fun DatePicker.toDate() : Date {
    val year = year
    val month = month
    val day = dayOfMonth
    return Calendar.getInstance().let {
        it.set(year, month, day)
        it.time
    }
}


fun DatePicker.setDate(date: Date) {
    val d = Calendar.getInstance().apply { time = date }
    updateDate(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH))
}


//fun MarkerOptions.icon(icon: Bitmap, color: Int) {
//    val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)  // Or PorderDuff.Mode.SRC_IN
//    val bitmap = icon.copy(Bitmap.Config.ARGB_8888, true)
//    val paint = Paint()
//    paint.colorFilter = colorFilter
//    val canvas = Canvas(bitmap)
//    canvas.drawBitmap(bitmap, 0f, 0f, paint)
//    this.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
//}


fun AlertDialog.Builder.showMessage(message: String) {
    setMessage(message)
    show()
}


fun android.app.AlertDialog.Builder.showMessage(message: String) {
    setMessage(message)
    show()
}


//var clientInfo: RestClient.ClientInfo? = null
//    get() = SalesforceSDKManager
//            .getInstance()
//            .clientManager
//            ?.peekRestClient()
//            ?.clientInfo
//
//
//fun ImageView.loadImage(context: Context, url: String?) {
//    url?.let {
//        Glide.with(context)
//                .load(it)
//                .centerCrop()
//                .crossFade()
//                .placeholder(null)
//                .into(this)
//    }
//}


fun Activity.setOnKeyboardListener(rootView: View? = null, onKeyboard: (isOpen: Boolean) -> Unit) {
    val view = rootView ?: this.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    view.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        view.getWindowVisibleDisplayFrame(r)
        val screenHeight = view.rootView.height
        val keypadHeight = screenHeight - r.bottom
        onKeyboard.invoke(keypadHeight > screenHeight * 0.15)
    }
}


fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun FloatingActionButton.hideWhenKeyboardIsVisible(activity: Activity) {
    activity.setOnKeyboardListener { isOpen ->
        when (isOpen) {
            true -> this.switchVisibility(false)
            false -> this.show()
        }
    }
}



// String extensions

fun String.noAccents() : String {
    return (Normalizer.normalize(this, Normalizer.Form.NFD)).replace("[^\\p{ASCII}]", "")
}


fun String?.stripAccents(): String? {
    if (this == null) return null
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")//$NON-NLS-1$
    val decomposed = StringBuilder(Normalizer.normalize(this, Normalizer.Form.NFD))
    convertRemainingAccentCharacters(decomposed)
    // Note that this doesn't correctly remove ligatures...
    return pattern.matcher(decomposed).replaceAll("")
}

private fun convertRemainingAccentCharacters(decomposed: StringBuilder) {
    for (i in 0 until decomposed.length) {
        if (decomposed[i] == '\u0141') {
            decomposed.deleteCharAt(i)
            decomposed.insert(i, 'L')
        } else if (decomposed[i] == '\u0142') {
            decomposed.deleteCharAt(i)
            decomposed.insert(i, 'l')
        }
    }
}


fun String?.toStateAbbreviation(): String? {
    return when (this?.trim()?.stripAccents()?.toLowerCase()) {
        "acre" -> "AC"
        "alagoas" -> "AL"
        "amapa" -> "AP"
        "amazonas" -> "AM"
        "bahia" -> "BA"
        "ceara" -> "CE"
        "distrito federal" -> "DF"
        "espírito santo" -> "ES"
        "goias" -> "GO"
        "maranhao" -> "MA"
        "mato grosso" -> "MT"
        "mato grosso do sul" -> "MS"
        "minas gerais" -> "MG"
        "para" -> "PA"
        "paraiba" -> "PB"
        "parana" -> "PR"
        "pernambuco" -> "PE"
        "piaui" -> "PI"
        "rio de janeiro" -> "RJ"
        "rio grande do norte" -> "RN"
        "rio grande do sul" -> "RS"
        "rondonia" -> "RO"
        "roraima" -> "RR"
        "santa catarina" -> "SC"
        "sao paulo" -> "SP"
        "sergipe" -> "SE"
        "tocantins" -> "TO"
        else -> null
    }
}

fun String?.isStoneUrl() : Boolean {
    return this?.startsWith("https://stone") == true
            || this?.startsWith("https://comercial-sharon.herokuapp.com") == true
}



// Date

fun Date.toString(format: String): String {
    sdf.applyPattern(format)
    return sdf.format(this)
}

fun Date.toString(sdf: SimpleDateFormat): String {
    return sdf.format(this)
}


fun Date.ignoreSeconds() : Date {
    val seconds = this.time % 60000
    this.time = this.time - seconds
    return this
}


fun Date.roundToNearestFiveMinutes() : Date {
    val precision = (60000 * 5)
    val notRounded = this.time.toDouble() / precision
    this.time = Math.round(notRounded) * precision
    return this
}



// View extensions

fun View.switchVisibility(isVisible: Boolean? = null) {
    val show = isVisible ?: (this.visibility == View.GONE)
    this.visibility = if (show) View.VISIBLE else View.GONE
}


fun View.animateVisibility(isVisible: Boolean? = null) {
    val show = isVisible ?: (this.visibility == View.GONE)
    if (show) this.expand() else this.collapse()
}


fun View.expand() {
    if (visibility == View.GONE) {
        visibility = View.VISIBLE
        val animation = ScaleAnimation(1f, 1f, 0f, 1f).apply {
            duration = 200
        }
        this.startAnimation(animation)
    }
}


fun View.collapse(){
    if (visibility == View.VISIBLE) {
        val animation = ScaleAnimation(1f, 1f, 1f, 0f).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    this@collapse.visibility = View.GONE
                }
            })
            duration = 200
        }
        this@collapse.startAnimation(animation)
    }
}