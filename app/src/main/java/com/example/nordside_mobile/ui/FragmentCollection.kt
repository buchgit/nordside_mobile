package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentCollectionBinding
import com.example.nordside_mobile.databinding.ItemCollectionViewHolderBinding
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.viewmodel.FragmentCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCollection : Fragment(R.layout.fragment_collection) {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private val TAG = "${FragmentCollection::class.simpleName} ###"
    private var adapter: ItemCollectionAdapter = ItemCollectionAdapter(emptyList())
    private val collectionViewModel by viewModels<FragmentCollectionViewModel>()
    private var callbacks: Callback? = null
    private var categoryId: String? = null
    private var categoryTitle: String? = null

    companion object {
        fun createArgs(category: Category) = bundleOf(
            "id" to category.id,
            "category_title" to category.title
        )
    }


    interface Callback {
        fun onCollectionSelected(nomenclatureCollection: NomenclatureCollection)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = requireArguments().getString("id")
        categoryTitle = requireArguments().getString("category_title")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCollectionBinding.bind(view)

        binding.recyclerViewFragmentCollection.layoutManager = GridLayoutManager(
            context, 1, GridLayoutManager.VERTICAL, false
        )

        collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewFragmentCollection.adapter = ItemCollectionAdapter(it.data!!)
        })

        collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner,
            Observer { nomList ->
                Log.v(TAG, nomList.data?.size.toString())
                Log.v(TAG, collectionViewModel.nomenclatureListLiveData.value?.data.toString())
                adapter = ItemCollectionAdapter(nomList.data!!)
            })

        categoryId?.let { onCategorySelected(it) }

    }

    inner class ItemCollectionAdapter(var collectionList: List<NomenclatureCollection>) :
        RecyclerView.Adapter<ItemCollectionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCollectionHolder {
            val inflater = LayoutInflater.from(context)
            val binding = ItemCollectionViewHolderBinding.inflate(inflater, parent, false)
            return ItemCollectionHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemCollectionHolder, position: Int) {
            return holder.bind(collectionList[position])

        }

        override fun getItemCount(): Int {
            return collectionList.size
        }

    }

    inner class ItemCollectionHolder(
        private var binding: ItemCollectionViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var currentCollection: NomenclatureCollection

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(nomenclatureCollection: NomenclatureCollection) {
            currentCollection = nomenclatureCollection
            binding.tvItemCollectionViewHolder.text = currentCollection.title

            when (currentCollection.title) {
                "Панели с офсетной печатью" -> {
                    binding.ivCollection.setImageResource(R.drawable.paneli_ofsetnaya)
                    binding.tvItemCollectionDescription.text =
                        "Устойчивы к воздействию ультрафиолетовых лучей и к механическим воздействиям."
                }
                "Панели с термопереводной печатью" -> {
                    binding.ivCollection.setImageResource(R.drawable.paneli_termoperev)
                    binding.tvItemCollectionDescription.text =
                        "Используются для внутренней отделки стен и потолков помещений различного назначения."
                }
                "Панели с цифровой печатью" -> {
                    // Todo: На сайте другая коллекция и друга картинка
                    binding.ivCollection.setImageResource(R.drawable.paneli_matov_white)
                    binding.tvItemCollectionDescription.text =
                        "!!!"
                }
                "Панели ламинированные" -> {
                    binding.ivCollection.setImageResource(R.drawable.paneli_laminirov)
                    binding.tvItemCollectionDescription.text =
                        "Самые ударопрочные и износостойкие декоративные панели ПВХ"
                }
                "Панели потолочные" -> {
                    binding.ivCollection.setImageResource(R.drawable.paneli_pvh_potoloch)
                    binding.tvItemCollectionDescription.text =
                        "Благодаря устойчивости к влаге их можно использовать как в жилых помещениях, так и в ванных комнатах."
                }
                else -> {

                }
            }
        }

        override fun onClick(v: View?) {
            callbacks?.onCollectionSelected(currentCollection)
        }

    }

    //отработка клика по категории
    private fun onCategorySelected(id: String) {
        Log.v(TAG, id)
        collectionViewModel.getNomenclatureCollectionByCategoryId(id)
        collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewFragmentCollection.adapter = ItemCollectionAdapter(it.data!!)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}