package com.geek.restaurants.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Restaurant (
    @PrimaryKey var _id: ObjectId? = null,
    var address: RestaurantAddress? = null,
    var borough: String? = null,
    var cuisine: String? = null,
    var name: String? = null,
    var restaurant_id: String? = null,
    var reviews: RealmList<Reviews> = RealmList()
): RealmObject()
