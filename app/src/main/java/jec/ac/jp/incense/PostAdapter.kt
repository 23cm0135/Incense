package jec.ac.jp.incense;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jec.ac.jp.incense.Post
import jec.ac.jp.incense.R

class PostAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUsername: TextView = view.findViewById(R.id.txtUsername)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        holder.txtUsername.text = post.username
        holder.txtContent.text = post.content
    }

    override fun getItemCount() = postList.size
}
