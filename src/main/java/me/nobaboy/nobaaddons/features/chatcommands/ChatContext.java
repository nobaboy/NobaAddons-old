package me.nobaboy.nobaaddons.features.chatcommands;

public class ChatContext {
    private final String user, command, fullMessage;
    private final String[] args;

    public ChatContext(String user, String command, String[] args, String fullMessage) {
        this.user = user;
        this.command = command;
        this.args = args;
        this.fullMessage = fullMessage;
    }

    public String user() {
        return this.user;
    }

    public String command() {
        return command;
    }

    public String[] args() {
        return args;
    }

    public String fullMessage() {
        return fullMessage;
    }
}