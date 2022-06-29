package skill;

import Luxiel.Main;
import Util.Sit;
import Util.Utils;
import attack.ChargedAttack;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Water3 {
    final public static int manacost = 50;
    public static int cost = 4;
    public static int levelreq = 10;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Cửu Thức: Thủy Lưu Phi Mạt";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Nhảy lên 1 vệt nước để nhảy lên cao hơn",ChatColor.GRAY+"Có thể sử dụng 3 lần trước khi hồi chiêu",
            ChatColor.GRAY+"Khi tập trung tuyệt đối nhảy cao hơn."});

    public static int cooldown = 20;
    public static double cooldownr = 0.07;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static ArrayList<UUID> castin = new ArrayList<>();
    public static HashMap<UUID, BukkitTask> task = new HashMap<>();


    public static void r(Player p) {
        if (p.getInventory().getItem(2) == null) return;
        ItemStack im = p.getInventory().getItem(2);
        if (im.getAmount() < manacost) {
            if (Sit.isSitting(p)) return;
            if (castin.contains(p.getUniqueId())) return;
            if (im.getAmount() == 1) {
                p.setCooldown(Material.MAGENTA_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
                if (!lastuse.containsKey(p.getUniqueId())) {
                    lastuse.put(p.getUniqueId(), 0l);
                } else {
                    lastuse.put(p.getUniqueId(), System.currentTimeMillis());
                }
                p.getInventory().getItem(2).setAmount(manacost);
                if (task.containsKey(p.getUniqueId()))
                    task.get(p.getUniqueId()).cancel();

            } else {
                p.getInventory().getItem(2).setAmount(im.getAmount() - 1);
            }
            // cast lightning dash

            for (double i = 0; i <= Math.PI; i += Math.PI / 360) {
                double x = 3 * Math.cos(i) * ThreadLocalRandom.current().nextDouble(-0.4, 0.4);
                double z = 3 * Math.sin(i) * ThreadLocalRandom.current().nextDouble(-0.4, 0.4);
                Utils.coloredRedstone(p.getLocation().clone().add(x, 0, z), Color.AQUA);

            }
            Vector v = p.getLocation().getDirection();
            if (Math.abs(v.getY()) >= 0.3) {
                v.setY(0.3);
            }
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1, 1);
            if (ChargedAttack.isCharged(p.getUniqueId())) {
                ChargedAttack.setCharged(p.getUniqueId(), false);
                p.setVelocity(v.multiply(3));
            } else {
                p.setVelocity(v.multiply(2));
            }


        } else {
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
            if (castin.contains(p.getUniqueId())) {
                return;
            } else {
                castin.add(p.getUniqueId());
            }
            p.setCooldown(Material.MAGENTA_GLAZED_TERRACOTTA, 20 * 20);

            Utils.sendTitleBar(p, "", ChatColor.BOLD + name);

            p.getInventory().getItem(2).setAmount(3);
            castin.remove(p.getUniqueId());
            r(p);
            task.put(p.getUniqueId(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.getInventory().getItem(2) == null) SkillCastList.setGlassPane(p);
                    if (p.getInventory().getItem(2).getAmount() != manacost) {
                        SkillCastList.setGlassPane(p);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskLater(Main.m, 20 * 20));

        }

    }

}
