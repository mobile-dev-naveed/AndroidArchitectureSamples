package com.naveed.samples.apputils.events

/**
 * Created by naveedali on 9/9/17.
 */

class SimpleEvent : BaseEvent {

    override var eventId: Int = 0
    override var message: String
    override var status: Boolean = false

    constructor(pStatus: Boolean, pMessage: String) {
        this.status = pStatus
        this.message = pMessage

    }

    constructor(pStatus: Boolean, pId: Int, pMessage: String) {
        this.status = pStatus
        this.eventId = pId
        this.message = pMessage

    }
}
