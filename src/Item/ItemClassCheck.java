package Item;

import ChucNghiep.Classes;
import Item.ItemStats;
import Luxi.SPlayer.RPGPlayerListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemClassCheck implements Listener {

    @EventHandler
    public void changeslot(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        ItemStack i = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (i != null && i.getType() != Material.AIR) {

            ArrayList<Classes> claxx = ItemStats.getClasses(i);
            if (!claxx.contains(RPGPlayerListener.get(p).getPlayerClass()) && claxx.size() > 0 && !p.hasPermission("camdolinhtinh")) {
                e.setCancelled(true);
                p.sendMessage("§4Vũ khí không phù hợp với class của bạn.");
            }
        }
    }
}
