package com.example.gridviewer

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


@Suppress("DEPRECATION")
class JSONParser(
    private var c: Context,
    private var jsonData: String,
    private var gridView: GridView,
    private var screenWidth: Int
) : AsyncTask<Void, Void, Boolean>() {
    private var cells = ArrayList<Cell>()

    override fun doInBackground(vararg voids: Void): Boolean? {
        return parse()
    }

    override fun onPostExecute(isParsed: Boolean?) {
        super.onPostExecute(isParsed)

        if (isParsed!!) {
            gridView.adapter = CellAdapter(c, cells, screenWidth)
        } else {
            Toast.makeText(c, "unParse!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parse(): Boolean {
        try {
            val ja = JSONArray(jsonData)
            var jo: JSONObject

            cells.clear()
            var cell: Cell

            for (i in 0 until ja.length()) {
                jo = ja.getJSONObject(i)

                val id = jo.getString("id")
                val desc = jo.getString("title")
                val url = jo.getString("url")
                val thumbnailUrl = jo.getString("thumbnailUrl")
                cell = Cell(id, desc, url, thumbnailUrl)
                cells.add(cell)
            }

            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

    class CellAdapter(
        private var c: Context,
        private var cell: ArrayList<Cell>,
        private var screenWidth: Int
    ) : BaseAdapter() {
        val columnNum = 4
        override fun getCount(): Int {
            return cell.size
        }

        override fun getItem(pos: Int): Any {
            return cell[pos]
        }

        override fun getItemId(pos: Int): Long {
            return pos.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            var convertView = view
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.cell_entry, viewGroup, false)
            }

            var ww = screenWidth / columnNum
            convertView?.layoutParams?.width = ww
            convertView?.layoutParams?.height = ww
            val image = convertView!!.findViewById<ImageView>(R.id.cellImg) as ImageView
            val idTxt = convertView!!.findViewById<TextView>(R.id.cellName) as TextView
            val descTxt = convertView!!.findViewById<TextView>(R.id.cellDisc) as TextView

            val cc = this.getItem(i) as Cell

            idTxt.text = cc.id
            descTxt.text = cc.desc
            cc.GetBitMap(c, image)

            convertView.setOnClickListener {
                val intent = Intent(c, ImageActivity::class.java)
                intent.putExtra("url", cc.imgUrl)
                intent.putExtra("info", cc.id)
                intent.putExtra("info2", cc.desc)
                c!!.startActivity(intent)
            }

            return convertView
        }
    }
}


