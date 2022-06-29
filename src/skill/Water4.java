package skill;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.Utils;
import attack.ChargedAttack;
import guild.Familia;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Water4 {
    final public static int manacost = 100;
    public static int cost = 4;
    public static int levelreq = 20;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Thập Nhất Thức: Tĩnh";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Tạo ra 1 làn nước tĩnh đẩy tất cả kẻ địch ra xa",ChatColor.GRAY+"Hồi máu cho tất cả đồng minh và cho hiệu ứng bất tử." ,
            ChatColor.GRAY+"Khi tập trung tuyệt đối sẽ tạo ra 1 vùng băng dưới chân",ChatColor.GRAY+"Hiệu ứng bất tử sẽ lâu gấp đôi và được tăng tốc."});
    public static int cooldown = 40;
    public static double cooldownr = 0.1;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();


    public static void r(Player p) {
        if (!p.isOnGround()) return;
        if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
        Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
        if (!lastuse.containsKey(p.getUniqueId())) {
            lastuse.put(p.getUniqueId(), 0l);
        } else {
            lastuse.put(p.getUniqueId(), System.currentTimeMillis());

        }
        p.setCooldown(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
        Location loc = p.getLocation();
        loc.getWorld().playSound(loc, Sound.BLOCK_WATER_AMBIENT, 1, 1);
        int ch = 1;
        if (ChargedAttack.isCharged(p.getUniqueId())) {
            ChargedAttack.setCharged(p.getUniqueId(), false);
            ch = 2;
        }
        int finalCh = ch;
        Thread cc = new Thread() {
            boolean run = true;
            int ra = 1;
            @Override
            public void run() {
                while (run) {
                    if (ra >= 10) run = false;
                    for (double i = 0; i <= Math.PI * 2; i += Math.PI / 36) {
                        double x = ra * Math.cos(i);
                        double z = ra * Math.sin(i);
                        Block b = loc.clone().add(x, 0, z).getBlock();
                        if (finalCh == 2) {
                            b = loc.clone().add(x, -1, z).getBlock();
                            Utils.sendBlockChange(b.getLocation(), Material.ICE, 30);

                        } else {
                            Utils.sendBlockChangeWaterLevel(b.getLocation(), Material.WATER, 30, 7);

                        }

                        Block finalB = b;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                finalB.getState().update();
                            }
                        }.runTaskLater(Main.m, 20 * 2);
                    }
                    ra++;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        cc.start();

        RPGPlayer rpg = RPGPlayerListener.get(p);
        boolean fambuff=false;
        if(rpg.getGuild() != null){
            if(rpg.getGuild().getFamilia() == Familia.AMATERASU){
                fambuff=true;
                Utils.Heal(p,(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.1) , true   );
            }

        }
        boolean finalFambuff = fambuff;

        Utils.getNearbyLivingEntities(p.getLocation(), 10).stream().forEach(en -> {
            if (Utils.damageable(en, p)) {
                SatThuong.SpellDamage(p, en, 5);
                en.setVelocity(p.getLocation().toVector().subtract(en.getLocation().toVector()).normalize().multiply(-3 / (en.getLocation().distance(p.getLocation()))));
                en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 3));
            }
            if (Utils.isAlly(p, en)) {
                Utils.getNearbyPlayerAsync(p.getLocation(), 30).stream().forEach(nplayer -> {
                    Utils.setGlowing(en, nplayer, true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Utils.setGlowing(en, nplayer, false);
                        }
                    }.runTaskLaterAsynchronously(Main.m, 5 * 20 * finalCh);

                });
                if (finalCh == 2)
                    en.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20 * finalCh, 2));
                en.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5 * 20 * finalCh, 1));
                Utils.Heal(en, ((p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.5 + rpg.getInt() ) * finalCh),true);
                if(finalFambuff){
                    Utils.Heal(en,(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05) , true);
                }
            }

        });


    }


}
