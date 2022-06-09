package Kyu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TabEvent {

    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;

    public TabEvent(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public CommandSender sender() {
        return sender;
    }

    public Command command() {
        return command;
    }

    public String label() {
        return label;
    }

    public String[] args() {
        return args;
    }

}