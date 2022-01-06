package com.example.nordside_mobile.ui.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R

class PagerDecorator : RecyclerView.ItemDecoration() {

    private var paintStroke: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#C4C4C4")
    }

    private val paintFillGreen = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#00B169")
    }

    private val paintFillWhite = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#D3D3D3")
    }

    // save the center coordinates of all indicators
    private val indicators = mutableListOf<Pair<Float, Float>>()

    private val indicatorRadius = 15f
    private val indicatorPadding = indicatorRadius * 4

    private var activeIndicator = 0
    private var isInitialized = false

    // create three indicators for three slides
    override fun onDrawOver(canvas: Canvas,
                            parent: RecyclerView,
                            state: RecyclerView.State) {

        if (!isInitialized) {
            setupIndicators(parent)
        }

        // draw three indicators with stroke style
        parent.adapter?.let {
            with(canvas) {
                drawCircle(indicators[0].first, indicators[0].second)
                drawCircle(indicators[1].first, indicators[1].second)
                drawCircle(indicators[2].first, indicators[2].second)
                drawCircle(indicators[3].first, indicators[3].second)
            }

            val visibleItem = (parent.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()

            if (visibleItem >= 0) {
                activeIndicator = visibleItem
            }

            // paint over the needed circle
            when (activeIndicator) {
                0 -> canvas.drawCircle(indicators[0].first, indicators[0].second, true)
                1 -> canvas.drawCircle(indicators[1].first, indicators[1].second, true)
                2 -> canvas.drawCircle(indicators[2].first, indicators[2].second, true)
                3 -> canvas.drawCircle(indicators[3].first, indicators[3].second, true)
            }
        }
    }

    private fun Canvas.drawCircle(x: Float, y: Float, isActive: Boolean = false) {
        if (isActive){
            drawCircle(x, y, indicatorRadius,  paintFillGreen)
        } else {
            drawCircle(x, y, indicatorRadius,  paintFillWhite)
//            drawCircle(x, y, indicatorRadius,  paintStroke)
        }

    }

    private fun setupIndicators(recyclerView: RecyclerView) {
        isInitialized = true

        val indicatorTotalWidth = indicatorPadding * 3f
        val indicatorPosX = (recyclerView.width - indicatorTotalWidth) / 2f
        val indicatorPosY = recyclerView.height - indicatorRadius * 3f


        indicators.add(indicatorPosX to indicatorPosY)
        indicators.add((indicatorPosX + indicatorPadding) to indicatorPosY)
        indicators.add((indicatorPosX + indicatorPadding * 2) to indicatorPosY)
        indicators.add((indicatorPosX + indicatorPadding * 3) to indicatorPosY)
    }
}