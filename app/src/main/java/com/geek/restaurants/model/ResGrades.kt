package com.geek.restaurants.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass(embedded = true)
open class ResGrades(
    var date: Date? = null,
    var grade: String? = null,
    var score: Long? = null
): RealmObject() {}
