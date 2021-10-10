package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.viewmodel.FragmentCollectionViewModel

class FragmentCollection : Fragment(), FragmentCategory.Callback {

    private val TAG = "${FragmentCollection::class.simpleName} ###"
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private var adapter: ItemCollectionAdapter = ItemCollectionAdapter(emptyList())
    private val collectionViewModel by viewModels<FragmentCollectionViewModel>()
    private var callbacks: Callback? = null
    private var categoryId: String? = null

    companion object {
        fun newInstance(): FragmentCollection {
            return FragmentCollection()
        }
    }

    interface Callback {
        fun onCollectionSelected(id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = requireArguments().getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)
        textView = view.findViewById(R.id.tw_fragment_collection)
        recyclerView = view.findViewById(R.id.recycler_view_fragment_collection) as RecyclerView
        //recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //код ниже - для поворота экрана
        collectionViewModel.nomenclatureList?.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = ItemCollectionAdapter(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionViewModel.nomenclatureList?.observe(viewLifecycleOwner,
            Observer { nomList ->
                Log.v(TAG, nomList.size.toString())
                Log.v(TAG, collectionViewModel.nomenclatureList!!.value.toString())
                adapter = ItemCollectionAdapter(nomList)
            })

        categoryId?.let { onCategorySelected(it) }

    }

    inner class ItemCollectionAdapter(var collectionList: List<NomenclatureCollection>) :
        RecyclerView.Adapter<ItemCollectionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCollectionHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_collection_view_holder, parent, false)
            return ItemCollectionHolder(view)
        }

        override fun onBindViewHolder(holder: ItemCollectionHolder, position: Int) {
            return holder.bind(collectionList[position])

        }

        override fun getItemCount(): Int {
            return collectionList.size
        }

    }

    inner class ItemCollectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var cardView: CardView = itemView.findViewById(R.id.card_view_fragment_item_collection)
        var textView: TextView = itemView.findViewById(R.id.tw_item_collection_view_holder)
        lateinit var nomeCollection: NomenclatureCollection

        fun bind(nomenclatureCollection: NomenclatureCollection) {
            nomeCollection = nomenclatureCollection
            textView.setText(nomeCollection.title)
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.onCollectionSelected(nomeCollection.id.toString())
        }

    }

    //отработка клика по категории
    override fun onCategorySelected(id: String) {
        Log.v(TAG, id)
        collectionViewModel.getNomenclatureCollectionByCategoryId(id)
        collectionViewModel.nomenclatureList?.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = ItemCollectionAdapter(it)
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