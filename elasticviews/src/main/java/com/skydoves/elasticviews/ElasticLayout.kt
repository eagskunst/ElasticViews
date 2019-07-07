/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 skydoves
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.skydoves.elasticviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

@Suppress("unused")
class ElasticLayout : FrameLayout {

  private lateinit var view: View
  private var listener: OnClickListener? = null
  private var onFinishListener: ElasticFinishListener? = null

  private var round = 3
  private var scale = 0.9f
  private var color = ContextCompat.getColor(context, R.color.colorPrimary)
  private var duration = 500

  constructor(context: Context) : super(context) {
    onCreate()
  }

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    onCreate()
    getAttrs(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
    onCreate()
    getAttrs(attributeSet, defStyle)
  }

  private fun onCreate() {
    val inflaterService = Context.LAYOUT_INFLATER_SERVICE
    val layoutInflater = context.getSystemService(inflaterService) as LayoutInflater
    view = layoutInflater.inflate(R.layout.elasticlayout, this, false)
    addView(view)
    view.isClickable = true
  }

  private fun getAttrs(attrs: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticLayout)
    setTypeArray(typedArray)
  }

  private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticLayout, defStyle, 0)
    setTypeArray(typedArray)
  }

  private fun setTypeArray(typedArray: TypedArray) {
    val bgShape = view.background as GradientDrawable

    round = typedArray.getInt(R.styleable.ElasticLayout_layout_round, round)
    bgShape.cornerRadius = round.toFloat()

    color = typedArray.getInt(R.styleable.ElasticLayout_layout_backgroundColor, color)
    bgShape.setColor(color)

    scale = typedArray.getFloat(R.styleable.ElasticLayout_layout_scale, scale)

    duration = typedArray.getInt(R.styleable.ElasticLayout_layout_duration, duration)
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_UP) {
      if (listener != null || onFinishListener != null) {
        if (view.scaleX == 1f) {
          elasticAnimation(this) {
            setDuration(duration)
            setScaleX(scale)
            setScaleY(scale)
            setOnFinishListener(object : ElasticFinishListener {
              override fun onFinished() {
                onClick()
              }
            })
          }.doAction()
        }
      }
    }
    return super.dispatchTouchEvent(event)
  }

  override fun setOnClickListener(listener: OnClickListener?) {
    this.listener = listener
  }

  fun setOnFinishListener(listener: ElasticFinishListener) {
    this.onFinishListener = listener
  }

  private fun onClick() {
    listener?.onClick(this)
    onFinishListener?.onFinished()
  }
}
