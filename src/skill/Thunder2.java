package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Thunder2 {

    final public static int manacost = 50;
    public static int cost = 5;
    public static int levelreq = 5;
    public static String name = ChatColor.GREEN + ChatColor.ITALIC.toString() + "Nhị Thức: Đạo Hồn";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém 5 phát xung quanh bản thân",ChatColor.GRAY+"Đồng thời triệu hồi sấm xung quanh gây sát thương."});
    public static int cooldown = 10;
    public static double cooldownr = 0.05;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();

    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.GREEN_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        Location f1 = p.getLocation();
        Location loc = p.getLocation();

        Location loc1 = p.getLocation();
        loc1.setYaw(0);
        loc1.setPitch(0);
        Vector vec = loc1.getDirection().normalize().multiply(5);
        for (int z = 0; z < 6; z++) {
            Utils.rotateAroundAxisY(vec, Math.toRadians(60));
            p.getWorld().spigot().strikeLightningEffect(loc1.clone().add(vec), true);

        }
        p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 2, 1);
        p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 2, 1);
        for (LivingEntity en : Utils.getNearbyLivingEntities(f1, 5)) {
            if (Utils.damageable(en, p)) {
                SatThuong.SpellDamage(p, en, 2);
            }
        }
        Utils.SwingHand(p, false);
        loc.setYaw(f1.getYaw() - 180);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 5) {
                    p.teleport(f1);
                    this.cancel();
                } else {
                    loc.setYaw(loc.getYaw() + 36);
                    p.teleport(loc);
                    Utils.Attack(p);
                    i++;
                }

            }
        }.runTaskTimer(Main.m, 1, 1);


    }
}
