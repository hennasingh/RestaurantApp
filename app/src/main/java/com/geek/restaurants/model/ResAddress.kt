package com.geek.restaurants.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass(embedded = true)
open class ResAddress(
    var building: String? = null,
    @Required
    var coord: RealmList<Double> = RealmList(),
    var street: String? = null,
    var zipcode: String? = null
): RealmObject() {}