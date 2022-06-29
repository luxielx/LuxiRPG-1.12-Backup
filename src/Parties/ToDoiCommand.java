package Parties;

import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class ToDoiCommand implements CommandExecutor, Listener {
    HashMap<UUID, Long> cooldown = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player sd = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("todoi")) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.YELLOW + "/td " + ChatColor.GRAY + "| Mở giao diện tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi tao <tên> " + ChatColor.GRAY + "| Tạo tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi giaitan " + ChatColor.GRAY + "| Giải tán tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi sut <tên người chơi> " + ChatColor.GRAY + "| Sút ra khỏi tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi moi <tên người chơi> " + ChatColor.GRAY + "| Mời vào tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi thoat " + ChatColor.GRAY + "| Rời khỏi tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi doiten <tên mới> " + ChatColor.GRAY + "| Đổi tên tổ đội");
            } else {
                if (args[0].equalsIgnoreCase("tao")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/todoi tao <tên>  " + ChatColor.GRAY + "| Tạo tổ đội");
                    } else {
                        if (args[1].length() >= 3 && args[1].length() <= 10) {
                            PartyUtils.createParty(sd.getUniqueId(), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.RED + "Tên tổ đội phải từ 3 -> 10 chữ cái");
                        }

                    }
                } else if (args[0].equalsIgnoreCase("vaotodoingaybaygioluonne")) {
                    if (PartyUtils.getParty(args[1]) != null)
                        PartyUtils.addPlayer(PartyUtils.getParty(args[1]), sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("moi")) {
                    // 1000 -> 2000
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/todoi moi <tên người chơi>  " + ChatColor.GRAY + "| Mời vào tổ đội");
                    } else {

                        Player of = Bukkit.getPlayer(args[1]);
                        if (of == null) {
                            sd.sendMessage(ChatColor.RED + "Người chơi không tồn tại");
                        } else {
                            if (!Utils.isIgnored(of, sd))
                                PartyUtils.invite(sd.getUniqueId(), of.getUniqueId());
                        }
                        cooldown.put(sd.getUniqueId(), System.currentTimeMillis());


                    }
                } else if (args[0].equalsIgnoreCase("giaitan")) {
                    PartyUtils.deleteParty(sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("sut")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/todoi sut <tên người chơi>  " + ChatColor.GRAY + "| Sút ra khỏi tổ đội");
                    } else {
                        OfflinePlayer of = Bukkit.getOfflinePlayer(args[1]);
                        if (of == null) {
                            sd.sendMessage(ChatColor.RED + "Người chơi không tồn tại");
                        } else {
                            PartyUtils.kickPlayer(sd.getUniqueId(), of.getUniqueId());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("thoat")) {
                    PartyUtils.leaveParty(sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("doiten")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/todoi doiten <Tên mới>  " + ChatColor.GRAY + "| Đổi tên tổ đội");
                    } else {
                        if (args[1].length() >= 3 && args[1].length() <= 10) {
                            PartyUtils.renameParty(sd.getUniqueId(), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "Tên tổ đội phải từ 3-10 chữ cái");
                        }
                    }
                }
            }
        }


        return true;
    }


    @EventHandler
    public void playerOut(PlayerQuitEvent e) {
        if (PartyUtils.haveParty(e.getPlayer().getUniqueId())) {
            PartyUtils.getParty(e.getPlayer().getUniqueId()).removeMember(e.getPlayer().getUniqueId());
        }
    }
}
