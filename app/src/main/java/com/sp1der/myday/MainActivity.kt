package com.sp1der.myday

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.info.view.*
import kotlinx.android.synthetic.main.setting.view.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var colorSelect:String = "@color/DayNight"
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var filewrite:FileOutputStream
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())
        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        val checkP1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
        val checkP2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
        if ((checkP1 == PackageManager.PERMISSION_GRANTED)&&(checkP2 == PackageManager.PERMISSION_GRANTED)){
            println("yes")
        } else {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR),1)
        }

        try {
            if(readStringFromFile(this,"todos") != "null\n"){
                val rtmp:List<String> = readStringFromFile(this,"todos").split("\n")
//                println(rtmp)
                rtmp.forEach{
//                    println(it)
                    if(it != ""){
                        val todotmp: List<String> = it.split(" ")
                        todoAdapter.addTodo(
                            Todo(
                                title = todotmp[0],
                                isChecked = todotmp[1].toBoolean(),
                                color = todotmp[2],
                                info = todotmp[3]
                            )
                        )
                    }
                }
            }
//            println(readStringFromFile(this,"todos"))
        }catch (e:FileNotFoundException) {
            filewrite = openFileOutput("todos",MODE_PRIVATE)
            filewrite.write("".toByteArray())
            filewrite.close()
        } catch (e:IOException) {
            e.printStackTrace()
        }

        btnAddTodo.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.setting,null)
            val customDialog = AlertDialog.Builder(this,R.style.CustomDialog)
                .setView(view)
                .setPositiveButton(
                "确定"
            ) { _, _ ->
                val n = view.et_name.text.toString()
                var i = view.et_info.text.toString()
                if (i.isEmpty()){
                    i = "无"
                }
                if(n.isNotEmpty()) {
                    val todo = Todo(title = n, color = colorSelect, info = i)
                    todoAdapter.addTodo(todo)
                    colorSelect = "@color/DayNight"
                    var tmp:String = ""
                    todoAdapter.todos.forEach{
                        tmp += (it.title+" "+it.isChecked.toString()+" "+it.color+" "+it.info+"\n")
                    }
//                    println(tmp)
                    filewrite = openFileOutput("todos",MODE_PRIVATE)
                    filewrite.write(tmp.toByteArray())
                    filewrite.close()
//                    println("save")
                }
            }
                .setNegativeButton(
                "取消"
            ) { _, _ ->

            }
                .show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.more1 -> {
                todoAdapter.deleteDoneTodos()
                var tmp:String = ""
                todoAdapter.todos.forEach{
                    tmp += (it.title+" "+it.isChecked.toString()+" "+it.color+" "+it.info+"\n")
                }
//                println(tmp)
                filewrite = openFileOutput("todos",MODE_PRIVATE)
                filewrite.write(tmp.toByteArray())
                filewrite.close()
//                println("save")
                true
            }
            R.id.more2 -> {
                val customDialog = AlertDialog.Builder(this,R.style.CustomDialog)
                val view = LayoutInflater.from(this).inflate(R.layout.info,null)
                view.info_message.text = "小提示"
                view.info_name.visibility = View.GONE
                view.info_info.text = "每次设置越少的任务，可以让你越专注"
                view.info_info.minLines = 3
                view.remind_button.visibility = View.GONE
                customDialog.setView(view)
                customDialog.show()
                true
            }
            R.id.more3 -> {
                startActivity(Intent(this,AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun black(view: View){
        colorSelect = "@color/DayNight"
    }
    fun red(view: View){
        colorSelect = "#d81e06"
    }
    fun pink(view: View){
        colorSelect = "#d4237a"
    }
    fun blue(view: View){
        colorSelect = "#1296db"
    }
    fun dblue(view: View){
        colorSelect = "#13227a"
    }
    fun green(view: View){
        colorSelect = "#1afa29"
    }
    fun yellow(view: View){
        colorSelect = "#f4ea2a"
    }
    fun readStringFromFile(context: Context, fileName: String): String {
        return try {
            val inputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
//            val stringBuilder = StringBuilder()
            var s:String = ""
            var line: String? = bufferedReader.readLine()+"\n"
            while (line != null+"\n") {
                s += line
                line = bufferedReader.readLine()+"\n"
            }
            inputStream.close()
//            println(s)
            s
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    fun cbSave(view: View){
        var tmp:String = ""
        todoAdapter.todos.forEach{
            tmp += (it.title+" "+it.isChecked.toString()+" "+it.color+" "+it.info+"\n")
        }
//                    println(tmp)
        filewrite = openFileOutput("todos",MODE_PRIVATE)
        filewrite.write(tmp.toByteArray())
        filewrite.close()
    }

}