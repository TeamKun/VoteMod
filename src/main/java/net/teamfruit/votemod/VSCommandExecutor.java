package net.teamfruit.votemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class VSCommandExecutor extends CommandBase {
    private final VoteMod mod;

    public VSCommandExecutor(VoteMod mod) {
        this.mod = mod;
    }

    @Override public String getName() {
        return "vs";
    }

    @Override public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override public String getUsage(ICommandSender iCommandSender) {
        return "/vs 投票を開始します";
    }

    @Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                mod.getConfig().load();
                sender.sendMessage(new TextComponentString("設定をリロードしました"));
                return;
            }
            if ("setup".equals(args[0])) {
                mod.getPlayers().set(server.getOnlinePlayerNames());
                mod.getConfig().save();
                sender.sendMessage(new TextComponentString("設定を今いるプレイヤー全員にセットしました"));
                return;
            }
        }

        if (this.mod.isVoting) {
            this.mod.isVoting = false;
            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GOLD + "投票を終了しました。"));
            List<Map.Entry<String, Integer>> list_entries = new ArrayList(this.mod.data.entrySet());
            Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                    return ((Integer) obj2.getValue()).compareTo((Integer) obj1.getValue());
                }
            });
            int c = 0;
            String[] s = new String[6];
            s[0] = TextFormatting.GREEN + "   投票結果   ";
            Iterator var8 = list_entries.iterator();

            while (var8.hasNext()) {
                Map.Entry<String, Integer> entry = (Map.Entry) var8.next();
                ++c;
                server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.AQUA + "" + c + "位 " + TextFormatting.GOLD + (String) entry.getKey() + TextFormatting.GRAY + " (" + TextFormatting.GRAY + entry.getValue() + "票)"));
                s[c] = "" + TextFormatting.GREEN + TextFormatting.AQUA + "" + c + "位 " + TextFormatting.GOLD + (String) entry.getKey() + TextFormatting.GRAY + " (" + TextFormatting.GRAY + entry.getValue() + "票)";
                if (c > 5) {
                    break;
                }
            }

            var8 = server.getPlayerList().getPlayers().iterator();

            while (var8.hasNext()) {
                EntityPlayer p = (EntityPlayer) var8.next();
                //ScoreboardUtil.unrankedSidebarDisplay(p, s);
            }
        } else {
            this.mod.isVoting = true;
            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GOLD + "投票を開始しました。"));
            server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN + "/v <プレイヤー名>で投票をしてください。"));
            Iterator var10 = server.getPlayerList().getPlayers().iterator();

            while (var10.hasNext()) {
                EntityPlayerMP p = (EntityPlayerMP) var10.next();
                //ScoreboardUtil.unrankedSidebarDisplay(p, new String[]{""});
            }

            this.mod.votedplayers = new ArrayList<>();
            this.mod.votedata = new HashMap<>();
            this.mod.data = new HashMap<>();
        }
    }
}
