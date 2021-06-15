package sv.com.castroluna.pocketm.go.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.model.Captured
import sv.com.castroluna.pocketm.go.model.Friend

class CapturedRecyclerViewAdapter(var capturedList: ArrayList<Captured>):
    RecyclerView.Adapter<CapturedRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgBeast : ImageView = view.findViewById(R.id.imgBeast)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_captured, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val c = capturedList[position]
        Glide.with(holder.imgBeast.getContext()).load(c.pkImage)
            .placeholder(R.drawable.pokeball01)
            .error(R.drawable.pokeball01)
            .circleCrop()
            .into(holder.imgBeast)
    }

    override fun getItemCount(): Int {
        return capturedList.size
    }

}