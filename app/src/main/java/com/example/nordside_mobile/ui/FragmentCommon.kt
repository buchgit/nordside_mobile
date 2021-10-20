package com.example.nordside_mobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nordside_mobile.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCommon : Fragment() {

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

        val currentFragmentPartner =
            childFragmentManager.findFragmentById(R.id.container_fragment_3)
        if (currentFragmentPartner == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container_fragment_3, FragmentPartner.newInstance())
                .commit()
        }

        return view
    }

}
