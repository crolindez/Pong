package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen())
    }

}


// TODO Setting windows bigger and closing keyboard
// TODO button to exit from gameOver
// TODO button to pause (and pause when enter in configuration)
// TODO improve up/down buttons
// TODO color and noise with goal