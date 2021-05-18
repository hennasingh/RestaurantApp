package com.geek.restaurants.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Restaurant (
    @PrimaryKey var _id: ObjectId? = null,
    var address: ResAddress? = null,
    var borough: String? = null,
    var cuisine: String? = null,
    var grades: RealmList<ResGrades> = RealmList(),
    var name: String? = null,
    var restaurant_id: String? = null
): RealmObject() {}
