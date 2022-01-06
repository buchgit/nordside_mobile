package com.example.nordside_mobile.ui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.databinding.CartViewHolderBinding

class ProductCardAdapter(
    private var cartPositionList: List<CartPositionPojo?>,
    private val listener: ProductCardRecyclerListener
): RecyclerView.Adapter<ProductCardAdapter.ProductCardHolder>() {

    // Функция, чтобы RecyclerView обновлял только нужны Item, а не обновлялся целиком
    fun updateAdapter(newPositionList: List<CartPositionPojo?>) {
        val diffCallback = CartDiffCallback(cartPositionList, newPositionList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        cartPositionList = newPositionList
        diffResult.dispatchUpdatesTo(this@ProductCardAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CartViewHolderBinding.inflate(inflater, parent, false)
        return ProductCardHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductCardHolder, position: Int) {
        holder.binding(cartPositionList[position])
    }

    override fun getItemCount(): Int {
        return cartPositionList.size
    }


    inner class ProductCardHolder(
        private val binding: CartViewHolderBinding
    ):RecyclerView.ViewHolder(binding.root){
        private var currentCartPosition: CartPositionPojo? = null
        private var currentCount: Double? = null
        private var currentSumma: Double? = null
        private var currentPrice: Double? = null
        private val BUTTON_PLUS = true
        private val BUTTON_MINUS = false

        init {
            with(binding) {
                buttonPlusCartViewHolder.setOnClickListener {
                    onPlusMinusClickDefault(BUTTON_PLUS)
                    listener.onClickButtonPlusMinusCardProduct(
                        currentCartPosition,
                        currentCount,
                        currentSumma
                    )
                }
                buttonMinusCartViewHolder.setOnClickListener {
                    onPlusMinusClickDefault(BUTTON_MINUS)
                    listener.onClickButtonPlusMinusCardProduct(
                        currentCartPosition,
                        currentCount,
                        currentSumma
                    )
                }
            }
        }

        private fun onPlusMinusClickDefault(event: Boolean) {
            //Todo: лучше передавать Price с сервера, а полную стоимость не сохранять в Room(1)
            currentPrice = currentSumma!! / currentCount!!
            currentSumma = currentCount!! * currentPrice!!

            when (event) {
                BUTTON_PLUS -> {
                    currentCount = currentCount!! + 1
                    currentSumma = currentSumma!! + currentPrice!!

                }
                BUTTON_MINUS -> {
                    currentCount = currentCount!! - 1
                    currentSumma = currentSumma!! - currentPrice!!
                }
            }
        }

        fun binding(cartPosition: CartPositionPojo?) {
            currentCartPosition = cartPosition

            //Todo: лучше передавать Price с сервера, а полную стоимость не сохранять в Room(2)
            currentCount = currentCartPosition?.count
            currentSumma = currentCartPosition?.summa

            with(binding) {
                twCartViewHolderTitle.text = currentCartPosition?.title ?: "*"
                twCartViewHolderUnit.text = currentCartPosition?.unit ?: "*"
                twCartViewHolderCount.text = String.format("%.2f",currentCount)  // two digits after decimal point
                twCartViewHolderSumma.text = String.format("%.2f",currentSumma)

                if (currentCartPosition?.imageUri != null) {
                    Glide.with(this.root)
                        .load(currentCartPosition?.imageUri)
                        .centerCrop()
                        .into(ivProductImage)
                } else {
                    ivProductImage.setImageResource(R.drawable.image_fasad_panel)
                }
            }
        }
    }

}

// Интерфейс для связки Адаптера с ViewModel Фрагмента, реализуется фрагментом
interface ProductCardRecyclerListener {
    fun onClickButtonPlusMinusCardProduct(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?
    )
}