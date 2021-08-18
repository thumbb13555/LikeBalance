package com.noahliu.likebalance.Module.EditText

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.noahliu.likebalance.R


@SuppressLint("ViewConstructor", "UseCompatLoadingForDrawables")
class PasswordEditText : AppCompatEditText {
    var drawable: Drawable? = compoundDrawables[2]
    var isPwdOpen = false
    constructor(context: Context?) : super(context!!) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!,attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context!!, attrs, defStyleAttr) {
        init()
    }
    private fun init() {
        if (drawable == null) drawable = resources.getDrawable(R.drawable.ic_pwd_close)
        setDrawVisible()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_UP){
            val start = width-totalPaddingRight+paddingRight-100
            val end = width
            val available = (event.x>start)&&(event.x<end)
            if (available){
                isPwdOpen = !isPwdOpen
                isPwdOpen(isPwdOpen)
                return false
            }
        }
        return super.onTouchEvent(event)
    }
    private fun isPwdOpen(isOpen:Boolean){
        drawable = if (isOpen){
            transformationMethod = HideReturnsTransformationMethod.getInstance()
            resources.getDrawable(R.drawable.ic_pwd_open)

        }else{
            transformationMethod = PasswordTransformationMethod.getInstance()
            resources.getDrawable(R.drawable.ic_pwd_close)
        }
        setDrawVisible()
    }


    private fun setDrawVisible() {
        val rights: Drawable? = drawable
        val left:Drawable = resources.getDrawable(R.drawable.ic_password)
        left.setBounds(0, 0, left.intrinsicWidth, left.intrinsicHeight)
        rights!!.setBounds(0, 0, rights.intrinsicWidth, rights.intrinsicHeight)
        setCompoundDrawables(
            left,
            compoundDrawables[1],
            rights,
            compoundDrawables[3]
        )
    }

}
