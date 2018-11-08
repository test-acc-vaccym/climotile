package com.github.quarck.climotext

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.ArrayAdapter

@SuppressLint("Registered")
open class QSHubCommandService(val dstId: Int) : TileService() {

//    private var isTileActive: Boolean = false

    override fun onClick() {

        val builder = AlertDialog.Builder(this)
        builder.setIcon(android.R.drawable.ic_menu_directions)
        builder.setTitle("Choose command")

        val names = listOf<String>("Boost 0.5", "Boost 1", "Boost 2", "Cancel Boost")
        val values = listOf<String>("0.5", "1", "2", "")

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, names)

        builder.setNegativeButton(android.R.string.cancel) {
                dialog, which -> dialog.dismiss()
        }

        builder.setAdapter(arrayAdapter) {
                dialog, which ->
            if (which >= 0 && which < values.size && which < names.size) {
                val duration = values[which]
                if (duration != "")
                    HubCommands.boost(dstId, duration)
                else
                    HubCommands.cancelBoost(dstId)
            }
        }

        this.showDialog(builder.create())
    }

    private fun updateTile() {
        val tile = super.getQsTile()
        val activeState = Tile.STATE_ACTIVE
        tile.state = activeState
        tile.updateTile()
    }
}


class QSHubCommandServiceLiving: QSHubCommandService(HubCommands.DESTINATION_LIVING)
class QSHubCommandServiceBed: QSHubCommandService(HubCommands.DESTINATION_BED)
class QSHubCommandServiceWater: QSHubCommandService(HubCommands.DESTINATION_WATER)