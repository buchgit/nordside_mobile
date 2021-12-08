package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.databinding.CartViewHolderBinding
import com.example.nordside_mobile.databinding.FragmentCartBinding
import com.example.nordside_mobile.databinding.FragmentOrderBinding
import com.example.nordside_mobile.entity.CartPosition
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentCart:Fragment(R.layout.fragment_cart) {

    private val cartViewModel by viewModels<FragmentCartViewModel>()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private var COUNT_TO_CART_PLUS = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private var CURRENT_SUMMA = 0.00
    private var callbacks:BottomNavigationButtonCallback? = null

//    interface Callback{
//        fun hideCartInBottomNavigation()
//        fun setVisibleCartInBottomNavigation()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)
        binding.recyclerViewFragmentCart.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

//        //  Чтобы не мигало при изменение суммы
//        val itemChangeAnimation = binding.recyclerViewFragmentCart.itemAnimator
//        if (itemChangeAnimation is DefaultItemAnimator) {
//            itemChangeAnimation.supportsChangeAnimations = false
//        }

        viewLifecycleOwner.lifecycleScope.apply {
            launchWhenStarted {
                cartViewModel.allCartPosition.collect {
                    it?.let {
                        if (binding.recyclerViewFragmentCart.adapter == null) {
                            binding.recyclerViewFragmentCart.adapter = CartAdapter(it)
                        } else {
                            (binding.recyclerViewFragmentCart.adapter as CartAdapter)
                                .updateAdapter(it)
                        }
                    }
                }
            }
        }

        callbacks!!.setButtonVisible(R.string.cart, false)
        callbacks!!.setButtonVisible(R.string.making_an_order, true)
    }


    override fun onStop() {
        super.onStop()
        callbacks!!.setButtonVisible(R.string.cart, true)
        callbacks!!.setButtonVisible(R.string.making_an_order, false)
    }


    inner class CartAdapter( private var cartPositionList: List<CartPositionPojo?>):RecyclerView.Adapter<CartHolder>() {

        fun updateAdapter(newPositionList: List<CartPositionPojo?>) {
            val diffCallback = CartDiffCallback(cartPositionList, newPositionList)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            cartPositionList = newPositionList
            diffResult.dispatchUpdatesTo(this@CartAdapter)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
            val inflater = LayoutInflater.from(context)
            val binding = CartViewHolderBinding.inflate(inflater, parent, false)
            return CartHolder(binding)
        }

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            holder.binding(cartPositionList[position])
        }

        override fun getItemCount(): Int {
            return cartPositionList.size
        }
    }


    inner class CartHolder(
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
                    changeCart(BUTTON_PLUS)
                }
                buttonMinusCartViewHolder.setOnClickListener {
                    changeCart(BUTTON_MINUS)
                }
            }
        }

        fun binding(cartPosition: CartPositionPojo?) {
            currentCartPosition = cartPosition

            //Todo: лучше передавать Price с сервера, а полную стоимость не сохранять в Room(1)
            currentCount = currentCartPosition?.count
            currentSumma = currentCartPosition?.summa

            with(binding) {
                twCartViewHolderTitle.text = currentCartPosition?.title ?: "*"
                twCartViewHolderUnit.text = currentCartPosition?.unit ?: "*"
                twCartViewHolderCount.text = String.format("%.2f",currentCount)  // two digits after decimal point
                twCartViewHolderSumma.text = String.format("%.2f",currentSumma)

                if (currentCartPosition?.imageUri != null) {
                    Glide.with(this@FragmentCart)
                        .load(currentCartPosition?.imageUri)
                        .centerCrop()
                        .into(ivProductImage)
                } else {
                    ivProductImage.setImageResource(R.drawable.image_fasad_panel)
                }
            }
        }

        private fun changeCart(event: Boolean) {
            //Todo: лучше передавать Price с сервера, а полную стоимость не сохранять в Room(2)
            currentPrice = currentSumma!! / currentCount!!
            currentSumma = currentCount!! * currentPrice!!

            when (event) {
                BUTTON_PLUS -> {
                    currentCount = currentCount!! + 1
                    currentSumma = currentSumma!! + currentPrice!!

                    cartViewModel.saveToCart(
                        currentCartPosition?.code!!,
                        currentCount!!,
                        currentSumma!!,
                        currentCartPosition?.title!!,
                        currentCartPosition?.unit!!,
                        currentCartPosition?.imageUri
                    )
                }
                BUTTON_MINUS -> {
                    currentCount = currentCount!! - 1
                    currentSumma = currentSumma!! - currentPrice!!

                    cartViewModel.saveToCart(
                        currentCartPosition?.code!!,
                        currentCount!!,
                        currentSumma!!,
                        currentCartPosition?.title!!,
                        currentCartPosition?.unit!!,
                        currentCartPosition?.imageUri
                    )
                }
            }
        }

//        private fun changeCart(event:View){
//
//            currentPrice = currentSumma!! / currentCount!!
//            CURRENT_SUMMA = currentSumma!!
//
//            if (event.id == R.id.button_plus_cart_view_holder) {
//                CURRENT_SUMMA += currentPrice!!
//                COUNT_TO_CART_PLUS = currentCount?.plus(1.00) ?: 1.00
//                currentCartPosition?.let { it1 ->
//                    it1.code?.let { it2 ->
//                        currentCartPosition!!.title?.let { it3 -> currentCartPosition!!.unit?.let { it4 ->
//                            cartViewModel.saveToCart(  it2, COUNT_TO_CART_PLUS, CURRENT_SUMMA, it3,  it4, currentCartPosition!!.imageUri
//                            ) } } } }
//            } else if (event.id == R.id.button_minus_cart_view_holder) {
//                CURRENT_SUMMA -= currentPrice!!
//                if (currentCount == null) {
//                    return
//                }
//                COUNT_TO_CART_MINUS = currentCount?.minus(1.00) ?: 0.00
//                if (COUNT_TO_CART_MINUS.compareTo(0.00) <= 0) {
//                    currentCartPosition?.let { it1 -> it1.code?.let {
//                        cartViewModel.deleteCartPosition(it) } }
//
//                } else {
//                    currentCartPosition?.let { it1 ->
//                        it1.code?.let { it2 ->
//                            currentCartPosition!!.title?.let { it3 -> currentCartPosition!!.unit?.let { it4 ->
//                                cartViewModel.saveToCart(  it2, COUNT_TO_CART_MINUS, CURRENT_SUMMA, it3,  it4, currentCartPosition!!.imageUri
//                                ) } } } }
//                }
//            }
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as BottomNavigationButtonCallback
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
        callbacks = null
    }
}