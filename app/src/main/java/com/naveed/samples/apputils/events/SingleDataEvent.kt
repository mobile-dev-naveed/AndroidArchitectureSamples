package com.naveed.samples.apputils.events

/**
 * Created by naveedali on 9/9/17.
 */

class SingleDataEvent<T>(pStatus: Boolean, pId: Int, pMessage: String, var data: T) : BaseEvent {

    private val simpleEvent: SimpleEvent = SimpleEvent(pStatus, pId, pMessage)


    override val eventId: Int
        get() = simpleEvent.eventId

    override val status: Boolean
        get() = simpleEvent.status

    override val message: String
        get() = simpleEvent.message


}
