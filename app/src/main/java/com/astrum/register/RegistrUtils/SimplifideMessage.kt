package com.astrum.register.RegistrUtils

import org.json.JSONException
import org.json.JSONObject

object SimplifideMessage {
    fun get(StringMessage: String): HashMap<String, String> {
        val message = HashMap<String, String>()
        val jsonObject = JSONObject(StringMessage)

        try {
            val jsonMessage = jsonObject.getJSONObject("message")
            jsonMessage.keys().forEach { message[it] = jsonMessage.getString(it) }
        } catch (e: JSONException) {
            message["message"] = jsonObject.getString("message")
        }
        return message
    }
}