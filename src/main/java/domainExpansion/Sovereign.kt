package domainExpansion

import view.SovereignView

class Sovereign(var on: Cell) {
    private lateinit var view: SovereignView

    init {
        on.occupy(this)
    }

    fun setView(view: SovereignView) {
        this.view = view
    }

    fun travel(path: List<Cell>) {
        if (path.isEmpty()) {
            return
        }

        val end = path.last()
        on.free()
        end.occupy(this)
        on = end

        for (i in path.indices) {
            val cell = path[i]
            view.move(cell.x, cell.y, (i + 1).toDouble() / path.size.toDouble() * 0.8)
        }
    }

    override fun toString(): String {
        return "$on"
    }
}