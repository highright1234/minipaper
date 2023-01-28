package io.github.highright1234.minipaper.kommand

import io.github.monun.kommand.node.KommandNode

interface SubKommand {
    fun init(node: KommandNode)
}