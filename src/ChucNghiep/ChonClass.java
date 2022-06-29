package ChucNghiep;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Util.FItem;
import net.milkbowl.vault.chat.Chat;
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


public class ChonClass implements Listener, CommandExecutor {

    public static void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "§0Chọn hơi thở");
        inv.addItem(new FItem(Material.IRON_SPADE).setName("§fChọn §aLôi").addLore(ChatColor.GRAY+"Hơi thở sấm sét. Sử dụng sấm sét để nhanh chóng di chuyển và tấn công.")
                .addLore(ChatColor.GRAY+"Các kiếm sĩ dùng hơi thở này ra đòn rất nhanh và sát thương cao.").toItemStack());
        inv.addItem(new FItem(Material.WEB).setName("§fChọn §aPhong").addLore(ChatColor.GRAY+"Hơi thở của gió. Sử dụng gió để tấn công và hỗ trợ đồng đội.")
                .addLore(ChatColor.GRAY+"Các đòn từ hơi thở này có khả năng khống chế diện rộng.").toItemStack());
        inv.addItem(new FItem(Material.WATER_BUCKET).setName("§fChọn §bThủy").addLore(ChatColor.GRAY+"Hơi thở của nước. Sử dụng nước để hồi máu và tấn công.")
                .addLore(ChatColor.GRAY+"Các đòn đánh từ nước có khả năng khống chế, sát thương cùng với hồi máu diện rộng.").toItemStack());
        inv.addItem(new FItem(Material.LAVA_BUCKET).setName("§fChọn §cHỏa").addLore(ChatColor.GRAY+"Hơi thở của lửa. Sử dụng lửa để hồi máu và tấn công.")
                .addLore(ChatColor.GRAY+"Các đòn đánh từ lửa có sát thương diện rộng cao, cùng với khả năng tự hồi phục.").toItemStack());
        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getView().getTitle().equals("§0Chọn hơi thở")) {
            e.setCancelled(true);
            try {
                Player p = (Player) e.getWhoClicked();
                RPGPlayer rpgPlayer = RPGPlayerListener.get(p);
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().getType() == Material.IRON_SPADE) {
                        rpgPlayer.setClass(Classes.THUNDER);
                        p.getInventory().setItem(0,null);
                    }
                    if (e.getCurrentItem().getType() == Material.WEB) {
                        rpgPlayer.setClass(Classes.WIND);
                        p.getInventory().setItem(0,null);
                    }
                    if (e.getCurrentItem().getType() == Material.WATER_BUCKET) {
                        rpgPlayer.setClass(Classes.WATER);
                        p.getInventory().setItem(0,null);
                    }
                    if (e.getCurrentItem().getType() == Material.LAVA_BUCKET) {
                        rpgPlayer.setClass(Classes.FIRE);
                        p.getInventory().setItem(0,null);
                    }
                    e.getWhoClicked().closeInventory();
                }

            } catch (Exception ex) {
            }
        }
    }


    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
        if (a.equalsIgnoreCase("chonclass")) {
            if (sender.hasPermission("rpg.doiclass")|| !(sender instanceof Player)) {
                if (args.length == 1) {
                    Player p = Bukkit.getPlayer(args[0]);

                    if (p != null) {
                        open(p);
                    }
                }

                if (args.length == 0) {
                    open((Player) sender);
                }
            }
        }


        return true;
    }


}
