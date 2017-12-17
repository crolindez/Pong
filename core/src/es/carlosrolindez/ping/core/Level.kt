package es.carlosrolindez.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import es.carlosrolindez.ping.entities.Ball
import es.carlosrolindez.ping.entities.Paddle
import es.carlosrolindez.ping.entities.Walls
import es.carlosrolindez.ping.net.Network


class Level(private var pingScreen: es.carlosrolindez.core.PingScreen) {

    companion object {
        val TAG: String = es.carlosrolindez.core.Level::class.java.name
    }

    enum class LevelState{ INITIAL_STATE, BEEPS_STATE, PLAYING_STATE, GAME_OVER }

    private val fieldRect = Rectangle( MARGIN - SCREEN_WIDTH/2f,  MARGIN - SCREEN_HEIGHT/2f,
            SCREEN_WIDTH - 2*MARGIN, SCREEN_HEIGHT - 2*MARGIN )
    internal val player1 = Paddle(this, Paddle.Side.LEFT)
    internal val player2 = Paddle(this, Paddle.Side.RIGHT)

    internal val ball = Ball(this)
    internal val walls = Walls()

    private var initialTime  = TimeUtils.nanoTime() * MathUtils.nanoToSec
    private var state: es.carlosrolindez.core.Level.LevelState = es.carlosrolindez.core.Level.LevelState.INITIAL_STATE

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
        state = es.carlosrolindez.core.Level.LevelState.INITIAL_STATE
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
        state = es.carlosrolindez.core.Level.LevelState.PLAYING_STATE
    }

    internal fun update(delta: Float) {

        when (state) {
            es.carlosrolindez.core.Level.LevelState.INITIAL_STATE -> {
                if ( MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= LOADING_TIME) {
                    if (GamePreferences.instance.sound) Assets.instance.startSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                    state = es.carlosrolindez.core.Level.LevelState.BEEPS_STATE
                }
            }

            es.carlosrolindez.core.Level.LevelState.BEEPS_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= INTRO_TIME) {
                    if (GamePreferences.instance.music) {
                        Assets.instance.music.play()
                        Assets.instance.music.isLooping = true
                        Assets.instance.music.volume = MUSIC_VOLUME * GamePreferences.instance.volMusic
                    }

                    state = es.carlosrolindez.core.Level.LevelState.PLAYING_STATE
                }
            }
            es.carlosrolindez.core.Level.LevelState.PLAYING_STATE -> {
                if (MathUtils.nanoToSec * TimeUtils.nanoTime() - initialTime >= REINTRO_TIME) {
                    player1.update(delta, leftUpPressed, leftDownPressed)
                    player2.update(delta, rightUpPressed, rightDownPressed)
                    ball.update(delta)

                    if (ball.checkCollisionWall(fieldRect))
                        if (GamePreferences.instance.sound) Assets.instance.hitSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)

                    if (ball.checkCollisionPaddle(player1)) {
                        if (GamePreferences.instance.sound) Assets.instance.hitSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                        if (Network.connection!=null)
                            Network.bounce(ball.previousPosition, ball.position, ball.velocity)
                    }

                    if (Network.connection==null)
                        if (ball.checkCollisionPaddle(player2))
                            if (GamePreferences.instance.sound) Assets.instance.hitSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)


                    when (ball.checkGoal()) {
                        1 -> {  if (GamePreferences.instance.sound)
                                    Assets.instance.goalSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
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
                                if (GamePreferences.instance.sound)
                                    Assets.instance.goalSound.play(SOUND_VOLUME * GamePreferences.instance.volSound)
                                pingScreen.scorePlayer1++
                                relaunchBall()
                                pingScreen.gui.flashScore(1)
                            }
                    }

                    if (pingScreen.gameover) {
                        state = es.carlosrolindez.core.Level.LevelState.GAME_OVER
                    }
                }
            }
            es.carlosrolindez.core.Level.LevelState.GAME_OVER -> {

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
        setSound(!GamePreferences.instance.sound)
    }

    internal fun setSound(value : Boolean)  {
        GamePreferences.instance.sound = value
        GamePreferences.instance.save()
    }

    internal fun setVolumeSound(value : Float)  {
        GamePreferences.instance.volSound = value
        GamePreferences.instance.save()
    }

    internal fun switchMusic(){
        setMusic(!GamePreferences.instance.music)
    }

    internal fun setMusic(value: Boolean) {
        GamePreferences.instance.music = value
        GamePreferences.instance.save()
        if (GamePreferences.instance.music)
            Assets.instance.music.play()
        else
            Assets.instance.music.pause()

    }

    internal fun setVolumeMusic(value : Float)  {
        GamePreferences.instance.volMusic = value
        Assets.instance.music.volume = MUSIC_VOLUME * GamePreferences.instance.volMusic
        GamePreferences.instance.save()
    }

    internal fun dispose() {
 //       walls.dispose()
    }

}
