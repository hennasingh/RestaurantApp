package com.geek.restaurants.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Borough (
    @PrimaryKey
    var _id: ObjectId = ObjectId(),

    var borough: String? = "borough",

    var location: String = ""
): RealmObject() {
}