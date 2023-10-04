package by.beltelecom.innowise.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ViewBindingViewHolder<B : ViewBinding>(
    parent: ViewGroup,
    bindingInflater: (inflater: LayoutInflater, container: ViewGroup, attach: Boolean) -> B,
    protected val binding: B = bindingInflater(LayoutInflater.from(parent.context), parent, false)
) : RecyclerView.ViewHolder(binding.root) {
    protected val context: Context = itemView.context
}