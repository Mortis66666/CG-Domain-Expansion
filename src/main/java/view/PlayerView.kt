package view

import com.codingame.game.Player
import com.codingame.gameengine.module.entities.Circle
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.codingame.gameengine.module.entities.Sprite
import com.codingame.gameengine.module.entities.Text


class PlayerView(private val player: Player, private val graphics: GraphicEntityModule) {
    private val group = graphics.createGroup()
    private var msg: Text = graphics.createText(" ")

    init {
        player.view = this
        player.message = msg
        drawHud()
    }

    private fun drawHud() {
        // Draw the HUD
        val x = if (player.index == 0) 280 else 1920 - 280
        val y = if (player.index == 0) 220 else 1080 - 220 - 140

        graphics
            .createCircle()
            .setRadius(70)
            .setX(x)
            .setY(y)
            .setLineWidth(0.0)
            .setFillColor(player.colorToken)
            .setZIndex(-1)

        graphics
            .createCircle()
            .setRadius(60)
            .setX(x)
            .setY(y)
            .setLineWidth(0.0)
            .setFillColor(0xffffff)
            .setZIndex(-1)

        val text = graphics.createText(player.nicknameToken)
            .setX(x)
            .setY(y + 120)
            .setZIndex(20)
            .setFontSize(40)
            .setFillColor(player.colorToken)
            .setAnchor(0.5)

        val avatarMask: Circle = graphics.createCircle()
            .setX(x)
            .setY(y)
            .setRadius(55)

        val avatar: Sprite = graphics.createSprite()
            .setX(x)
            .setY(y)
            .setZIndex(20)
            .setImage(player.avatarToken)
            .setAnchor(0.5)
            .setBaseHeight(110)
            .setBaseWidth(110)
            .setMask(avatarMask)

        player.message.setX(x)
            .setY(y + 200)
            .setZIndex(20)
            .setFontSize(30)
            .setFillColor(0xffffff)
            .setAnchor(0.5)

        group.add(text, avatar, msg)
    }
}