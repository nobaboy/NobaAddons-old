package me.nobaboy.nobaaddons.features.chatcommands

class ChatContext(
    private val user: String,
    private val command: String,
    private val args: List<String>,
    private val fullMessage: String
) {
    fun user(): String {
        return user
    }

    fun command(): String {
        return command
    }

    fun args(): List<String> {
        return args
    }

    fun fullMessage(): String {
        return fullMessage
    }
}