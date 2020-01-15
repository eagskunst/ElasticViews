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
import android.util.AttributeSet
import android.view.View.OnClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ElasticFloatingActionButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FloatingActionButton(context, attrs, defStyle) {

  var scale = 0.9f
  var duration = 500

  private var onClickListener: OnClickListener? = null
  private var onFinishListener: ElasticFinishListener? = null

  init {
    onCreate()
    when {
      attrs != null && defStyle != 0 -> getAttrs(attrs, defStyle)
      attrs != null -> getAttrs(attrs)
    }
  }

  private fun onCreate() {
    this.isClickable = true
    super.setOnClickListener {
      elasticAnimation(this) {
        setDuration(this@ElasticFloatingActionButton.duration)
        setScaleX(this@ElasticFloatingActionButton.scale)
        setScaleY(this@ElasticFloatingActionButton.scale)
        setOnFinishListener { invokeListeners() }
      }.doAction()
    }
  }

  private fun getAttrs(attrs: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticFloatingActionButton)
    setTypeArray(typedArray)
  }

  private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
    val typedArray =
      context.obtainStyledAttributes(attrs, R.styleable.ElasticFloatingActionButton, defStyle, 0)
    setTypeArray(typedArray)
  }

  private fun setTypeArray(typedArray: TypedArray) {
    this.scale = typedArray.getFloat(R.styleable.ElasticFloatingActionButton_fabutton_scale, scale)
    this.duration =
      typedArray.getInt(R.styleable.ElasticFloatingActionButton_fabutton_duration, duration)
  }

  override fun setOnClickListener(listener: OnClickListener?) {
    this.onClickListener = listener
  }

  fun setOnFinishListener(listener: ElasticFinishListener) {
    this.onFinishListener = listener
  }

  private fun invokeListeners() {
    this.onClickListener?.onClick(this)
    this.onFinishListener?.onFinished()
  }

  /** Builder class for creating [ElasticFloatingActionButton]. */
  class Builder(context: Context) {
    private val elasticFloatingButton = ElasticFloatingActionButton(context)

    fun setScale(value: Float) = apply { this.elasticFloatingButton.scale = value }
    fun setDuration(value: Int) = apply { this.elasticFloatingButton.duration = value }
    fun setOnClickListener(block: () -> Unit) = apply {
      val onClickListener = OnClickListener { block() }
      this.elasticFloatingButton.setOnClickListener(onClickListener)
    }

    fun setOnClickListener(value: OnClickListener) = apply {
      this.elasticFloatingButton.setOnClickListener(value)
    }

    fun setOnFinishListener(block: () -> Unit) = apply {
      val onElasticFinishListener = object : ElasticFinishListener {
        override fun onFinished() {
          block()
        }
      }
      this.elasticFloatingButton.setOnFinishListener(onElasticFinishListener)
    }

    fun setOnFinishListener(value: ElasticFinishListener) = apply {
      this.elasticFloatingButton.setOnFinishListener(value)
    }

    fun build() = this.elasticFloatingButton
  }
}
