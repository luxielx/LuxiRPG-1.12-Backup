package stat;

import Item.ItemStats;
import ThreadManager.ManaRegen;
import Util.Utils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class HoiMau implements Listener {

    @EventHandler
    public void consume(PlayerItemConsumeEvent e) {
        try {
            ItemStack i = e.getItem();
            if (ItemStats.HPR.getStats(i) > 0) {
                e.setCancelled(true);
            }
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void changeslot(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        try {
            ItemStack i = p.getInventory().getItemInMainHand();
            if (!(i.getType() == Material.POTION)) return;
            int hoiphucmau = ItemStats.HPR.getStats(i);
            if (hoiphucmau > 0 && p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - p.getHealth() >= 1) {
                e.setCancelled(true);

                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }

                p.updateInventory();
                Utils.Heal(p,hoiphucmau,true);
            }
            int hoiphucmana = ItemStats.MANAR.getStats(i);
            if (hoiphucmana > 0 && StatPlayer.MaxMana(p) - ManaRegen.getMana(p) >= 1) {
                e.setCancelled(true);
                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                };
                p.updateInventory();
                ManaRegen.setMana(p,ManaRegen.getMana(p)+hoiphucmana);

            }
        } catch (Exception ex) {
        }

    }
}
