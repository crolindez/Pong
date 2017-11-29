package es.carlosrolindez.pong

import com.badlogic.gdx.Game
import es.carlosrolindez.pong.screens.PongScreen

class PongGame : Game() {

    override fun create() {
        setScreen(PongScreen())
    }

}


// TODO improve up/down buttons
// TODO color and noise with goal
// TODO Change coordinates to center 0,0