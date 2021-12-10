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
import com.example.nordside_mobile.ui.utils.ProductCardAdapter
import com.example.nordside_mobile.ui.utils.ProductCardRecyclerListener
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentCart : Fragment(R.layout.fragment_cart), ProductCardRecyclerListener {

    private val cartViewModel by viewModels<FragmentCartViewModel>()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private var callbacks:BottomNavigationButtonCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        binding.recyclerViewFragmentCart.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        //  Чтобы не мигало при изменение суммы
        binding.recyclerViewFragmentCart.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.apply {
            launchWhenStarted {
                cartViewModel.allCartPosition.collect {
                    it?.let {

                        if (binding.recyclerViewFragmentCart.adapter == null) {
                            binding.recyclerViewFragmentCart.adapter =
                                ProductCardAdapter(it, this@FragmentCart)
                        } else {
                            // Todo: Fragment предполагает, что у полученного адаптера есть метод updateAdapter, исправить это. или это норма?
                            (binding.recyclerViewFragmentCart.adapter as ProductCardAdapter)
                                .updateAdapter(it)
                        }
                    }
                }
            }
        }
        callbacks!!.setButtonVisible(R.string.cart, false)
        callbacks!!.setButtonVisible(R.string.making_an_order, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as BottomNavigationButtonCallback
    }

    override fun onStop() {
        super.onStop()
        callbacks!!.setButtonVisible(R.string.cart, true)
        callbacks!!.setButtonVisible(R.string.making_an_order, false)
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
        callbacks = null
    }

    override fun onClickButtonPlusMinusCardProduct(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?
    ) {
        cartViewModel.changeCart(
            currentCartPosition,
            currentCount,
            currentSumma
        )
    }
}