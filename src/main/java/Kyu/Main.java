package Kyu;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.plugin.java.JavaPlugin;
import Kyu.LangSupport.LanguageHelper;
import net.kyori.adventure.text.Component;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        
        LanguageHelper helper = new LanguageHelper(this, "en", getTextResource("en.yml"), "testPrefix");
        SCommand command = new SCommand(this, "test", helper);
        Argument arg1 = command.addArgument("arg1", 0, e -> {
            e.player().sendMessage(Component.text("arg1"));
        });
        command.addArgument("arg2", 0, e -> {
            e.player().sendMessage(Component.text("arg2"));
        });

        arg1.setTabValues(() -> {
            return new ArrayList<String>(Arrays.asList("test"));
        });

        arg1.addArgument("deepArg1", 0, e -> {
            e.player().sendMessage(Component.text("deepArg1"));
        });

        arg1.addArgument("testArg", 2, e -> {
            e.player().sendMessage(Component.text("testArg"));
        });
    }

    @Override
    public void onDisable() {
        
    }
    
}
