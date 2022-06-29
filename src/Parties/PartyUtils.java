package Parties;


import Luxiel.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class PartyUtils {
    public static String getPartyName(UUID p) {
        if (haveParty(p))
            return getParty(p).getName();
        return "";
    }

    public static Party getParty(UUID player) {
        if (haveParty(player))
            return Party.partylist.get(player);
        return null;
    }

    public static Party getParty(String name) {
        for (Party party : Party.partylist.values()) {
            if (party.getName().equalsIgnoreCase(name)) return party;
        }
        return null;
    }


    public static boolean isLeader(UUID leader) {
        if (!haveParty(leader)) return false;
        Party party = getParty(leader);
        return party.getLeader() == leader;
    }

    public static void kickPlayer(UUID leader, UUID pp) {
        if (isLeader(leader)) {
            if (isSameParty(leader, pp)) {
                Party party = getParty(leader);
                if (Bukkit.getPlayer(pp).isOnline())
                    Bukkit.getPlayer(pp).sendMessage(ChatColor.RED + Bukkit.getPlayer(leader).getName() + ChatColor.GRAY + " đã sút bạn ra khỏi tổ đội " + party.getColor() + party.getName() + ChatColor.GRAY + "!");
                Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Bạn đã sút " + ChatColor.RED + Bukkit.getOfflinePlayer(pp).getName() + ChatColor.GRAY + " ra khỏi tổ đội!");
                party.removeMember(pp);
            } else {
                Bukkit.getPlayer(leader).sendMessage(ChatColor.RED + "Người chơi này không thuộc tổ đội của bạn");
            }
        } else {
            Bukkit.getPlayer(leader).sendMessage(ChatColor.RED + "Bạn không phải đội trưởng");
        }
    }

    public static void createParty(UUID leader, String name) {
        if (!haveParty(leader)) {
            Party p = new Party(leader, name);
            Party.partylist.put(leader, p);
            Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Tổ đội " + p.getColor() + name + ChatColor.GRAY + " đã được tạo!!");
        }
    }

    public static void deleteParty(UUID leader) {
        if (!haveParty(leader)) return;
        Party party = getParty(leader);
        if (isLeader(leader)) {
            party.disband();
        } else {
            Bukkit.getPlayer(leader).sendMessage(ChatColor.GRAY + "Bạn phải là đội trưởng mới có thể giải tán đội , nếu muốn thoát tổ đội " + ChatColor.RED + "/todoi thoat");
        }


    }

    public static boolean haveParty(UUID p) {
        return Party.partylist.containsKey(p);

    }

    public static boolean isSameParty(UUID p, UUID z) {
        if (!haveParty(p)) return false;
        if (!haveParty(z)) return false;
        Party party = Party.partylist.get(p);
        return party.getMembers().contains(z);

    }


    public static void leaveParty(UUID sd) {
        if (haveParty(sd)) {
            getParty(sd).removeMember(sd);
            Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Đã thoát khỏi tổ đội.");

        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Bạn không thuộc tổ đội nào cả.");
        }
    }

    public static void renameParty(UUID sd, String arg) {
        if (isLeader(sd)) {
            Party pt = getParty(sd);
            pt.setName(arg);
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Đổi tên tổ đội thành công ! Tên mới: " + pt.getColor() + arg);
        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Chỉ đội trưởng mới có thể đổi tên");
        }
    }

    public static void addPlayer(Party party, UUID player) {
        if (party == null) return;
        if (party.getMembers().size() >= 5) {
            Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Tổ đội đã đầy!!");
        } else {
            party.addMember(player);
            if (!party.getMembers().contains(player))
                Bukkit.getPlayer(player).sendMessage(ChatColor.GRAY + "Đã gia nhập tổ đội " + party.getColor() + party.getName());
        }

    }

    public static void invite(UUID sd, UUID of) {
        if (isLeader(sd)) {
            if (getParty(sd).getMembers().size() < 5) {
                if (!isSameParty(sd, of)) {
                    Party pt = getParty(sd);
                    Bukkit.getPlayer(sd).sendMessage(ChatColor.GREEN + "Đã mời " + ChatColor.WHITE + Bukkit.getPlayer(of).getName() + ChatColor.GREEN + " tham gia tổ đội! Hãy chờ bạn ấy trả lời");
                    TextComponent tt = new TextComponent(ChatColor.RED + Bukkit.getPlayer(sd).getName() + ChatColor.GRAY + " Mời bạn vào tổ đội " + ChatColor.YELLOW + pt.getName() + ChatColor.GRAY + " bấm vào tin nhắn này để đồng ý");
                    HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.LIGHT_PURPLE + "Bấm vào đây để gia nhập tổ đội " + ChatColor.YELLOW + pt.getName())
                            .create());
                    tt.setHoverEvent(hover);
                    tt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/todoi vaotodoingaybaygioluonne " + pt.getName()));
                    Bukkit.getPlayer(of).spigot().sendMessage(tt);
                }
            } else {
                Bukkit.getPlayer(sd).sendMessage(ChatColor.RED + "Tổ đội của bạn đã đầy không thể thêm thành viên");
            }
        } else {
            Bukkit.getPlayer(sd).sendMessage(ChatColor.GRAY + "Chỉ đội trưởng mới có thể mời.");
        }


    }
}
