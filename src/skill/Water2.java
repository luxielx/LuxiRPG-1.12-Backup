package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import attack.ChargedAttack;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Water2 {
    final public static int manacost = 30;
    public static int cost = 4;
    public static int levelreq = 5;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhị Thức: Thủy Xa";
    public static String name2 = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Bát Thức: Lưu Bộc Thác";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Khi kĩ năng được thi triển dưới đất kiếm sĩ sẽ chém liên tục xung quanh bản thân",ChatColor.GRAY+
            "Khi tập trung tuyệt đối sẽ chém nhanh gấp đôi.", ChatColor.GRAY+"Còn khi sử dụng trên không kiếm sĩ lao thẳng về phía dưới","" +
            ChatColor.GRAY+"Khi bắt đầu lao xuống và khi chạm đất đều để lại 1 vệt chém vòng cung.",
            ChatColor.GRAY+"Các kẻ địch gần nơi chạm đất đều bị nước đẩy lên không trung."});

    public static int cooldown = 15;
    public static double cooldownr = 0.07;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static ArrayList<UUID> onair = new ArrayList<>();

    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        p.setCooldown(Material.ORANGE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        if (!p.isOnGround()) {
            Location loz = p.getLocation();
            Utils.sendTitleBar(p, "", ChatColor.BOLD + name2);
            Vector v = p.getLocation().getDirection();
            if (v.getY() > 0) {
                v.setY(-v.getY());
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 5, 1);
            p.setVelocity(v.multiply(5));
            onair.add(p.getUniqueId());
            new BukkitRunnable() {
                double i = 0;

                @Override
                public void run() {
                    if (!onair.contains(p.getUniqueId())) {
                        onair.remove(p.getUniqueId());
                        this.cancel();
                    }
                    if (i >= 20 * 5) {
                        onair.remove(p.getUniqueId());
                        this.cancel();
                    }
                    Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
                        p.getWorld().spawnParticle(Particle.WATER_SPLASH, p.getLocation().clone().add(0, .5, 0), 10);
                        p.getWorld().spawnParticle(Particle.WATER_SPLASH, p.getLocation().clone().add(0, 1, 0), 10);
                        p.getWorld().spawnParticle(Particle.WATER_SPLASH, p.getLocation().clone().add(0, 1.5, 0), 10);
                    });
                    if (p.isOnGround()) {
                        double distance = loz.distance(p.getLocation());
                        loz.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
                        loz.getWorld().playSound(p.getLocation(), Sound.ENTITY_BOBBER_SPLASH, 1, 1);
                        if (onair.contains(p.getUniqueId())) {
                            onair.remove(p.getUniqueId());
                            Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
                                p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, p.getLocation().add(0, 0.1, 0), 0);
                                p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().add(0, 0.5, 0), 50);
                                slash(p, false);
                            });

                            for (LivingEntity en : Utils.getNearbyLivingEntities(p.getLocation(), 5)) {
                                if (Utils.damageable(en, p)) {
                                    SatThuong.SpellDamage(p, en, 1 + distance / 5);
                                    createWaterSpike(p, en, distance);
                                }

                            }
                        }


                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimerAsynchronously(Main.m, 0, 1);
            slash(p, false);
        } else {
            Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
            slash(p, true);
        }


    }

    private static void slash(Player p, boolean follow) {
        double angle = Math.toRadians(p.getLocation().getYaw());
        Location dr = p.getLocation();
        dr.setPitch(0);
        Vector v = dr.getDirection();
        int radius = 5;

        Thread cc = new Thread() {
            boolean run = true;
            double i = angle;
            double i2 = Math.PI + angle;
            Location loc = p.getEyeLocation();
            int time = 2;
            int count=0;
            @Override
            public void run() {
                if (follow) time = 5;
                if (ChargedAttack.isCharged(p.getUniqueId())) {
                    ChargedAttack.setCharged(p.getUniqueId(), false);
                    time *= 2;
                }
                loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_SPLASH, 5, 1);
                while (run) {
                    if (i >= Math.PI * time + angle) run = false;
                    if (follow) {
                        loc = p.getLocation().add(0, 1.3, 0);
                    }
                    double x = radius * Math.cos(i);
                    double z = radius * Math.sin(i);
//                    double xx = radius * Math.cos(i2);
//                    double zz = radius * Math.sin(i2);


                    Utils.coloredRedstone(loc.clone().add(x, 0, z), Color.AQUA);
                    p.getWorld().spawnParticle(Particle.WATER_BUBBLE, loc.clone().add(x, 0, z), 0, v.getX(), v.getY(), v.getZ(), 0.5);
                    Utils.coloredRedstone(loc.clone().add(x, 0, z), Color.PURPLE);
                    if(count % 100 == 0)
                    for (LivingEntity en : Utils.getNearbyLivingEntities(loc, radius)) {
//                        if (damaged.contains(en)) continue;
                        if (Utils.damageable(en, p)) {
                            SatThuong.SpellDamage(p, en, 1);
                        }
                    }
                    count++;
                    i += 0.05;
                    i2 -= 0.05;
                    try {
                        Thread.sleep(10 / time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        cc.start();
    }


    private static void createWaterSpike(Player p, LivingEntity en, double distance) {
        en.setVelocity(new Vector(0, ThreadLocalRandom.current().nextDouble(0.2, 1) + (distance / 20), 0));
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i >= 10) this.cancel();
                Block b = en.getLocation().getBlock();
                Utils.sendBlockChange(b.getLocation(), Material.WATER, 30);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        b.getState().update();
                    }
                }.runTaskLater(Main.m, 10);
                i++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);


    }


}
