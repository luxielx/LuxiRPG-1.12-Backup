package skill;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.Utils;
import guild.Familia;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Wind2 {
    final public static int manacost = 100;
    public static int cost = 4;
    public static int levelreq = 5;
    public static String name = ChatColor.GREEN + ChatColor.ITALIC.toString() + "Nhị Thức: Khoa Hộ Phong";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Tạo ra cơn gió nhẹ xung quanh bản thân",ChatColor.GRAY+"Tất cả đồng đội đi vào cơn gió sẽ được tăng tốc độ di chuyển." ,ChatColor.GRAY+
            " Trong thời gian cơn gió tồn tại",ChatColor.GRAY+"Nhất thức sẽ giảm thời gian hồi chiêu còn 1s",ChatColor.GRAY+"Và khi sử dụng Nhất thức đồng thời sẽ sử dụng Lục Thức"});

    public static int cooldown = 30;
    public static double cooldownr = 0.1;
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
        p.setCooldown(Material.LIME_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        if (!casting.contains(p.getUniqueId())) {
            casting.add(p.getUniqueId());
        }
        RPGPlayer rpg = RPGPlayerListener.get(p);
        boolean fambuff=false;
        if(rpg.getGuild() != null){
            if(rpg.getGuild().getFamilia() == Familia.AMATERASU){
                fambuff=true;
            }

        }
        boolean finalFambuff = fambuff;
        new BukkitRunnable() {
            int counter = 0;
            Location loc = p.getLocation().add(0, .1, 0);
            boolean run = true;
            @Override
            public void run() {
                if (counter >= 5) {
                    casting.remove(p.getUniqueId());
                    run = false;
                    this.cancel();
                }
                loc = p.getLocation();
                for (Player s : Utils.getNearbyPlayerAsync(loc, 20)) {
                    if (Utils.isAlly(p, s)) {
                        Bukkit.getScheduler().runTask(Main.m, () -> {
                            if(finalFambuff){
                                Utils.Heal(s,(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05) , true   );
                            }
                            s.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 3));
                        });
                    }
                }
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_BREATHE, 1, 0.2f);
                circle(p);
                int lines = 8;
                double radius_increasement = 1;
                int angle = 0;
                for (int l = 0; l <= lines; l++) {
                    int finalL = l;
                    new BukkitRunnable() {
                        double y = 0;

                        @Override
                        public void run() {
                            if(!run) this.cancel();
                            if (y > 20) this.cancel();

                            double radius = y * radius_increasement;
                            double x = Math.cos(Math.toRadians(360 / lines * finalL + y * 25 - angle)) * radius;
                            double z = Math.sin(Math.toRadians(360 / lines * finalL + y * 25 - angle)) * radius;
                            Utils.coloredRedstone(loc.clone().add(x, 0, z), Color.WHITE);
                            y += 0.1;

                        }
                    }.runTaskTimerAsynchronously(Main.m, 0, 1);

                }
                counter++;
            }
        }.runTaskTimerAsynchronously(Main.m, 0, 20 * 2);


    }

    public static void circle(Player p) {
        Location loc = p.getLocation();
        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = 20 * Math.cos(radians);
            double z = 20 * Math.sin(radians);
            loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x, 0, z), 0);

        }
    }

}