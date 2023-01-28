package io.github.highright1234.minipaper.kommand.processor

import io.github.highright1234.minipaper.isolation.ChatIsolation
import io.github.highright1234.minipaper.isolation.IsolationSetting
import io.github.highright1234.minipaper.isolation.TabListIsolation
import io.github.highright1234.minipaper.kommand.SubKommand
import io.github.highright1234.shotokonoko.monun.suspendingExecutes
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode

object Isolation: SubKommand {
    private val options = mapOf<String, IsolationSetting>(
        "chat" to ChatIsolation,
        "tab_list" to TabListIsolation
    )

    override fun init(node: KommandNode) {
        node.then("isolation") {
            then("option" to dynamicByMap(options)) {
                then("bypass") {
                    requires { isPlayer }
                    suspendingExecutes { context ->
                        val option: IsolationSetting by context
                    }
                    then("enable") {
                        suspendingExecutes { context ->
                            val option: IsolationSetting by context

                        }
                    }
                    then("disable") {
                        suspendingExecutes { context ->
                            val option: IsolationSetting by context

                        }
                    }
                }
            }
        }
    }
}