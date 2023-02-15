package io.github.highright1234.minipaper.game

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import org.bukkit.entity.Player
import java.util.*

interface GameDatastore {

    fun <T> setAsync(key: String, value: T?): Job
    suspend fun <T : Any> set(key: String, value: T?)

    fun plusAsync(key: UUID, value: Int): Job
    suspend fun <T> plus(key: UUID, value: Int): T?

    fun minusAsync(key: UUID, value: Int): Job
    suspend fun <T> minus(key: UUID, value: Int): T?

    fun <T> getAsync(key: String, clazz: Class<T>): Deferred<T?>
    suspend fun <T> get(key: String, clazz: Class<T>): T?

    fun <T> setAsync(key: UUID, value: T?): Job
    suspend fun <T : Any> set(key: UUID, value: T?)

    fun <T> getAsync(key: UUID, clazz: Class<T>): Deferred<T?>
    suspend fun <T> get(key: UUID, clazz: Class<T>): T?


    fun remove(key: String)

    fun <T> getKeysAsync(): Deferred<List<String>>
    suspend fun <T> getKeys(): List<String>

}
