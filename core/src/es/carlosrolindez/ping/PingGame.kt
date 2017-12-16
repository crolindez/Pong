package es.carlosrolindez.ping

import com.badlogic.gdx.Game

class PingGame : Game() {

    override fun create() {
        setScreen(PingScreen())
    }
    // TODO check version in App Store
    // TODO manage when connecting oneself
}