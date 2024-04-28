package me.nobaboy.nobaaddons.features.chatcommands.impl.dm

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.Utils
import kotlin.time.Duration.Companion.seconds

class WarpUserCommand : IChatCommand {
    companion object {
        @JvmField
        var isWarpingUser = false

        @JvmField
        var playerJoined = false

        @JvmField
        var player: String? = null
    }

    override val name: String = "warpme"

    override val isEnabled: Boolean
        get() = NobaAddons.config.dmWarpMeCommand

    override fun run(ctx: ChatContext) {
        if (ctx.user() == Utils.getPlayerName()) return

        if (isWarpingUser) {
            ChatUtils.queueCommand("r Warp-in is on cooldown, try again later!")
            return
        }

        player = ctx.user().lowercase()
        isWarpingUser = true
        warpUser()
    }

    private fun warpUser() {
        val partyStatus = PartyAPI.inParty
        var membersToInvite: MutableList<String> = mutableListOf()

        NobaAddons.coroutineScope.launch {
            var secondsPassed = 0

            if (partyStatus) {
                membersToInvite = PartyAPI.partyMembers
                ChatUtils.queueCommand("pc Someone requested a warp-in, will re-invite everyone after 15 seconds.")
                ChatUtils.queueCommand("p disband")
            }

            ChatUtils.queueCommand("p $player")

            while (isWarpingUser) {
                if (secondsPassed++ >= 15) {
                    ChatUtils.queueCommand("msg $player Warp timed out since you did not join the party.")
                    ChatUtils.queueCommand("p disband")
                    isWarpingUser = false
                    break
                }

                if (playerJoined) {
                    ChatUtils.queueCommand("p warp")
                    ChatUtils.queueCommand("p kick $player")
                    playerJoined = false
                    isWarpingUser = false
                    break
                }

                delay(1.seconds)
            }

            if (partyStatus) {
                membersToInvite.forEach {
                    if (it != Utils.getPlayerName()) ChatUtils.queueCommand("p invite $it")
                }
            }
        }
    }
}