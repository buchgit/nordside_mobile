package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.databinding.FragmentOrderBinding
import com.example.nordside_mobile.ui.utils.ProductCardAdapter
import com.example.nordside_mobile.ui.utils.ProductCardRecyclerListener
import com.example.nordside_mobile.viewmodel.FragmentOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentOrder : Fragment(R.layout.fragment_order), ProductCardRecyclerListener {

    private val orderViewModel by viewModels<FragmentOrderViewModel>()
    private var callback: BottomNavigationButtonCallback? = null
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderBinding.bind(view)

        binding.recyclerViewFragmentOrder.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        //  Чтобы не мигало при изменение суммы
        binding.recyclerViewFragmentOrder.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.apply {

            launchWhenStarted {
                orderViewModel.allCartPosition.collect {
                    it?.let {

                        if (binding.recyclerViewFragmentOrder.adapter == null) {
                            binding.recyclerViewFragmentOrder.adapter =
                                ProductCardAdapter(it, this@FragmentOrder)
                        } else {
                            // Todo: Fragment предполагает, что у полученного адаптера есть метод updateAdapter, исправить это. или это норма?
                            (binding.recyclerViewFragmentOrder.adapter as ProductCardAdapter)
                                .updateAdapter(it)
                        }
                    }
                }
            }

            launchWhenStarted {
                orderViewModel.totalCartSumma.collect {
                    it?.let {
                        binding.twTextViewSumma.text =
                            "${getString(R.string.totalOrderSumma)} " +
                                    "${String.format("%.2f",it)} ${getString(R.string.rubles)}"
                    }
                }
            }
        }
        callback!!.setButtonVisible(R.string.buy, true)
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

    override fun onClickButtonPlusMinusCardProduct(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?
    ) {
        orderViewModel.changeCart(
            currentCartPosition,
            currentCount,
            currentSumma
        )
    }

}