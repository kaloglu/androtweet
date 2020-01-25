package com.zsk.androtweet.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.zsk.androtweet.R
import com.zsk.androtweet.enums.Enums

/**
 * Created by kaloglu on 22/05/16.
 */
class CustomDialog(
        context: Context,
        private val titleID: Int,
        private val descriptionID: Int,
        private val rulesID: Int,
        private val actionButtonID: Int
) : Dialog(context) {
    private val dialogType: Enums.DialogType? = null
    private var title: TextView? = null
    private var description: TextView? = null
    private var rules: TextView? = null
    private var okButton: Button? = null
    private var actionButton: Button? = null
    private fun initView() {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = window
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT)
        }
        this.setContentView(R.layout.custom_dialog_screen)
        setCanceledOnTouchOutside(false)
        title = findViewById<View>(R.id.dialog_title) as TextView
        title!!.text = getString(titleID)
        description = findViewById<View>(R.id.dialog_description) as TextView
        description!!.text = getString(descriptionID)
        //        description.
        rules = findViewById<View>(R.id.dialog_rules) as TextView
        if (rulesID != 0) {
            rules!!.text = getString(rulesID)
        } else {
            rules!!.text = ""
        }
        okButton = findViewById<View>(R.id.dialog_okButton) as Button
        actionButton = findViewById<View>(R.id.dialog_actionButton) as Button
        if (actionButtonID != 0) {
            actionButton!!.text = getString(actionButtonID)
        } else {
            okButton!!.background = ContextCompat.getDrawable(context, R.drawable.pop_up_accept)
            actionButton!!.visibility = View.GONE
        }
        if (dialogType != null && dialogType == Enums.DialogType.INFO) okButton!!.text = getString(R.string.close)
        show()
    }

    private fun getString(stringResourceID: Int): String {
        return context.resources.getString(stringResourceID)
    }

    fun initOkButtonClickListener(onClick: () -> Unit) {
        okButton!!.setOnClickListener { onClick() }
    }

    fun initActionButtonClickListener(onClick: () -> Unit) {
        actionButton!!.setOnClickListener { onClick() }
    }

    init {
        initView()
    }
}