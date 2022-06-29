package Item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by ADMIN on 31/12/2018.
 */
public class Rarity {

    public static int RarityLevel(ItemStack is) {
        int rarity = 0;
        if (Modifier.hasModifier(is)) {
            Modifier mod = Modifier.getModifier(is);
            rarity += rarityvalue(mod.tier);
        }
        if (ItemTier.getTier(is) != null) {
            ItemTier tier = ItemTier.getTier(is);
            rarity += rarityvalue(tier);
        }
        return rarity;

    }


    public static int rarityvalue(ItemTier it) {
        if (it == ItemTier.COMMON) return 1;
        if (it == ItemTier.UNCOMMON) return 2;
        if (it == ItemTier.RARE) return 3;
        if (it == ItemTier.ULTIMATE) return 4;
        if (it == ItemTier.LEGENDARY) return 5;
        return 0;
    }

    public static ChatColor raritycolor(int level) {
        if (level <= 2) {
            return ChatColor.GRAY;
        } else if (level <= 4) {
            return ChatColor.GREEN;
        } else if (level <= 6) {
            return ChatColor.AQUA;
        } else if (level <= 8) {
            return ChatColor.RED;
        } else if (level <= 10) {
            return ChatColor.GOLD;
        }
        return ChatColor.WHITE;
    }

    public static ArrayList<String> BIGLETTERS(int rarity) {
        ArrayList<String> list = new ArrayList<>();
        if (rarity <= 2) {
            list.add("       ⬛⬜⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
            list.add("       ⬛⬜⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GRAY + "⬛"));
        } else if (rarity <= 4) {
            list.add("       ⬛⬛⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));
            list.add("       ⬛⬛⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GREEN + "⬛"));

        } else if (rarity <= 6) {
            list.add("       ⬛⬜⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
            list.add("       ⬛⬜⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.AQUA + "⬛"));
        } else if (rarity <= 8) {
            list.add("       ⬛⬛⬛⬜⬛⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬛⬜⬛⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬜⬜⬜⬜⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.RED + "⬛"));

        } else if (rarity <= 10) {
            list.add("       ⬛⬛⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬛⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬛⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬜⬛⬛⬛⬜⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
            list.add("       ⬛⬛⬜⬜⬜⬛⬛       ".replace("⬛", ChatColor.WHITE + "⬛").replace("⬜", ChatColor.GOLD + "⬛"));
        }

        return list;
    }


}
