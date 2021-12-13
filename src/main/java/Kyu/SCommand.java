package Kyu;

import Kyu.LangSupport.LanguageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class SCommand implements CommandExecutor {

    private Consumer<CommandEvent> consumer = null;
    private String execPerm = null;
    private int minArgs = 0;
    private boolean playerOnly = false;
    private LanguageHelper helper;

    public SCommand(JavaPlugin plugin, String command, LanguageHelper helper) {
        this.helper = helper;
        plugin.getCommand(command).setExecutor(this);
    }

    public void execPerm(String perm) {
        execPerm = perm;
    }

    public void minArgs(int args) {
        minArgs = args;
    }

    public void playerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public void exec(Consumer<CommandEvent> con) {
        consumer = con;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandEvent e = new CommandEvent(sender, command, label, args);
        if (playerOnly && !e.isPlayer()) {
            sender.sendMessage(helper.getMess("PlayerOnly"));
            return false;
        }
        if (execPerm != null && e.isPlayer() && !e.player().hasPermission(execPerm)) {
            sender.sendMessage(helper.getMess(e.player(), "NEPerms"));
            return false;
        }
        if (args.length < minArgs) {
            if (e.isPlayer()) {
                sender.sendMessage(helper.getMess(e.player(), "NEArgs"));
            } else {
                sender.sendMessage(helper.getMess("NEArgs"));
            }
            return false;
        }

        if (consumer != null) {
            consumer.accept(e);
            return true;
        } else return false;
    }

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
}
