package stat;

import Item.ItemStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class LevelTooLow implements Listener {

    int[] slots = {
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35};

    @EventHandler
    public void close(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        ItemStack i = p.getInventory().getItemInMainHand();
        if (i != null && i.getType() != Material.AIR) {
            int lvreq = ItemStats.LEVEL.getStats(i);
            if (p.getLevel() < lvreq) {
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                boolean full = true;
                for (int slot : slots) {
                    if (p.getInventory().getItem(slot) == null || p.getInventory().getItem(slot).getType() == Material.AIR) {
                        full = false;
                        p.getInventory().setItem(slot, i);
                        break;
                    }
                }

                if (full) {
                    p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), i);
                }


                p.sendMessage("§4Bạn không đủ level để sử dụng item này.");
            }

            //////////////////////////////////////////////////////////////////

            i = p.getInventory().getHelmet();

            //noinspection deprecation
            if (i != null && i.getType() != Material.AIR) {
                if (p.getLevel() < ItemStats.LEVEL.getStats(i)) {
                    p.getInventory().setHelmet(new ItemStack(Material.AIR));
                    boolean full = true;
                    for (int slot : slots) {
                        if (p.getInventory().getItem(slot) == null || p.getInventory().getItem(slot).getType() == Material.AIR) {
                            full = false;
                            p.getInventory().setItem(slot, i);
                            break;
                        }
                    }
                    if (full) {
                        p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), i);
                    }
                    p.sendMessage("§4Bạn không đủ level để trang bị mũ này.");
                }
            }

            //////////////////////////////////////////////////////////////////

            i = p.getInventory().getChestplate();
            //noinspection deprecation

            if (i != null && i.getType() != Material.AIR) {
                if (p.getLevel() < ItemStats.LEVEL.getStats(i)) {
                    p.getInventory().setChestplate(new ItemStack(Material.AIR));
                    boolean full = true;
                    for (int slot : slots) {
                        if (p.getInventory().getItem(slot) == null || p.getInventory().getItem(slot).getType() == Material.AIR) {
                            full = false;
                            p.getInventory().setItem(slot, i);
                            break;
                        }
                    }
                    if (full) {
                        p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), i);
                    }
                    p.sendMessage("§4Bạn không đủ level để trang bị áo giáp này.");
                }
            }

            //////////////////////////////////////////////////////////////////

            i = p.getInventory().getLeggings();
            //noinspection deprecation

            if (i != null && i.getType() != Material.AIR) {
                if (p.getLevel() < ItemStats.LEVEL.getStats(i)) {
                    p.getInventory().setLeggings(new ItemStack(Material.AIR));
                    boolean full = true;
                    for (int slot : slots) {
                        if (p.getInventory().getItem(slot) == null || p.getInventory().getItem(slot).getType() == Material.AIR) {
                            full = false;
                            p.getInventory().setItem(slot, i);
                            break;
                        }
                    }
                    if (full) {
                        p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), i);
                    }
                    p.sendMessage("§4Bạn không đủ level để trang bị quần này.");
                }
            }

            //////////////////////////////////////////////////////////////////

            i = p.getInventory().getBoots();
            //noinspection deprecation
            if (i != null && i.getType() != Material.AIR) {
                if (p.getLevel() < ItemStats.LEVEL.getStats(i)) {
                    p.getInventory().setBoots(new ItemStack(Material.AIR));
                    boolean full = true;
                    for (int slot : slots) {
                        if (p.getInventory().getItem(slot) == null || p.getInventory().getItem(slot).getType() == Material.AIR) {
                            full = false;
                            p.getInventory().setItem(slot, i);
                            break;
                        }
                    }
                    if (full) {
                        p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), i);
                    }
                    p.sendMessage("§4Bạn không đủ level để trang bị giày này.");
                }
            }
        }
    }

    @EventHandler
    public void changeslot(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        ItemStack i = e.getPlayer().getInventory().getItem(e.getNewSlot());
        //noinspection deprecation
        if (i != null && i.getType() != Material.AIR) {

            int lvreq = ItemStats.LEVEL.getStats(i);

            if (p.getLevel() < lvreq) {
                e.setCancelled(true);

                p.sendMessage("§4Bạn không đủ level để sử dụng item này.");
            }
        }
    }
}
