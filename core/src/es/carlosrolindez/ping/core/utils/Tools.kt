package es.carlosrolindez.ping.core.utils

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion


internal fun drawTextureRegion(batch: SpriteBatch, region: TextureRegion, x: Float, y: Float,
                               width : Float, height : Float, rotation : Float, flipX : Boolean, flipY : Boolean) {
    batch.draw(
            region.texture,
            x, y,
            width/2f,
            height/2f,
            width, height,
            1f,1f,
            rotation,
            region.regionX,
            region.regionY,
            region.regionWidth,
            region.regionHeight,
            flipX, flipY)
}