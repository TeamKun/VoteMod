package net.teamfruit.votemod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VCommandExecutor extends CommandBase {
    private final VoteMod mod;

    public VCommandExecutor(VoteMod mod) {
        this.mod = mod;
    }

    @Override public String getName() {
        return "v";
    }

    @Override public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override public String getUsage(ICommandSender iCommandSender) {
        return "/v <player> 投票します";
    }

    @Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!this.mod.isVoting) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "投票は始まっていません。"));
            return;
        } else if (this.mod.votedplayers.contains(sender.getName())) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "すでに投票済みです。"));
            return;
        } else if (args.length < 1) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "/v <プレイヤー名>で投票してください。"));
            return;
        } else if (!ArrayUtils.contains(this.mod.getPlayers().getStringList(), args[0])) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + args[0] + "は投票リストに入っていません。"));
            return;
        } else {
            this.mod.votedplayers.add(sender.getName());
            if (this.mod.data.containsKey(args[0])) {
                this.mod.data.replace(args[0], (Integer) this.mod.data.get(args[0]) + 1);
            } else {
                this.mod.data.put(args[0], 1);
            }

            this.mod.votedata.put(sender.getName(), args[0]);
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN + args[0] + "に投票しました。"));
            return;
        }
    }

    @Override public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> listbase = Arrays.asList(this.mod.getPlayers().getStringList());
        List<String> pp = new ArrayList();
        if (args[0].length() < 1) {
            return listbase;
        } else {
            String str = args[0];
            Iterator var8 = listbase.iterator();

            while (var8.hasNext()) {
                String s = (String) var8.next();
                if (s.startsWith(str)) {
                    pp.add(s);
                }
            }

            return pp;
        }
    }
}
