@file:JvmName("EventReceiver")

package io.sbox.library.extensions

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType


data class ReceiverItem<T>(val type: Any, var item: T?)

private var eventReceiver: HashMap<Any, ArrayList<Pair<Any, Function<*>>>> = HashMap()


fun Any.addEventReceiver(type: Any, listener: Function<*>) {
    val list = eventReceiver.get(this) ?: arrayListOf()
    val pair = type.to(listener)

    if (list.contains(pair)) {
        return
    } else {
        list.filter {
            it.first == type
        }?.forEach {
            if (it.second !is KFunction) {
                removeEventReceiver(it.first, it.second)
            }
        }
        list.add(pair)
    }

    eventReceiver.put(this, list)
}

fun Any.removeEventReceiver(type: Any? = null, listener: Function<*>? = null) {

    if (type == null && listener == null) {
        eventReceiver.remove(this)
    } else {
        try {
            var temp = eventReceiver.clone() as HashMap<Any, ArrayList<Pair<Any, Function<*>>>>

            for ((target, arrayList) in temp) {

                arrayList.forEach {

                    var pair = it
                    if (type == null) {
                        // check listener
                        if (listener == pair.second
                            || listener is Function && pair.second !is KFunction
                        ) {
                            eventReceiver.get(target)?.remove(pair)
                        }

                    } else if (listener == null) {
                        // check type
                        if (type == pair.first) {
                            eventReceiver.get(target)?.remove(pair)
                        }
                    } else {
                        if (type == pair.first) {
                            if (listener == pair.second
                                || listener is Function && pair.second !is KFunction
                            ) {
                                eventReceiver.get(target)?.remove(pair)
                            }

                        }
                    }
                }

                if (arrayList.size == 0) eventReceiver.remove(target)
            }
            temp.clear()

        } catch (e: Exception) {
        }

    }
}

fun Any.dispatcherEvent(type: Any) = dispatcherEvent(type, null)

fun <T : Any> Any.dispatcherEvent(type: Any, item: T?) {

    for ((target, arrayList) in eventReceiver) {

        try {
            arrayList.filter {
                it.first == type
            }?.map {
                it.second
            }?.forEach {
                val func = it
                if (func is KFunction) {
                    val parameters = func.parameters

                    if (parameters.size == 1) {
                        if (parameters.get(0).type.classifier == ReceiverItem::class && parameters.get(
                                0
                            ).type.arguments.size == 1
                        ) {
                            HashMap<KParameter, ReceiverItem<T>>(parameters.size).let {
                                if (parameters.get(0).type.arguments.get(0).type?.javaType == item?.javaClass || item == null) {
                                    it.put(parameters.get(0), ReceiverItem(type, item))
                                    func.callBy(it)
                                }
                            }
                        } else if (parameters.get(0).type.classifier == item?.javaClass?.kotlin || item == null) {
                            if (parameters.get(0).isOptional && item == null) {
                                func.callBy(mapOf())
                            } else {
                                HashMap<KParameter, T?>(parameters.size).let {
                                    it.put(parameters.get(0), item)
                                    func.callBy(it)
                                }
                            }
                        }
                    } else {
                        func.call()
                    }
                } else if (func is Function0) {
                    func.invoke()
//                    removeEventReceiver(type, func)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
