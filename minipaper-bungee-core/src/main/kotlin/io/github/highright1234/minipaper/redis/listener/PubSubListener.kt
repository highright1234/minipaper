package io.github.highright1234.minipaper.redis.listener

import io.lettuce.core.pubsub.RedisPubSubAdapter

object PubSubListener : RedisPubSubAdapter<String, String>() {
    override fun message(channel: String, message: String) {

    }
}