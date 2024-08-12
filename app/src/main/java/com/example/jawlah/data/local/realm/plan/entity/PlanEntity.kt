package com.example.jawlah.data.local.realm.plan.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class PlanEntity: RealmObject {
    @PrimaryKey
    var id: String = ""
    var hasBudget: Boolean = false
    var name: String = ""
    var distenations: RealmList<String> = realmListOf()
    var startDate: Long = 0L
    var endDate: Long = 0L
    var places: RealmList<PlaceEntity> = realmListOf()
}

class PlaceEntity: EmbeddedRealmObject {
    var planId: String = ""
    var name: String = ""
    var description: String = ""
    var locationUrl: String = ""
    private var placeType: String = PlaceType.Place.name
    var type: PlaceType get() = PlaceType.valueOf(placeType)
    set(value) {
        placeType = value.name
    }
}

enum class PlaceType {
    Place,
    Activity,
    Lodging,
    Other
}