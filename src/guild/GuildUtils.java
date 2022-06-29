package guild;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.UUID;

public class GuildUtils {
    public static String getGuildName(UUID p) {
        if (haveGuild(p))
            return getGuild(p).getName();
        return "";
    }

    public static Guild getGuild(UUID player) {
        if (haveGuild(player)) {
            return Utils.getRpgPlayer(player).getGuild();
        }
        return null;

    }

    public static Guild getGuild(String name) {
        for (Guild g : Guild.guildlist.keySet()) {
            if (g.getName().equalsIgnoreCase(name)) return g;
        }
        return null;
    }


    public static boolean isLeader(UUID leader) {
        if (!haveGuild(leader)) return false;
        Guild g = getGuild(leader);
        return g.getGuildMaster().toString().equalsIgnoreCase(leader.toString());
    }

    public static void kickPlayer(UUID leader, UUID pp) {
        if (isLeader(leader)) {
            if (isSameGuild(leader, pp)) {
                Guild Guild = getGuild(leader);

                if (Bukkit.getPlayer(pp) != null && Bukkit.getPlayer(pp).isOnline())
                    Bukkit.getPlayer(pp).sendMessage(ChatColor.RED + Bukkit.getPlayer(leader).getName() + ChatColor.GRAY + " đã sút bạn ra khỏi Guild " + Guild.getColor() + Guild.getName() + ChatColor.GRAY + "!");
                Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Bạn đã sút " + ChatColor.RED + Bukkit.getOfflinePlayer(pp).getName() + ChatColor.GRAY + " ra khỏi Guild!");
                Guild.removeMember(pp);
            } else {
                Bukkit.getPlayer(leader).sendMessage(ChatColor.RED + "Người chơi này không thuộc Guild của bạn");
            }
        } else {
            Bukkit.getPlayer(leader).sendMessage(ChatColor.RED + "Bạn không phải chủ Guild");
        }
    }

    public static void createGuild(UUID leader, String name, Familia fam) {
        if (Guild.getGuild(name) != null) {
            Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Guild " + name + ChatColor.GRAY + " đã tồn tại!!");
        } else if (!haveGuild(leader)) {
            Guild p = new Guild(name, fam, leader, 0);
            Guild.guildlist.put(p, p.getMembers());
            Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Guild " + p.getColor() + name + ChatColor.GRAY + " đã được tạo!!");
            p.saveToFile();
        }

    }


    public static void deleteGuild(UUID leader) {
        if (!haveGuild(leader)) return;
        Guild Guild = getGuild(leader);
        if (isLeader(leader)) {
            Guild.disband();
        } else {
            Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Bạn phải là Chủ Guild mới có thể giải tán đội , nếu muốn thoát Guild " + ChatColor.RED + "/guild thoat");
        }


    }

    public static boolean haveGuild(UUID p) {
        RPGPlayer rpg = RPGPlayerListener.get(p.toString());
        return rpg.getGuild() != null;

    }

    public static boolean isSameGuild(UUID p, UUID z) {
        if (!haveGuild(p)) return false;
        if (!haveGuild(z)) return false;
        RPGPlayer pp = RPGPlayerListener.get(p.toString());
        RPGPlayer zz = RPGPlayerListener.get(z.toString());
        return pp.getGuild() == zz.getGuild();

    }


    public static void leaveGuild(UUID sd) {
        if (haveGuild(sd)) {
            getGuild(sd).removeMember(sd);
            Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Đã thoát khỏi Guild.");
        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Bạn không thuộc Guild nào cả.");
        }
    }

    public static void renameGuild(UUID sd, String arg) {
        if (!isValidName(arg)) {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Tên " + arg + " đã tồn tại! Hãy chọn tên khác!");
            return;
        }
        if (isLeader(sd)) {
            Guild pt = getGuild(sd);
            pt.setName(arg);
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Đổi tên Guild thành công ! Tên mới: " + pt.getColor() + arg);
        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Chỉ Chủ Guild mới có thể đổi tên");
        }
    }

    private static boolean isValidName(String arg) {
        return Guild.getGuild(arg) == null;
    }

    public static void addPlayer(Guild Guild, UUID player) {
        if (Guild == null) return;
        if (Guild.getMembers().size() >= 35) {
            Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Guild đã đầy!!");
        } else {
            Guild.addMember(player);
            if (!Guild.getMembers().contains(player))
                Bukkit.getPlayer(player).sendMessage(ChatColor.GRAY + "Đã gia nhập Guild " + Guild.getColor() + Guild.getName());
        }

    }

    public static void invite(UUID sd, UUID of) {
        if (isLeader(sd)) {
            if (getGuild(sd).getMembers().size() < 35) {
                if (!isSameGuild(sd, of)) {
                    Guild pt = getGuild(sd);
                    Bukkit.getPlayer(sd).sendMessage(ChatColor.GREEN + "Đã mời " + ChatColor.WHITE + Bukkit.getPlayer(of).getName() + ChatColor.GREEN + " tham gia Guild! Hãy chờ bạn ấy trả lời");
                    TextComponent tt = new TextComponent(ChatColor.RED + Bukkit.getPlayer(sd).getName() + ChatColor.GRAY + " Mời bạn vào Guild " + ChatColor.YELLOW + pt.getName() + ChatColor.GRAY + " bấm vào tin nhắn này để đồng ý");
                    HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Bấm vào đây để gia nhập Guild " + ChatColor.YELLOW + pt.getName())
                            .create());
                    tt.setHoverEvent(hover);
                    tt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild vaoguildbaygiolunne " + pt.getName()));
                    Bukkit.getPlayer(of).spigot().sendMessage(tt);
                }
            } else {
                Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Guild của bạn đã đầy không thể thêm thành viên");
            }
        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Chỉ Chủ Guild mới có thể mời.");
        }


    }

    public static void loadGuildFiles() {
        File[] fileNames = new File(Main.m.getDataFolder() + File.separator + "Guild").listFiles();
        if (fileNames.length <= 0) return;
        for (File f : fileNames) {
            Guild g = new Guild(f.getName().replace(".yml", ""));
            g.loadFromFile();
            Guild.guildlist.put(g, g.members);

        }

    }


}
