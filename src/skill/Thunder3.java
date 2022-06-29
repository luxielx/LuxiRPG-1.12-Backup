package skill;

import Lightning.DefaultLightningCreator;
import Lightning.Lightning;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.ColorTransition;
import Util.Utils;
import guild.Familia;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Thunder3 {

    final public static int manacost = 20;
    public static int cost = 10;
    public static int levelreq = 10;
    public static String name = ChatColor.GOLD + ChatColor.ITALIC.toString() + "Lục Thức: Điện Oanh Lôi Oanh";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Triệu hồi tia sét về phía trước!"});
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
        p.setCooldown(Material.RED_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        final Vector dir = p.getLocation().getDirection().normalize();
        p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 2, 1);

        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            Collection<Lightning> lightnings = DefaultLightningCreator.createLightning(
                    p.getWorld(),
                    p.getLocation().add(0, 1.5, 0),
                    dir.clone(),
                    1, 1, 25, 100,
                    1, 1.03, 0.5, 1.02
//                    0.1, 1, 25, 10,
//                    2, 1.2, 0.3, 1.02
            );
            Bukkit.getScheduler().callSyncMethod(Main.m, () -> {
                Set<Entity> entities = Lightning.create(lightnings, Lightning.NO_LIMIT, new ColorTransition(10035967, 7829503,
                        Lightning.countTotalLightnings(lightnings)));
                entities.stream().forEach(en -> {
                    if (en instanceof LivingEntity) {
                        if (Utils.damageable(en, p)) {
                            SatThuong.SpellDamage(p, (LivingEntity) en, 5);
                        }
                    }

                });
                return null;
            });
        });

    }

}
