package com.example.nordside_mobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.viewmodel.FragmentPartnerViewModel

class FragmentPartner : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val viewModel by viewModels<FragmentPartnerViewModel>()

    companion object {
        fun newInstance(): FragmentPartner {
            return FragmentPartner()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_partner, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_fragment_partner) as RecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.partnerList.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = PartnerAdapter(it)
        })
        return view
    }

    inner class PartnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.card_view_fragment_partner)
        var textView: TextView = itemView.findViewById(R.id.tw_partner_view_holder)

        fun bind(partner: Partner) {
            textView.setText(partner.title)
        }
    }

    inner class PartnerAdapter(var partnerList: List<Partner>) :
        RecyclerView.Adapter<PartnerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.partner_view_holder, parent, false)
            return PartnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
            holder.bind(partnerList[position])
        }

        override fun getItemCount(): Int {
            return partnerList.size
        }

    }
}
