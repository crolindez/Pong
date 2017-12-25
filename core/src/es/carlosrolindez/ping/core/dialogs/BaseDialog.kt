package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_HEIGHT
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_WIDTH


abstract class BaseDialog(protected val gameScreen: PingScreen, private val scale : Float) : InputAdapter() {

    private val viewport = ExtendViewport(DIALOG_SCREEN_WIDTH, DIALOG_SCREEN_HEIGHT)
    private var stage : Stage
    private var activated : Boolean


    init {

        viewport.camera.position.set(0f, 0f,0f)
        activated = false
        stage = Stage(viewport)
    }

    internal fun render(batch: SpriteBatch) {
        if (activated) {
            viewport.apply()

            batch.projectionMatrix = viewport.camera.combined
            batch.begin()

            stage.draw()
            batch.end()
        }
    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(0f, 0f,0f)
    }

    internal fun dispose() {
        stage.dispose()
    }

    internal fun update(delta: Float) {
        if (activated)
            stage.act(delta)
    }

    internal fun openDialog() {
        activated = true
        val stack = Stack()
        stage.addActor(stack)
        stack.setSize(DIALOG_SCREEN_WIDTH * scale, DIALOG_SCREEN_HEIGHT * scale)

        prepareUi()
        stack.add(getUiActor())
        stack.setPosition(-DIALOG_SCREEN_WIDTH * scale /2f, -DIALOG_SCREEN_HEIGHT * scale /2f)
        Gdx.app.input.inputProcessor = stage

    }

    internal fun closeDialog() {
        if (activated) {
            activated = false
            stage.clear()
            Gdx.app.input.inputProcessor = gameScreen.gui
            closeUi()
        }
    }

    internal fun isActivated() = activated

    abstract fun getUiActor() : Actor
    abstract fun prepareUi()
    abstract fun closeUi()

}