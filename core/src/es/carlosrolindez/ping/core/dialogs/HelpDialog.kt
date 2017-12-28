package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_WIDTH


class HelpDialog(pingScreen: PingScreen) : BaseDialog(pingScreen , 0.5f, 4) {
    private var ui : HelpUI
    private var singleSetup = true

    init {
        ui = HelpUI(Assets.skin)
    }

    override fun getUiActor(): Actor {
        return ui.helpWin
    }

    override fun prepareUi() {
        if (singleSetup) {
            singleSetup = false

            val lbl1 = Label("", Assets.skin)
            lbl1.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl1.style.background = Assets.skin.newDrawable("white")
            val lbl2 = Label("", Assets.skin)
            lbl2.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl2.style.background = Assets.skin.newDrawable("white")
            val lbl3 = Label("", Assets.skin)
            lbl3.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl3.style.background = Assets.skin.newDrawable("white")

            when {
                Gdx.app.getType() == Application.ApplicationType.Desktop -> {
                    ui.helpTable.add(Label(Assets.stringBundle.format("desktopInstructions1"), Assets.skin, "font", Color.BLACK)).expand().padBottom(5f)
                    ui.helpTable.row()
                    ui.helpTable.add(Label(Assets.stringBundle.format("desktopInstructions2"), Assets.skin, "font", Color.BLACK)).expand().padBottom(10f)
                    ui.helpTable.row()
                }

                Gdx.app.getType() == Application.ApplicationType.Android -> {
                    ui.helpTable.add(Label(Assets.stringBundle.format("androidInstructions"), Assets.skin, "font", Color.BLACK)).expand().padBottom(10f)
                    ui.helpTable.row()
                }
            }

            ui.helpTable.add(lbl1).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
            ui.helpTable.row()
            ui.helpTable.add(Label(Assets.stringBundle.format("networkInstructions"), Assets.skin, "font", Color.BLACK)).expand().padBottom(10f)
            ui.helpTable.row()
            ui.helpTable.add(lbl2).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
            ui.helpTable.row()

            when {
                pingScreen.versionAvailable -> {
                    val lblLinkVersion = Label(Assets.stringBundle.format("newVersion"), Assets.skin, "font", Color.BLUE)
                    lblLinkVersion.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            Gdx.net.openURI(Assets.stringBundle.format("linkVersion"))
                        }
                    })
                    ui.helpTable.add(lblLinkVersion).expand().padBottom(10f)
                    ui.helpTable.row()
                    ui.helpTable.add(lbl3).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
                    ui.helpTable.row()
                }

                Gdx.app.getType() == Application.ApplicationType.Android -> {
                    val lblDesktopVersion = Label(Assets.stringBundle.format("desktopVersion"), Assets.skin, "font", Color.BLUE)
                    lblDesktopVersion.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            Gdx.net.openURI(Assets.stringBundle.format("linkVersion"))
                        }
                    })
                    ui.helpTable.add(lblDesktopVersion).expand().padBottom(10f)
                    ui.helpTable.row()
                    ui.helpTable.add(lbl3).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
                    ui.helpTable.row()
                }
            }


            ui.helpTable.add(ui.btnOk)
        }
    }

    override fun closeUi() {
    }

    inner class HelpUI(skin : Skin) {
        internal var helpWin = Window(Assets.stringBundle.format("help"),skin)

        internal var helpTable = Table()

        internal var btnOk: TextButton

        init {
            btnOk = TextButton("Ok", skin)
            btnOk.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@HelpDialog.closeDialog()
                }
            })

            helpWin.add(helpTable)
//            helpWin.debugAll()
            helpWin.pack()

        }
    }

}