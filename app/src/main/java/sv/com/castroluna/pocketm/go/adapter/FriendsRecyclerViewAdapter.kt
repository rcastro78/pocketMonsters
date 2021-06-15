package sv.com.castroluna.pocketm.go.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.com.castroluna.pocketm.go.R
import sv.com.castroluna.pocketm.go.model.Friend
import java.text.SimpleDateFormat
import java.util.*


class FriendsRecyclerViewAdapter(var friendList: ArrayList<Friend>):RecyclerView.Adapter<FriendsRecyclerViewAdapter.MyViewHolder>() {




    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblTrainer: TextView = view.findViewById(R.id.lblTrainer)
        var lblCaptured: TextView = view.findViewById(R.id.lblLastCaptured)
        var imgTrainer : ImageView = view.findViewById(R.id.imgTrainer)
        var imgBeast : ImageView = view.findViewById(R.id.imgBeast)
    //var year: TextView = view.findViewById(R.id.year)
        //var genre: TextView = view.findViewById(R.id.genre)
      val font1 = Typeface.createFromAsset(view.context.assets, "fonts/Lato-Bold.ttf")
      val font2 = Typeface.createFromAsset(view.context.assets, "fonts/roboto_regular.ttf")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_community, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val f = friendList[position]
        holder.lblTrainer.typeface = holder.font1
        holder.lblCaptured.typeface = holder.font2
        holder.lblTrainer.text = "Trainer "+f.name


        val capturedAt:String = changeDateFormat(f.pokemon?.captured_at.toString())
        holder.lblCaptured.text = "Captured "+ (f.pokemon?.name?.toLowerCase() ?: "") +" in "+capturedAt
        if(f.name.equals("Misty")) {holder.imgTrainer.setImageResource(R.drawable.misty01)}
        if(f.name.equals("Tracey Sketchit")) {holder.imgTrainer.setImageResource(R.drawable.tracey)}
        if(f.name.equals("May")) {holder.imgTrainer.setImageResource(R.drawable.may_sk)}
        if(f.name.equals("Brock")) {holder.imgTrainer.setImageResource(R.drawable.brock)}

        //A partir del nombre del pokemon obtener su url

        Glide.with(holder.imgBeast.getContext()).load(f.pkImage)
            .placeholder(R.drawable.pokeball01)
            .error(R.drawable.pokeball01)
            .circleCrop()
            .into(holder.imgBeast)




    }
    override fun getItemCount(): Int {
        return friendList.size
    }

    fun changeDateFormat(dateCaptured: String): String {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)
        val output = SimpleDateFormat("MM/dd/yyyy")
        val date:Date = sdf.parse(dateCaptured)
        return output.format(date)
    }

}