package ChucNghiep;

import org.bukkit.ChatColor;

public enum Races {

    HUMAN(ChatColor.WHITE + "Người"), DEMON(ChatColor.LIGHT_PURPLE + "Quỷ");

    String name;

    Races(String name) {
        this.name = name;
    }

    public static Races getByName(String s) {
        for (Races sz : Races.values()) {
            if (ChatColor.stripColor(sz.getName()).equalsIgnoreCase(s)) {
                return sz;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

}
