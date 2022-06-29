package skill;

import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import stat.StatPlayer;

import java.util.*;


public class Fire3 {
    final public static int manacost = 70;
    public static int cost = 5;
    public static int levelreq = 10;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Tứ Thức: Thịnh Viêm Hải Triều";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Hút tất cả các cột lửa đang cháy trong phạm vi xung quanh",ChatColor.GRAY+"Hồi máu và gây sát thương ra xung quanh dựa trên số cột lửa hút được.\n"});
    public static double cooldown = 20;
    public static double cooldownr = 0.05;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());
        }
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        p.setCooldown(Material.PURPLE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        List<Block> blist = Utils.getNearbyBlocks(p.getLocation(), 10);
        int firecount = 0;

        for (Block b : blist)
            if (b.getType() == Material.FIRE) {
                Utils.drawLineParticle(b.getLocation(), p.getEyeLocation().subtract(0, 0.2, 0), Particle.SPELL_WITCH, 5, true);
                firecount++;
            }
        int finalFirecount = firecount;
        new BukkitRunnable() {
            int i = 1;
            Location loc = p.getLocation().clone();
            @Override
            public void run() {
                if (i >= 3 + finalFirecount/2) this.cancel();
                for (int d = 0; d <= 15*i/2; d++) {
                    double x = i * Math.cos(d);
                    double z = i * Math.sin(d);
                    if (i == 3 + finalFirecount/2) {
                        p.getWorld().spawnParticle(Particle.FLAME,loc.clone().add(x, 0.2, z),0);
                    } else {
                        Utils.coloredRedstone(loc.clone().add(x, 0.2, z), Color.ORANGE);
                    }
                }
                i++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 1);
        p.playSound(p.getLocation(),Sound.ENTITY_LIGHTNING_IMPACT,1,1);
        p.playSound(p.getLocation(),Sound.BLOCK_REDSTONE_TORCH_BURNOUT,1,1);
        ArrayList<LivingEntity> damaged = new ArrayList<>();
        for (LivingEntity e : Utils.getNearbyLivingEntities(p.getLocation(), 3 + firecount)) {
            if (Utils.damageable(e, p)) {
                if (!damaged.contains(e)) SatThuong.SpellDamage(p, e, 2 + firecount);
                e.setFireTicks(100);
                damaged.add(e);
            }
        }
        double healamount = (StatPlayer.inTel(p) * firecount/2);
        Utils.Heal(p,healamount,true);


    }

}
