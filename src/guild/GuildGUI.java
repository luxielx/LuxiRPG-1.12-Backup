package guild;


import Luxiel.Profile;
import Util.FItem;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
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

public class GuildGUI implements Listener, CommandExecutor {

    public static void openGuildGUI(Player p) {
        Guild pt = GuildUtils.getGuild(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(new GuildGuiHolder(), 54, pt.getColor() + pt.getName());
        ItemStack ldskull = new FItem(Profile.playerInfo(Bukkit.getOfflinePlayer(pt.getGuildMaster()))).setName(ChatColor.RED + "Chủ hội: " + ChatColor.GRAY + Bukkit.getOfflinePlayer(pt.getGuildMaster()).getName()).toItemStack();
        inv.setItem(4, ldskull);
        List<UUID> mlist = new ArrayList<>(pt.getMembers());

        mlist.remove(pt.getGuildMaster());
        int start = 9;
        for (UUID uuid : mlist) {
            if (start == 54) break;
            inv.setItem(start, Profile.playerInfo(Bukkit.getOfflinePlayer(uuid)));
            start++;
        }
        for (int i = 9; i < 45; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, invitemore());
        }

        inv.setItem(52, pt.fam.symbol());
        inv.setItem(53, GuildStats(pt));
        for (int i = 0; i < 54; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, Utils.blankItem(Material.STAINED_GLASS_PANE, 15));
        }
        p.openInventory(inv);
    }

    private static ItemStack invitemore() {
        return new FItem(Material.STAINED_GLASS_PANE).setColorShort(1).setName(ChatColor.GREEN + "Hãy mời thêm thành viên!").toItemStack();

    }


    private static ItemStack GuildStats(Guild p) {
        ItemStack is = new FItem(Material.KNOWLEDGE_BOOK).setName(ChatColor.GOLD + "Thông tin Guild")
                .setLore(Arrays.asList(ChatColor.GRAY + "Tên Guild: " + p.getColor() + p.getName(),
                        ChatColor.GRAY + "Tổng cấp độ: " + ChatColor.AQUA + p.calcTotalLevel(),
                        ChatColor.GRAY + "Cấp độ trung bình: " + ChatColor.AQUA + p.calcAvgLevel(),
                        ChatColor.GRAY + "Điểm Guild: " + p.getColor() + p.powerpoint(),
                        ChatColor.GRAY + "Familia: " + ChatColor.AQUA + p.getFamilia().getName(),
                        ChatColor.GRAY + "Số lượng: " + ChatColor.WHITE + p.members.size() + "/35"


                ))
                .glow(true)
                .toItemStack();


        return is;
    }

    public static void openCreateGUI(UUID uniqueId, String name) {
        Player p = Bukkit.getPlayer(uniqueId);
        Inventory inv = Bukkit.createInventory(new FamGuiHolder(), 9, ChatColor.DARK_GRAY + "Tạo Guild " + ChatColor.WHITE + name);
        inv.setItem(0, Familia.famitem());
        inv.setItem(4, createGuild(false, null));
        p.openInventory(inv);

    }

    private static ItemStack createGuild(boolean allOkay, Familia familia) {
        if (allOkay) {

            return new FItem(Material.ENCHANTED_BOOK).hideAttributes(true).setName(ChatColor.WHITE + "Familia " + familia.color + familia.getName()).addLore(ChatColor.GRAY + "Nếu đồng ý với tùy chỉnh hãy bấm vào đây để tạo Guild!").glow(true).toItemStack();
        }
        return new FItem(Material.ENCHANTED_BOOK).hideAttributes(true).setName(ChatColor.RED + "Hãy chọn Familia!!").toItemStack();
    }
    @Nullable
    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getHolder() instanceof GuildGuiHolder) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
                if (e.getCurrentItem().getType() == Material.SKULL && e.getSlot() != 4) {
                    Profile.openOther(p, Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
                }
            if (e.getSlot() == 4) {
                Profile.openOther(p, Bukkit.getOfflinePlayer(Guild.getGuild(p.getUniqueId()).getGuildMaster()));
            }
        }
        if (e.getInventory().getHolder() instanceof FamGuiHolder) {
            if (e.getSlot() == 0)
                Familia.openFamiliarGUI(e.getWhoClicked().getUniqueId(), Utils.unhideS(Utils.colorCode(e.getView().getTitle().replace(ChatColor.DARK_GRAY + "Tạo Guild ", ""))));
            if (e.getSlot() == 4)
                if (e.getClickedInventory().getItem(4).containsEnchantment(Enchantment.DURABILITY)) {
                    GuildUtils.createGuild(p.getUniqueId(), Utils.colorCode(e.getView().getTitle().replace(ChatColor.DARK_GRAY + "Tạo Guild " + ChatColor.WHITE, "")),
                            Familia.getByName(ChatColor.stripColor(e.getClickedInventory().getItem(4).getItemMeta().getDisplayName().replace("Familia ", ""))));
                    openGuildGUI(p);
                }
            e.setCancelled(true);
        }
        if (e.getInventory().getHolder() instanceof FamHolder) {
            openCreateGUI(p.getUniqueId(), Utils.unhideS(Utils.colorCode(e.getView().getTitle().replace(ChatColor.RED + "Chọn Familia " + ChatColor.WHITE, ""))));
            p.getOpenInventory().getTopInventory().setItem(4, createGuild(true, Familia.getByName(ChatColor.stripColor(e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getDisplayName()))));
            e.setCancelled(true);
        }

    }

    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player sd = (Player) sender;
        if (a.equalsIgnoreCase("g")) {
            if (GuildUtils.haveGuild(sd.getUniqueId())) {
                openGuildGUI(sd);
            } else {
                sd.sendMessage(ChatColor.RED + "Bạn không có Guild! ");
                sender.sendMessage(ChatColor.YELLOW + "/g " + ChatColor.GRAY + "| Mở giao diện Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild tao <tên> " + ChatColor.GRAY + "| Tạo Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild giaitan " + ChatColor.GRAY + "| Giải tán Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild sut <tên người chơi> " + ChatColor.GRAY + "| Sút ra khỏi Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild moi <tên người chơi> " + ChatColor.GRAY + "| Mời vào Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild thoat " + ChatColor.GRAY + "| Rời khỏi Guild");
                sender.sendMessage(ChatColor.YELLOW + "/guild doiten <tên mới> " + ChatColor.GRAY + "| Đổi tên Guild");
            }

        }

        return true;
    }
}

class GuildGuiHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}

class FamGuiHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}

