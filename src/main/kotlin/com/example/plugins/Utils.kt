package com.example.plugins

import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.JSONB

fun Any.toJsonString(): String = ObjectMapper().writeValueAsString(this)
fun Any.toJsonB() = JSONB.jsonb(this.toJsonString())
