package es.carlosrolindez.core

import com.badlogic.gdx.Game

class PingGame : Game() {

    override fun create() {

        setScreen(PingScreen())
    }
    // TODO check version in App Store
    // TODO manage when connecting oneself
    // TODO something happens with hit annimation
}