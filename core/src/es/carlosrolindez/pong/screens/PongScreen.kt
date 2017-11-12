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
        val TAG = PongScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch
    internal var level = Level(this)
    private var GUI = GUIOverlay(this)


    override fun hide() {
        super.hide()
    }

    override fun show() {
        spriteBatch = SpriteBatch()
        inputProcessor = this.inputProcessor


    }

    override fun render(delta: Float) {
        GUI.update(delta)
        level.update(delta)


        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        level.render(spriteBatch)
        GUI.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width,height)
        GUI.resize(width,height)
    }

    override fun pause() {
        super.pause()
    }

    override fun resume() {
        super.resume()
    }
    override fun dispose() {
        spriteBatch.dispose()
        level.dispose()
    }

    override var inputProcessor: InputProcessor?
        get() = GUI
        set(value) {Gdx.input.setInputProcessor(value)}
}