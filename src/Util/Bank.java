package Util;


import Luxiel.Main;
import org.apache.commons.lang.math.NumberUtils;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Bank implements CommandExecutor, Listener {


    public static void openBankGUI(Player p) {
        Inventory inv = Bukkit.createInventory(new BankGuiHolder(), 54, ChatColor.GOLD + "Bỏ tiền muốn cất vào đây");
        p.openInventory(inv);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player sd = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("ruttien")) {
            if (args.length > 0) {
                if (NumberUtils.isNumber(args[0])) {
                    Integer amount = Integer.valueOf(args[0]);
                    if (Main.getEconomy().getBalance(sd) >= amount) {
                        convertMoney(sd, amount);
                    } else {
                        sd.sendMessage(ChatColor.RED + "Không đủ tiền!");
                    }
                } else {
                    sd.sendMessage(ChatColor.RED + "Hãy điền số tiền muốn rút!");
                    sd.sendMessage(ChatColor.RED + "/ruttien [số tiền]");
                }
            } else {
                sd.sendMessage(ChatColor.RED + "Hãy điền số tiền muốn rút!");
                sd.sendMessage(ChatColor.RED + "/ruttien [số tiền]");
            }
        }
        if (cmd.getName().equalsIgnoreCase("noptien") && sd.hasPermission("rpg.noptien")) {
            openBankGUI(sd);
        }
        return true;
    }

    public void convertMoney(Player p, int amount) {
        if (amount >= 64) {
            int mcoin = amount / 64;
            int ocoin = amount % 64;
            if (ocoin > 0) {
                if (ocoin < Utils.invSpace(p.getInventory(), Material.GOLD_NUGGET)) {
                    p.getInventory().addItem(coin(ocoin));
                } else {
                    p.sendMessage(ChatColor.RED + "Túi đồ của bạn hơi nhỏ để đựng tiền!");
                    return;
                }
            }
            if (mcoin < Utils.invSpace(p.getInventory(), Material.GOLD_INGOT)) {
                p.getInventory().addItem(coin64(mcoin));
            } else {
                p.sendMessage(ChatColor.RED + "Túi đồ của bạn hơi nhỏ để đựng tiền!");
                return;
            }
            Utils.takeMoney(p, amount);
            p.sendMessage(ChatColor.GREEN + "Bạn đã rút thành công " + ChatColor.GOLD + amount + ChatColor.GREEN + "¥");
        } else {
            if (amount < Utils.invSpace(p.getInventory(), Material.GOLD_NUGGET)) {
                p.getInventory().addItem(coin(amount));
                Utils.takeMoney(p, amount);
                p.sendMessage(ChatColor.GREEN + "Bạn đã rút thành công " + ChatColor.GOLD + amount + ChatColor.GREEN + "$");
            } else {
                p.sendMessage(ChatColor.RED + "Túi đồ của bạn hơi nhỏ để đựng tiền!");
            }

        }


    }

    private ItemStack coin(int amount) {
        ItemStack is = new ItemStack(Material.GOLD_NUGGET, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "1 " + ChatColor.GOLD + "đồng vàng" + "");
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    private ItemStack coin64(int amount) {
        ItemStack is = new ItemStack(Material.GOLD_INGOT, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "64 " + ChatColor.GOLD + "đồng vàng" + "");
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() instanceof BankGuiHolder) {
            if (e.getCursor().getType() != Material.AIR) {
                ItemStack is = e.getCursor();
                if (is.getType() != Material.GOLD_NUGGET && is.getType() != Material.GOLD_INGOT) {
                    e.setCancelled(true);
                }
            } else {
                if (e.getCurrentItem().getType() == Material.GOLD_NUGGET || e.getCurrentItem().getType() == Material.GOLD_INGOT) {
                    e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
                    e.getClickedInventory().setItem(e.getSlot(), null);
                } else {
                    e.setCancelled(true);
                }


            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof BankGuiHolder) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getPlayer();
            int moneycount = 0;
            for (int i = 0; i <= 53; i++) {
                if (inv.getItem(i) != null) {
                    ItemStack is = inv.getItem(i);
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (ChatColor.stripColor(im.getDisplayName()).contains("64 đồng vàng")) {
                            moneycount += 64 * is.getAmount();
                        } else if (ChatColor.stripColor(im.getDisplayName()).contains("1 đồng vàng")) {
                            moneycount += 1 * is.getAmount();
                            ;
                        }else{
                            p.getInventory().addItem(is);
                        }
                    }else{
                        p.getInventory().addItem(is);
                    }
                }
            }
            Utils.addMoney(p, moneycount);
            p.sendMessage(ChatColor.GREEN + "Bạn đã bỏ thành công " + ChatColor.GOLD + moneycount + ChatColor.GREEN + "¥ vào ngân hàng!");
        }
    }


}

class BankGuiHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}