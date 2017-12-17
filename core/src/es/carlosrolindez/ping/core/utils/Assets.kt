package es.carlosrolindez.core.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.I18NBundle


class Assets private constructor(): Disposable, AssetErrorListener, FileHandleResolver {

    companion object {
        private val TAG = Assets::class.java.name

        private val ASSETS_IMAGES_PATH = "images/pong.pack.atlas"
        private val ASSETS_FONTS_PATH = "fonts/seven_segments.fnt"

        private val ATLAS_AREA_BALL_0 = "ball0"
        private val ATLAS_AREA_BALL_1 = "ball1"
        private val ATLAS_AREA_BALL_2 = "ball2"
        private val ATLAS_AREA_BALL_3 = "ball3"
        private val ATLAS_AREA_BALL_4 = "ball4"
        private val ATLAS_AREA_BALL_5 = "ball5"
        private val ATLAS_AREA_BALL_6 = "ball6"
        private val ATLAS_AREA_BALL_7 = "ball7"
        private val ATLAS_AREA_BALL_8 = "ball8"
        private val ATLAS_AREA_BALL_9 = "ball9"

        private val ATLAS_AREA_PADDLE_HIT_0 = "paddlev0"
        private val ATLAS_AREA_PADDLE_HIT_1 = "paddlev1"
        private val ATLAS_AREA_PADDLE_HIT_2 = "paddlev2"
        private val ATLAS_AREA_PADDLE_HIT_3 = "paddlev3"
        private val ATLAS_AREA_PADDLE_HIT_4 = "paddlev4"
        private val ATLAS_AREA_PADDLE_HIT_5 = "paddlev5"
        private val ATLAS_AREA_PADDLE_HIT_6 = "paddlev6"
        private val ATLAS_AREA_PADDLE_HIT_7 = "paddlev7"
        private val ATLAS_AREA_PADDLE_HIT_8 = "paddlev8"
        private val ATLAS_AREA_PADDLE_HIT_9 = "paddlev9"

        private val ATLAS_AREA_WALL_HIT_0 = "paddleh0"
        private val ATLAS_AREA_WALL_HIT_1 = "paddleh1"
        private val ATLAS_AREA_WALL_HIT_2 = "paddleh2"
        private val ATLAS_AREA_WALL_HIT_3 = "paddleh3"
        private val ATLAS_AREA_WALL_HIT_4 = "paddleh4"
        private val ATLAS_AREA_WALL_HIT_5 = "paddleh5"
        private val ATLAS_AREA_WALL_HIT_6 = "paddleh6"
        private val ATLAS_AREA_WALL_HIT_7 = "paddleh7"
        private val ATLAS_AREA_WALL_HIT_8 = "paddleh8"
        private val ATLAS_AREA_WALL_HIT_9 = "paddleh9"

        private val ATLAS_AREA_BUTTON_RELEASED = "arrow"
        private val ATLAS_AREA_BUTTON_PRESSED = "arrow_pressed"
        private val ATLAS_AREA_MUSIC_ON = "music_on"
        private val ATLAS_AREA_MUSIC_OFF = "music_off"
        private val ATLAS_AREA_SOUND_ON = "sound_on"
        private val ATLAS_AREA_SOUND_OFF = "sound_off"
        private val ATLAS_AREA_SETTINGS = "settings"
        private val ATLAS_AREA_AUTO = "auto"
        private val ATLAS_AREA_NETWORK = "network"
        private val ATLAS_AREA_PLAY = "play"

        private val ATLAS_AREA_BACKGROUND = "background"

        private val HIT_SOUND_PATH = "sounds/blip.wav"
        private val FIREWORK_SOUND_PATH = "sounds/firework.mp3"
        private val START_SOUND_PATH = "sounds/start.wav"
        private val MUSIC_PATH = "sounds/kf.mp3"
        private val GOAL_PATH = "sounds/goal.wav"

        private val PARTICLES_PATH = "particles"
        private val SMOKE_PARTICLES_PATH = "particles/smoke.pfx"
        private val LINE_FIREWORKS_PARTICLES_PATH = "particles/lineFirework.pfx"
        private val CIRCLE_FIREWORKS_PARTICLES_PATH = "particles/circleFirework.pfx"

        private val STRING_BUNDLE_PATH = "bundles/strings"

        private val TEXTURE_ATLAS_UI_PATH = "skins/pong_configuration.atlas"
        private val SKIN_UI_PATH = "skins/pong_configuration.json"


        internal val instance = Assets()
    }


    lateinit private var assetManager: AssetManager
    lateinit private var textureAtlas: TextureAtlas

    lateinit internal var paddleAsset: PaddleAsset
    lateinit internal var buttonAsset: ButtonAsset

    lateinit internal var hitSound: Sound
    lateinit internal var startSound: Sound
    lateinit internal var fireworkSound: Sound
    lateinit internal var goalSound: Sound
    lateinit internal var music: Music

    lateinit internal var sevenFont: BitmapFont

    lateinit internal var smokeParticles: ParticleEffect
    lateinit internal var lineFireworksParticles: ParticleEffect
    lateinit internal var circleFireworksParticles: ParticleEffect

    lateinit internal var stringBundle : I18NBundle



    lateinit internal var skin : Skin


    override fun resolve(fileName: String?): FileHandle {
        return Gdx.files.internal(fileName)
    }

