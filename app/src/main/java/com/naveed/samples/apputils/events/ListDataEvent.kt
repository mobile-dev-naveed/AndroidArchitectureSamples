package com.naveed.samples.apputils.events

/**
 * Created by naveedali on 9/9/17.
 */

class ListDataEvent<T>(pStatus: Boolean, pId: Int, pMessage: String) : BaseEvent {

    private val simpleEvent: SimpleEvent = SimpleEvent(pStatus, pId, pMessage)
    lateinit var listData: List<T>

    override val status: Boolean
        get() = simpleEvent.status

    override val eventId: Int
        get() = simpleEvent.eventId

    override val message: String
        get() = simpleEvent.message

    constructor(pStatus: Boolean, pId: Int, pMessage: String, listData: List<T>) : this(pStatus, pId, pMessage) {
        this.listData = listData

    }


}
