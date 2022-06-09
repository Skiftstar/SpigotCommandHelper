package Kyu;

import org.bukkit.plugin.java.JavaPlugin;

import Kyu.LangSupport.LanguageHelper;
import net.kyori.adventure.text.Component;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        
        LanguageHelper helper = new LanguageHelper(this, "en", getTextResource("en.yml"), "testPrefix");
        SCommand command = new SCommand(this, "test", helper);
        Argument arg1 = new Argument("arg1", 0, e -> {
            e.player().sendMessage(Component.text("arg1"));
        });
        Argument arg2 = new Argument("arg2", 0, e -> {
            e.player().sendMessage(Component.text("arg2"));
        });

        Argument arg3 = new Argument("deepArg1", 0, e -> {
            e.player().sendMessage(Component.text("deepArg1"));
        });

        command.addArgument(arg1);
        command.addArgument(arg2);
        arg1.addArgument(arg3);
    }

    @Override
    public void onDisable() {
        
    }
    
}
