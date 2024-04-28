package me.nobaboy.nobaaddons.features.chatcommands.impl.shared

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nobaboy.nobaaddons.NobaAddons
import me.nobaboy.nobaaddons.api.PartyAPI
import me.nobaboy.nobaaddons.features.chatcommands.ChatContext
import me.nobaboy.nobaaddons.features.chatcommands.IChatCommand
import me.nobaboy.nobaaddons.util.ChatUtils
import me.nobaboy.nobaaddons.util.Utils
import kotlin.time.Duration.Companion.seconds

class WarpOutCommand(private val command: String, private val enabled: Boolean) : IChatCommand {
    companion object {
        @JvmField
        var isWarpingOut: Boolean = false

        @JvmField
        var playerJoined: Boolean = false

        @JvmField
        var player: String? = null
    }

    override val name: String = "warpout"

    override val usage: String = "warpout [username]"

    override val isEnabled: Boolean
        get() = enabled

    override fun run(ctx: ChatContext) {
        if (isWarpingOut) {
            ChatUtils.queueCommand("$command Warp out is on cooldown, try again later!")
        } else if (ctx.args().isEmpty()) {
            ChatUtils.queueCommand("$command Please provide a username.")
        } else {
            player = ctx.args()[0].lowercase()
            isWarpingOut = true
            warpOutPlayer()
        }
    }

    private fun warpOutPlayer() {
        val partyStatus = PartyAPI.inParty
        var membersToInvite: MutableList<String> = mutableListOf()

        NobaAddons.coroutineScope.launch {
            var secondsPassed = 0

            if (partyStatus) {
                membersToInvite = PartyAPI.partyMembers
                ChatUtils.queueCommand("pc Someone requested a warp-out, will re-invite everyone after 20 seconds.")
                ChatUtils.queueCommand("p disband")
            }

            ChatUtils.queueCommand("p $player")

            while (isWarpingOut) {
                if (secondsPassed++ >= 20) {
                    if (!playerJoined) {
                        ChatUtils.queueCommand("$command Warp out failed, $player did not join the party.")
                        isWarpingOut = false
                        break
                    }
                }

                if (playerJoined) {
                    ChatUtils.queueCommand("p warp")
                    ChatUtils.queueCommand("p disband")
                    ChatUtils.queueCommand("$command Warp out successful.")
                    playerJoined = false
                    isWarpingOut = false
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