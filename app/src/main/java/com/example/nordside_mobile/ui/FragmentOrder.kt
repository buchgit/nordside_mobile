package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.databinding.CartViewHolderBinding
import com.example.nordside_mobile.databinding.FragmentLoginBinding
import com.example.nordside_mobile.databinding.FragmentOrderBinding
import com.example.nordside_mobile.viewmodel.FragmentOrderViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.math.MathContext
import java.math.RoundingMode

@AndroidEntryPoint
class FragmentOrder:Fragment(R.layout.fragment_order) {

    private val orderViewModel by viewModels<FragmentOrderViewModel>()
    private var callback: BottomNavigationButtonCallback? = null
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private var COUNT_TO_CART_PLUS = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private var CURRENT_SUMMA = 0.00
    private var currentCount:Double? = null
    private var currentSumma:Double? = null
    private var currentPrice:Double? = null


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderBinding.bind(view)

        binding.recyclerViewFragmentOrder.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        viewLifecycleOwner.lifecycleScope.apply {

            launchWhenStarted {
                orderViewModel.allCartPosition.collect {
                    it?.let { binding.recyclerViewFragmentOrder.adapter = OrderAdapter(it) }
                }
            }

            launchWhenStarted {
                orderViewModel.totalCartSumma.collect {
                    it?.let {
//                    val bigDecimal:java.math.BigDecimal = java.math.BigDecimal(it)
                        binding.twTextViewSumma.text =
                            "${getString(R.string.totalOrderSumma)} " +
                                    "${String.format("%.2f",it)} ${getString(R.string.rubles)}"
                    }
                }
            }
        }
        callback!!.setButtonVisible(R.string.buy, true)
    }


    inner class OrderAdapter(
        private var cartPositionList:List<CartPositionPojo?>
        ) : RecyclerView.Adapter<OrderHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CartViewHolderBinding.inflate(inflater, parent, false)
            return OrderHolder(binding)
        }

        override fun onBindViewHolder(holder: OrderHolder, position: Int) {
            holder.binding(cartPositionList[position])
        }

        override fun getItemCount(): Int {
            return cartPositionList.size
        }
    }


    inner class OrderHolder(
        private val binding: CartViewHolderBinding
    ):RecyclerView.ViewHolder(binding.root){
        private var currentCartPosition: CartPositionPojo? = null

        init {
            with(binding) {
                buttonPlusCartViewHolder.setOnClickListener {
                    changeCart(buttonPlusCartViewHolder)
                }
                buttonMinusCartViewHolder.setOnClickListener {
                    changeCart(buttonMinusCartViewHolder)
                }
            }
        }

        fun binding(cartPosition: CartPositionPojo?) {
            currentCartPosition = cartPosition
            currentCount = currentCartPosition?.count
            currentSumma = currentCartPosition?.summa

            with(binding) {
                twCartViewHolderTitle.text = currentCartPosition?.title ?: "*"
                twCartViewHolderUnit.text = currentCartPosition?.unit ?: "*"
                twCartViewHolderCount.text = String.format("%.2f",currentCount)  // two digits after decimal point
                twCartViewHolderSumma.text = String.format("%.2f",currentSumma)

                if (currentCartPosition?.imageUri != null) {
                    Glide.with(this@FragmentOrder)
                        .load(currentCartPosition?.imageUri)
                        .into(ivProductImage)
                } else {
                    ivProductImage.setImageResource(R.drawable.image_fasad_panel)
                }
            }
        }

        private fun changeCart(event:View){

            currentPrice = currentSumma!! / currentCount!!
            CURRENT_SUMMA = currentSumma!!

            if (event.id == R.id.button_plus_cart_view_holder) {
                CURRENT_SUMMA += currentPrice!!
                COUNT_TO_CART_PLUS = currentCount?.plus(1.00) ?: 1.00
                currentCartPosition?.let { it1 ->
                    it1.code?.let { it2 ->
                        currentCartPosition!!.title?.let { it3 ->
                            currentCartPosition!!.unit?.let { it4 ->
                                orderViewModel.saveToCart(  it2, COUNT_TO_CART_PLUS, CURRENT_SUMMA, it3,  it4, currentCartPosition!!.imageUri)
                            }
                        }
                    }
                }
            } else if (event.id == R.id.button_minus_cart_view_holder) {
                CURRENT_SUMMA -= currentPrice!!
                if (currentCount == null) {
                    return
                }
                COUNT_TO_CART_MINUS = currentCount?.minus(1.00) ?: 0.00
                if (COUNT_TO_CART_MINUS.compareTo(0.00) <= 0) {
                    currentCartPosition?.let { it1 -> it1.code?.let {
                        orderViewModel.deleteCartPosition(it) } }

                } else {
                    currentCartPosition?.let { it1 ->
                        it1.code?.let { it2 ->
                            currentCartPosition!!.title?.let { it3 -> currentCartPosition!!.unit?.let { it4 ->
                                orderViewModel.saveToCart(  it2, COUNT_TO_CART_MINUS, CURRENT_SUMMA, it3,  it4, currentCartPosition!!.imageUri
                                ) } } } }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        callback!!.setButtonVisible(R.string.buy, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as BottomNavigationButtonCallback

    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
        callback = null
    }

}