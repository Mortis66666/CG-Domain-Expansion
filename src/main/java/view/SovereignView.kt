package view

import com.codingame.gameengine.module.entities.*
import domainExpansion.*

class SovereignView(
    private val sovereign: Sovereign,
    private val graphics: GraphicEntityModule,
    colorToken: Int
) {
    lateinit var group: Group;
    lateinit var circle: Circle;

    init {
        sovereign.setView(this)
        group = graphics.createGroup()

        circle = graphics.createCircle()
            .setX(sovereign.on.x * Constant.CELL_SIZE + Constant.CELL_SIZE / 2)
            .setY(sovereign.on.y * Constant.CELL_SIZE + Constant.CELL_SIZE / 2)
            .setRadius(Constant.CELL_SIZE / 2 - 5)
            .setFillColor(colorToken)
            .setLineWidth(0.0)

        group.add(circle)
    }

    fun move(x: Int, y: Int, progress: Double) {
        circle.x = x * Constant.CELL_SIZE + Constant.CELL_SIZE / 2
        circle.y = y * Constant.CELL_SIZE + Constant.CELL_SIZE / 2

        graphics.commitEntityState(progress, circle)
    }

}