    internal fun initialize() {
        assetManager = AssetManager()
        assetManager.setErrorListener(this)
        assetManager.load(ASSETS_IMAGES_PATH, TextureAtlas::class.java)
        assetManager.load(HIT_SOUND_PATH, Sound::class.java)
        assetManager.load(START_SOUND_PATH, Sound::class.java)
        assetManager.load(FIREWORK_SOUND_PATH, Sound::class.java)
        assetManager.load(MUSIC_PATH, Music::class.java)
        assetManager.load(GOAL_PATH, Sound::class.java)
        assetManager.load(ASSETS_FONTS_PATH, BitmapFont::class.java)
        val pep = ParticleEffectLoader.ParticleEffectParameter()
        pep.imagesDir =   resolve(PARTICLES_PATH)
        assetManager.load(SMOKE_PARTICLES_PATH, ParticleEffect::class.java, pep)
        assetManager.load(LINE_FIREWORKS_PARTICLES_PATH, ParticleEffect::class.java, pep)
        assetManager.load(CIRCLE_FIREWORKS_PARTICLES_PATH, ParticleEffect::class.java, pep)
        assetManager.load(STRING_BUNDLE_PATH, I18NBundle::class.java)
        assetManager.load(TEXTURE_ATLAS_UI_PATH, TextureAtlas::class.java)
        assetManager.load(SKIN_UI_PATH, Skin::class.java, SkinLoader.SkinParameter(TEXTURE_ATLAS_UI_PATH))
        assetManager.finishLoading()

        textureAtlas = assetManager.get(ASSETS_IMAGES_PATH)

        paddleAsset = PaddleAsset(textureAtlas)
        buttonAsset = ButtonAsset(textureAtlas)

        hitSound = assetManager.get(HIT_SOUND_PATH)
        startSound = assetManager.get(START_SOUND_PATH)
        fireworkSound = assetManager.get(FIREWORK_SOUND_PATH)
        music = assetManager.get(MUSIC_PATH)
        goalSound = assetManager.get(GOAL_PATH)

        sevenFont = assetManager.get(ASSETS_FONTS_PATH)
        sevenFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        smokeParticles = assetManager.get(SMOKE_PARTICLES_PATH)

        lineFireworksParticles = assetManager.get(LINE_FIREWORKS_PARTICLES_PATH)

        circleFireworksParticles = assetManager.get(CIRCLE_FIREWORKS_PARTICLES_PATH)

        stringBundle = assetManager.get(STRING_BUNDLE_PATH)

        skin = assetManager.get(SKIN_UI_PATH)

    }





    class PaddleAsset(atlas : TextureAtlas) {

        internal var ballHitAnimation: Animation<TextureAtlas.AtlasRegion>
        internal var paddleHitAnimation: Animation<TextureAtlas.AtlasRegion>
        internal var wallHitAnimation: Animation<TextureAtlas.AtlasRegion>

        init {
            val ballHitFrames = Array<TextureAtlas.AtlasRegion>()
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_0))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_1))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_2))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_3))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_4))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_5))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_6))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_7))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_8))
            ballHitFrames.add(atlas.findRegion(ATLAS_AREA_BALL_9))
            ballHitAnimation = Animation(
                    ANIMATION_FRAME_DURATION,
                    ballHitFrames,
                    Animation.PlayMode.NORMAL)

            val paddleHitFrames = Array<TextureAtlas.AtlasRegion>()
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_0))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_1))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_2))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_3))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_4))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_5))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_6))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_7))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_8))
            paddleHitFrames.add(atlas.findRegion(ATLAS_AREA_PADDLE_HIT_9))
            wallHitAnimation = Animation(
                    ANIMATION_FRAME_DURATION,
                    paddleHitFrames,
                    Animation.PlayMode.NORMAL)

            val wallHitFrames = Array<TextureAtlas.AtlasRegion>()
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_0))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_1))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_2))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_3))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_4))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_5))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_6))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_7))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_8))
            wallHitFrames.add(atlas.findRegion(ATLAS_AREA_WALL_HIT_9))
            paddleHitAnimation = Animation(
                    ANIMATION_FRAME_DURATION,
                    wallHitFrames,
                    Animation.PlayMode.NORMAL)
        }


    }

    class ButtonAsset(atlas : TextureAtlas) {
        internal val buttonPressed = atlas.findRegion(ATLAS_AREA_BUTTON_PRESSED)
        internal val buttonReleased = atlas.findRegion(ATLAS_AREA_BUTTON_RELEASED)
        internal val buttonMusicOn = atlas.findRegion(ATLAS_AREA_MUSIC_ON)
        internal val buttonMusicOff = atlas.findRegion(ATLAS_AREA_MUSIC_OFF)
        internal val buttonSoundOn = atlas.findRegion(ATLAS_AREA_SOUND_ON)
        internal val buttonSoundOff = atlas.findRegion(ATLAS_AREA_SOUND_OFF)
        internal val buttonSettings = atlas.findRegion(ATLAS_AREA_SETTINGS)
        internal val buttonAuto = atlas.findRegion(ATLAS_AREA_AUTO)
        internal val buttonNetwork = atlas.findRegion(ATLAS_AREA_NETWORK)
        internal val background = atlas.findRegion(ATLAS_AREA_BACKGROUND)
        internal val buttonPlay = atlas.findRegion(ATLAS_AREA_PLAY)
    }




    override fun dispose() {
        assetManager.dispose()
    }

    override fun error(asset: AssetDescriptor<*>?, throwable: Throwable?) {
    }
}