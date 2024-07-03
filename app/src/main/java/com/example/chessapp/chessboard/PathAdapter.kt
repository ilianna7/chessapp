package com.example.chessapp.chessboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chessapp.R

class PathAdapter(private var paths: List<List<Position>>) : RecyclerView.Adapter<PathAdapter.PathViewHolder>() {

    private var onItemClickListener: ((List<Position>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PathViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_path, parent, false)
        return PathViewHolder(view)
    }

    override fun onBindViewHolder(holder: PathViewHolder, position: Int) {
        val path = paths[position]
        holder.bind(path)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(path)
        }
    }

    override fun getItemCount(): Int = paths.size

    fun updatePaths(newPaths: List<List<Position>>) {
        paths = newPaths
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (List<Position>) -> Unit) {
        onItemClickListener = listener
    }

    inner class PathViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pathTextView: TextView = itemView.findViewById(R.id.pathTextView)

        fun bind(path: List<Position>) {
            pathTextView.text = path.joinToString(" -> ") { "(${it.row}, ${it.col})" }
        }
    }
}
