package es.carlosrolindez.ping

import com.badlogic.gdx.Game

class PingGame : Game() {

    override fun create() {
        setScreen(PingScreen())
    }
    // TODO avoid copy of players on Player list (after rejecting a connection)
    // TODO fix issue with client disconnection when closing game
}