package skill;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.FItem;
import Util.Utils;
import attack.ChargedAttack;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.rmi.CORBA.Util;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Water1 {
    final public static int manacost = 10;
    public static int cost = 4;
    public static int levelreq = 1;
    public static String name = ChatColor.GRAY + ChatColor.ITALIC.toString() + "Nhất Thức: Thủy Diện Trảm";
    public static List<String> desc = Arrays.asList(new String[]{ChatColor.GRAY+"Gây sát thương lên kẻ địch vừa trúng đòn đánh thường trước đó."
            ,ChatColor.GRAY+"Khi tập trung tuyệt đối sát thương x3"});

    public static double cooldown = 1;
    public static double cooldownr = 0.01  ;
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static HashMap<UUID, ArrayList<LivingEntity>> map = new HashMap<>();


    public static void r(Player p) {
        if (map.containsKey(p.getUniqueId())) {
            if(map.get(p.getUniqueId()).size() == 0) return;
            AtomicBoolean empty = new AtomicBoolean(true);
            map.get(p.getUniqueId()).stream().forEach(en ->{
                if(!en.isDead() && en.isValid()) empty.set(false);
            });
            if(empty.get()) return;
            if (!SkillCastEvent.castAble(p, levelreq, cooldown, cooldownr, manacost, lastuse)) return;
            Utils.sendTitleBar(p, "", ChatColor.BOLD + name);
            if (!lastuse.containsKey(p.getUniqueId())) {
                lastuse.put(p.getUniqueId(), 0l);
            } else {
                lastuse.put(p.getUniqueId(), System.currentTimeMillis());

            }
            boolean charged = ChargedAttack.isCharged(p.getUniqueId());
            ChargedAttack.setCharged(p.getUniqueId(), false);
            p.setCooldown(Material.WHITE_GLAZED_TERRACOTTA, (int) ((cooldown - cooldownr * p.getLevel()) * 20));
            ArrayList<LivingEntity> hitted = map.get(p.getUniqueId());
            Location d1 = p.getLocation();
            d1.setPitch(0);
            Vector v = d1.getDirection();
            for(LivingEntity en : hitted){
                if (en.isDead()) return;
                if (en.getWorld() == p.getWorld()) {
                    if (en.getLocation().distance(p.getLocation()) <= 10) {
                        for (double i = 0; i <= Math.PI * 2; i += Math.PI / 18) {
                            double x = 1 * Math.cos(i);
                            double z = 1 * Math.sin(i);
                            p.getWorld().spawnParticle(Particle.WATER_BUBBLE, en.getEyeLocation().clone().add(x, -0.5, z), 0, v.getX(), v.getY(), v.getZ(), 0.5);

//                            p.getWorld().spawnParticle(Particle.WATER_SPLASH, en.getEyeLocation().clone().add(x, 0, z),0);

                        }
                        SatThuong.SpellDamage(p, en, 2);
                        if (charged) {
                            SatThuong.SpellDamage(p, en, 4);
                        }
                        en.getWorld().playSound(en.getLocation(), Sound.ENTITY_BLAZE_HURT, 0.5f, 1);
                        en.getWorld().playSound(en.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1, 1);
                    }
//
                }
            }

            RPGPlayer rpg = RPGPlayerListener.get(p);
            ArrayList<Player> healed = new ArrayList<>();
            Utils.Heal(p, ((p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.1 + rpg.getInt()/5 ) ),true);

            for(Player en : Utils.getNearbyPlayer(p.getLocation(),10)){
                if(Utils.isAlly(en,p)){

                    if(!healed.contains(en)) {
                        Bukkit.getScheduler().runTaskAsynchronously(Main.m,()->{
                            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 18) {
                                double x = 1 * Math.cos(i);
                                double z = 1 * Math.sin(i);
                                p.getWorld().spawnParticle(Particle.WATER_DROP, en.getEyeLocation().clone().add(x, -0.5, z), 0, v.getX(), v.getY(), v.getZ(), 0.5);
                                if(Utils.percentRoll(50))
                                p.getWorld().spawnParticle(Particle.SPELL_WITCH, en.getEyeLocation().clone().add(x, -0.5, z), 0, v.getX(), v.getY(), v.getZ(), 0.5);
//                            p.getWorld().spawnParticle(Particle.WATER_SPLASH, en.getEyeLocation().clone().add(x, 0, z),0);

                            }
                        });
                        if(!en.getUniqueId().equals(p.getUniqueId()))
                        Utils.Heal(en, ((p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05 + rpg.getInt() / 5)), true);
                        healed.add(en);
                    }
                }
            }

            map.remove(p.getUniqueId());
            shineicon(p, false);
        }


    }

    public static void shineicon(Player p, boolean shine) {
        p.getInventory().setItem(0, new FItem(Material.GRAY_GLAZED_TERRACOTTA).setName(name).setAmount(manacost).glow(shine).toItemStack());
    }
}
