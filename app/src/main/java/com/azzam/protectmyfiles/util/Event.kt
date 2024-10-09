package com.azzam.protectmyfiles.util

import androidx.lifecycle.Observer

class Event<T>(private var data: T? = null) {

    private var consumed: Boolean = false

    fun isConsumed(): Boolean = consumed

    fun isNotConsumed(): Boolean = !consumed

    fun consume(): T? {
        consumed = true
        return data
    }

    fun peekContent(): T? = data
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(t: Event<T>) {
        t?.let { event ->
            if (event.isNotConsumed())
                event.consume()?.let { data ->
                    onEventUnhandledContent(data)
                }
        }
    }
}