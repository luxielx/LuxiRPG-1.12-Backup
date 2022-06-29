package Item;

import ChucNghiep.Classes;
import ChucNghiep.Races;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

import static Item.RandomItem.reformatItem;

/**
 * Created by ADMIN on 26/08/2018.
 */
public enum ItemStats {
    CLASS(false, "Nghề", ChatColor.LIGHT_PURPLE, false, false, 0, 0, ListTL(ToolList.WEAPON)),
    LEVEL(true, "Level", ChatColor.WHITE, false, false, 50, 0, ListTL(ToolList.ALL)),
    RACE(false, "Tộc", ChatColor.GRAY, false, false, 0, 0, ListTL(ToolList.WEAPON)),

    STR(false, "Sức Mạnh", ChatColor.RED, true, false, 250, 5, ListTL(ToolList.ALL)),
    AGI(false, "Nhanh Nhẹn", ChatColor.YELLOW, true, false, 250, 5, ListTL(ToolList.ALL)),
    INTEL(false, "Ma Pháp", ChatColor.AQUA, true, false, 250, 5, ListTL(ToolList.ALL)),
    VITAL(false, "Sinh Lực", ChatColor.GREEN, true, false, 500, 5, ListTL(ToolList.ARMOR)),

    DAMAGE(false, "Sát Thương", ChatColor.DARK_RED, false, false, 50000, 3, ListTL(ToolList.WEAPON)),
    ATTSPK(false, "Tốc Độ Đánh", ChatColor.DARK_GREEN, false, false, 100, 10, ListTL(ToolList.WEAPON)),

    MOVESPK(false, "Tốc độ Di Chuyển", ChatColor.GRAY, false, true, 100, 3, ListTL(ToolList.BOOTS, ToolList.BOW, ToolList.DAGGER)),
    CRIT(false, "Chí Mạng", ChatColor.GOLD, false, true, 100, 3, ListTL(ToolList.BOOTS, ToolList.WEAPON)),

    HP(false, "Máu", ChatColor.DARK_RED, false, false, 5000, 1, ListTL(ToolList.ARMOR)),
    HPP(false, "Giáp", ChatColor.DARK_PURPLE, false, false, 200, 5, ListTL(ToolList.ARMOR)),


    MANAR(false, "Hồi Phục Mana", ChatColor.AQUA, true, false, 50000, 5, ListTL(ToolList.POTION)),
    HPR(false, "Hồi Phục Máu", ChatColor.GREEN, true, false, 50000, 5, ListTL(ToolList.POTION)),


    ;


    boolean require;
    String name;
    ChatColor color;
    boolean isBasicStats;
    boolean isPercentage;
    int max;
    int cost;
    ArrayList<ToolList> tl;

    ItemStats(boolean require, String name, ChatColor color, boolean isBasicStats, boolean isPercentage, int max, int cost, ArrayList<ToolList> tl) {
        this.require = require;
        this.name = name;
        this.color = color;
        this.isBasicStats = isBasicStats;
        this.isPercentage = isPercentage;
        this.max = max;
        this.cost = cost;
        this.tl = tl;
    }

    public static ArrayList<ToolList> ListTL(ToolList... tl) {
        ArrayList<ToolList> li = new ArrayList<>();
        for (ToolList to : tl) {
            li.add(to);
        }

        return li;
    }

    public static boolean hasStats(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        return is.getItemMeta().hasLore();
    }

