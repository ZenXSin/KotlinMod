package zinxs

import mindustry.content.Blocks

object TT {
    @JvmStatic
    fun loadTechTree() {
        TechTree.addToNode(Block.临时协议签署所!!, { TechTree.node(Blocks.duo) })
    }
}