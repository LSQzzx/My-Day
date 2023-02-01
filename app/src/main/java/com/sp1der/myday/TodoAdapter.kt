package com.sp1der.myday

import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.media.MediaPlayer
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info.view.*
import kotlinx.android.synthetic.main.item_todo.view.*

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
            }
            linear_info.setOnClickListener{
                val customDialog = AlertDialog.Builder(context,R.style.CustomDialog)
                val view = LayoutInflater.from(context).inflate(R.layout.info,null)
                view.info_name.text = curTodo.title
                view.info_info.text = curTodo.info
                customDialog.setView(view)
                customDialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}