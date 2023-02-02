package com.sp1der.myday

import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.media.MediaPlayer
import android.os.Build
import android.text.format.Time
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import kotlinx.android.synthetic.main.remind.view.*
import java.text.ParsePosition
import java.text.SimpleDateFormat

class TodoAdapter(
    val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.isChecked
            when(curTodo.color){
                "@color/DayNight" -> {
                    card.setCardBackgroundColor(resources.getColor(R.color.DayNight))
                }
                else -> {
                    card.setCardBackgroundColor(Color.parseColor(curTodo.color))
                }
            }
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
                if (isChecked){
                    MediaPlayer.create(context,R.raw.done).start()
                }
            }
            tvTodoTitle.setOnClickListener{
                val customDialog = AlertDialog.Builder(context,R.style.CustomDialog)
                val view = LayoutInflater.from(context).inflate(R.layout.info,null)
                view.info_name.text = curTodo.title
                view.info_info.text = curTodo.info
                customDialog.setView(view)
                customDialog.show()
                view.remind_button.setOnClickListener{
                    val remindView = LayoutInflater.from(context).inflate(R.layout.remind,null)
                    val remindDialog = AlertDialog.Builder(context,R.style.CustomDialog)
                        .setView(remindView)
                        .setPositiveButton(
                            "确定"
                        ) { _, _ ->

                        }
                        .setNegativeButton(
                            "取消"
                        ) { _, _ ->

                        }
                        .show()
                    remindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
                    remindDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
                    remindView.time_button.setOnClickListener{
                        DialogFactory.createTimePickerDialog(context) { year, month, day, hour, minute ->
                            val showMonth = if(month > 10 ) "$month" else "0$month"
                            val showDay = if(day > 10 ) "$day" else "0$day"
                            val showHour = if(hour > 10 ) "$hour" else "0$hour"
                            val showMinute = if(minute > 10 ) "$minute" else "0$minute"
                            remindView.t_year.text = "$year"
                            remindView.t_month.text = showMonth
                            remindView.t_day.text = showDay
                            remindView.t_hour.text = showHour
                            remindView.t_min.text = showMinute
                        }
                    }

                }
            }
            linear_info.setOnClickListener{
                val customDialog = AlertDialog.Builder(context,R.style.CustomDialog)
                val view = LayoutInflater.from(context).inflate(R.layout.info,null)
                view.info_name.text = curTodo.title
                view.info_info.text = curTodo.info
                customDialog.setView(view)
                customDialog.show()
                view.remind_button.setOnClickListener{
                    val remindView = LayoutInflater.from(context).inflate(R.layout.remind,null)
                    val remindDialog = AlertDialog.Builder(context,R.style.CustomDialog)
                        .setView(remindView)
                        .setPositiveButton(
                            "确定"
                        ) { _, _ ->
                            val date = "${remindView.t_year.text}-${remindView.t_month.text}-${remindView.t_day.text}-${remindView.t_hour.text}-${remindView.t_min.text}-00"
                            val ts = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(date, ParsePosition(0)).time
                            CalendarReminderUtils.addCalendarEvent(context,"666","6666",ts,15)
//                            println(ts)
                        }
                        .setNegativeButton(
                            "取消"
                        ) { _, _ ->

                        }
                        .show()
                    remindDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
                    remindDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
                    remindView.time_button.setOnClickListener{
                        DialogFactory.createTimePickerDialog(context) { year, month, day, hour, minute ->
                            val showMonth = if(month > 10 ) "$month" else "0$month"
                            val showDay = if(day > 10 ) "$day" else "0$day"
                            val showHour = if(hour > 10 ) "$hour" else "0$hour"
                            val showMinute = if(minute > 10 ) "$minute" else "0$minute"
                            remindView.t_year.text = "$year"
                            remindView.t_month.text = showMonth
                            remindView.t_day.text = showDay
                            remindView.t_hour.text = showHour
                            remindView.t_min.text = showMinute
                        }
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}