package domainExpansion

class Wall (val a: Cell, val b: Cell) {

    init {
    }

    override fun toString(): String {
        return "$a $b"
    }
}