package Parties;

import Luxiel.Profile;
import Util.FItem;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PartiesGUI implements Listener, CommandExecutor {

    public static void openPartyGUI(Player p) {
        Party pt = PartyUtils.getParty(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(new PartyGuiHolder(), 27, pt.getColor() + pt.getName());
        ItemStack ldskull = new FItem(Profile.playerInfo(Bukkit.getPlayer(pt.getLeader()))).setName(ChatColor.RED + "Đội Trưởng: " + ChatColor.GRAY + Bukkit.getPlayer(pt.getLeader()).getName()).toItemStack();
        inv.setItem(4, ldskull);
        List<UUID> mlist = new ArrayList<>(pt.getMembers());

        mlist.remove(pt.getLeader());
        int start = 11;
        for (UUID uuid : mlist) {
            if (start == 15) break;
            inv.setItem(start, Profile.playerInfo(Bukkit.getPlayer(uuid)));
            start++;
        }
        for (int i = 11; i <= 15; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, invitemore());
        }
        inv.setItem(15, partyStats(pt));
        for (int i = 0; i < 27; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, Utils.blankItem(Material.STAINED_GLASS_PANE, 15));
        }
        p.openInventory(inv);
    }

    private static ItemStack invitemore() {
        return new FItem(Material.STAINED_GLASS_PANE).setColorShort(1).setName(ChatColor.GREEN + "Hãy mời thêm thành viên!").toItemStack();

    }


    private static ItemStack partyStats(Party p) {
        ItemStack is = new FItem(Material.KNOWLEDGE_BOOK).setName(ChatColor.GOLD + "Thông tin tổ đội")
                .setLore(Arrays.asList(ChatColor.GRAY + "Tên tổ đội: " + p.getColor() + p.getName(),
                        ChatColor.GRAY + "Tổng cấp độ: " + ChatColor.AQUA + p.calcTotalLevel(),
                        ChatColor.GRAY + "Cấp độ trung bình: " + ChatColor.AQUA + p.calcAvgLevel(),
                        ChatColor.GRAY + "Rank: " + p.getColor() + p.calcPartyRank().toString()

                ))
                .glow(true)
                .toItemStack();


        return is;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getInventory().getHolder() instanceof PartyGuiHolder) {
            e.setCancelled(true);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player sd = (Player) sender;

        if (a.equalsIgnoreCase("td")) {
            if (PartyUtils.haveParty(sd.getUniqueId())) {
                PartiesGUI.openPartyGUI(sd);
            } else {
                sd.sendMessage(ChatColor.RED + "Bạn không có tổ đội! ");
                sender.sendMessage(ChatColor.YELLOW + "/td " + ChatColor.GRAY + "| Mở giao diện tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi tao <tên> " + ChatColor.GRAY + "| Tạo tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi giaitan " + ChatColor.GRAY + "| Giải tán tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi sut <tên người chơi> " + ChatColor.GRAY + "| Sút ra khỏi tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi moi <tên người chơi> " + ChatColor.GRAY + "| Mời vào tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi thoat " + ChatColor.GRAY + "| Rời khỏi tổ đội");
                sender.sendMessage(ChatColor.YELLOW + "/todoi doiten <tên mới> " + ChatColor.GRAY + "| Đổi tên tổ đội");
            }

        }

        return true;
    }
}

class PartyGuiHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}
