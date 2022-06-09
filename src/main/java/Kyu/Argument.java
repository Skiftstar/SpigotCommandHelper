package Kyu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Argument {
    
    private String argName;
    private Map<Integer, List<Argument>> args = new HashMap<>();
    private int argSlot;
    private Consumer<CommandEvent> consumer;
    private List<String> tabValues = null;
    private Supplier<List<String>> tabSupplier = null;
    private Argument parent = null;

    Argument(String argName, int argSlot, Consumer<CommandEvent> consumer) {
        this.argName = argName;
        this.argSlot = argSlot;
        this.consumer = consumer;
    }

    public void setTabValues(List<String> tabValues) {
        this.tabValues = tabValues;
        this.tabSupplier = null;
    }

    public void setTabValues(Supplier<List<String>> function ) {
        this.tabSupplier = function;
        tabValues = null;
    }

    public List<String> getTabValues() {
        if (tabValues != null) {
            return tabValues;
        } else if (tabSupplier != null) {
            return tabSupplier.get();
        } else {
            return Collections.emptyList();
        }
    }

    void setParent(Argument parent) {
        this.parent = parent;
    }

    Argument getParent() {
        return parent;
    }

    public String getArgName() {
        return argName;
    }

    public int getArgSlot() {
        return argSlot;
    }

    public int getArgSlotAbsolute() {
        if (parent == null) {
            return argSlot;
        } else {
            return parent.getArgSlotAbsolute() + 1 + argSlot;
        }
    }

    public void removeArgument(Argument arg) {
        args.get(arg.getArgSlot()).remove(arg);
    }

    public void addArgument(Argument arg) {
        int slot = arg.getArgSlot();
        if (!args.containsKey(slot)) {
            args.put(slot, new ArrayList<>(Arrays.asList( arg )));
        }
        args.get(slot).add(arg);
        if (arg.getParent() != null) {
            arg.getParent().removeArgument(arg);
        }
        arg.setParent(this);
    }

    public List<Argument> getArgumentsList() {
        List<Argument> list = new ArrayList<>();
        for (int slot : args.keySet()) {
            list.addAll(args.get(slot));
        }
        return list;
    }


    public List<Argument> getArguments(int slot) {
        return args.getOrDefault(slot, new ArrayList<>());
    }

    public Map<Integer, List<Argument>> getArguments() {
        return args;
    }

    List<Integer> getSlots() {
        return new ArrayList<>(args.keySet());
    }

    void accept(CommandEvent e) {
        for (int slot : getSlots()) {
            if (slot + argSlot + 1 > e.args().length - 1) {
                continue;
            }
            for (Argument arg : getArguments(slot)) {
                if (e.args()[slot].equalsIgnoreCase(arg.getArgName())) {
                    arg.accept(e);
                    return;
                }
            }
        }
        if (consumer != null) {
            consumer.accept(e);
        }
    }


}
