package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import androidx.lifecycle.Observer
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentNomenclatureItemBinding
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.viewmodel.NomenclatureItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentNomenclatureItem : Fragment(R.layout.fragment_nomenclature_item) {

    private var _binding: FragmentNomenclatureItemBinding? = null
    private val binding get() = _binding!!

    private val TAG = "${FragmentNomenclatureItem::class.simpleName} ###"
    private lateinit var currentNomenclatureWithPrice: PriceTable
    private lateinit var currentNomenclature: Nomenclature
    private var currentNomenclatureName: String? = null
    private val nomenclatureItemViewModel by viewModels<NomenclatureItemViewModel>()
    private var COUNT_TO_CART_PLUS = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private var CURRENT_SUMMA = 0.00
    private var currentCount: Double? = null
    private var currentSumma: Double? = null
    private var currentPrice: Double? = null

    companion object {
        fun createArgs(nomenclatureWithPrice: PriceTable) = bundleOf(
            "nomenclatureWithPrice" to nomenclatureWithPrice,
            "nomenclature_name" to (nomenclatureWithPrice.nomenclature?.title ?: "")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO тут какое-то говно с аргументами
        currentNomenclatureName = arguments?.getString("nomenclature")
        currentNomenclatureWithPrice =
            arguments?.getSerializable("nomenclatureWithPrice") as PriceTable
        currentNomenclature = currentNomenclatureWithPrice.nomenclature as Nomenclature
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNomenclatureItemBinding.bind(view)

        with(binding) {

            twFragmentNomenclatureItem1.text = currentNomenclature.fullName
            twFragmentNomenclatureItem2.text = currentNomenclature.unit
            twFragmentNomenclatureItem3.text = getString(R.string.zero)

            currentNomenclature.let { nomenclature ->
                nomenclatureItemViewModel.getCartPositionCount(nomenclature.code)
                    .observe(viewLifecycleOwner,
                        Observer { cart ->
                            currentCount = 0.00
                            currentSumma = 0.00
                            if (cart != null) {
                                if (cart.count != null) {
                                    currentCount = cart.count
                                    currentSumma = cart.summa
                                }
                            }
                            twFragmentNomenclatureItem3.text =
                                String.format("%.2f", currentCount ?: getString(R.string.zero))
                        }
                    )
            }

            buttonAddFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                changeCart(this.buttonAddFragmentNomenclatureItem)
            })

            buttonDeleteFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                changeCart(this.buttonDeleteFragmentNomenclatureItem)
            })

        }
        // comment

        Glide.with(this)
            .load(currentNomenclature.imageUri)
            .into(binding.ivFragmentNomenclatureItem1)
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }


    private fun changeCart(event: View) {

        if (event.id == R.id.button_add_fragment_nomenclature_item) {
            currentPrice = currentNomenclatureWithPrice.price
            CURRENT_SUMMA = currentSumma as Double
            CURRENT_SUMMA += currentPrice!!
            COUNT_TO_CART_PLUS = currentCount?.plus(1.00) ?: 1.00
            nomenclatureItemViewModel.saveToCart(
                viewLifecycleOwner,
                currentNomenclature.code,
                COUNT_TO_CART_PLUS,
                CURRENT_SUMMA,
                currentNomenclature.title,
                currentNomenclature.unit,
                currentNomenclature.imageUri
            )
        } else if (event.id == R.id.button_delete_fragment_nomenclature_item) {

            if (currentCount == null || currentSumma == null || currentCount!!.compareTo(0.00) == 0) {
                currentPrice = 0.00
            } else {
                currentPrice = currentSumma!! / currentCount!!
                CURRENT_SUMMA -= currentPrice!!
                COUNT_TO_CART_MINUS = currentCount?.minus(1.00) ?: 0.00
                if (COUNT_TO_CART_MINUS.compareTo(0.00) <= 0) {
                    nomenclatureItemViewModel.deleteCartPosition(currentNomenclature.code)
                } else {
                    nomenclatureItemViewModel.saveToCart(
                        viewLifecycleOwner,
                        currentNomenclature.code,
                        COUNT_TO_CART_MINUS,
                        -CURRENT_SUMMA,
                        currentNomenclature.title,
                        currentNomenclature.unit,
                        currentNomenclature.imageUri
                    )
                }
            }
        }
    }

}