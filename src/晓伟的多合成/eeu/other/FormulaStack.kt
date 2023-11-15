package 晓伟的多合成.eeu.other

import arc.struct.Seq
import mindustry.gen.Building
import mindustry.world.Block


class FormulaStack {
    var formulas: Seq<Formula>

    constructor() {
        formulas = Seq()
    }

    constructor(formulas: Seq<Formula>) {
        this.formulas = formulas
    }

    fun getFormula(index: Int): Formula {
        return formulas[index]
    }

    fun setFormula(index: Int, formula: Formula) {
        formulas[index] = formula
    }

    fun addFormula(formula: Formula) {
        formulas.add(formula)
    }

    fun outputItems(index: Int): Boolean {
        return getFormula(index).outputItems != null
    }

    fun outputItems(): Boolean {
        for (f in formulas) {
            if (f.outputItems != null) return true
        }
        return false
    }

    fun trigger(build: Building?) {
        for (f in formulas) {
            f.trigger(build)
        }
    }

    fun apply(block: Block?) {
        for (f in formulas) {
            f.apply(block)
        }
    }

    fun size(): Int {
        return formulas.size
    }

    companion object {
        fun with(vararg formulas: Formula): FormulaStack {
            return FormulaStack(Seq(formulas))
        }
    }
}