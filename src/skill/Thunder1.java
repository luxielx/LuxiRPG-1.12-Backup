package skill;

import Lightning.DefaultLightningCreator;
import Lightning.Lightning;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.ColorTransition;
import Util.Sit;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Util.Utils.ChineseNumberString;

public class Thunder1 {
    final public static int manacost = 50;
    public static int cost = 1;
    public static int levelreq = 1;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhất Thức: Phích Lịch Nhất Thiểm";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Kiếm sĩ sẽ nhận được ngẫu nhiên số lần tái sử dụng của kĩ năng này"
            ,ChatColor.GRAY+"Mỗi lần sử dụng sẽ lướt đi 1 quãng ngắn ",ChatColor.GRAY+"Chém 2 liên kích trước mặt",ChatColor.GRAY+"Để lại 1 tia sét sau lưng gây sát thương lên kẻ địch chạm phải."});
    public static int cooldown = 10;
    public static double cooldownr = 0.07;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static ArrayList<UUID> castin = new ArrayList<>();
    public static HashMap<UUID, BukkitTask> task = new HashMap<>();

    public static void r(Player p) {
        if (p.getInventory().getItem(0) == null) return;
        ItemStack im = p.getInventory().getItem(0);
        if (im.getAmount() < manacost) {
            if (castin.contains(p.getUniqueId())) return;
            if (Sit.isSitting(p)) return;
            if (im.getAmount() == 1) {
                p.setCooldown(Material.BROWN_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
                if (!lastuse.containsKey(p.getUniqueId())) {
                    lastuse.put(p.getUniqueId(), 0l);
                } else {
                    lastuse.put(p.getUniqueId(), System.currentTimeMillis());
                }
                p.getInventory().getItem(0).setAmount(manacost);
                if (task.containsKey(p.getUniqueId()))
                    task.get(p.getUniqueId()).cancel();

            } else {
                p.getInventory().getItem(0).setAmount(im.getAmount() - 1);
            }
            // cast lightning dash
            int dashrange = 7;
            p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1, 1);
            Location df = p.getLocation().clone();
            Location loc = p.getLocation().clone();
            Location zxc = p.getEyeLocation();
            zxc.setPitch(0);
            Vector dir = zxc.getDirection().normalize().multiply(0.1);
            ArrayList<LivingEntity> damaged = new ArrayList<>();
            for (int dis = 0; dis <= 10 * dashrange; dis++) {
                loc.add(dir);
                if (loc.getBlock().getType().isTransparent() && loc.getBlock().getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
                    p.teleport(loc);
                    for (LivingEntity en : Utils.getNearbyLivingEntities(loc, 1)) {
                        if (en instanceof LivingEntity) {
                            if (Utils.damageable(en, p)) {
                                if (!damaged.contains(en)) {
                                    SatThuong.SpellDamage(p, en, 1);
                                    damaged.add(en);
                                }

                            }
                        }
                    }
                } else {
                    break;
                }
            }


            new BukkitRunnable() {
                @Override
                public void run() {
                    Collection<Lightning> lightnings = DefaultLightningCreator.createLightning(
                            p.getWorld(),
                            df.clone().add(0, 1, 0),
                            dir.clone(),
                            0.7, 1, 10, 50,
                            1, 1.03, 0.1, 1.02
                    );
                    Bukkit.getScheduler().callSyncMethod(Main.m, () -> {
                        Set<Entity> entities = Lightning.create(lightnings, Lightning.NO_LIMIT, new ColorTransition(15597427, 16776960,
                                Lightning.countTotalLightnings(lightnings)));
                        entities.stream().forEach(en -> {
                            if (en instanceof LivingEntity) {
                                if (Utils.damageable(en, p)) {
                                    SatThuong.SpellDamage(p, (LivingEntity) en, 1);
                                }
                            }

                        });
                        return null;

                    });
                }
            }.runTaskLaterAsynchronously(Main.m, 5);

            Utils.SwingHand(p, false);
            Utils.Attack(p);


        } else {
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
            if (castin.contains(p.getUniqueId())) {
                return;
            } else {
                castin.add(p.getUniqueId());
            }
            p.setCooldown(Material.GRAY_GLAZED_TERRACOTTA, 20 * 20);

            Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
            new BukkitRunnable() {
                int time = 1;
                int i = 0;

                @Override
                public void run() {
                    if (i < 5) {
                        int z = ThreadLocalRandom.current().nextInt(1, 9);
                        Utils.sendTitleBar(p, Utils.randomChatColor() + z, ChatColor.BOLD + name);
                        time = z;
                        i++;
                    } else {
                        Utils.sendTitleBar(p, ChatColor.GOLD + ChatColor.BOLD.toString() + ChineseNumberString(time) + " Liên", ChatColor.BOLD + name);
                        p.getInventory().getItem(0).setAmount(time);
                        castin.remove(p.getUniqueId());
                        this.cancel();
                        // cast
                    }
                }
            }.runTaskTimer(Main.m, 1, 5);
            task.put(p.getUniqueId(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (p.getInventory().getItem(0) == null) SkillCastList.setGlassPane(p);
                    if (p.getInventory().getItem(0).getAmount() != manacost) {
                        SkillCastList.setGlassPane(p);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskLater(Main.m, 20 * 20));

        }

    }


}
