package skill;

import Luxiel.Main;
import Util.Utils;
import attack.ChargedAttack;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class Dash {
    final public static int manacost = 0;
    public static int cost = 1;
    public static int levelreq = 1;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "";
    public static double cooldown = 5;
    public static double cooldownr = 0;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        Location loc = p.getLocation();
        loc.setPitch(0);
        Vector dash = loc.getDirection().normalize().multiply(-2);
        p.setVelocity(dash);
    }




}
