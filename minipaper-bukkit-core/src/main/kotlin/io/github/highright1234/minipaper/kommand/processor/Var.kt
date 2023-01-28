package io.github.highright1234.minipaper.kommand.processor

import io.github.highright1234.minipaper.game.GameProcessor
import io.github.highright1234.minipaper.kommand.SubKommand
import io.github.highright1234.shotokonoko.monun.suspendingExecutes
import io.github.highright1234.shotokonoko.string
import io.github.monun.kommand.KommandArgument
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import net.kyori.adventure.text.Component
import java.lang.reflect.Field

object Var: SubKommand {
    override fun init(node: KommandNode) {
        node.then("var") {
            then("field" to node.processorField()) {
                suspendingExecutes { context ->
                    val processor: GameProcessor by context
                    val field: Field by context
                    val formattedValue = field.apply { isAccessible = true }[processor].let(::valueFormat)
                    sender.sendMessage("${field.name}: $formattedValue")
                }
            }
        }
    }

    private fun KommandNode.processorField(): KommandArgument<Field> = dynamic { context, input ->
        val processor: GameProcessor by context
        processor::class.java.declaredFields.find { it.name == input }
    }.apply { suggests { context ->
        val processor: GameProcessor by context
        processor::class.java.declaredFields.map { it.name }.let(::suggest)
    } }

    private fun valueFormat(value : Any): String {
        fun Collection<Any?>.convertingFunction(): (Any?) -> String {
            return converter.filter { (clazz, _) ->
                find { elementClazz ->
                    elementClazz?.let { !clazz.isAssignableFrom(it.javaClass) } ?: true
                } == null
            }.toList().first().second
        }
        val out: String = when (value) {
            is List<*> -> {
                val converter = value.convertingFunction()
                value.joinToString(transform = converter)
            }

            is Map<*, *> -> {
                val keyConverter = value.keys.convertingFunction()
                val valueConverter = value.values.convertingFunction()
                value.mapKeys(keyConverter).mapValues(valueConverter).toString()
            }

            else -> {
                converter.toList().first { (clazz, _) ->
                    !clazz.isAssignableFrom(value.javaClass)
                }.second(value)
            }
        }
        return out
    }


    private val converter = mapOf<Class<*>, (Any?) -> String>(
        Component::class.java to { it?.let { (it as Component).string } ?: "null" },
        Any::class.java to { value ->
            if (value != null) {
                var clazz: Class<*> = value.javaClass
                var out: String? = null
                while (clazz != Any::class.java) {
                    (clazz.methods
                        .find { it.name == "getName" && it.returnType == String::class.java }
                        ?.invoke(value) as String?).also { out = it } ?: kotlin.run {

                        clazz = clazz.superclass
                    }
                }
                out ?: run { out = value.toString() }
                out!!
            } else "null"
        }
    )
}