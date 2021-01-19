package com.example.coronawarnpremium.ui.contactbook

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RecyclerTouchListener"
class ItemTouchListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener) : RecyclerView.SimpleOnItemTouchListener() {
    interface OnRecyclerClickListener{
        fun OnItemClick(view: View, position: Int)
        fun OnItemLongClick(view: View, position: Int)
    }

    private val gestureDetector = GestureDetectorCompat(context, object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            if (childView != null) {
                listener.OnItemClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.v(TAG, ".onInterceptTouchEvent: starts $e")
        return gestureDetector.onTouchEvent(e)
    }
}