    public static ArrayList<Classes> getClasses(ItemStack i) {
        if (!hasStats(i)) return new ArrayList<>();
        ArrayList<Classes> value = new ArrayList<>();
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String lore : i.getItemMeta().getLore()) {
                lore = ChatColor.stripColor(lore);
                lore = lore.replaceAll(" ", "").replaceAll("\\+", "").replaceAll(" \\-", "");
                if (lore.startsWith("Nghề:")) {
                    value = new ArrayList<>();
                    String classs = lore.split(":")[1].toUpperCase();
                    if (classs.contains(",")) {
                        String[] comma = classs.split(",");
                        for (String s : comma) {
                            value.add(Classes.getByName(s));
                        }
                    } else {
                        value.add(Classes.getByName(classs));
                    }

                }
            }
        }

        return value;
    }

    public static ArrayList<Races> getRace(ItemStack i) {
        if (!hasStats(i)) return new ArrayList<>();

        ArrayList<Races> value = new ArrayList<>();
        if (i != null && i.hasItemMeta() && i.getItemMeta().hasLore()) {
            for (String lore : i.getItemMeta().getLore()) {
                lore = ChatColor.stripColor(lore);
                lore = lore.replaceAll(" ", "").replaceAll("\\+", "").replaceAll(" \\-", "");
                if (lore.startsWith("Tộc:")) {
                    value = new ArrayList<>();
                    String classs = lore.split(":")[1].toUpperCase();
                    if (classs.contains(",")) {
                        String[] comma = classs.split(",");
                        for (String s : comma) {
                            value.add(Races.getByName(s));
                        }
                    } else {
                        value.add(Races.getByName(classs));
                    }

                }
            }
        }

        return value;
    }

    public void apply(ItemStack is, String value) {
        String lore2 = name + ":" + value;
        if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
            ArrayList<String> list = new ArrayList<>(is.getItemMeta().getLore());
            list.add(lore2);
            ItemMeta im = is.getItemMeta();
            im.setLore(list);
            is.setItemMeta(im);
        } else {
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(lore2));
            is.setItemMeta(im);
        }
        reformatItem(is);
    }

    public void apply(ItemStack is, int amount) {
        String lore2 = "";
        String percent;
        remove(is);
        if (amount == 0) return;
        if (amount > 0) {
            if (isPercentage) {
                percent = "%";
            } else {
                percent = "";
            }
            lore2 = ("  §a§l+ " + ChatColor.GRAY + name + ": " + color + amount + percent);
        } else if (amount < 0) {
            if (isPercentage) {
                percent = "%";
            } else {
                percent = "";
            }
            lore2 = ("  §c§l- " + ChatColor.GRAY + name + ": " + color + amount + percent);
        }
        if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
            ArrayList<String> list = new ArrayList<>(is.getItemMeta().getLore());
            list.add(lore2);
            ItemMeta im = is.getItemMeta();
            im.setLore(list);
            is.setItemMeta(im);
        } else {
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(lore2));
            is.setItemMeta(im);
        }
        reformatItem(is);
    }

    public void remove(ItemStack is) {
        if (!is.hasItemMeta()) return;
        ItemMeta im = is.getItemMeta();
        if (!im.hasLore()) return;
        ArrayList<String> list = new ArrayList<>(im.getLore());
        int index = 0;
        for (String s : list) {
            if (ChatColor.stripColor(s.toLowerCase()).contains(name.toLowerCase())) break;
            index++;
        }
        if (index == list.size()) return;
        list.remove(index);
        im.setLore(list);
        is.setItemMeta(im);
    }

    // + Sức Mạnh: 20 (+20)
    public int getAdditionalStats(ItemStack is) {
        try {
            if (!hasStats(is)) return 0;
            int value = 0;
            boolean neg = false;
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                for (String lore : is.getItemMeta().getLore()) {
                    String lore2 = ChatColor.stripColor(lore);
                    if (lore2.contains("(")) {
                        lore2 = lore2.split("\\(")[1];
                    } else {
                        continue;
                    }
                    if(ChatColor.stripColor(lore2).contains("  \\(-"))neg = true;
                    lore2 = lore2.replaceAll("\\+", "").replaceAll("  \\-", "")
                            .replaceAll("\\(", "").replaceAll("\\)", "")
                            .replaceAll("%", "").replaceAll(" ", "")
                            .toLowerCase();
                    if (lore.toLowerCase().contains(this.name.toLowerCase())) {
                        if(neg){
                            value = -Integer.parseInt(lore2);
                        }else{
                            value = Integer.parseInt(lore2);
                        }

                        return value;
                    }
                }

            }
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    public void applyAdditionalStats(ItemStack is, int amount) {
        if(amount==0) return;
        String lore2 = "";
        String percent;
        int basevalue = this.getBase(is);
        remove(is);
        if (isPercentage) {
            percent = "%";
        } else {
            percent = "";
        }
        if(basevalue < 0) amount=-amount;
        if (basevalue > 0) {
            lore2 = ("  §a§l+ " + ChatColor.GRAY + name + ": " + color + basevalue + percent + ChatColor.GRAY + " (" + amount + percent + ")");
        } else {
            lore2 = ("  §c§l- " + ChatColor.GRAY + name + ": " + color + basevalue + percent + ChatColor.GRAY + " (" + amount + percent + ")");
        }
        if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
            ArrayList<String> list = new ArrayList<>(is.getItemMeta().getLore());
            list.add(lore2);
            ItemMeta im = is.getItemMeta();
            im.setLore(list);
            is.setItemMeta(im);
        } else {
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(lore2));
            is.setItemMeta(im);
        }
    }

    public void removeAdditionalStats(ItemStack is) {
        applyAdditionalStats(is, 0);
    }

    public int getBase(ItemStack is) {
        try {
            if (!hasStats(is)) return 0;
            int value = 0;
            if (isPercentage) {
                value += StatsType.percent(is, this.name);
            } else if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                for (String lore : is.getItemMeta().getLore()) {
                    lore = ChatColor.stripColor(lore);
                    if (lore.contains("(")) {
                        lore = lore.split("\\(")[0];
                    }
                    lore = lore.replaceAll("\\+", "").replaceAll("  \\-", "").replaceAll(" ", "").toLowerCase();
                    if (lore.startsWith(this.name.toLowerCase().replaceAll(" ", "") + ":")) {
                        value = Integer.parseInt(lore.split(":")[1]);
                        if(value == 0) value = 1;
                        return value;
                    }else{
                        value = 0;
                        continue;
                    }
                }

            }
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    // + Sức Mạnh: 20 (+20)
    public int getStats(ItemStack is) {
        if(is.getType() == Material.SKULL_ITEM) return 0;
        return getBase(is) + getAdditionalStats(is);
    }

}
