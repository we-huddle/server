package com.example.plugins

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jooq.JSONB

val gsonMapper: Gson = GsonBuilder().create()
fun Any.toJsonString(): String = gsonMapper.toJson(this)
fun Any.toJsonB() = JSONB.jsonb(this.toJsonString())
fun <T> String.toObject(classOf: Class<T>): T = gsonMapper.fromJson(this, classOf)
