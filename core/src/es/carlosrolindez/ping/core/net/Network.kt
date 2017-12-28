package es.carlosrolindez.ping.core.net

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.EndPoint
import com.esotericsoftware.minlog.Log
import com.esotericsoftware.minlog.Log.LEVEL_NONE
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.*


// This class is a convenient place to keep things common to both the client and server.
object Network {
    private val TAG = Network::class.java.name
    private var lastPlayerPosition : Float = 0f
    private val PLAYER_POSITION_MIN_STEP = 5f

    init {
        Log.set(LEVEL_NONE)
    }

    internal val TCP_PORT = 54722
    internal val UDP_PORT = 54723
    internal var connection : Connection? = null
    lateinit private var pingScreen: PingScreen

    // This registers objects that are going to be sent over the network.
    internal fun register(endPoint: EndPoint?,screen : PingScreen) {

        pingScreen = screen

        val kryo = endPoint?.kryo
        kryo?.register(Login::class.java)
        kryo?.register(LoginRejected::class.java)
        kryo?.register(LoginAccepted::class.java)

        kryo?.register(Play::class.java)
        kryo?.register(Goal::class.java)
        kryo?.register(NewBall::class.java)
        kryo?.register(Bounce::class.java)
        kryo?.register(PlayerPosition::class.java)

        kryo?.register(Pause::class.java)
        kryo?.register(Resume::class.java)

        kryo?.register(RequestState::class.java)
        kryo?.register(AnswerState::class.java)

    }

    open internal class BasicRegister (internal var stampTime : Long)


    //  Connection classes

    internal class Login : BasicRegister(TimeUtils.nanoTime()) {
        internal var clientName = Assets.stringBundle.format("player")
        internal var address = ""

    }

    internal class LoginRejected : BasicRegister(TimeUtils.nanoTime())


    internal class LoginAccepted : BasicRegister(TimeUtils.nanoTime()) {
        internal var serverName = Assets.stringBundle.format("player")
    }

    // Playing classes

