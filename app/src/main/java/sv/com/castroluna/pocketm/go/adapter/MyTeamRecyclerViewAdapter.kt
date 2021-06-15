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
import sv.com.castroluna.pocketm.go.model.MyTeam


class MyTeamRecyclerViewAdapter(var teamList: ArrayList<MyTeam>): RecyclerView.Adapter<MyTeamRecyclerViewAdapter.MyViewHolder>() {



    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblPokeName: TextView = view.findViewById(R.id.lblPokeName)
        var lblHP: TextView = view.findViewById(R.id.lblHP)
        var lblType: TextView = view.findViewById(R.id.lblType)
        var lblCaptured: TextView = view.findViewById(R.id.lblCaptured)
        var imgBeast:ImageView = view.findViewById(R.id.imgBeast)
        val font1 = Typeface.createFromAsset(view.context.assets, "fonts/Lato-Bold.ttf")
        val font2 = Typeface.createFromAsset(view.context.assets, "fonts/roboto_regular.ttf")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_myteam, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val t = teamList[position]

        holder.lblPokeName.typeface = holder.font1
        holder.lblHP.typeface = holder.font2
        holder.lblCaptured.typeface = holder.font2
        holder.lblType.typeface = holder.font2

        holder.lblPokeName.text=t.name
        holder.lblHP.text = t.hp
        holder.lblCaptured.text = t.captured_at
        holder.lblType.text = t.type

        Glide.with(holder.imgBeast.getContext()).load(t.pokeUrl)
            .placeholder(R.drawable.pokeball01)
            .error(R.drawable.pokeball01)
            .circleCrop()
            .into(holder.imgBeast)


    }

    override fun getItemCount(): Int {
        return teamList.size
    }


}