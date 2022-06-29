package Item;

import Util.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Enhancement {


    public static boolean haveEnhancementlevel(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("  [")) return true;
        if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("  [")) return true;
        if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("  [")) return true;
        return ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("  [");
    }

    public static void setEnhancementLevel(ItemStack is, int level) {
        if (haveEnhancementlevel(is)) {
            removeEnhancement(is);
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(im.getDisplayName() + ChatColor.GRAY + "  [" + getColor(level) + getDisplay(level) + ChatColor.GRAY + "]");
        is.setItemMeta(im);

        for (ItemStats stat : ItemStats.values()) {
            if (stat == ItemStats.LEVEL || stat == ItemStats.CLASS | stat == ItemStats.RACE) continue;
            if (stat.getBase(is) != 0) {
                stat.apply(is, getTotalStats(stat.getBase(is), level));
            }
        }
        RandomItem.reformatItem(is);
    }

    // 10 + 12 = 10 + (120%*10)
    // x + (  z/10 *x) = y
    // 50 + (20/10 *50) = 150
    // 23 + (2/10*23)
    public static int getTotalStats(int stats, int enhancelevel) {
//        double z = (enhancelevel/10)  *
        if (stats < 0) return stats;

        double z = stats * enhancelevel / 10;
        double a = stats + z;

        if ((int) a < 1) a = 1;
//        Bukkit.broadcastMessage(ChatColor.RED.toString()+a+"");
        return (int) a;
    }


    // 10 + 12 = +12
    // y = x + ( z/10* x ) => x = ?
    // x =y - (z/10*x) , y = 2 z = 12
    // x = 10y/(10+z)
    // 50 = 10*150/(10+20)
    public static int getBaseStats(int modifiedstats, int enhancelevel) {
        int bonus = 0;
        if (modifiedstats <= 0) return modifiedstats;
        if (enhancelevel % 2 != 0) bonus = 1;
        double a = (float) (10 * modifiedstats) / (10 + enhancelevel);
        int result = (int) a + bonus;
        if (result < 1) result = 1;
        return result;
    }


    public static void removeEnhancement(ItemStack is) {
        if (!haveEnhancementlevel(is)) return;
        ItemMeta im = is.getItemMeta();
        int level = getEnhancementLevel(is);
        String name = im.getDisplayName();
        name = ChatColor.stripColor(name);
        String[] split = name.split("  ");
        name = name.replace("  " + split[split.length - 1], "");
        im.setDisplayName(name);
        is.setItemMeta(im);
        for (ItemStats stat : ItemStats.values()) {
            if (stat == ItemStats.LEVEL || stat == ItemStats.CLASS | stat == ItemStats.RACE) continue;
            if (stat.getBase(is) != 0) {
                stat.apply(is, getBaseStats(stat.getBase(is), level));
            }
        }
        RandomItem.reformatItem(is);
    }

    public static String removeEnhancementFromName(String name) {
        name = ChatColor.stripColor(name);
        String[] split = name.split("  ");
        name = name.replace("  " + split[split.length - 1], "");
        return name;
    }

    public static int getEnhancementLevel(ItemStack is) {
        if (!haveEnhancementlevel(is)) return 0;
        String displayname = is.getItemMeta().getDisplayName();
        String[] split = ChatColor.stripColor(displayname).split("  ");
        String z = split[split.length - 1].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\+", "");
        if (NumberUtils.isParsable(z)) {
            return Integer.valueOf(z);
        } else {
            if (z.toLowerCase().contains("pri")) {
                return 16;
            } else if (z.toLowerCase().contains("duo")) {
                return 17;
            } else if (z.toLowerCase().contains("tri")) {
                return 18;
            } else if (z.toLowerCase().contains("tet")) {
                return 19;
            } else if (z.toLowerCase().contains("pen")) {
                return 20;
            } else {
                return 0;
            }

        }
    }

    public static ChatColor getColor(int level) {
        if (level <= 5) return ChatColor.WHITE;
        if (level <= 10) return ChatColor.AQUA;
        if (level <= 15) return ChatColor.RED;
        if (level <= 20) return ChatColor.GOLD;
        return ChatColor.WHITE;
    }

    public static String getDisplay(int level) {
        if (level == 16) {
            return "PRI";
        } else if (level == 17) {
            return "DUO";
        } else if (level == 18) {
            return "TRI";
        } else if (level == 19) {
            return "TET";
        } else if (level == 20) {
            return "PEN";
        }
        return "+" + level;
    }

    public static double getSuccessChance(int enhancementLevel) {
        switch (enhancementLevel) {
            case (1):
            case (2):
                return 90;
            case (3):
                return 80;
            case (4):
                return 70;
            case (5):
            case (6):
                return 50;
            case (7):
                return 40;
            case (8):
                return 30;
            case (9):
                return 20;
            case (10):
                return 10;
            case (11):
                return 7;
            case (12):
                return 3;
            case (13):
                return 2;
            case (14):
                return 1.5;
            case (15):
                return 1;
            case (16):
                return 0.5;
            case (17):
                return 0.3;
            case (18):
                return 0.2;
            case (19):
            case (20):
                return 0.1;
        }
        return 0;
    }

    public static boolean EnhanceWithChance(int currentlevel) {
        return Utils.percentRoll(getSuccessChance(currentlevel + 1));
    }

    public static int getStoneRequireLevel(int enhancementlevel) {
        if (enhancementlevel <= 8) return 1;
        if (enhancementlevel <= 15) return 2;
        return 3;
    }

    public static String getSuccessChanceDisPlay(int enhancementLevel) {
        String chance = Math.round(getSuccessChance(enhancementLevel)) + "％";
        if (getSuccessChance(enhancementLevel) < 1) {
            chance = "<1％";
        }
        return chance;
    }

    public static int EnhanceStoneLevel(ItemStack is) {
        if (!is.hasItemMeta()) return 0;
        if (!is.getItemMeta().hasDisplayName()) return 0;
        if (ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("Đá cường hóa")) {
            String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
            String[] split = name.split(" ");
            return Utils.romanConvert(split[split.length - 1]);

        }
        return 0;
    }

}
