package Item;

import Util.FNum;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;


public class StatsType {

    public static double integer(ItemStack i, String ww) {
        ww = ChatColor.stripColor(ww.replaceAll(" ", "").toLowerCase());
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String lore : i.getItemMeta().getLore()) {
                if (lore.contains("(")) {
                    lore = lore.split("\\(")[0];
                }
                lore = ChatColor.stripColor(lore.replaceAll(" ", "").toLowerCase());
                if (lore.endsWith(ww)) {
                    String w = lore.replaceAll(ww, "");
                    if (!w.endsWith("%")) {
                        if (w.startsWith("-")) {
                            return -FNum.rd(w.substring(1));
                        }
                        if (!w.startsWith("-")) {
                            return FNum.rd(w);
                        }
                    }
                }
            }
        }

        return 0;
    }

    public static double percent(ItemStack i, String ww) {
        double plusplus = 0;
        ww = ChatColor.stripColor(ww.replaceAll(" ", "").toLowerCase());
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String lore : i.getItemMeta().getLore()) {
                if (lore.contains("(")) {
                    lore = lore.split("\\(")[0];
                }
                lore = ChatColor.stripColor(lore).replaceAll("\\+", "").replaceAll("  -", "").replaceAll(" ", "").toLowerCase();
                if (lore.startsWith(ww)) {
                    String w = lore.replaceAll(ww, "").replaceAll(":", "");
                    if (w.endsWith("%")) {
                        if (w.startsWith("-")) {
                            return -FNum.rd(w.substring(1).replaceAll("%", ""));
                        }
                        if (!w.startsWith("-")) {
                            return FNum.rd(w.replaceAll("%", ""));
                        }
                    }
                }
            }
        }

        return plusplus;
    }


}
