package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.viewmodel.FragmentOrderViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.math.MathContext
import java.math.RoundingMode

@AndroidEntryPoint
class FragmentOrder:Fragment() {
    private val orderViewModel by viewModels<FragmentOrderViewModel>()
    private var callback: BottomNavigationButtonCallback? = null
    private lateinit var recyclerView: RecyclerView
    private var COUNT_TO_CART_PLUS = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private var CURRENT_SUMMA = 0.00
    private var currentCount:Double? = null
    private var currentSumma:Double? = null
    private var currentPrice:Double? = null
    private lateinit var textView_summa:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val textView_title = view.findViewById(R.id.tw_fragment_order_1) as TextView
        val textView_items = view.findViewById(R.id.tw_fragment_order_2) as TextView
        textView_summa = view.findViewById(R.id.tw_fragment_order_3) as TextView
        recyclerView =   view.findViewById(R.id.recycler_view_fragment_order) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        orderViewModel.getAllCartPosition().observe(viewLifecycleOwner,
            Observer {
                recyclerView.adapter = OrderAdapter(it)
            })
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel.getAllCartPosition().observe(viewLifecycleOwner,
            Observer {
                recyclerView.adapter = OrderAdapter(it)
            })

        orderViewModel.totalCartSumma.observe(
                viewLifecycleOwner,
            Observer {
                //if (it is Resource.Success) {
//                    textView_summa.setText(
//                        "${getString(R.string.totalOrderSumma)} ${it.data.toString()} ${getString(R.string.rubles)}"
//                    )
                //}
                val bigDecimal:java.math.BigDecimal = java.math.BigDecimal(it)
                textView_summa.setText("${getString(R.string.totalOrderSumma)} ${String.format("%.2f",it)} ${getString(R.string.rubles)}")
            }
        )

        callback!!.setButtonVisible(R.string.buy, true)

    }

    override fun onStop() {
        super.onStop()
        callback!!.setButtonVisible(R.string.buy, false)
    }

//    override fun onDetach() {
//        super.onDetach()
//        _binding = null
//    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class OrderAdapter(private var cartPositionList:List<CartPositionPojo?>):RecyclerView.Adapter<OrderHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
            val inflater = LayoutInflater.from(context)
            val view:View = inflater.inflate(R.layout.cart_view_holder, parent, false)
            return OrderHolder(view)
        }

        override fun onBindViewHolder(holder: OrderHolder, position: Int) {
            holder.binding(cartPositionList[position])
        }

        override fun getItemCount(): Int {
            return cartPositionList.size
        }
    }

    inner class OrderHolder(view: View):RecyclerView.ViewHolder(view){

        private val textView_title:TextView = itemView.findViewById(R.id.tw_cart_view_holder_1)
        private val textView_unit:TextView = itemView.findViewById(R.id.tw_cart_view_holder_2)
        private val textView_count:TextView = itemView.findViewById(R.id.tw_cart_view_holder_3)
        private val textView_summa:TextView = itemView.findViewById(R.id.tw_cart_view_holder_4)
        private val button_plus: MaterialButton = itemView.findViewById(R.id.button_plus_cart_view_holder)
        private val button_minus: MaterialButton = itemView.findViewById(R.id.button_minus_cart_view_holder)
        private var currentCartPosition: CartPositionPojo? = null


        fun binding(cartPosition: CartPositionPojo?) {
            currentCartPosition = cartPosition
            currentCount = currentCartPosition?.count
            currentSumma = currentCartPosition?.summa
            textView_title.setText(currentCartPosition?.title ?: "*")
            textView_unit.setText(currentCartPosition?.unit ?: "*")
            textView_count.setText(String.format("%.2f",currentCount)) //two digits after decimal point
            textView_summa.setText(String.format("%.2f",currentSumma))
        }

        init {
            button_plus.setOnClickListener(View.OnClickListener {
                changeCart(this.button_plus)})
            button_minus.setOnClickListener(View.OnClickListener {
                changeCart(this.button_minus)
            })
        }

        private fun changeCart(event:View){

            currentPrice = currentSumma!! / currentCount!!
            CURRENT_SUMMA = currentSumma!!

            if (event.id == R.id.button_plus_cart_view_holder) {
                CURRENT_SUMMA += currentPrice!!
                COUNT_TO_CART_PLUS = currentCount?.plus(1.00) ?: 1.00
                currentCartPosition?.let { it1 ->
                    it1.code?.let { it2 ->
                        currentCartPosition!!.title?.let { it3 -> currentCartPosition!!.unit?.let { it4 ->
                            orderViewModel.saveToCart(  it2, COUNT_TO_CART_PLUS, CURRENT_SUMMA, it3,  it4
                            ) } } } }
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
                                orderViewModel.saveToCart(  it2, COUNT_TO_CART_MINUS, CURRENT_SUMMA, it3,  it4
                                ) } } } }
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as BottomNavigationButtonCallback

    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}