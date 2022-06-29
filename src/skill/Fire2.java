package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import attack.ChargedAttack;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Fire2 {
    final public static int manacost = 50;
    public static int cost = 5;
    public static int levelreq = 5;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhị Thức: Thăng Viêm Thiên";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém từ trên xuống ngay trước mặt gây sát thương trong vùng ảnh hưởng và đốt cháy mặt đất.",ChatColor.GRAY+ "Nếu đang trong trạng thái tập trung tuyệt đối vùng lửa và sát thương sẽ cháy tiếp về phía trước mặt"});
    public static double cooldown = 20;
    public static double cooldownr = 0.1;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        p.setCooldown(Material.CYAN_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        boolean charge = false;
        if (ChargedAttack.isCharged(p.getUniqueId())) {
            ChargedAttack.setCharged(p.getUniqueId(), false);
            charge = true;
        }
        Location loc1 = p.getEyeLocation();

        Color clo = Color.fromRGB(227, 140, 45);
        Color clo2 = Color.fromRGB(254, 100, 0);
        Vector dir = loc1.getDirection().normalize();
        Location loc2 = p.getLocation();
        loc2.setPitch(0);
        Location locend = p.getLocation().add(loc2.getDirection().normalize().multiply(3));
        Utils.SwingHand(p, false);
        boolean finalCharge = charge;
        Thread cc = new Thread() {
            int i = 270 / 4;
            boolean run = true;

            @Override
            public void run() {
                while (run) {
                    if (i <= 120 / 4) run = false;
                    double radians = Math.toRadians(i * 4);
                    double x = 3 * Math.cos(radians);
                    double z = 3 * Math.sin(radians);
                    Vector v = new Vector(x, 0, z);
                    v = Utils.rotateAroundAxisX(v, Math.toRadians(90));
                    v = Utils.rotateAroundAxisY(v, Math.toRadians(90 - loc1.getYaw()));
                    Utils.coloredRedstone(loc1.clone().add(v), clo);
                    Utils.coloredRedstone(loc1.clone().add(dir.clone().multiply(0.2)).add(v), clo);
                    Utils.coloredRedstone(loc1.clone().add(dir.clone().multiply(0.8)).add(v), clo);
                    Utils.coloredRedstone(loc1.clone().add(dir.clone().multiply(0.6)).add(v), clo2);
                    Utils.coloredRedstone(loc1.clone().add(dir.clone().multiply(0.4)).add(v), clo2);
                    if (finalCharge)
                        p.getWorld().spawnParticle(Particle.FLAME, loc1.clone().add(dir.clone().multiply(0.4)).add(v), 0);

                    i--;
                    try {
                        Thread.sleep(7l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        cc.start();

        Bukkit.getScheduler().runTaskLater(Main.m, () -> {
            p.playSound(locend, Sound.ENTITY_LIGHTNING_IMPACT, 1, 1);
            p.playSound(locend, Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            ArrayList<LivingEntity> damaged = new ArrayList<>();
            p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, locend, 0);
            p.getWorld().spawnParticle(Particle.FLAME, locend, 100);
            int radius = 1;
            if (finalCharge) radius = 2;
            for (Block b : Utils.getNearbyBlocks(locend, radius)) {
                if (b.getType() != Material.AIR) {
                    b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().add(0, 1, 0), 10, new MaterialData(b.getType()));
                    Utils.sendCrackPacket(ThreadLocalRandom.current().nextInt(1, 8), b);
                }
            }
            ArrayList<Block> fireb = new ArrayList<>();
            if (!finalCharge) {
                for (int i = 0; i <= 36; i++) {
                    double rad = Math.toRadians(i * 10);
                    double x = 3 * Math.cos(rad);
                    double z = 3 * Math.sin(rad);
                    p.getWorld().spawnParticle(Particle.FLAME, locend.clone().add(x, 0.1, z), 0);
                }
                for (LivingEntity e : Utils.getNearbyLivingEntities(locend, 2)) {
                    if (Utils.damageable(e, p)) {
                        if (!damaged.contains(e)) SatThuong.SpellDamage(p, e, 3);
                        damaged.add(e);
                    }
                }
                for (Block b : Utils.getNearbyBlocks(locend.clone(), 1)) {
                    if (b.getType() == Material.AIR) {
                        Utils.setBlock(b, Material.FIRE);
                        fireb.add(b);
                    }
                }
            } else {

                Location l1 = p.getLocation();
                l1.setPitch(0);
                Location l2 = l1.clone();
                l2.setYaw(l1.getYaw() + 15);
                Location l3 = l1.clone();
                l3.setYaw(l1.getYaw() - 15);
                Vector v1 = l1.getDirection().normalize();
                Vector v2 = l2.getDirection().normalize();
                Vector v3 = l3.getDirection().normalize();
                for (int i = 0; i <= 10; i++) {
                    Location lc1 = locend.clone().add(0, 0.2, 0).add(v1.clone().multiply(i));
                    Location lc2 = locend.clone().add(0, 0.2, 0).add(v2.clone().multiply(i));
                    Location lc3 = locend.clone().add(0, 0.2, 0).add(v3.clone().multiply(i));
                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, lc1, 0);
                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, lc2, 0);
                    p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, lc3, 0);
                    if (lc1.getBlock().getType() == Material.AIR) {
                        Block b = locend.clone().add(0, 0.2, 0).add(v1.clone().multiply(i)).getBlock();
                        Utils.setBlock(b, Material.FIRE);
                        fireb.add(b);

                    }
                    if (lc2.getBlock().getType() == Material.AIR) {
                        Block b = locend.clone().add(0, 0.2, 0).add(v2.clone().multiply(i)).getBlock();
                        Utils.setBlock(b, Material.FIRE);
                        fireb.add(b);

                    }
                    if (lc3.getBlock().getType() == Material.AIR) {
                        Block b = locend.clone().add(0, 0.2, 0).add(v3.clone().multiply(i)).getBlock();
                        Utils.setBlock(b, Material.FIRE);
                        fireb.add(b);

                    }
                    for (LivingEntity e : Utils.getNearbyLivingEntities(lc1, 1)) {
                        if (Utils.damageable(e, p)) {
                            if (!damaged.contains(e)) SatThuong.SpellDamage(p, e, 3);
                            damaged.add(e);
                        }
                    }
                    for (LivingEntity e : Utils.getNearbyLivingEntities(lc2, 1)) {
                        if (Utils.damageable(e, p)) {
                            if (!damaged.contains(e)) SatThuong.SpellDamage(p, e, 3);
                            damaged.add(e);
                        }
                    }
                    for (LivingEntity e : Utils.getNearbyLivingEntities(lc3, 1)) {
                        if (Utils.damageable(e, p)) {
                            if (!damaged.contains(e)) SatThuong.SpellDamage(p, e, 3);
                            damaged.add(e);
                        }
                    }


                }


            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Block b : fireb)
                        Utils.setBlock(b, Material.AIR);
                }
            }.runTaskLater(Main.m, 100);
            damaged.clear();
        }, 5);


    }
}
