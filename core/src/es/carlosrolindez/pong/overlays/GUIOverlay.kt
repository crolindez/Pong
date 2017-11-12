package es.carlosrolindez.pong.overlays


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import es.carlosrolindez.pong.screens.PongScreen
import es.carlosrolindez.pong.utils.*
import sun.rmi.runtime.Log


class GUIOverlay(private val gameScreen: PongScreen) {
    val viewport: Viewport


    init {
        viewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
    }

    fun render(batch: SpriteBatch) {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined

        batch.begin()


        drawTextureRegion(batch,Assets.instance.buttonAsset.buttonReleased,
                BUTTON_MARGEN_X,
                SCREEN_HEIGHT/2 + BUTTON_MARGEN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,Assets.instance.buttonAsset.buttonPressed,
                BUTTON_MARGEN_X,
                SCREEN_HEIGHT/2  - BUTTON_HEIGHT - BUTTON_MARGEN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        drawTextureRegion(batch,Assets.instance.buttonAsset.buttonReleased,
                SCREEN_WIDTH  - BUTTON_WIDTH - BUTTON_MARGEN_X,
                SCREEN_HEIGHT/2 + BUTTON_MARGEN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, false)

        drawTextureRegion(batch,Assets.instance.buttonAsset.buttonPressed,
                SCREEN_WIDTH - BUTTON_WIDTH - BUTTON_MARGEN_X,
                SCREEN_HEIGHT/2  - BUTTON_HEIGHT - BUTTON_MARGEN_Y,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                0f,
                false, true)

        batch.end()
    }


    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2,0f)
    }

    fun dispose() {

    }
}
