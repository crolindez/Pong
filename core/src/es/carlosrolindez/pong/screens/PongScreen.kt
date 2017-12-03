package es.carlosrolindez.pong.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.net.Network
import es.carlosrolindez.pong.net.NetworkClient
import es.carlosrolindez.pong.net.NetworkServer
import es.carlosrolindez.pong.overlays.ConfigurationStage
import es.carlosrolindez.pong.overlays.GUIOverlay
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.BACKGROUND_COLOR
import es.carlosrolindez.pong.utils.GAMEOVER_SCORE
import es.carlosrolindez.pong.utils.GamePreferences


class PongScreen :ScreenAdapter() {

    companion object {
        val TAG: String = PongScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch

    internal var netServer = NetworkServer()
    internal var netClient = NetworkClient()

    lateinit internal var level : Level
    lateinit internal var gui : GUIOverlay
    lateinit internal var configurationStage : ConfigurationStage

    internal var scorePlayer1 = 0
    internal var scorePlayer2 = 0
    internal val gameover : Boolean
            get() = (scorePlayer1>= GAMEOVER_SCORE || scorePlayer2>= GAMEOVER_SCORE)
    internal var paused = false


    override fun show() {
        netServer.start()
        spriteBatch = SpriteBatch()
        Assets.instance.initialize()
        GamePreferences.instance.load()
        level = Level(this)
        gui = GUIOverlay(this)
        configurationStage = ConfigurationStage(this)
        Gdx.app.input.inputProcessor = gui
    }

    override fun render(delta: Float) {
        gui.update(delta)
        if (!paused) level.update(delta)
        configurationStage.update(delta)

        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gui.render(spriteBatch)
        level.render(spriteBatch)
        configurationStage.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width, height)
        gui.resize(width, height)
        configurationStage.resize(width, height)
    }

    override fun dispose() {
        Assets.instance.dispose()
        spriteBatch.dispose()
        level.dispose()
        gui.dispose()
        configurationStage.dispose()
        netServer.stop()
    }


    override fun hide() {
        dispose()
    }


}