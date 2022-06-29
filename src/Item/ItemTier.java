package Item;

import Util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ADMIN on 25/08/2018.
 */
public enum ItemTier {
    COMMON(ChatColor.GRAY, 1, "Rởm"), UNCOMMON(ChatColor.GREEN, 2, "Thường"), RARE(ChatColor.AQUA, 3, "Hiếm"), ULTIMATE(ChatColor.DARK_RED, 4, "Tối Thượng"), LEGENDARY(ChatColor.GOLD, 5, "Huyền Thoại");


    ChatColor color;
    int bonus;
    String name;

    ItemTier(ChatColor color, int bonus, String name) {
        this.color = color;
        this.bonus = bonus;
        this.name = name;
    }

    public static ItemTier getRandomTierByPercent(int modifier) {
        if (Utils.percentRoll(50/modifier)) {
            return COMMON;
        } else if (Utils.percentRoll(50/modifier)) {
            return UNCOMMON;
        } else if (Utils.percentRoll(50)) {
            return RARE;
        } else if (Utils.percentRoll(10)) {
            return ULTIMATE;
        } else if (Utils.percentRoll(5)) {
            return LEGENDARY;
        }
        return COMMON;
    }

    public static ItemTier getRandomTier() {
        return Arrays.asList(ItemTier.values()).get(ThreadLocalRandom.current().nextInt(ItemTier.values().length));
    }

    public static ItemTier getTier(ItemStack i) {
        if (!ItemStats.hasStats(i))
            return ItemTier.COMMON;
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore())
            for (String lore : i.getItemMeta().getLore()) {
                lore = Utils.unhideS(lore);
                lore = ChatColor.stripColor(lore);
                lore = lore.replaceAll(" ", "");
                if (lore.startsWith("Tier:")) {
                    String classs = lore.split(":")[1].toUpperCase();
                    for (ItemTier it : ItemTier.values()) {
                        if (it.getName().replace(" ", "").equalsIgnoreCase(classs))
                            return it;
                    }
                }
            }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getBonus() {
        return bonus;
    }

    public ChatColor getColor() {
        return color;
    }


}
