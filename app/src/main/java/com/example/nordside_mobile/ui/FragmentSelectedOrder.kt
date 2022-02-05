package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentSelectedOrderBinding
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.viewmodel.FragmentSelectedOrderViewModel
import com.google.android.material.textview.MaterialTextView

class FragmentSelectedOrder:Fragment(R.layout.fragment_selected_order) {

    private val TAG = "${FragmentSelectedOrder::class.java.simpleName} ###"
    private var _binding : FragmentSelectedOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FragmentSelectedOrderAdapter
    private val viewModel by viewModels<FragmentSelectedOrderViewModel>()

    companion object {
        fun createArgs(order: Order) = bundleOf(
            "order" to order
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.order = (arguments?.get("order") as Order)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSelectedOrderBinding.bind(view)

        binding.recyclerViewFragmentSelectedOrder.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewFragmentSelectedOrder.adapter = FragmentSelectedOrderAdapter(viewModel.order!!.itemTable)

    }

    inner class FragmentSelectedOrderAdapter(orderLineList:List<ClientOrderLine>): RecyclerView.Adapter<FragmentSelectedOrderHolder>() {

        private val orderItemsList: List<ClientOrderLine> = orderLineList

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FragmentSelectedOrderHolder {
            val inflater = LayoutInflater.from(context)
            val view:View = inflater.inflate(R.layout.selected_order_view_holder, parent,false)
            return FragmentSelectedOrderHolder(view)
        }

        override fun onBindViewHolder(holder: FragmentSelectedOrderHolder, position: Int) {
            holder.bind(orderItemsList[position])
        }

        override fun getItemCount(): Int {
            return orderItemsList.size
        }

    }

    inner class FragmentSelectedOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(clientOrderLine: ClientOrderLine){
            val tv_title = itemView.findViewById(R.id.tw_selected_order_view_holder_title) as MaterialTextView
            val tv_count = itemView.findViewById(R.id.tw_selected_order_view_holder_count) as MaterialTextView
            val tv_summa = itemView.findViewById(R.id.tw_selected_order_view_holder_summa) as MaterialTextView

            tv_title.text = clientOrderLine.title
            tv_count.text = clientOrderLine.count.toString()
            tv_summa.text = "${String.format("%.2f", clientOrderLine.summa)} ${getString(R.string.rubles)}"
        }

    }

}