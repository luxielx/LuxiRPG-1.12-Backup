package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;


public class Fire4 {
    final public static int manacost = 100;
    public static int cost = 4;
    public static int levelreq = 20;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Cửu Thức: Luyện Ngục";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém 1 nhát cực mạnh về phía trước để lại vệt lửa trên đường đi của nhát chém",ChatColor.GRAY+"Nếu nhát chém trúng mục tiêu hoặc hết tầm sẽ lan ra các nhát chém nhỏ xung quanh\n"});
    public static double cooldown = 30;
    public static double cooldownr = 0.1;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.BLUE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        Utils.SwingHand(p,false);
        summonFireSlash(p, 2, p.getEyeLocation(), true);

    }

    private static void summonFireSlash(Player p, double radius, Location loz, boolean split) {
        loz.setPitch(0);
        Vector dir = loz.getDirection().normalize();
        double angle = Math.toRadians(loz.getYaw());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 0f);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_BURN, 1, 0f);

        new BukkitRunnable() {
            int l = 0;
            ArrayList<LivingEntity> damaged = new ArrayList<>();
            boolean hit = false;
            boolean zz = true;
            ArrayList<Block> fireb = new ArrayList<>();

            @Override
            public void run() {
                if (split) {
                    if (l >= 20) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Block b : fireb)
                                    Utils.setBlock(b,Material.AIR);

                            }
                        }.runTaskLater(Main.m, 100);
                        this.cancel();
                    }
                } else {
                    if (l >= 10) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Block b : fireb)
                                    Utils.setBlock(b,Material.AIR);
                            }
                        }.runTaskLater(Main.m, 100);
                        this.cancel();
                    }
                }
                if (hit && split && zz) {
                    loz.getWorld().spawnParticle(Particle.EXPLOSION_HUGE,loz.clone().subtract(0,0.3,0),1);
                    loz.getWorld().playSound(loz,Sound.ENTITY_GENERIC_EXPLODE,1,1);
                    Location loz2 = loz.clone();
                    Location loz3 = loz.clone();
                    Location loz4 = loz.clone();
                    Location loz5 = loz.clone();
                    loz2.setYaw(loz.getYaw() + 90);
                    loz3.setYaw(loz.getYaw() - 90);
                    loz4.setYaw(loz.getYaw() - 45);
                    loz5.setYaw(loz.getYaw() + 45);
                    summonFireSlash(p, 1, loz2, false);
                    summonFireSlash(p, 1, loz3, false);
                    summonFireSlash(p, 1, loz4, false);
                    summonFireSlash(p, 1, loz5, false);
                    zz = false;
                }
                for (double i = angle + Math.toRadians(20); i <= Math.PI + angle - Math.toRadians(20); i += 0.05) {
                    double x = radius * Math.cos(i);
                    double z = radius * Math.sin(i);
                    p.spawnParticle(Particle.FLAME, loz.clone().add(x, -.2, z), 0);
                }
                Block b = loz.clone().subtract(0, 1, 0).getBlock();
                if (b.getType() == Material.AIR) {
                    if (b.getLocation().distance(p.getLocation()) >= 2) {
                        Bukkit.getScheduler().runTask(Main.m, () -> {
                            Utils.setBlock(b,Material.FIRE);
                        });
                        fireb.add(b);
                    }
                }

                for (LivingEntity en : Utils.getNearbyLivingEntities(loz, 2)) {
                    if (Utils.damageable(en, p) && !damaged.contains(en)) {
                        SatThuong.SpellDamage(p, en, 5);
                        damaged.add(en);
                        hit = true;
                    }
                }
                if(l>=19){
                    if(hit == false) hit = true;
                }
                loz.add(dir);
                l++;

            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);

    }

}
