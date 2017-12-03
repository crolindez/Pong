package es.carlosrolindez.pong.net

import com.badlogic.gdx.Gdx
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Listener.ThreadedListener
import java.io.IOException
import java.net.InetAddress

class NetworkClient() {

	var clientNet : Client? = null
    var connected : Boolean = false
    var serverName : String? = null
    var clientName : String = "es.carlosrolindez.pong.client"

    internal fun stop() {
        clientNet?.stop()
        clientNet?.dispose()
        clientNet = null
        connected = false

    }

    private fun sendLogin() {

        val message = Network.Login()
        message.clientName = clientName
        clientNet?.sendTCP(message)
        return
    }

	internal fun start() {
        val client = Client()
        clientNet = client
        client.start()
        val address = client.discoverHost(Network.UDP_PORT, 1000)
        if (address == null)
            Gdx.app.error("InetAddress", " NOT found")
        else {
            Gdx.app.error("InetAddress", address.toString())
            try {
                client.connect(5000, address, Network.TCP_PORT, Network.UDP_PORT)
                sendLogin()
            } catch (e: IOException) {
                e.printStackTrace()
                stop()
            }
        }

        Network.register(client)

        client.addListener(ThreadedListener(object : Listener() {

            override fun connected(connection: Connection?) {
            }

            override fun received(connection: Connection?, genObject: Any?) {
                if (genObject is Network.RegisterOk) {
                    if (genObject.serverName == null) { // failed connection
                        client.stop()
                        return
                    }
                    serverName = genObject.serverName
                }
            }

            override fun disconnected(connection: Connection?) {
                stop()
            }

        }))
    }
}