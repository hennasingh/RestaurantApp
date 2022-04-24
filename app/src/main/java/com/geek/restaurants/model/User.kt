package com.geek.restaurants.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class User (
    @PrimaryKey var _id: String = "",

    var borough: String? = "",

    var email: String = "",

    var friends: RealmList<Friend> = RealmList(),

    var sharedRestaurantId: RealmList<ObjectId> = RealmList(),

    var name: String = ""
): RealmObject() {}
