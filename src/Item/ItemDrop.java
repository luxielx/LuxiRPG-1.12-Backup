package Item;

import Luxiel.Main;
import Util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;


public class ItemDrop implements Listener {


//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void ded(EntityDeathEvent e) {
//        LivingEntity en = e.getEntity();
//        if (en.getKiller() == null) return;
//        if (en instanceof Player) return;
//        Player p = en.getKiller();
//        int minlevel = 0;
//        int maxlevel = getLevel(en);
//        if (getLevel(en) > 10) minlevel = getLevel(en) - 10;
//        if (maxlevel == 0) maxlevel = 1;
//
//        if (Utils.percentRoll(5)) {
//            e.getDrops().add(RandomItem.getRandomItem(ToolList.WEAPON.getRandomTool(), ItemTier.getRandomTierByPercent(1), minlevel, maxlevel));
//        } else if (Utils.percentRoll(5)) {
//            e.getDrops().add(RandomItem.getRandomItem(ToolList.ARMOR.getRandomTool(), ItemTier.getRandomTierByPercent(1), minlevel, maxlevel));
//        }
//        for (ItemStack s : e.getDrops()) {
//            Item i = en.getWorld().dropItemNaturally(en.getLocation(), s);
//            setDropItem(i, p);
//        }
//        e.getDrops().clear();
//    }

    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        if (ToolList.ALL.getItemList().contains(e.getItemDrop().getType())) e.setCancelled(true);
        setDropItem(e.getItemDrop(), e.getPlayer());
    }

    public void setDropItem(Item i, Player p) {
        Utils.setGlowingWhite(i, p, true);
        ItemStack s = i.getItemStack();
        String name = s.getType().name();
        String amount = "";
        if (s.hasItemMeta()) {
            if (s.getItemMeta().hasDisplayName()) name = s.getItemMeta().getDisplayName();
        }
        if (s.getAmount() > 1) {
            amount = ChatColor.GREEN + " x" + s.getAmount();
        }
        i.setCustomNameVisible(true);
        i.setCustomName(ChatColor.RED + "[" + ChatColor.GRAY + p.getName() + ChatColor.RED + "] " + ChatColor.RESET + name + amount);
        i.setMetadata("pickup.owner", new FixedMetadataValue(Main.m, p.getUniqueId().toString()));
    }

    @EventHandler
    public void pickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getItem().hasMetadata("pickup.owner")) {
                if (!e.getItem().getMetadata("pickup.owner").get(0).asString().equalsIgnoreCase(p.getUniqueId().toString())) {
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void burn(EntityCombustEvent e) {
        if (e.getEntity().getType() == EntityType.DROPPED_ITEM) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            event.setCancelled(true);
        }
    }


    private int getLevel(LivingEntity e) {
        String name = e.getName();
        if (!name.contains("Lv.")) return 0;
        String[] spl = name.split(" ");
        String re = spl[spl.length - 1].replace("Lv.", "");
        return Integer.parseInt(re);
    }
}
