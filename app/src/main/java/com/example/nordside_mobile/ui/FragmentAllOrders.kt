package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.ui.utils.ProductCardAdapter
import com.example.nordside_mobile.ui.utils.ProductCardRecyclerListener
import com.example.nordside_mobile.viewmodel.FragmentAllOrdersViewModel
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class FragmentAllOrders:Fragment(R.layout.fragment_all_orders),ProductCardRecyclerListener {
    private val allOrdersViewModel by viewModels<FragmentAllOrdersViewModel>()
    private val cartViewModel by viewModels<FragmentCartViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var tv_title: TextView
    private var callback: BottomNavigationButtonCallback? = null


     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View? {
         super.onCreateView(inflater, container, savedInstanceState)

         val view = inflater.inflate(R.layout.fragment_all_orders, container, false)
         tv_title = view.findViewById(R.id.tw_fragment_all_orders_1)
         tv_title.setText("Все ваши заказы:")
         recyclerView = view.findViewById(R.id.recycler_view_fragment_all_orders)
         recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

         //заполнили список для адаптера
         allOrdersViewModel.getPersonalOrderList()
         allOrdersViewModel.orderList.observe(viewLifecycleOwner, Observer {
             recyclerView.adapter = FragmentAllOrdersAdapter(it)
         })

         return view
     }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback!!.setButtonVisible(R.string.buy, false)

        makeAnOrder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeAnOrder() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cartViewModel.allCartPosition.collect {
                val summaOfOrder = 0.00
                val orderLineList = mutableListOf<ClientOrderLine>()

                it?.forEach { cartLine ->
                    cartLine?.apply {
                        orderLineList.add(ClientOrderLine(title, unit, count, summa))
                        summaOfOrder.plus(summa ?: 0.00)
                    }
                }

                val order = Order(Date(),summaOfOrder, orderLineList)
                allOrdersViewModel.saveOrderOnServer(order)
            }
        }

    }

    inner class FragmentAllOrdersAdapter(var orderList: List<Order>) :
        RecyclerView.Adapter<FragmentAllOrdersHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentAllOrdersHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.all_orders_view_holder, parent, false)
            return FragmentAllOrdersHolder(view)
        }

        override fun onBindViewHolder(holder: FragmentAllOrdersHolder, position: Int) {
            return holder.bind(orderList[position])
        }

        override fun getItemCount(): Int {
            return orderList.size
        }

    }

    inner class FragmentAllOrdersHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var textView_date: TextView =  itemView.findViewById(R.id.tw_all_orders_view_holder_date)
        private var textView_summa: TextView = itemView.findViewById(R.id.tw_all_orders_view_holder_summa)

        fun bind(order:Order) {
            textView_date.setText(order.date.toString())
            textView_summa.setText(order.summa.toString())

        }

        override fun onClick(v: View?) {
//            callbacks?.onNomenclatureSelected(currentNomenclatureWithPrice)
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

    override fun onClickButtonPlusMinusCardProduct(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?
    ) {
        TODO("Not yet implemented")
    }

}