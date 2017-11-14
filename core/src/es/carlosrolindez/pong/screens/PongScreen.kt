package es.carlosrolindez.pong.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.PongGame
import es.carlosrolindez.pong.overlays.GUIOverlay
import es.carlosrolindez.pong.utils.BACKGROUND_COLOR


class PongScreen(private val game: PongGame):AbstractScreen() {

    companion object {
        val TAG: String = PongScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch
    internal var level = Level(this)
    private var gui = GUIOverlay(this)

    internal var scorePlayer1 = 0
    internal var scorePlayer2 = 0


    override fun show() {
        spriteBatch = SpriteBatch()
        inputProcessor = this.inputProcessor


    }

    override fun render(delta: Float) {
        gui.update(delta)
        level.update(delta)

        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        level.render(spriteBatch)
        gui.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width,height)
        gui.resize(width,height)
    }

    override fun dispose() {
        spriteBatch.dispose()
        level.dispose()
        gui.dispose()
    }

    override var inputProcessor: InputProcessor?
        get() = gui
        set(value) {
            Gdx.input.inputProcessor = value
        }
}