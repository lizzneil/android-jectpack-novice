package com.gabe.navigateapplication.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gabe.navigateapplication.R
import com.gabe.navigateapplication.util.visibleWhen


class StateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<StateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.load_state_view, parent, false),
            retry
        )
    }

    inner class LoadStateViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<Button>(R.id.load_state_retry).setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            itemView.apply {
                itemView.findViewById<ProgressBar>(R.id.load_state_progress)
                    .visibleWhen(loadState is LoadState.Loading)
                itemView.findViewById<TextView>(R.id.load_state_errorMessage)
                    .visibleWhen(loadState is LoadState.Error)
                itemView.findViewById<View>(R.id.load_state_retry)
                    .visibleWhen(loadState is LoadState.Error)
            }
            if (loadState is LoadState.Error) {
                itemView.findViewById<TextView>(R.id.load_state_errorMessage).text =
                    loadState.error.localizedMessage
            }
        }
    }
}

