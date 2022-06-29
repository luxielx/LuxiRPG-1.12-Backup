package Luxiel;

import Attr.Attributes_12;
import Item.ItemStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class XoaItemData implements Listener {


    @EventHandler
    public void pickUp(EntityPickupItemEvent e) {
        ItemStack i = e.getItem().getItemStack();
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (ItemStats.DAMAGE.getStats(i) > 0) {
            Attributes_12 at = new Attributes_12(i);

            at.remove(Attributes_12.Attribute.newBuilder().name("generic.attackDamage").type(Attributes_12.AttributeType.GENERIC_ATTACK_DAMAGE).build());

            e.setCancelled(true);
            e.getItem().remove();
            player.getInventory().addItem(at.getStack());
        }
    }
}
