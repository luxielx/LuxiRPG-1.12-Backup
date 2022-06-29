package guild;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.CenteredMessage;
import Util.Utils;
import levelsystem.LevelTop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class GuildCommand implements CommandExecutor, Listener {

    HashMap<UUID, Long> cooldown = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player sd = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("guild")) {
            if (args.length <= 0) {
                sender.sendMessage(ChatColor.YELLOW + "/g " + ChatColor.GRAY + "| Mở giao diện Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild tao <tên> " + ChatColor.GRAY + "| Tạo Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild giaitan " + ChatColor.GRAY + "| Giải tán Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild sut <tên người chơi> " + ChatColor.GRAY + "| Sút ra khỏi Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild moi <tên người chơi> " + ChatColor.GRAY + "| Mời vào Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild thoat " + ChatColor.GRAY + "| Rời khỏi Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild doiten <tên mới> " + ChatColor.GRAY + "| Đổi tên Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild top " + ChatColor.GRAY + "| Top Guild");
                if (sd.hasPermission("admincuasv")) {
                    sender.sendMessage(ChatColor.YELLOW + "/guild xoa <tên> " + ChatColor.GRAY + "| Xóa Guild");
                    sender.sendMessage(ChatColor.YELLOW + "/guild setpoint <tên> <point> " + ChatColor.GRAY + "| set điểm Guild");
                }
            } else {
                if (args[0].equalsIgnoreCase("tao")) {
                    if(sd.hasPermission("rpg.taoguild")) {
                        if (args.length <= 1) {
                            sender.sendMessage(ChatColor.YELLOW + "/guild tao <tên>  " + ChatColor.GRAY + "| Tạo Guild");
                        } else {
                            if (args[1].length() >= 3 && args[1].length() <= 10) {
                                if (Guild.getGuild(args[1]) != null) {
                                    sender.sendMessage(ChatColor.GRAY + "Guild " + args[1] + ChatColor.GRAY + " đã tồn tại!!");

                                } else {
                                    GuildGUI.openCreateGUI(sd.getUniqueId(), args[1]);
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Tên Guild phải từ 3 -> 10 chữ cái");
                            }

                        }
                    }else{
                        sd.sendMessage(ChatColor.RED + "Rank Mizunoe trở lên mới có thể tạo Guild!");
                    }
                } else if (args[0].equalsIgnoreCase("vaoguildbaygiolunne")) {
                    if (GuildUtils.getGuild(args[1]) != null)
                        GuildUtils.addPlayer(GuildUtils.getGuild(args[1]), sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("moi")) {
                    // 1000 -> 2000
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/guild moi <tên người chơi>  " + ChatColor.GRAY + "| Mời vào Guild");
                    } else {

                        Player of = Bukkit.getPlayer(args[1]);
                        if (of == null) {
                            sd.sendMessage(ChatColor.RED + "Người chơi không tồn tại");
                        } else {
                            if (!Utils.isIgnored(of, sd))
                                GuildUtils.invite(sd.getUniqueId(), of.getUniqueId());
                        }
                        cooldown.put(sd.getUniqueId(), System.currentTimeMillis());


                    }
                } else if (args[0].equalsIgnoreCase("giaitan")) {
                    GuildUtils.deleteGuild(sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("sut")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/guild sut <tên người chơi>  " + ChatColor.GRAY + "| Sút ra khỏi Guild");
                    } else {
                        OfflinePlayer of = Bukkit.getOfflinePlayer(args[1]);
                        if (of == null) {
                            sd.sendMessage(ChatColor.RED + "Người chơi không tồn tại");
                        } else {
                            GuildUtils.kickPlayer(sd.getUniqueId(), of.getUniqueId());
                        }
                    }
                } else if (args[0].equalsIgnoreCase("thoat")) {
                    GuildUtils.leaveGuild(sd.getUniqueId());
                } else if (args[0].equalsIgnoreCase("doiten")) {
                    if (args.length <= 1) {
                        sender.sendMessage(ChatColor.YELLOW + "/guild doiten <Tên mới>  " + ChatColor.GRAY + "| Đổi tên Guild");
                    } else {
                        if (args[1].length() >= 3 && args[1].length() <= 10) {
                            GuildUtils.renameGuild(sd.getUniqueId(), args[1]);
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "Tên Guild phải từ 3-10 chữ cái");
                        }
                    }
                }else if(args[0].equalsIgnoreCase("top")){
                    int page = 1;
                    if (args.length > 1) {
                        page = Integer.valueOf(args[1]);
                    }
                    RPGPlayer rpgPlayer = RPGPlayerListener.get(sd);
//                    boolean haveguild=true;
                    Guild g=rpgPlayer.getGuild();;

                    sd.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "Đang tải dữ liệu, vui lòng chờ chút");
                    int finalPage = page;
                    Bukkit.getScheduler().runTaskAsynchronously(Main.m, new Runnable() {
                        @Override
                        public void run() {
                            CenteredMessage.sendCenteredMessage(sd,net.md_5.bungee.api.ChatColor.RED + "Top Guild, Trang " + finalPage);
                            sd.sendMessage("");
                            int index = (finalPage - 1) * 10;
                            for (Guild pp : GuildTop.getTop(index, index + 11)) {
                                if (pp == null)
                                    continue;
                                CenteredMessage.sendCenteredMessage(sd,net.md_5.bungee.api.ChatColor.WHITE + "" + index+ChatColor.GRAY + " Guild " + pp.getColor()
                                        + pp.getName()  + net.md_5.bungee.api.ChatColor.GRAY + " Cấp độ trung bình "
                                        + net.md_5.bungee.api.ChatColor.GREEN + "" + pp.calcAvgLevel() +ChatColor.GRAY + " Thành Viên: " + ChatColor.GREEN + pp.members.size());
//                                sd.sendMessage(net.md_5.bungee.api.ChatColor.WHITE + "" + index+ChatColor.GRAY + " Guild " + pp.getColor()
//                                        + pp.getName()  + net.md_5.bungee.api.ChatColor.GRAY + " Cấp độ trung bình "
//                                        + net.md_5.bungee.api.ChatColor.GREEN + "" + pp.calcAvgLevel() +ChatColor.GRAY + " Thành Viên: " + ChatColor.GREEN + pp.members.size());
                                index++;
                            }
                            sd.sendMessage("");
                            if(g != null)
                                CenteredMessage.sendCenteredMessage(sd,net.md_5.bungee.api.ChatColor.GRAY + "Vị trí Guild "+g.getColor()+g.getName() +ChatColor.GRAY+" là "+ net.md_5.bungee.api.ChatColor.AQUA
                                     +GuildTop.getPlace(g));

                        }
                    });
                }
                else if (args[0].equalsIgnoreCase("xoa")) {
                    if (Guild.getGuild(args[1]) != null) {
                        Guild.getGuild(args[1]).disband();
                        sender.sendMessage(ChatColor.RED + "Guild đã bị xóa!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Guild này không tồn tại!");
                    }

                } else if (args[0].equalsIgnoreCase("setpoint")) {
                    if (Guild.getGuild(args[1]) != null) {
                        Guild g = Guild.getGuild(args[1]);
                        g.setPowerpoint(Integer.valueOf(args[2]));

                    } else {
                        sender.sendMessage(ChatColor.RED + "Guild này không tồn tại!");
                    }

                }
            }
        }


        return true;
    }


}

