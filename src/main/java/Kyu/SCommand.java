package Kyu;

import Kyu.LangSupport.LanguageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SCommand implements CommandExecutor, TabCompleter {

    private Consumer<CommandEvent> consumer = null;
    private Function<TabEvent, List<String>> tabConsumer = null;
    private String execPerm = null;
    private int minArgs = 0;
    private boolean playerOnly = false;
    private boolean handleTabByArguments = true;
    private Supplier<List<String>> tabSupplier = null;
    private LanguageHelper helper;
    private Map<Integer, List<Argument>> args = new HashMap<>();

    public SCommand(JavaPlugin plugin, String command, LanguageHelper helper) {
        this.helper = helper;
        plugin.getCommand(command).setExecutor(this);
    }

    public void setLangHelper(LanguageHelper helper) {
        this.helper = helper;
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

    public void handleTabByArguments(boolean handleTabByArguments) {
        this.handleTabByArguments = handleTabByArguments;
    }

    public void zeroArgTab(Supplier<List<String>> function) {
        tabSupplier = function;
    }

    public void exec(Consumer<CommandEvent> con) {
        consumer = con;
    }

    public void tabComplete(Function<TabEvent, List<String>> con) {
        tabConsumer = con;
    }

    public void addArgument(Argument arg) {
        int slot = arg.getArgSlot();
        if (!args.containsKey(slot)) {
            args.put(slot, new ArrayList<>(Arrays.asList(arg)));
        }
        args.get(slot).add(arg);
    }

    public Argument addArgument(String name, int relativeSlot, Consumer<CommandEvent> e) {
        Argument arg = new Argument(name, relativeSlot, e);
        addArgument(arg);
        return arg;
    }

    public void removeArgument(Argument arg) {
        int slot = arg.getArgSlot();
        if (args.containsKey(slot)) {
            args.get(slot).remove(arg);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        CommandEvent e = new CommandEvent(sender, command, label, args);
        if (playerOnly && !e.isPlayer()) {
            sender.sendMessage(helper.getMess("PlayerOnly"));
            return false;
        }
        if (execPerm != null && e.isPlayer() && !e.player().hasPermission(execPerm)) {
            sender.sendMessage(helper.getMess(e.player(), "NEPerms", true));
            return false;
        }
        if (args.length < minArgs) {
            if (e.isPlayer()) {
                sender.sendMessage(helper.getMess(e.player(), "NEArgs", true));
            } else {
                sender.sendMessage(helper.getMess("NEArgs"));
            }
            return false;
        }

        for (int slot : this.args.keySet()) {
            if (slot > e.args().length - 1) {
                continue;
            }
            for (Argument arg : this.args.get(slot)) {
                if (e.args()[slot].equalsIgnoreCase(arg.getArgName())) {
                    arg.accept(e);
                    return true;
                }
            }
        }

        if (consumer != null) {
            consumer.accept(e);
            return true;
        } else
            return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String label, @NotNull String[] args) {
        if (!handleTabByArguments) {
            return tabConsumer != null ? tabConsumer.apply(new TabEvent(sender, cmd, label, args))
                    : Collections.emptyList();
        }
        List<Argument> arguments = new ArrayList<>();
        for (int slot : this.args.keySet()) {
            arguments.addAll(this.args.get(slot));
        }

        if (args.length == 1 && tabSupplier != null) {
            return tabSupplier.get();
        }

        for (int slot : this.args.keySet()) {
            if (slot > args.length - 1) {
                continue;
            }
            for (Argument arg : this.args.get(slot)) {
                if (args[slot].equalsIgnoreCase(arg.getArgName())) {
                    return arg.getTabValues(args);
                }
            }
        }
        return Collections.emptyList();
    }
}
