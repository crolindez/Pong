package es.carlosrolindez.ping.core.dialogs

import com.badlogic.gdx.Application
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import es.carlosrolindez.ping.core.PingScreen
import es.carlosrolindez.ping.core.utils.Assets
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import es.carlosrolindez.ping.core.utils.DIALOG_SCREEN_WIDTH


class HelpDialog(gameScreen: PingScreen) : BaseDialog(gameScreen , 0.5f) {
    private var ui : HelpUI
    private var singleSetup = true

    init {
        ui = HelpUI(Assets.instance.skin)
    }

    override fun getUiActor(): Actor {
        return ui.helpWin
    }

    override fun prepareUi() {
        if (singleSetup) {
            singleSetup = false

            val lbl1 = Label("", Assets.instance.skin)
            lbl1.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl1.style.background = Assets.instance.skin.newDrawable("white")
            val lbl2 = Label("", Assets.instance.skin)
            lbl2.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl2.style.background = Assets.instance.skin.newDrawable("white")
            val lbl3 = Label("", Assets.instance.skin)
            lbl3.setColor(0.5f, 0.5f, 0.5f, 1f)
            lbl3.style.background = Assets.instance.skin.newDrawable("white")

            when {
                Gdx.app.getType() == Application.ApplicationType.Desktop -> {
                    ui.helpTable.add(Label(Assets.instance.stringBundle.format("desktopInstructions1"), Assets.instance.skin, "font", Color.BLACK)).expand().padBottom(5f)
                    ui.helpTable.row()
                    ui.helpTable.add(Label(Assets.instance.stringBundle.format("desktopInstructions2"), Assets.instance.skin, "font", Color.BLACK)).expand().padBottom(10f)
                    ui.helpTable.row()
                }

                Gdx.app.getType() == Application.ApplicationType.Android -> {
                    ui.helpTable.add(Label(Assets.instance.stringBundle.format("androidInstructions"), Assets.instance.skin, "font", Color.BLACK)).expand().padBottom(10f)
                    ui.helpTable.row()
                }
            }

            ui.helpTable.add(lbl1).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
            ui.helpTable.row()
            ui.helpTable.add(Label(Assets.instance.stringBundle.format("networkInstructions"), Assets.instance.skin, "font", Color.BLACK)).expand().padBottom(10f)
            ui.helpTable.row()
            ui.helpTable.add(lbl2).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
            ui.helpTable.row()

            when {
                gameScreen.versionAvailable -> {
                    val lblLinkVersion = Label(Assets.instance.stringBundle.format("newVersion"), Assets.instance.skin, "font", Color.BLUE)
                    lblLinkVersion.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            Gdx.net.openURI(Assets.instance.stringBundle.format("linkVersion"))
                        }
                    })
                    ui.helpTable.add(lblLinkVersion).expand().padBottom(10f)
                    ui.helpTable.row()
                    ui.helpTable.add(lbl3).height(1f).width(DIALOG_SCREEN_WIDTH * ConfigurationDialog.scale / 2f).pad(0f, 1f, 10f, 0f)
                    ui.helpTable.row()
                }

                Gdx.app.getType() == Application.ApplicationType.Android -> {
                    val lblDesktopVersion = Label(Assets.instance.stringBundle.format("desktopVersion"), Assets.instance.skin, "font", Color.BLUE)
                    lblDesktopVersion.addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            Gdx.net.openURI(Assets.instance.stringBundle.format("linkVersion"))
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
        internal var helpWin = Window(Assets.instance.stringBundle.format("help"),skin)

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