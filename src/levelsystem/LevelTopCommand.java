package levelsystem;

import Luxi.SPlayer.RPGPlayer;
import Luxiel.Main;
import Util.CenteredMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelTopCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (cmd.getName().equalsIgnoreCase("leveltop")) {
            int page = 1;
            if (args.length > 0) {
                page = Integer.valueOf(args[0]);
            }
            Player p = (Player) sender;
            p.sendMessage(ChatColor.GREEN + "Đang tải dữ liệu, vui lòng chờ chút");
            int finalPage = page;
            Bukkit.getScheduler().runTaskAsynchronously(Main.m, new Runnable() {
                @Override
                public void run() {
                    CenteredMessage.sendCenteredMessage(p,ChatColor.RED + "Top cấp độ, Trang " + finalPage);
                    p.sendMessage("");
                    int index = (finalPage - 1) * 10;
                    for (RPGPlayer pp : LevelTop.getTop(index, index + 11)) {
                        if (pp == null)
                            continue;
                        CenteredMessage.sendCenteredMessage(p,ChatColor.GRAY + "" + index + " " + ChatColor.RED
                                + pp.getName() + " " + ChatColor.GRAY + " Cấp "
                                + ChatColor.GREEN + "" + pp.getLevel());
                        index++;
                    }
                    p.sendMessage("");
                    CenteredMessage.sendCenteredMessage(p,ChatColor.YELLOW + "Vị trí của bạn " + ChatColor.AQUA
                            + LevelTop.getPlace(p.getUniqueId()));

                }
            });

        }
        return false;
    }

}
