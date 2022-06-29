package ChucNghiep;


import org.bukkit.ChatColor;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ADMIN on 8/8/2018.
 */
public enum Classes {

    NONE("No Class", Races.HUMAN),

    THUNDER(ChatColor.YELLOW + "Lôi", Races.HUMAN),
    WIND(ChatColor.WHITE + "Phong", Races.HUMAN),
    FIRE(ChatColor.RED + "Hỏa", Races.HUMAN),
    WATER(ChatColor.AQUA + "Thủy", Races.HUMAN),


    ;


    String name;
    Races race;

    Classes(String name, Races race) {
        this.name = name;
        this.race = race;
    }

    public static Classes getByName(String s) {
        for (Classes sz : Classes.values()) {
            if (ChatColor.stripColor(sz.getName()).equalsIgnoreCase(s)) {
                return sz;
            }
        }
        return NONE;
    }
    public static Classes getRandom() {
        ArrayList<Classes> al = new ArrayList<>();
        al.add(THUNDER);
        al.add(FIRE);
        al.add(WIND);
        al.add(WATER);
        return al.get(ThreadLocalRandom.current().nextInt(0,al.size()));
    }


    public String getName() {
        return this.name;
    }

    public Races getRace() {
        return this.race;
    }

}
