package com.naveed.samples.apputils.events

/**
 * Created by naveedali on 11/12/17.
 */

interface BaseEvent {
    val eventId: Int
    val status: Boolean
    val message: String
}
