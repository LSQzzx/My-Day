package com.sp1der.myday

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import java.util.*

object DialogFactory {

    /**
     * 创建时间选择弹窗
     */
    fun createTimePickerDialog(context: Context,onSelect:(year:Int,month:Int,day:Int,hour:Int,minute:Int)->Unit){
        val calendar = Calendar.getInstance()

        val bottomDialogView = View.inflate(context,R.layout.time_picker,null)
        val numberPickerYear = bottomDialogView.findViewById<NumberPicker>(R.id.numberPickerYear)
        val numberPickerMonth = bottomDialogView.findViewById<NumberPicker>(R.id.numberPickerMonth)
        val numberPickerDay = bottomDialogView.findViewById<NumberPicker>(R.id.numberPickerDay)
        val numberPickerHour = bottomDialogView.findViewById<NumberPicker>(R.id.numberPickerHour)
        val numberPickerMinute = bottomDialogView.findViewById<NumberPicker>(R.id.numberPickerMinute)
        val tvOk = bottomDialogView.findViewById<TextView>(R.id.tvOk)
        val tvCancel = bottomDialogView.findViewById<TextView>(R.id.tvCancel)


        //初始化年选择器
        val currentYear = calendar.get(Calendar.YEAR)
        numberPickerYear.apply {
            minValue = currentYear - 10
            maxValue = currentYear
            value = currentYear
            //不循环滚动
            wrapSelectorWheel = false
            //当年发生改变的时候要重新设置当前选择的天数（因为有的2月份是29天）
            setOnValueChangedListener { picker, oldVal, newVal ->
                calendar.set(Calendar.YEAR,newVal)
                val currentMonth = calendar.get(Calendar.MONTH) + 1
                //如果当前是二月份才重新设置
                if(currentMonth == 2){
                    numberPickerDay.apply {
                        minValue = 1
                        maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        value = value.coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                    }
                }
            }
        }


        //初始化月份选择器
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        numberPickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = currentMonth
            setFormatter {
                if (it < 10){
                    "0$it"
                }else{
                    "$it"
                }
            }
            //当前月份发生改变的时候，调整选择的天数，（因为月份有30天、31天、28/29天）
            setOnValueChangedListener { picker, oldVal, newVal ->
                calendar.set(Calendar.MONTH,newVal - 1)
                numberPickerDay.apply {
                    minValue = 1
                    maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    value = value.coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                }
            }
        }

        //初始化天选择器
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        numberPickerDay.apply {
            minValue = 1
            maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            value = currentDay
            //当值小于10的时候在前面加个0
            setFormatter {
                if (it < 10){
                    "0$it"
                }else{
                    "$it"
                }
            }
        }

        //初始化小时选择器
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        numberPickerHour.apply {
            minValue = 0
            maxValue = 23
            value = currentHour
            setFormatter {
                if (it < 10){
                    "0$it"
                }else{
                    "$it"
                }
            }
        }

        //初始化分钟选择器
        val currentMinute = calendar.get(Calendar.MINUTE)
        numberPickerMinute.apply {
            minValue = 0
            maxValue = 59
            value = currentMinute
            setFormatter {
                if (it < 10){
                    "0$it"
                }else{
                    "$it"
                }
            }
        }


        val bottomDialog = Dialog(context, R.style.dialog_bottom_full)
        bottomDialog.setCanceledOnTouchOutside(true)//点击窗体外部可以关闭弹窗
        bottomDialog.setCancelable(true)
        bottomDialog.window?.let {
            it.setGravity(Gravity.BOTTOM)//设置为底部显示
            it.setWindowAnimations(R.style.share_animation)//设置动画
            it.setContentView(bottomDialogView)
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)//设置横向全屏
        }

        tvOk.setOnClickListener {
            onSelect(numberPickerYear.value,numberPickerMonth.value,numberPickerDay.value,numberPickerHour.value,numberPickerMinute.value)
            bottomDialog.dismiss()
        }

        tvCancel.setOnClickListener {
            bottomDialog.dismiss()
        }

        bottomDialog.show()
    }
}