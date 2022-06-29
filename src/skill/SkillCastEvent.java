package skill;

import Item.ToolList;
import ThreadManager.ManaRegen;
import Util.Sit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ADMIN on 8/5/2018.
 */

public class SkillCastEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    int skill;
    private Player player;
    private boolean cancelled;

    public SkillCastEvent(Player player, int skillid) {
        this.player = player;
        this.skill = skillid;

    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static boolean castAble(Player p, int level, double cooldown, double cooldownreduction, int manacost, HashMap<UUID, Long> lastuse) {
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        }
        if (Sit.isSitting(p)) return false;
        if (ManaRegen.getMana(p) < manacost) {
            return false;
        }
        if (System.currentTimeMillis() - lastuse.get(p.getUniqueId()) <= (cooldown - cooldownreduction * p.getLevel()) * 1000) {
            p.playSound(p.getEyeLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
            return false;
        }
        if (p.getLevel() < level) {
            return false;
        }
        if(!ToolList.WEAPON.getItemList().contains(p.getInventory().getItemInMainHand().getType())) return false;
        ManaRegen.setMana(p, ManaRegen.getMana(p) - manacost);
        return true;
    }

    public int getSkillID() {
        return skill;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean Boolean) {
        this.cancelled = Boolean;
    }
// 5000 time
    // cd 10s
    // cdr 1
    // lv 5
    // 16000 - 5000 = 11000 >= (10- 1*5)* 1000

    public HandlerList getHandlers() {
        return handlers;
    }

}
