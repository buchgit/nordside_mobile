package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.viewmodel.NomenclatureListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentNomenclatureList : Fragment() {

    private val TAG = FragmentNomenclatureList::class.simpleName
    private lateinit var recyclerView: RecyclerView
    private var adapter: ItemCollectionAdapter = ItemCollectionAdapter(emptyList())
    private val collectionViewModel : NomenclatureListViewModel by viewModels()
    private lateinit var collectionId: String
    private lateinit var collection_title: String
    private var callbacks: CallbackNomenclature? = null
    private lateinit var textView: TextView

    companion object {
        fun createArgs(nomenclatureCollection: NomenclatureCollection) = bundleOf(
            "id" to nomenclatureCollection.id,
            "collection_title" to nomenclatureCollection.title
        )
    }

    interface CallbackNomenclature {
        fun onNomenclatureSelected(nomenclatureWithPrice: PriceTable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectionId = arguments?.getString("id") ?: "100008" //TODO    подумать, убрать ли хардкор
        collection_title = arguments?.getString("collection_title")?:""

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nomenclature_list, container, false)
        textView = view.findViewById(R.id.tw_fragment_nomenclature)
        recyclerView = view.findViewById(R.id.recycler_view_fragment_nomenclature) as RecyclerView
        //recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //код ниже - для поворота экрана
//         collectionViewModel.getNomenclatureByCollection(collectionId) //TODO заменил на персональный лист номенклатуры

//         collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner, Observer {
//             recyclerView.adapter = ItemCollectionAdapter(it.data!!)
//         })

        collectionViewModel.getPersonalNomenclatureListByCollection(collectionId)
        collectionViewModel.nomenclaturePersonalListLiveData.observe(viewLifecycleOwner, Observer {
                  recyclerView.adapter = ItemCollectionAdapter(it.data!!)
            })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         collectionViewModel.getNomenclatureByCollection(collectionId) //TODO заменил на персональный лист номенклатуры
//         collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner,
//             Observer { nomList ->
//                 adapter = ItemCollectionAdapter(nomList.data!!)
//             })

        collectionViewModel.getPersonalNomenclatureListByCollection(collectionId)
        collectionViewModel.nomenclaturePersonalListLiveData.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = ItemCollectionAdapter(it.data!!)
        })


        onCollectionSelected(collectionId)
    }

    inner class ItemCollectionAdapter(var collectionList: List<PriceTable>) :
        RecyclerView.Adapter<ItemCollectionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCollectionHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.nomenclature_list_view_holder, parent, false)
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
        var cardView: CardView = itemView.findViewById(R.id.card_view_fragment_nomenclature)
        private var textView_title: TextView =
            itemView.findViewById(R.id.tw_nomenclature_view_holder_1)
        private var textView_lenght: TextView =
            itemView.findViewById(R.id.tw_nomenclature_view_holder_2)
        private var textView_count: TextView =
            itemView.findViewById(R.id.tw_nomenclature_view_holder_3)
        private var button_plus: Button =
            itemView.findViewById(R.id.button_plus)
        private var button_minus: Button =
            itemView.findViewById(R.id.button_minus)

        private lateinit var currentNomenclatureWithPrice: PriceTable
        fun bind(nomenclatureWithPrice: PriceTable) {
            currentNomenclatureWithPrice = nomenclatureWithPrice
            val currentNomenclature = currentNomenclatureWithPrice.nomenclature!!
            textView_title.setText(currentNomenclature.title)
            textView_lenght.setText(currentNomenclature.length.toString())

        }

        init {
            itemView.setOnClickListener(this)
            button_plus.setOnClickListener(View.OnClickListener {
                //TODO
            })
            button_minus.setOnClickListener(View.OnClickListener {
                //TODO
            })
        }

        override fun onClick(v: View?) {
            callbacks?.onNomenclatureSelected(currentNomenclatureWithPrice)
        }

    }

    private fun onCollectionSelected(id: String) {

//         collectionViewModel.getNomenclatureByCollection(id) //TODO заменил на персональный лист номенклатуры
//         collectionViewModel.nomenclatureListLiveData.observe(viewLifecycleOwner,
//             Observer { nomList ->
//                 adapter = ItemCollectionAdapter(nomList.data!!)
//             })

        collectionViewModel.getPersonalNomenclatureListByCollection(collectionId)
        collectionViewModel.nomenclaturePersonalListLiveData.observe(viewLifecycleOwner, Observer {
            adapter = ItemCollectionAdapter(it.data!!)
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CallbackNomenclature
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}