package es.carlosrolindez.pong.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

class Assets /*private constructor() */: Disposable, AssetErrorListener {

    companion object {
        private val TAG = Assets::class.java.name

        private val ASSETS_IMAGES_PATH = "images/pong.pack.atlas"
        private val ASSETS_FONTS_PATH = "fonts/seven_segments.fnt"

        private val ATLAS_AREA_PADDLE = "paddle"
        private val ATLAS_AREA_PADDLE_HIT = "paddle_hit"
        private val ATLAS_AREA_BALL = "ball"
        private val ATLAS_AREA_BALL_HIT = "ball_hit"

        private val ATLAS_AREA_BUTTON_RELEASED = "button_released"
        private val ATLAS_AREA_BUTTON_PRESSED = "button_pressed"

        private val HIT_SOUND_PATH = "sounds/blip.wav"
        private val START_SOUND_PATH = "sounds/start.wav"
        private val MUSIC_PATH = "sounds/kf.mp3"



        internal val instance = Assets()
    }

    private val assetManager = AssetManager()
    private val textureAtlas : TextureAtlas

    internal val paddleAsset : PaddleAsset
    internal val buttonAsset : ButtonAsset

    internal val hitSound : Sound
    internal val startSound : Sound
    internal val music : Music
    internal val sevenFont:BitmapFont

    init {

        assetManager.setErrorListener(this)
        assetManager.load(ASSETS_IMAGES_PATH, TextureAtlas::class.java)
        assetManager.load(HIT_SOUND_PATH, Sound::class.java)
        assetManager.load(START_SOUND_PATH, Sound::class.java)
        assetManager.load(MUSIC_PATH, Music::class.java)
        assetManager.load(ASSETS_FONTS_PATH, BitmapFont::class.java)
        assetManager.finishLoading()

        textureAtlas = assetManager.get(ASSETS_IMAGES_PATH)

        paddleAsset = PaddleAsset(textureAtlas)
        buttonAsset = ButtonAsset(textureAtlas)

        hitSound = assetManager.get(HIT_SOUND_PATH)
        startSound = assetManager.get(START_SOUND_PATH)
        music = assetManager.get(MUSIC_PATH)

        sevenFont = assetManager.get(ASSETS_FONTS_PATH)
        sevenFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

    }

    class PaddleAsset(atlas : TextureAtlas) {
        internal val paddle = NinePatch(atlas.findRegion(ATLAS_AREA_PADDLE),
                PADDLE_EDGE,PADDLE_EDGE,PADDLE_EDGE,PADDLE_EDGE)
        internal val paddle_hit = NinePatch(atlas.findRegion(ATLAS_AREA_PADDLE_HIT),
                PADDLE_EDGE,PADDLE_EDGE,PADDLE_EDGE,PADDLE_EDGE)
        internal val ball = NinePatch(atlas.findRegion(ATLAS_AREA_BALL),
                BALL_EDGE,BALL_EDGE,BALL_EDGE,BALL_EDGE)
        internal val ball_hit = NinePatch(atlas.findRegion(ATLAS_AREA_BALL_HIT),
                BALL_EDGE,BALL_EDGE,BALL_EDGE,BALL_EDGE)
    }


    class ButtonAsset(atlas : TextureAtlas) {
        internal val buttonPressed = atlas.findRegion(ATLAS_AREA_BUTTON_PRESSED)
        internal val buttonReleased = atlas.findRegion(ATLAS_AREA_BUTTON_RELEASED)
    }



    override fun dispose() {
        assetManager.dispose()
        hitSound.dispose()
        startSound.dispose()
        music.dispose()
    }

    override fun error(asset: AssetDescriptor<*>?, throwable: Throwable?) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset?.fileName, throwable)
    }
}