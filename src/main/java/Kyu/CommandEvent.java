package Kyu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEvent {
    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;

    public CommandEvent(CommandSender sender, Command command, String label, String[] args) {
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

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player player() {
        return sender instanceof Player ? (Player) sender : null;
    }


}
