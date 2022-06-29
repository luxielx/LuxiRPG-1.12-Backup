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


public class Fire1 {
    final public static int manacost = 10;
    public static int cost = 1;
    public static int levelreq = 1;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhất Thức: Bất Tri Hỏa";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Chém lửa vào các kẻ địch trong phạm vi xung quanh, gây sát thương, đốt cháy kẻ địch cùng với mặt đất nơi kẻ địch đứng."});
    public static double cooldown = 10;
    public static double cooldownr = 0.08;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        int time = 1;
        if (ChargedAttack.isCharged(p.getUniqueId())) {
            ChargedAttack.setCharged(p.getUniqueId(), false);
            time = 3;
        }
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        p.setCooldown(Material.SILVER_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        p.playSound(p.getLocation(),Sound.ENTITY_PLAYER_HURT_ON_FIRE,1,1);
        ArrayList<LivingEntity> enlist = Utils.getNearbyLivingEntities(p.getLocation(), 10);
        Utils.SwingHand(p, false);
        int count = 0;
        for(int i = 0 ; i<time;i++)
        for (LivingEntity e : enlist) {
            if (Utils.damageable(e, p) && count<=10 && (p.getLocation().getY()-e.getLocation().getY() < 4) ) {
                slash(e, p);
                SatThuong.SpellDamage(p, e, 1);
                count++;
                if(e.getLocation().getBlock().getType() == Material.AIR){
                    Block b = e.getLocation().getBlock();
//                    Utils.sendBlockChange(b.getLocation(), Material.FIRE, 30);
                    Utils.setBlock(b,Material.FIRE);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Utils.setBlock(b,Material.AIR);
                        }
                    }.runTaskLater(Main.m, 100);
                }
                if(time == 3){
                    e.setFireTicks(20*3);
                }
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            for (int i = 0; i <= 36; i++) {
                double radians = Math.toRadians(i);
                double x = 10 * Math.cos(radians*10);
                double z = 10 * Math.sin(radians*10);
                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().clone().add(x, 0.2, z), 0);
            }
            p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(0, 0.3, 0), 10);
        });



    }


    public static void slash(LivingEntity e, Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            Vector v = p.getEyeLocation().toVector().subtract(e.getEyeLocation().toVector()).normalize().multiply(1.7);
            Location loc = e.getEyeLocation();
            double angle = Math.toRadians(e.getLocation().getYaw());
            Color clo = Color.fromRGB(255, 160, 122);
            final double radius = 2;
            double y = ThreadLocalRandom.current().nextDouble(-2, 2);
            double plus = -y / 18;
            for (double asd = angle + Math.toRadians(20); asd <= Math.PI + angle - Math.toRadians(20); asd += 0.05) {
                double x = radius * Math.cos(asd);
                double z = radius * Math.sin(asd);
                y += plus;
                Utils.coloredRedstone(loc.clone().subtract(x, y, z).add(v), clo);
                if (Utils.percentRoll(30)) Utils.coloredRedstone(loc.clone().subtract(x, y, z).add(v), Color.WHITE);
            }
        });

    }

}
