# Adding to your Project
Add using maven:
```
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```
```
<dependency>
  <groupId>com.github.Skiftstar</groupId>
  <artifactId>SpigotCommandHelper</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
You also need the non-static version of [my SpigotLanguageHelper](https://github.com/Skiftstar/SpigotLangaugeHelper)

# How to use:
```
SCommand spawnCmd = new SCommand(pluginRef, "yourCommand", LanguageHelperRef); //Create new command (you still need to specify command in plugin.yml)
spawnCmd.execPerm("Your Permission"); //You can set an execution permission for the command
spawnCmd.playerOnly(true); //You can set whether the command is playerOnly or not
spawnCmd.minArgs(1); //You can set the minimum amount of arguments required
spawnCmd.exec(e -> { //Your Code past checks
  e.isPlayer(); //Returns whether sender is Player
  e.player(); //Returns Player or null if sender isn't Player
  
  //Get normal command parameters
  e.command(); //Returns Command (from onCommand in normal commands)
  e.sender(); //Returns CommandSender (from OnCommand in normal commands)
  e.args(); //Returns String[] args (from onCommand in normal commands)
  e.label(); //Returns String label (from onCommand in normal commands)
});
```
