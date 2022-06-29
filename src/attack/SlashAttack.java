package attack;

import ChucNghiep.Classes;
import ChucNghiep.Races;
import Item.ItemStats;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Luxiel.SatThuong;
import Util.FNum;
import Util.Sit;
import Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import skill.ThunderCharged;
import skill.Water1;
import skill.WindCharged;
import stat.StatPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SlashAttack implements Listener {
    public static final NamespacedKey key = new NamespacedKey(Main.m, "solarslashcolor");
    public static HashMap<String, Long> wtf = new HashMap<String, Long>();

    public static void slash(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            RPGPlayer rpgPlayer = RPGPlayerListener.get(p);
            ItemStack i = p.getInventory().getItemInMainHand();
            Vector vec = p.getEyeLocation().getDirection().normalize().multiply(0.5);
            Location loc = p.getEyeLocation();
//            p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 0f);
            ArrayList<LivingEntity> damaged = new ArrayList<>();
//            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
            boolean crit = false;
            if (FNum.randomDouble(0, 100) <= StatPlayer.chimang(p)) {
                crit = true;
            }
            loc.add(vec);
            double angle = Math.toRadians(p.getLocation().getYaw());
            boolean finalCrit = crit;
            Color clo = Color.fromRGB(254, 255, 255);
            Color finalClo = clo;
            double i2 = angle + Math.PI - Math.toRadians(20);
            final double radius = 2.5;
            boolean typ = true;
//            double y = Utils.getRandomWithExclusion(-2, 2, -0.3, 0.3);
            double y = ThreadLocalRandom.current().nextDouble(-2, 2);
            double plus = -y / 18;
            Location loz = p.getEyeLocation();
            Location loz2 = loz.clone();
            loz2.setPitch(0);
            Vector v = loz2.getDirection();
            double critint = 1;
            if (Utils.percentRoll(50)) typ = false;
            for (double asd = angle + Math.toRadians(20); asd <= Math.PI + angle - Math.toRadians(20); asd += 0.05) {
                double x, z;
                if (typ) {
                    x = radius * Math.cos(asd);
                    z = radius * Math.sin(asd);
                } else {
                    x = radius * Math.cos(i2);
                    z = radius * Math.sin(i2);
                }
                y += plus;
                Utils.coloredRedstone(loz.clone().add(x, y, z), finalClo);
                if (finalCrit) {
                    switch (rpgPlayer.getPlayerClass()) {
                        case WIND:
                            p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.clone().add(x, y, z), 0, v.getX(), v.getY(), v.getZ(), 0.1);
                            break;
                        case WATER:
                            p.getWorld().spawnParticle(Particle.WATER_BUBBLE, loc.clone().add(x, y, z), 0, v.getX(), v.getY(), v.getZ(), 0.3);
                            break;
                        case FIRE:
                            p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x, y, z), 0, v.getX(), v.getY(), v.getZ(), 0.1);
                            break;
                        case THUNDER:
                            p.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc.clone().add(x, y, z), 0);
                            break;
                    }
                    critint = StatPlayer.chimang(p);
                }
                double finalCritint = critint;
                double finalY = y;
                Bukkit.getScheduler().runTask(Main.m, () -> {
                    for (LivingEntity en : Utils.getNearbyLivingEntities(loz.clone().add(x, finalY, z), 1.3)) {
                        if (en instanceof LivingEntity) {
                            if (Utils.damageable(en, p)) {
                                if (!damaged.contains(en)) {
                                    SatThuong.damage(p,en);
//                                    Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(p, en, EntityDamageEvent.DamageCause.CUSTOM, finalCritint));

                                    damaged.add(en);
                                }

                            }
                        }
                        if (!damaged.isEmpty())
                            if (rpgPlayer.getPlayerClass() == Classes.WATER) {
                                Water1.map.put(p.getUniqueId(), damaged);
                                Water1.shineicon(p, true);
                            }

                    }
                });


                i2 -= 0.05;
            }
            damaged.clear();

        });


    }

    @EventHandler
    public void cast(PlayerAnimationEvent e) {
        if (!(e.getAnimationType() == PlayerAnimationType.ARM_SWING)) return;
        Player p = e.getPlayer();
        if (Sit.isSitting(p)) return;
        RPGPlayer rpgPlayer = RPGPlayerListener.get(p);
        ItemStack i = p.getInventory().getItemInMainHand();
        if (rpgPlayer.getPlayerClass().getRace() != Races.HUMAN) return;
        if (!ItemStats.getRace(i).contains(Races.HUMAN)) return;
        if (ChargedAttack.isCharged(p.getUniqueId())) {
            if (rpgPlayer.getPlayerClass() == Classes.THUNDER) {
                ThunderCharged.r(p);
            } else if (rpgPlayer.getPlayerClass() == Classes.WIND) {
                WindCharged.r(p);
            } else if (rpgPlayer.getPlayerClass() == Classes.WATER) {
                Utils.Attack(p);
                Utils.Attack(p);
            } else {
                return;
            }
        }
        if (wtf.containsKey(p.getUniqueId().toString())) {
            long last_ms = wtf.get(p.getUniqueId().toString());
            long curr_ms = System.currentTimeMillis();
            long distance = curr_ms - last_ms;
            long time = 1000 - (StatPlayer.attackspeed(p) * 10 );
            if (distance < time) return;
        }
        wtf.put(p.getUniqueId().toString(), System.currentTimeMillis());
        int cd = (int) ((1000 - (StatPlayer.attackspeed(p) * 10)) / 100 *2);
        if (cd > 0) {
            p.setCooldown(p.getInventory().getItemInMainHand().getType(), cd);
        }
        int lvreq = ItemStats.LEVEL.getStats(i);
        if (e.getPlayer().getLevel() < lvreq) {
            e.setCancelled(true);
            return;
        }
        if (e.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            slash(p);
        }
    }
}
