package com.example.nordside_mobile.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nordside_mobile.R

class FragmentCommon : Fragment(), FragmentCategory.Callback {

    private var TAG = FragmentCommon::class.simpleName

    companion object {
        fun newInstance(): FragmentCommon {
            return FragmentCommon()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_common, container, false)

        val currentFragmentCategory =
            childFragmentManager.findFragmentById(R.id.container_fragment_1)
        if (currentFragmentCategory == null) {
            childFragmentManager
                .beginTransaction()
                .add(R.id.container_fragment_1, FragmentCategory.newInstance(), "FRAGMENT_CATEGORY")
                .commit()
        }
        val currentFragmentCollection =
            childFragmentManager.findFragmentById(R.id.container_fragment_2)
        if (currentFragmentCollection == null) {
            childFragmentManager.beginTransaction()
                .add(
                    R.id.container_fragment_2,
                    FragmentCollection.newInstance(),
                    "FRAGMENT_COLLECTION"
                )
                .commit()
        }

        val currentFragmentPartner =
            childFragmentManager.findFragmentById(R.id.container_fragment_3)
        if (currentFragmentPartner == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container_fragment_3, FragmentPartner.newInstance())
                .commit()
        }

        return view
    }


    //проброска клика по категории во фрагмент
    override fun onCategorySelected(id: String) {
//        Log.v(TAG, id)
//        val collectionFragment =
//            childFragmentManager.findFragmentByTag("FRAGMENT_COLLECTION") as FragmentCollection
//        collectionFragment.onCategorySelected(id)
    }

//    override fun onCollectionSelected(id: String) {
//        Log.v(TAG,id)
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.container_fragment_4)
//        var fragmentNomenclature = FragmentNomenclature.newInstance()
//        if (currentFragment==null){
//            fragmentNomenclature = FragmentNomenclature.newInstance()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.container_fragment_4,fragmentNomenclature,"FRAGMENT_NOMENCLATURE")
//                .commit()
//        }else{
//           var fragment = supportFragmentManager.findFragmentByTag("FRAGMENT_NOMENCLATURE")
//            if (fragment == null){
//                fragmentNomenclature = FragmentNomenclature.newInstance()
//            }
//        }
//        fragmentNomenclature.onCollectionSelected(id)
//        Log.v(TAG,id)
//        val fragment = childFragmentManager.findFragmentByTag("FRAGMENT_NOMENCLATURE") as FragmentNomenclature
//        fragment.onCollectionSelected(id)
    //   }
}