package com.example.nordside_mobile.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.nordside_mobile.database.CartPositionPojo

class CartDiffCallback (
    private val oldList: List<CartPositionPojo?>,
    private val newList: List<CartPositionPojo?>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCartPosition = oldList[oldItemPosition]
        val newCartPosition = newList[newItemPosition]
        return oldCartPosition?.title == newCartPosition?.title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCartPosition = oldList[oldItemPosition]
        val newCartPosition = newList[newItemPosition]
        return oldCartPosition == newCartPosition
    }
}