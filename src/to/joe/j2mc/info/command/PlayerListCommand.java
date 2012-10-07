package to.joe.j2mc.info.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.info.J2MC_Info;

public class PlayerListCommand extends MasterCommand {

    J2MC_Info plugin;

    public PlayerListCommand(J2MC_Info Info) {
        super(Info);
        this.plugin = Info;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        int total = 0;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (!isPlayer || player.canSee(p))
                total++;
        }
        sender.sendMessage(ChatColor.AQUA + "Players (" + total + "/" + plugin.getServer().getMaxPlayers() + "):");
        if (total == 0) {
            sender.sendMessage(ChatColor.RED + "No one is online :(");
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (Player pl : plugin.getServer().getOnlinePlayers()) {
            if (!isPlayer || player.canSee(pl)) {
                String plName = pl.getName();
                String toAdd = ChatColor.GREEN + plName;
                //Set name color
                if (sender.hasPermission("j2mc.fun.admin") && J2MC_Manager.getPermissions().hasFlag(plName, 't'))
                    toAdd = ChatColor.DARK_GREEN + plName;
                if (J2MC_Manager.getPermissions().hasFlag(plName, 'd'))
                    toAdd = ChatColor.GOLD + plName;
                if (sender.hasPermission("j2mc.chat.admin.mute") && pl.hasPermission("j2mc.chat.mute"))
                    toAdd = ChatColor.YELLOW + plName;
                if (sender.hasPermission("j2mc.core.admin") && pl.hasPermission("j2mc.core.admin"))
                    toAdd = ChatColor.RED + plName;
                if (J2MC_Manager.getVisibility().isVanished(pl))
                    toAdd = ChatColor.AQUA + plName;
                //Icons
                if (sender.hasPermission("j2mc.chat.admin.nsa.toggle") && J2MC_Manager.getPermissions().hasFlag(plName, 'N'))
                    toAdd += ChatColor.DARK_AQUA + "\u00ab\u00bb";
                if (sender.hasPermission("j2mc.fun.admin") && J2MC_Manager.getPermissions().hasFlag(plName, 't'))
                    toAdd += ChatColor.DARK_GREEN + "[T]";
                if (sender.hasPermission("j2mc.chat.admin.mute") && pl.hasPermission("j2mc.chat.mute"))
                    toAdd += ChatColor.YELLOW + "[M]";

                builder.append(toAdd + ChatColor.WHITE + ", ");
                if (builder.length() > 75) {
                    builder.setLength(builder.length() - (toAdd + ChatColor.WHITE + ", ").length());
                    sender.sendMessage(builder.toString());
                    builder = new StringBuilder();
                    builder.append(toAdd + ChatColor.WHITE + ", ");
                }
            }
            builder.setLength(builder.length() - 2);
            sender.sendMessage(builder.toString());
        }
    }
}
