package es.carlosrolindez.pong.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import es.carlosrolindez.pong.Level
import es.carlosrolindez.pong.PongGame
import es.carlosrolindez.pong.overlays.GUIOverlay
import es.carlosrolindez.pong.utils.Assets
import es.carlosrolindez.pong.utils.BACKGROUND_COLOR
import es.carlosrolindez.pong.utils.GAMEOVER_SCORE


class PongScreen(private val game: PongGame):AbstractScreen() {

    companion object {
        val TAG: String = PongScreen::class.java.name
    }

    lateinit private var spriteBatch : SpriteBatch
    lateinit internal var level : Level
    lateinit private var gui : GUIOverlay

    internal var scorePlayer1 = 0
    internal var scorePlayer2 = 0
    internal val gameover : Boolean
            get() = (scorePlayer1>= GAMEOVER_SCORE || scorePlayer2>= GAMEOVER_SCORE)


    override fun show() {
        spriteBatch = SpriteBatch()
        Assets.instance.initialize()
        level = Level(this)
        gui = GUIOverlay(this)
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
/*        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.enableBlending()*/

        gui.render(spriteBatch)
        level.render(spriteBatch)

    }

    override fun resize(width: Int, height: Int) {
        level.resize(width,height)
        gui.resize(width,height)
    }

    override fun dispose() {
        Assets.instance.dispose()
        spriteBatch.dispose()
        level.dispose()
        gui.dispose()
    }

    override var inputProcessor: InputProcessor?
        get() = gui
        set(value) {
            Gdx.input.inputProcessor = value
        }

    override fun hide() {
        dispose()
    }


}