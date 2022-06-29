package Item;

import Util.FItem;
import Util.Utils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ModifierGUI implements Listener {


    public static void openModifierGUI(Player p) {
        Inventory inv = Bukkit.createInventory(new ModifierHolder(), 9, ChatColor.RED + "Tẩy Trang Bị" + ChatColor.GRAY + " (Chưa có trang bị)");
        setGlass(inv);
        p.openInventory(inv);
    }

    public static void setGlass(Inventory inv) {
        inv.setItem(0, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(1, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Hãy cho Trang Bị vào đây" + " -->").toItemStack());
        inv.setItem(3, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(5, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Hãy cho Thanh Tâm vào đây" + " -->").toItemStack());
        inv.setItem(4, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(7, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(8, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
    }

    private static void updateInv(Inventory inv, Player p) {
        boolean ready = false;
        if (inv.getItem(2) != null) {
            if (inv.getItem(6) != null) {
                Utils.updateInventoryTitle(p, ChatColor.GREEN + "Tẩy Trang Bị" + ChatColor.GRAY + "");
                ready = true;
            } else {
                Utils.updateInventoryTitle(p, ChatColor.RED + "Tẩy Trang Bị" + ChatColor.GRAY + " (Chưa có Thanh Tâm)");
            }
        } else {
            Utils.updateInventoryTitle(p, ChatColor.RED + "Tẩy Trang Bị" + ChatColor.GRAY + " (Chưa có Trang Bị)");
        }
        if (ready) {
            inv.setItem(4, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(ChatColor.WHITE + "Thanh Tẩy!").toItemStack());
        } else {
            inv.setItem(4, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        }
    }


    public static boolean isRefineFlower(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        if(is.getType() != Material.EMERALD) return false;
        return ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("Ma thạch tinh luyện");
    }

    @EventHandler
    public void invclose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof ModifierHolder) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getPlayer();
            if (inv.getItem(2) != null && inv.getItem(2).getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(inv.getItem(2));
            }
            if (inv.getItem(6) != null && inv.getItem(6).getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(inv.getItem(6));
            }

        }

    }

    @EventHandler
    public void invclick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getInventory().getHolder() instanceof ModifierHolder) {
            Inventory inv = e.getInventory();
            ItemStack air = new ItemStack(Material.AIR);
            ItemStack current = e.getCurrentItem();
            if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                e.setCancelled(true);
                return;
            }
            Player p = (Player) e.getWhoClicked();
            int clickslot = e.getSlot();
            if (e.getClickedInventory().getHolder() instanceof ModifierHolder) {
                if (clickslot == 2) {
                    if (inv.getItem(2) != null) {
                        inv.setItem(2, air);
                        p.getInventory().addItem(current);
                    }
                }
                if (clickslot == 6) {
                    if (inv.getItem(6) != null) {
                        inv.setItem(6, air);
                        p.getInventory().addItem(current);
                    }
                }
                if (clickslot == 4) {
                    if (current.getDurability() == 0) {
                        startModifier(inv.getItem(2), p, inv);
                        e.setCancelled(true);
                        return;
                    }
                }
            } else {
                if (ToolList.ALL.getItemList().contains(current.getType())) {
                    if (inv.getItem(2) == null) {
                        e.setCurrentItem(air);
                        inv.setItem(2, current);
                    }
                }
                if (isRefineFlower(current)) {
                    if (inv.getItem(6) == null) {
                        ItemStack iz = current.clone();
                        if (iz.getAmount() <= 1) {
                            e.setCurrentItem(air);
                            inv.setItem(6, current);
                        } else {
                            current.setAmount(current.getAmount() - 1);
                            iz.setAmount(1);
                            inv.setItem(6, iz);
                        }

                    }
                }


            }
            e.setCancelled(true);
            updateInv(inv, p);

        }


    }

    private void startModifier(ItemStack item, Player p, Inventory inv) {
        if (p.getOpenInventory() == null) return;
        if(inv.getItem(2) == null) return;
        if(inv.getItem(6) == null) return;

        p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK , 1, 1);
        Modifier mod = Modifier.applyRandomModifier(item);
        ItemStack air = new ItemStack(Material.AIR);

        inv.setItem(2, item);
        inv.setItem(6,air);
        updateInv(inv,p);
        Utils.updateInventoryTitle(p,  mod.tier.getColor() + mod.getName());

    }
}


class ModifierHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}