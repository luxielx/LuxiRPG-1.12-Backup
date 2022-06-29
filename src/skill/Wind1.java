package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static skill.Wind2.casting;

public class Wind1 {
    final public static int manacost = 10;
    public static int cost = 4;
    public static int levelreq = 1;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhất Thức: Trần Toàn Phong";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Đâm kiếm về phía trước",ChatColor.GRAY+" Đòn đâm thứ 3 chém xoáy tạo ra lốc",ChatColor.GRAY+" Tất cả kẻ địch trúng lốc sẽ bị cuốn vào giữa."});
    public static int cooldown = 5;
    public static double cooldownr = 0.03;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static HashMap<UUID, Integer> usetime = new HashMap<>();


    public static void r(Player p) {
        if (casting.contains(p.getUniqueId())) {
            if (!SkillCastEvent.castAble(p, levelreq, 1, 0, 0, lastuse)) return;
            p.setCooldown(Material.YELLOW_GLAZED_TERRACOTTA, 1 * 20);
            WindCharged.r(p);
        } else {
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
            p.setCooldown(Material.YELLOW_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        }
        Utils.sendTitleBar(p, getWindSymbol(p), ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }

        if (!usetime.containsKey(p.getUniqueId())) {
            usetime.put(p.getUniqueId(), 1);
        }
        if (usetime.containsKey(p.getUniqueId())) {
            Utils.SwingHand(p, false);
            if (usetime.get(p.getUniqueId()) >= 3) {
                usetime.remove(p.getUniqueId());
                // lốc
                Location l = p.getEyeLocation();
                Vector vec = l.getDirection().normalize().multiply(5);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 5, 1f);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 5, 1f);
                ArrayList<LivingEntity> damaged = new ArrayList<>();
                new BukkitRunnable() {
                    int i = 1;

                    @Override
                    public void run() {
                        if (i >= 5) this.cancel();
                        l.add(vec);
                        drawTornado(l.clone(), p);
                        for (LivingEntity en : Utils.getNearbyLivingEntities(l, 1)) {
                            if (Utils.damageable(en, p) && !damaged.contains(en)) {
                                SatThuong.SpellDamage(p, en, 3);
                                damaged.add(en);
                            }
                        }
                        i++;
                    }
                }.runTaskTimerAsynchronously(Main.m, 0, 3);


            } else {
                usetime.put(p.getUniqueId(), usetime.get(p.getUniqueId()) + 1);

                Location l = p.getEyeLocation();
                Vector vec = l.getDirection().normalize().multiply(0.1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1, 1);
                ArrayList<LivingEntity> damaged = new ArrayList<>();

                for (int i = 1; i < 20 * 5; i++) {

                    l.add(vec);

                    Utils.coloredRedstone(l, 255, 210, 255);
                    Utils.coloredRedstone(l, 210, 255, 255);
                    if (i == 20 * 5 - 1)
                        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, l, 10);
                    for (LivingEntity en : Utils.getNearbyLivingEntities(l, 1)) {
                        if (Utils.damageable(en, p) && !damaged.contains(en)) {
                            SatThuong.SpellDamage(p, en, 2);
                            damaged.add(en);
                        }
                    }

                }


            }
        }

    }

    private static String getWindSymbol(Player p) {
        String s = "";
        if (usetime.containsKey(p.getUniqueId())) {
            if (usetime.get(p.getUniqueId()) == 1) {
                s = ChatColor.GREEN + "風" + ChatColor.GRAY + "風";
            }
            if (usetime.get(p.getUniqueId()) == 2) {
                s = ChatColor.GREEN + "風風" + ChatColor.GRAY + "風";
            }
            if (usetime.get(p.getUniqueId()) == 3) {
                s = ChatColor.GREEN + "風風風";
            }
        } else {
            s = ChatColor.GREEN + "風" + ChatColor.GRAY + "風風";
        }
        return s;


    }

    public static void drawTornado(Location loc, Player p) {
        int max_height = 8;
        double max_radius = 4;
        int lines = 4;
        double height_increasement = 0.1;
        double radius_increasement = max_radius / max_height;
        int angle = 0;
        new BukkitRunnable() {
            int l = 0;
            @Override
            public void run() {
                if (l >= lines) this.cancel();
                for (double y = 0; y < max_height; y += height_increasement) {
                    double radius = y * radius_increasement;
                    double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                    double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                    Utils.coloredRedstone(loc.clone().add(x, y, z), Color.WHITE);
                }
                ArrayList<LivingEntity> damaged = new ArrayList<>();
                Bukkit.getScheduler().runTask(Main.m, () -> {
                    Utils.getNearbyLivingEntities(loc, 2).stream().forEach(en -> {
                        if (Utils.damageable(en, p) && !damaged.contains(en)) {
                            SatThuong.SpellDamage(p, en, 0.3);
                            en.teleport(loc);
                            damaged.add(en);
                        }
                    });
                });


                l++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);

    }


}
