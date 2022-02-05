package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.ui.utils.ProductCardRecyclerListener
import com.example.nordside_mobile.viewmodel.FragmentAllOrdersViewModel
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class FragmentAllOrders : Fragment(R.layout.fragment_all_orders){

    private val TAG = "${FragmentAllOrders::class.simpleName} ###"
    private val allOrdersViewModel by viewModels<FragmentAllOrdersViewModel>()
    private val cartViewModel by viewModels<FragmentCartViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var tv_title: TextView
    private var callbacksOrder: Callback? = null
    private var callback :BottomNavigationButtonCallback? = null

    interface Callback {
        fun onOrderSelected(order: Order)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_all_orders, container, false)
        tv_title = view.findViewById(R.id.tw_fragment_all_orders_1)
        recyclerView = view.findViewById(R.id.recycler_view_fragment_all_orders)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
                        val clientOrderLine : ClientOrderLine = ClientOrderLine()
                        clientOrderLine.title = title
                        clientOrderLine.count = count
                        clientOrderLine.unit = unit
                        clientOrderLine.summa = summa
                        orderLineList.add(clientOrderLine)
                        summaOfOrder.plus(summa ?: 0.00)
                    }
                }

                val order = Order()
                order.date = Date()
                order.summa = summaOfOrder
                order.itemTable = orderLineList
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
            Log.v(TAG, orderList[position].toString())
            return holder.bind(orderList[position])
        }

        override fun getItemCount(): Int {
            return orderList.size
        }

    }

    inner class FragmentAllOrdersHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private lateinit var currentOrder: Order

        private var textView_date: TextView =
            itemView.findViewById(R.id.tw_all_orders_view_holder_date)
        private var textView_summa: TextView =
            itemView.findViewById(R.id.tw_all_orders_view_holder_summa)

        fun bind(order: Order) {
            currentOrder = order
            textView_date.setText(order.date.toString())
            val summa = order.summa
            textView_summa.setText("${String.format("%.2f", summa)} ${getString(R.string.rubles)}")

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacksOrder?.onOrderSelected(currentOrder)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as BottomNavigationButtonCallback
        callbacksOrder = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
        callbacksOrder = null
    }

}