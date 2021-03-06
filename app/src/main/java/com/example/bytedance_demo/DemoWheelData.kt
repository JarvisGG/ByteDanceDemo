package com.example.bytedance_demo

import com.example.bytedance_demo.widget.IWheelEntity

class DemoWheelData(
    override val wheelText: String?
) : IWheelEntity

val demoList by lazy {
    val list = arrayListOf<IWheelEntity>()
    for (i in 1 until 100) {
        list.add(DemoWheelData(i.toString()))
    }
    list
}