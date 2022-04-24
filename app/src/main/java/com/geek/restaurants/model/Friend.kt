package com.geek.restaurants.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass(embedded = true)
open class Friend(
    var _id: String = "",

    var date: Date = Date(),

    var email: String = "",

    var name: String = ""

): RealmObject() {}
