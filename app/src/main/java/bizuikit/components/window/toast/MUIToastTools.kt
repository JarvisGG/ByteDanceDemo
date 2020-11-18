@file:JvmName("MUIToastTools")
@file:JvmMultifileClass

package bizuikit.components.window.toast

import android.content.Context
import androidx.annotation.DrawableRes
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import bizuikit.components.utils.runOnUIThread
import com.example.bytedance_demo.R

@Suppress("unused")
fun showToast(context: Context?, msg: CharSequence?) {
    if (context == null || msg.isNullOrEmpty()) {
        return
    }
    runOnUIThread(Runnable {
        try {
            val appContext = context.applicationContext
            val toastView = LayoutInflater.from(appContext).inflate(R.layout.mui_toast_text_layout, null)
            toastView.findViewById<TextView>(R.id.text_message).text = msg

            val toast = Toast(appContext)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.show()

        } catch (ignore: Throwable) {

        }
    })
}

@Suppress("unused")
fun showToast(context: Context?, msg: Int) {
    if (context == null || msg == 0) {
        return
    }
    runOnUIThread(Runnable {
        try {
            val appContext = context.applicationContext
            val toastView = LayoutInflater.from(appContext).inflate(R.layout.mui_toast_text_layout, null)
            toastView.findViewById<TextView>(R.id.text_message).setText(msg)

            val toast = Toast(appContext)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        } catch (ignore: Throwable) {

        }
    })

}

@Suppress("unused")
fun showIconToast(context: Context?, msg: CharSequence?, @DrawableRes resId: Int) {
    if (context == null || msg.isNullOrEmpty() || resId == 0) {
        return
    }
    runOnUIThread(Runnable {
        try {
            val appContext = context.applicationContext
            val toastView = LayoutInflater.from(appContext).inflate(R.layout.mui_toast_icon_layout, null)
            toastView.findViewById<ImageView>(R.id.image_icon).setImageResource(resId)
            toastView.findViewById<TextView>(R.id.text_message).text = msg

            val toast = Toast(appContext)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        } catch (ignore: Throwable) {

        }
    })

}

@Suppress("unused")
fun showIconToast(context: Context?, msg: Int, @DrawableRes resId: Int) {
    if (context == null || msg == 0 || resId == 0) {
        return
    }
    runOnUIThread(Runnable {
        try {
            val appContext = context.applicationContext
            val toastView = LayoutInflater.from(appContext).inflate(R.layout.mui_toast_icon_layout, null)
            toastView.findViewById<ImageView>(R.id.image_icon).setImageResource(resId)
            toastView.findViewById<TextView>(R.id.text_message).setText(msg)

            val toast = Toast(appContext)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.view = toastView
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        } catch (ignore: Throwable) {

        }
    })
}