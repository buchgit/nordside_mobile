package com.example.nordside_mobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.viewmodel.FragmentPartnerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPartner : Fragment(R.layout.fragment_partner) {

    private lateinit var recyclerView: RecyclerView
    private val partnerViewModel by viewModels<FragmentPartnerViewModel>()

    companion object {
        fun newInstance(): FragmentPartner {
            return FragmentPartner()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_fragment_partner) as RecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_circular_partner)
        progressBar.visibility = View.VISIBLE
        val partnerListLiveData = partnerViewModel.partnerListLiveData

        partnerListLiveData.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.INVISIBLE
            when (partnerListLiveData.value) {
                is Resource.Success -> {
                    recyclerView.adapter = PartnerAdapter(it.data!!)
                }
                is Resource.Error -> {
                    val errorTextView = view.findViewById<TextView>(R.id.error_message_partner)
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text =
                        (partnerListLiveData.value as Resource.Error<List<Partner>>).message
                }
            }
        })
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


    private fun showErrorMessage(message: String) {

    }
}
