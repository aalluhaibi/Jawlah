package com.example.jawlah.data.local.realm.plan.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CategoryEntity: RealmObject {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
}