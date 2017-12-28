package es.carlosrolindez.ping.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.ping.core.entities.Ball
import es.carlosrolindez.ping.core.entities.Paddle
import es.carlosrolindez.ping.core.entities.Walls
import es.carlosrolindez.ping.core.net.Network
import es.carlosrolindez.ping.core.utils.*


class Level(private var pingScreen: PingScreen) {

    companion object {
        val TAG: String = Level::class.java.name
    }

    enum class LevelState{ INITIAL_STATE, BEEPS_STATE, PLAYING_STATE, GAME_OVER }

    private val fieldRect = Rectangle( MARGIN - SCREEN_WIDTH/2f,  MARGIN - SCREEN_HEIGHT/2f,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    internal val player1 = Paddle(this, Paddle.Side.LEFT)
    internal val player2 = Paddle(this, Paddle.Side.RIGHT)

    internal val ball = Ball(this)
    internal val walls = Walls()

    private var initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
    private var state: Level.LevelState = Level.LevelState.INITIAL_STATE

    private val viewport: ExtendViewport = ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT)

    internal var leftUpPressed: Boolean = false
    internal var rightUpPressed: Boolean = false
    internal var leftDownPressed: Boolean = false
    internal var rightDownPressed: Boolean = false

    init {
        viewport.camera.position.set(0f,0f,0f)
        initBall()
    }

    internal fun initBall() {
        initBall(Vector2(MathUtils.randomSign() * BALL_INITIAL_VELOCITY_X,
                MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)))
    }

    internal fun initBall(velocity : Vector2) {
        ball.initState(velocity)
        player1.initState()
        player2.initState()
        initialTime = TimeUtils.nanoTime()  * MathUtils.nanoToSec
        state = Level.LevelState.INITIAL_STATE
        pingScreen.scorePlayer1 = 0
        pingScreen.scorePlayer2 = 0
    }

    private fun relaunchBall() {
        relaunchBall(Vector2(MathUtils.randomSign() * BALL_INITIAL_VELOCITY_X,
                MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)))
    }

    internal fun relaunchBall(velocity : Vector2) {
        ball.initState(velocity)
        initialTime = TimeUtils.nanoTime()  * MathUtils.nanoToSec
        state = Level.LevelState.PLAYING_STATE
    }

    internal fun update(delta: Float) {

        when (state) {
            Level.LevelState.INITIAL_STATE -> {
                if ( MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= LOADING_TIME) {
                    if (GamePreferences.sound) Assets.startSound.play(SOUND_VOLUME * GamePreferences.volSound)
                    state = Level.LevelState.BEEPS_STATE
                }
            }

            Level.LevelState.BEEPS_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= INTRO_TIME) {
                    if (GamePreferences.music) {
                        Assets.music.play()
                        Assets.music.isLooping = true
                        Assets.music.volume = MUSIC_VOLUME * GamePreferences.volMusic
                    }

                    state = Level.LevelState.PLAYING_STATE
                }
            }
            Level.LevelState.PLAYING_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= REINTRO_TIME) {
                    player1.update(delta, leftUpPressed, leftDownPressed)
                    player2.update(delta, rightUpPressed, rightDownPressed)
                    ball.update(delta)

                    if (ball.checkCollisionWall(fieldRect))
                        if (GamePreferences.sound) Assets.hitSound.play(SOUND_VOLUME * GamePreferences.volSound)

                    if (ball.checkCollisionPaddle(player1)) {
                        if (GamePreferences.sound) Assets.hitSound.play(SOUND_VOLUME * GamePreferences.volSound)
                        if (Network.connection!=null)
                            Network.bounce()
                    }

                    if (Network.connection==null) { // single devie
                        if (ball.checkCollisionPaddle(player2))
                            if (GamePreferences.sound) Assets.hitSound.play(SOUND_VOLUME * GamePreferences.volSound)
                    } else {                        // two devices
                        if (ball.furtherThanPaddle(player2)) {
                            Network.requestState()
                        }
                    }


                    when (ball.checkGoal()) {
                        1 -> {  if (GamePreferences.sound)
                                    Assets.goalSound.play(SOUND_VOLUME * GamePreferences.volSound)
                                pingScreen.scorePlayer2++
                                if (Network.connection!=null) {
                                    Network.goal()
                                    Network.newBall()
                                } else {
                                    relaunchBall()
                                }
                                pingScreen.gui.flashScore(2)
                            }
                        2 -> if(Network.connection==null) {
                                if (GamePreferences.sound)
                                    Assets.goalSound.play(SOUND_VOLUME * GamePreferences.volSound)
                                pingScreen.scorePlayer1++
                                relaunchBall()
                                pingScreen.gui.flashScore(1)
                            }
                    }

                    if (pingScreen.gameover) {
                        state = Level.LevelState.GAME_OVER
                    }
                }
            }
            Level.LevelState.GAME_OVER -> {

            }
        }

    }

    internal fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        viewport.camera.position.set(0f, 0f,0f)
    }

    internal fun render(batch: SpriteBatch) {

        viewport.apply()

        batch.projectionMatrix = viewport.camera.combined

        batch.begin()

        player1.render(batch)
        player2.render(batch)
        ball.render(batch)
        walls.render(batch)
        batch.end()



    }

    internal fun switchSound() {
        setSound(!GamePreferences.sound)
    }

    internal fun setSound(value : Boolean)  {
        GamePreferences.sound = value
        GamePreferences.save()
    }

    internal fun setVolumeSound(value : Float)  {
        GamePreferences.volSound = value
        GamePreferences.save()
    }

    internal fun switchMusic(){
        setMusic(!GamePreferences.music)
    }

    internal fun setMusic(value: Boolean) {
        GamePreferences.music = value
        GamePreferences.save()
        if (GamePreferences.music) {
            Assets.music.play()
            Assets.music.isLooping = true
            Assets.music.volume = MUSIC_VOLUME * GamePreferences.volMusic
        } else
            Assets.music.pause()

    }

    internal fun setVolumeMusic(value : Float)  {
        GamePreferences.volMusic = value
        Assets.music.volume = MUSIC_VOLUME * GamePreferences.volMusic
        GamePreferences.save()
    }

    internal fun dispose() {
 //       walls.dispose()
    }

}
