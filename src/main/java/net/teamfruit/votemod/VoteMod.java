package net.teamfruit.votemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(
        modid = VoteMod.MOD_ID,
        name = VoteMod.MOD_NAME,
        version = VoteMod.VERSION,
        acceptableRemoteVersions = "*"
)
public class VoteMod {

    public static final String MOD_ID = "votemod";
    public static final String MOD_NAME = "VoteMod";
    public static final String VERSION = "1.0-SNAPSHOT";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static VoteMod INSTANCE;

    public boolean isVoting;
    public List<String> votedplayers;
    public Map<String, String> votedata;
    public Map<String, Integer> data;

    private Configuration config;

    public Configuration getConfig() {
        return this.config;
    }

    public Property getPlayers() {
        return this.config.get("players", "general", new String[]{"aa", "ba", "ca", "da", "fa"}, "Player List");
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Log.log = event.getModLog();

        config = new Configuration(event.getSuggestedConfigurationFile());
        getPlayers();
        config.save();

        this.isVoting = false;
        this.votedplayers = new ArrayList<>();
        this.votedata = new HashMap<>();
        this.data = new HashMap<>();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new VCommandExecutor(this));
        event.registerServerCommand(new VGETCommandExecutor(this));
        event.registerServerCommand(new VSCommandExecutor(this));
    }
}
