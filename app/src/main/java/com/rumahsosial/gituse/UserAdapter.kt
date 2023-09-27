package com.rumahsosial.gituse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.rumahsosial.gituse.databinding.ItemUserBinding
import com.rumahsosial.gituse.model.ResponseUserGithubs

class UserAdapter (private val data : MutableList<ResponseUserGithubs.Item> = mutableListOf(),
private val listener : (ResponseUserGithubs.Item) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<ResponseUserGithubs.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemUserBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item : ResponseUserGithubs.Item) {
            v.image.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }

            v.username.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))



    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size


}