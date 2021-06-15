package sv.com.castroluna.pocketm.go.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.model.Captured

class CapturedAdapter(activity: Activity, items: ArrayList<Captured>): BaseAdapter() {
    protected var activity: Activity? = null
    protected var items: ArrayList<Captured> = ArrayList()
    lateinit var captured:Captured

    fun CapturedAdapter(activity: Activity, items: ArrayList<Captured>) {
        this.activity = activity
        this.items = items

    }



    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any? {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup?): View {
        captured = items[position]
        var holder: ViewHolder? = null
        var rView: View
        if (convertView == null) {

                val inflater =
                    activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    rView = inflater.inflate(R.layout.item_captured, null)
                holder = ViewHolder()
            holder.imgBeast = convertView.findViewById(R.id.imgBeast)
            convertView.tag = holder
        }else{
            holder =
                convertView.tag as ViewHolder
            rView = convertView
//Glide
            Glide.with(holder.imgBeast!!.context).load(captured.pkImage)
                .placeholder(R.drawable.pokeball01)
                .error(R.drawable.pokeball01)
                .circleCrop()
                .into(holder.imgBeast!!)


        }

        return rView
    }


    internal class ViewHolder {
        var imgBeast: ImageView? = null
    }

}