    internal class Play : BasicRegister(TimeUtils.nanoTime()) {
        var ballVelocityX : Float = BALL_INITIAL_VELOCITY_X
        var ballVelocityY : Float = MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)
    }

    internal class Goal : BasicRegister(TimeUtils.nanoTime()) {
        var score : Int = 0
    }

    internal class NewBall : BasicRegister(TimeUtils.nanoTime()) {
        var ballVelocityX : Float = BALL_INITIAL_VELOCITY_X
        var ballVelocityY : Float = MathUtils.randomSign() * MathUtils.random(BALL_INITIAL_VELOCITY_RANGE_MIN_Y, BALL_INITIAL_VELOCITY_RANGE_MAX_Y)
    }

    internal class Bounce : BasicRegister(TimeUtils.nanoTime()) {
        var ballPreviousPositionX = 0f
        var ballPreviousPositionY = 0f
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
    }

    internal class PlayerPosition : BasicRegister(TimeUtils.nanoTime()) {
        var verticalPosition = 0f
    }

    // Management classes

    internal class Pause : BasicRegister(TimeUtils.nanoTime()) {
        var ballPreviousPositionX = 0f
        var ballPreviousPositionY = 0f
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
    }

    internal class Resume : BasicRegister(TimeUtils.nanoTime())

    internal class RequestState : BasicRegister(TimeUtils.nanoTime())

    internal class AnswerState : BasicRegister(TimeUtils.nanoTime()) {
        var ballPreviousPositionX = 0f
        var ballPreviousPositionY = 0f
        var ballPositionX = 0f
        var ballPositionY = 0f
        var ballVelocityX = 0f
        var ballVelocityY = 0f
        var score : Int = 0
    }

    // Message Engine

    internal fun play() {
        val message = Play()
        connection?.sendTCP(message)
        pingScreen.paused = false
        pingScreen.level.initBall(Vector2(message.ballVelocityX, message.ballVelocityY))
    }

    internal fun newBall() {
        val message = NewBall()
        connection?.sendTCP(message)
        pingScreen.paused = false
        pingScreen.level.relaunchBall(Vector2(message.ballVelocityX, message.ballVelocityY))
    }

    internal fun goal() {
        val message = Goal()
        message.score = pingScreen.scorePlayer2
        connection?.sendTCP(message)
    }

    internal fun playerPosition () {
        if (Math.abs(lastPlayerPosition- pingScreen.level.player1.position.y)>PLAYER_POSITION_MIN_STEP) {
            lastPlayerPosition = pingScreen.level.player1.position.y
            val message = PlayerPosition()
            message.verticalPosition = lastPlayerPosition
            connection?.sendTCP(message)
        }
    }


    internal fun bounce() {
        val message= Bounce()
        message.ballPreviousPositionX = pingScreen.level.ball.previousPosition.x
        message.ballPreviousPositionY = pingScreen.level.ball.previousPosition.y
        message.ballPositionX = pingScreen.level.ball.position.x
        message.ballPositionY = pingScreen.level.ball.position.y
        message.ballVelocityX = pingScreen.level.ball.velocity.x
        message.ballVelocityY = pingScreen.level.ball.velocity.y
        connection?.sendTCP(message)
    }

    internal fun pause() {
        val message= Pause()
        message.ballPreviousPositionX = pingScreen.level.ball.previousPosition.x
        message.ballPreviousPositionY = pingScreen.level.ball.previousPosition.y
        message.ballPositionX = pingScreen.level.ball.position.x
        message.ballPositionY = pingScreen.level.ball.position.y
        message.ballVelocityX = pingScreen.level.ball.velocity.x
        message.ballVelocityY = pingScreen.level.ball.velocity.y
        connection?.sendTCP(message)
        pingScreen.paused = true
    }

    internal fun resume() {
        val message= Resume()
        connection?.sendTCP(message)
        pingScreen.paused = false
    }

    internal fun requestState() {
        connection?.sendTCP(RequestState())
    }

    internal fun answerState() {
        val message= AnswerState()
        message.ballPreviousPositionX =  pingScreen.level.ball.previousPosition.x
        message.ballPreviousPositionY = pingScreen.level.ball.previousPosition.y
        message.ballPositionX = pingScreen.level.ball.position.x
        message.ballPositionY = pingScreen.level.ball.position.y
        message.ballVelocityX = pingScreen.level.ball.velocity.x
        message.ballVelocityY = pingScreen.level.ball.velocity.y
        message.score = pingScreen.scorePlayer2
        connection?.sendTCP(message)
        pingScreen.paused = false
    }

    internal fun receivedPlayingMessage(genObject: Any) {
/*        if (genObject is BasicRegister)
            Gdx.app.error(TAG,"${genObject::class.java.name}: ${genObject.stampTime - TimeUtils.nanoTime()}")*/
        when (genObject) {
            is Network.Play -> {
                val velocity = Vector2(-genObject.ballVelocityX, genObject.ballVelocityY)
                pingScreen.paused = false
                pingScreen.level.initBall(velocity)
                Assets.lineFireworksParticles.reset()
                Assets.circleFireworksParticles.reset()
            }
            is Network.NewBall -> {
                val velocity = Vector2(-genObject.ballVelocityX, genObject.ballVelocityY)
                pingScreen.paused = false
                pingScreen.level.relaunchBall(velocity)
            }
            is Network.Goal -> {
                pingScreen.scorePlayer1 = genObject.score
                pingScreen.gui.flashScore(1)
                if (GamePreferences.sound)
                    Assets.goalSound.play(SOUND_VOLUME * GamePreferences.volSound)
            }
            is Network.Bounce -> {
                pingScreen.level.ball.previousPosition.set(-genObject.ballPreviousPositionX, genObject.ballPreviousPositionY)
                pingScreen.level.ball.position.set(-genObject.ballPositionX, genObject.ballPositionY)
                pingScreen.level.ball.velocity.set(-genObject.ballVelocityX, genObject.ballVelocityY)
                pingScreen.level.player2.setCollision()
                if (GamePreferences.sound)
                    Assets.hitSound.play(SOUND_VOLUME * GamePreferences.volSound)
            }
            is Network.PlayerPosition -> {
                pingScreen.level.player2.position.y = genObject.verticalPosition
            }
            is Network.Pause -> {
                pingScreen.level.ball.previousPosition.set(-genObject.ballPreviousPositionX, genObject.ballPreviousPositionY)
                pingScreen.level.ball.position.set(-genObject.ballPositionX, genObject.ballPositionY)
                pingScreen.level.ball.velocity.set(-genObject.ballVelocityX, genObject.ballVelocityY)
                pingScreen.paused = true
            }
            is Network.Resume -> {
                pingScreen.paused = false
            }
            is Network.RequestState -> {
                answerState()
            }
            is Network.AnswerState -> {
                pingScreen.level.ball.previousPosition.set(-genObject.ballPreviousPositionX, genObject.ballPreviousPositionY)
                pingScreen.level.ball.position.set(-genObject.ballPositionX, genObject.ballPositionY)
                pingScreen.level.ball.velocity.set(-genObject.ballVelocityX, genObject.ballVelocityY)
                pingScreen.scorePlayer1 = genObject.score
                pingScreen.paused = false
            }
        }
    }

}
