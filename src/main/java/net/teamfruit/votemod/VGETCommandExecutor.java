package net.teamfruit.votemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Iterator;
import java.util.Map;

public class VGETCommandExecutor extends CommandBase {
    private final VoteMod mod;

    public VGETCommandExecutor(VoteMod mod) {
        this.mod = mod;
    }

    @Override public String getName() {
        return "vget";
    }

    @Override public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override public String getUsage(ICommandSender iCommandSender) {
        return "/vget 投票先を表示します";
    }

    @Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (this.mod.isVoting) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "投票中は投票先を表示することはできません。"));
            return;
        } else {
            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN + "========= 投票先一覧 ========="));
            Iterator var5 = this.mod.votedata.entrySet().iterator();

            while (var5.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry) var5.next();
                server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.DARK_GREEN + (String) e.getKey() + " >> " + (String) e.getValue()));
            }

            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN + "========= 投票先一覧 ========="));
            return;
        }
    }
}
