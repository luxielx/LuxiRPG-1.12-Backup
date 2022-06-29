package skill;

import Lightning.DefaultLightningCreator;
import Lightning.Lightning;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.ColorTransition;
import Util.Sit;
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

public class Thunder4 {



    final public static int manacost = 100;
    public static int cost = 10;
    public static int levelreq = 20;
    public static String name = ChatColor.RED + ChatColor.ITALIC.toString() + "Tứ Thức: Viễn Lôi";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Tạo 1 vùng sấm sét liên tục đánh kẻ địch trong phạm vi"
            ,ChatColor.GRAY+"Đồng minh trong vùng sấm sét sẽ nhận giáp."});
    public static int cooldown = 40;
    public static double cooldownr = 0.15;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static ArrayList<UUID> casting = new ArrayList<>();

    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.BLACK_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        RPGPlayer rpg = RPGPlayerListener.get(p);
        boolean fambuff = false;
        if (rpg.getGuild() != null) {
            if (rpg.getGuild().getFamilia() == Familia.AMATERASU) {
                fambuff = true;
            }

        }
        boolean finalFambuff = fambuff;
        Location loc = p.getLocation().add(0, .1, 0);
        new BukkitRunnable() {
            int counter = 0;
            boolean run = true;

            @Override
            public void run() {
                if (counter >= 10) {
                    casting.remove(p.getUniqueId());
                    run = false;
                    this.cancel();
                }
                if(Sit.isSitting(p)){
                    casting.remove(p.getUniqueId());
                    run = false;
                    this.cancel();
                }
                for (Player s : Utils.getNearbyPlayerAsync(loc, 15)) {
                    if (Utils.isAlly(p, s)) {
                        Bukkit.getScheduler().runTask(Main.m, () -> {
                            if (finalFambuff) {
                                Utils.Heal(s, (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05), true);
                            }
                            if (!s.hasPotionEffect(PotionEffectType.ABSORPTION))
                                s.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 5));
                        });
                    }
                }
                p.playSound(loc, Sound.ENTITY_LIGHTNING_THUNDER,3,1);
                Utils.drawRedStoneLineParticle(loc.clone().add(15,0,15),loc.clone().add(-15,0,-15),20,177, 156, 217,1 ,true );
                Utils.getNearbyLivingEntities(loc, 12).stream().forEach(en -> {
                    if (Utils.damageable(en, p)) {
                        SatThuong.SpellDamage(p, en, 2);
                        if (en instanceof Player) {
                            Bukkit.getScheduler().runTask(Main.m, () -> {
                                if (!en.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                                    en.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 5 * 20, 1));
                                if (!en.hasPotionEffect(PotionEffectType.BLINDNESS))
                                    en.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 5 * 20, 1));
                            });
                        }
                        //summon lightning
                        Location start = loc.clone().add(0, 5, 0);
                        Vector dir = en.getEyeLocation().toVector().subtract(start.clone().toVector()).normalize();
                        start.setPitch(90);
                        Collection<Lightning> lightnings = DefaultLightningCreator.createLightning(
                                p.getWorld(),
                                start,
                                dir,
                                1, 1, 15, 10,
                                1, 1.03, 0.2, 1.02
                        );
                        Bukkit.getScheduler().callSyncMethod(Main.m, () -> {
                            Lightning.create(lightnings, Lightning.NO_LIMIT, new ColorTransition(10035967, 7829503,
                                    Lightning.countTotalLightnings(lightnings)));
                            return null;
                        });

                    }
                });
//
                circle(loc);
                counter++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 20);
    }

    public static void circle(Location loc) {
        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = 15 * Math.cos(radians);
            double z = 15 * Math.sin(radians);
            Utils.coloredRedstone(loc.clone().add(x, 0, z), 177, 156, 217);
        }
    }
}