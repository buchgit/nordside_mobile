package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.viewmodel.FragmentCategoryViewModel

class FragmentCategory: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val EMPTY_TITLE = "empty title of category"
    private val adapter: CategoryAdapter = CategoryAdapter(emptyList())
    private val categoryViewModel by viewModels<FragmentCategoryViewModel>()
    private val TAG = FragmentCategory::class.simpleName
    private var callbacks: Callback?=null

    companion object{
        fun newInstance(): FragmentCategory {
            return FragmentCategory()
        }
    }

    interface Callback{
        fun onCategorySelected(id:String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category,container,false)
        recyclerView = view.findViewById(R.id.recycler_view_fragment_category) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        categoryViewModel.categoryList.observe(viewLifecycleOwner,
        Observer {
            recyclerView.adapter = CategoryAdapter(it)
        })
        return view
    }



    //сюда прилетает вьюха (через конструктор) и модель (через bind), вьюху заполняем по модели
    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener {

        private var cardView:CardView = itemView.findViewById(R.id.card_view_fragment_category)
        private var textView:TextView = itemView.findViewById(R.id.tw_category_view_holder)
        private var category: Category? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${category?.id} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCategorySelected(category?.id.toString())
        }

        fun bind(param: Category?){
            category = param
            textView.text = category?.title ?: EMPTY_TITLE
        }

    }

    //adapter
    inner class CategoryAdapter(var categoryList: List<Category>): RecyclerView.Adapter<CategoryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            val inflater = LayoutInflater.from(context)
            val view:View = inflater.inflate(R.layout.category_view_holder,parent,false)
            return CategoryHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            holder.bind(categoryList[position])
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

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