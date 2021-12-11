package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

//        val notScrollingLayoutManager = object : LinearLayoutManager(
//            context, LinearLayoutManager.VERTICAL,false
//        ) {
//            override fun canScrollVertically(): Boolean {
//                return false
//            }
//        }

        binding.recyclerViewFragmentCart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        //  Чтобы не мигало при изменение суммы
        binding.recyclerViewFragmentCart.itemAnimator = null

        binding.recyclerViewFragmentCart.scrollState

        viewLifecycleOwner.lifecycleScope.apply {

            launchWhenStarted {
                cartViewModel.allCartPosition.collect {
                    it?.let {
                        updateAdapter(it)
                        updateUi(it.isEmpty())
                    }
                }
            }

            launchWhenStarted {
                cartViewModel.totalCartSumma.collect {
                    it?.let {
                        binding.tvTotalSummaNumbers.text =
                            "${String.format("%.2f",it)} ${getString(R.string.rubles)}"
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

    // Связывает Adapter c ViewModel
    override fun onClickButtonPlusMinusCardProduct(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?
    ) {
        if (currentCount!! <= 0.00) {
            cartViewModel.deleteCartPosition(currentCartPosition!!.code!!)
            return
        }
        cartViewModel.saveToCart(
            currentCartPosition?.code!!,
            currentCount,
            currentSumma!!,
            currentCartPosition.title!!,
            currentCartPosition.unit!!,
            currentCartPosition.imageUri
        )
    }

    private fun updateAdapter(newPositionList: List<CartPositionPojo?>) {
        if (binding.recyclerViewFragmentCart.adapter == null) {
            binding.recyclerViewFragmentCart.adapter =
                ProductCardAdapter(newPositionList, this@FragmentCart)
        } else {
            // Todo: Fragment предполагает, что у полученного адаптера есть метод updateAdapter, исправить это. или это норма?
            (binding.recyclerViewFragmentCart.adapter as ProductCardAdapter)
                .updateAdapter(newPositionList)
        }
    }

    private fun updateUi(isEmpty: Boolean) {
        // Todo: из корзины в Room при уменьшении количества товара сначала удаляется товар, потом insert-cя, поэтому может проскакивать cartEmpty, когда остается 1 товар
        if (isEmpty) {
            with(binding) {
                if (tvCartEmpty.visibility == View.VISIBLE) return
                tvCartEmpty.visibility = View.VISIBLE
                tvTotalSumma.visibility = View.GONE
                tvTotalSummaNumbers.visibility = View.GONE
                buttonCheckOut.visibility = View.GONE
            }
        } else {
            with(binding) {
                if (tvCartEmpty.visibility == View.GONE) return
                tvCartEmpty.visibility = View.GONE
                tvTotalSumma.visibility = View.VISIBLE
                tvTotalSummaNumbers.visibility = View.VISIBLE
                buttonCheckOut.visibility = View.VISIBLE
            }
        }
    }

}