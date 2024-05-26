package me.nobaboy.nobaaddons.features.chatcommands.impl.party

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.HypixelCommands
import org.apache.commons.lang3.StringUtils
import kotlin.time.Duration.Companion.seconds

class WarpCommand : IChatCommand {
    companion object {
        var cancel = false
    }

    private var delay = 0
    private var isWarping = false

    override val name: String = "warp"

    override val usage: String = "warp [optional: username]"

    override val isEnabled: Boolean
        get() = NobaAddons.config.chatCommands.partyCommands.warp

    override fun run(ctx: ChatContext) {
        if (!PartyAPI.isLeader()) return

        val time = if (ctx.args().isEmpty()) null else ctx.args()[0]
        warpParty(time)
    }

    private fun warpParty(seconds: String?) {
        if (seconds == null) {
            HypixelCommands.partyWarp()
        } else if (!StringUtils.isNumeric(seconds)) {
            HypixelCommands.partyChat("First argument can either be an integer or empty")
        } else if (seconds.toInt() > 15 || seconds.toInt() < 3) {
            HypixelCommands.partyChat("Warp delay has a maximum limit of 15 seconds and a minimum of 3.")
        } else {
            delay = seconds.toInt()
            isWarping = true
            startTimedWarp()
        }
    }

    private fun startTimedWarp() {
        HypixelCommands.partyChat("Warping in $delay (To cancel type '!cancel')")
        NobaAddons.coroutineScope.launch {
            while (delay-- >= 0) {
                delay(1.seconds)

                if (cancel) {
                    HypixelCommands.partyChat("Warp cancelled...")
                    isWarping = false
                    cancel = false
                    break
                }

                if (delay == 0) {
                    HypixelCommands.partyWarp()
                    isWarping = false
                    break
                }

                HypixelCommands.partyChat("$delay")
            }
        }
    }
}