package Luxiel;

import Item.ItemStats;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Util.Utils;
import attack.ChargedAttack;
import guild.Familia;
import guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import stat.StatPlayer;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SatThuong implements Listener {
    HashMap<String, Long> wtf = new HashMap<String, Long>();

    public static void drawMeleeParticle(LivingEntity le, int number, Particle par) {
        double x, z;
        x = ThreadLocalRandom.current().nextDouble(-2, 2);
        z = ThreadLocalRandom.current().nextDouble(-2, 2);
        switch (number) {
            case (1):
                Utils.drawLineParticle(le.getLocation().add(x, 2, x), le.getLocation().add(-x, 0.3, -x), par, 10, false);
                break;
            case (2):
                Utils.drawLineParticle(le.getLocation().add(z, 2, z), le.getLocation().add(-z, 0.3, -z), par, 10, false);
                break;
            case (3):
                Utils.drawLineParticle(le.getLocation().add(-x, 2, -z), le.getLocation().add(x, 0.3, z), par, 10, false);
                break;
            case (4):
                Utils.drawLineParticle(le.getLocation().add(-z, 2, -x), le.getLocation().add(z, 0.3, x), par, 10, false);
                break;
        }
    }

    //    public static void drawSlash(LivingEntity le) {
//        drawMeleeParticle(le, ThreadLocalRandom.current().nextInt(1, 4), Particle.REDSTONE);
//    }
//
//    public static void drawThrust(LivingEntity le) {
//        drawMeleeParticle(le, ThreadLocalRandom.current().nextInt(1, 4), Particle.REDSTONE);
//        drawMeleeParticle(le, ThreadLocalRandom.current().nextInt(1, 4), Particle.REDSTONE);
//    }
    public static double CalcTotalDamage(double itemdamage, double playerstat, double critdamage, double modify, double enemydefense) {
        return ((itemdamage + playerstat ) * critdamage * modify * (100 / (100 + enemydefense)) + 1);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Damage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                e.setCancelled(true);
            LivingEntity en = (LivingEntity) e.getEntity();
            if (en.hasPotionEffect(PotionEffectType.LUCK)) {
                e.setCancelled(true);
            }
        }
    }

    public static void damage(Player p , LivingEntity victim, double damage){


    }

    public static void SpellDamage(Player p, LivingEntity victim, double damage) {

        RPGPlayer rpgp = RPGPlayerListener.get(p);
        double str = StatPlayer.sTr(p);
        double intel = StatPlayer.inTel(p);
        double finaldamage = 1 + (damage * (intel*1.5 + 1) + (((str + 1) / 4) * damage));
        if (rpgp.getGuild() != null) {
            if (rpgp.getGuild().getFamilia() == Familia.HISAME) {
                finaldamage *= 1.2;
            }
        }
        double finalDamage = finaldamage;
//        Bukkit.getScheduler().callSyncMethod(Main.m, () -> {
//            Bukkit.getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(p, victim, EntityDamageEvent.DamageCause.CRAMMING, finalDamage));
//            return null;
//        });
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void Damage(EntityDamageByEntityEvent e) {
        // mob attack
        if (!(e.getDamager() instanceof Player) && e.getDamager() instanceof LivingEntity) {
            if (e.getEntity() instanceof Player) {
                double enemydefense = StatPlayer.giap((Player) e.getEntity());
                Player p = (Player) e.getEntity();
                RPGPlayer rpgp = RPGPlayerListener.get(p);
                if (rpgp.getGuild() != null) {
                    Guild g = rpgp.getGuild();
                    if (g.getFamilia() == Familia.AKUMA) enemydefense *= 1.2;

                }
                double damage = e.getDamage() * (100 / (100 + enemydefense));
                e.setDamage(damage);
                damageIndiCate((LivingEntity) e.getDamager(), p, damage, true, false);

            }

        }
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (e.getDamager() instanceof Player && e.getDamage() % 1 == 0) {
                e.setCancelled(true);
            }
        }
        // MAGIC
        if (e.getCause() == EntityDamageEvent.DamageCause.CRAMMING) {
            if (!(e.getDamager() instanceof Player)) return;
            Player p = (Player) e.getDamager();
            double damage = e.getDamage();
            damageIndiCate(p, (LivingEntity) e.getEntity(), damage, false, true);
            e.setDamage(0);
            ((LivingEntity) e.getEntity()).damage(damage + 0.1, p);
            ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
            // NORMAL ATACK
        } else if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            if ((e.getDamager() instanceof Player || e.getDamager() instanceof Arrow) && e.getEntity() instanceof LivingEntity) {
                Player p = null;
                double itemst = 0;
                double playerstrorint = 0;
                double critchance = 0;
                double critdamage = 1;
                double othermodify = 1;
                boolean ismagical = false;
                boolean ishunter = false;
                boolean isCharged = false;
                double enemydefense = 0;
                if (e.getDamager() instanceof Arrow) ishunter = true;
                if (ishunter) {
                    if (((Arrow) e.getDamager()).getShooter() instanceof Player)
                        p = (Player) ((Arrow) e.getDamager()).getShooter();
                } else {
                    p = (Player) e.getDamager();
                    if (e.getCause() != EntityDamageEvent.DamageCause.CUSTOM) return;
                }
                critchance = StatPlayer.chimang(p);
                critdamage = StatPlayer.satthuongchimang(p);
                isCharged = ChargedAttack.isCharged(p.getUniqueId());
                if (!Utils.damageable(e.getEntity(), p)) {
                    return;
                }
                if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() < 1024) {
                    p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024);
                }
                if (p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() > 1) {
                    p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                }

                if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                    playerstrorint = StatPlayer.sTr(p);
                    if (e.getDamage() >= 2) {
                        critchance = e.getDamage();
                    } else {
                        critdamage = 1;
                    }
                } else if (ishunter) {
                    Projectile pr = (Projectile) e.getDamager();
                    if (pr.hasMetadata("arrowdamage")) {
                        itemst = pr.getMetadata("arrowdamage").get(0).asDouble();
                    }
                    if (pr.hasMetadata("arrowstr")) {
                        playerstrorint = pr.getMetadata("arrowstr").get(0).asDouble();
                    }
                    if (pr.hasMetadata("arrowcrit")) {
                        critdamage = pr.getMetadata("arrowcrit").get(0).asDouble();
                    }

                }
                if (!ishunter) {

                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (!ItemStats.getClasses(is).contains(RPGPlayerListener.get(p).getPlayerClass())) {
                        ((LivingEntity) e.getEntity()).damage(CalcTotalDamage(0, 0, critdamage, othermodify, 0));
                        ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
                        return;
                    }
                    itemst = ItemStats.DAMAGE.getStats(is);
                    if (wtf.containsKey(p.getUniqueId().toString())) {
                        long last_ms = wtf.get(p.getUniqueId().toString());
                        long curr_ms = System.currentTimeMillis();
                        long distance = curr_ms - last_ms;
                        long time = 1000 - (StatPlayer.attackspeed(p) * 10);
                        if (distance < time) {
                            return;
                        }
                    }

                    wtf.put(p.getUniqueId().toString(), System.currentTimeMillis());
                    int cd = (int) ((1000 - (StatPlayer.attackspeed(p) * 10)) / 100 * 2);
                    if (cd > 0) {
                        p.setCooldown(p.getInventory().getItemInMainHand().getType(), cd);
                    }

                    if (e.getEntity() instanceof Player) {
                        Player zz = (Player) e.getEntity();
                        enemydefense = StatPlayer.giap(zz);
                        RPGPlayer rpgp = RPGPlayerListener.get(zz);
                        if (rpgp.getGuild() != null) {
                            Guild g = rpgp.getGuild();
                            if (g.getFamilia() == Familia.AKUMA) enemydefense *= 1.20;

                        }


                    }

                    if (!Utils.percentRoll(critchance) && !ismagical) {
                        critdamage = 1;
                    }
                }
                if (ishunter && ismagical) {
                    if (critdamage > 2) critdamage = 2;
                }
                if (isCharged) {
                    critdamage *= 2;
                }

                RPGPlayer rpgp = RPGPlayerListener.get(p);
                if (rpgp.getGuild() != null) {
                    Guild g = rpgp.getGuild();
                    if (g.getFamilia() == Familia.AKUMA) e.getEntity().setFireTicks(40);
                    if (g.getFamilia() == Familia.KAMINARI) {
                        Vector attackerDirection = p.getLocation().getDirection();
                        Vector victimDirection = e.getEntity().getLocation().getDirection();
                        if (attackerDirection.dot(victimDirection) > 0) {
                            othermodify *= 1.2;
                        }
                    }
                    if (g.getFamilia() == Familia.RAIJIN) {
                        enemydefense -= (enemydefense * (15 / 100));
                    }
                }

                double totaldamage = CalcTotalDamage(itemst, playerstrorint, critdamage, othermodify, enemydefense);
                damageIndiCate(p, (LivingEntity) e.getEntity(), totaldamage, critdamage >= 2, false);
                e.setDamage(0);
                ((LivingEntity) e.getEntity()).damage(totaldamage + 0.1, p);
                ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
                if (e.getEntity().isDead())
                    e.getEntity().setLastDamageCause(e);

            }

        }
    }

    private void damageIndiCate(LivingEntity p, LivingEntity e, double totaldamage, boolean crit, boolean spell) {
        String color = ChatColor.GREEN.toString();
        if (crit) color = ChatColor.YELLOW.toString();
        if (spell) color = ChatColor.AQUA.toString();
        Utils.spawnHologram(Utils.getNearbyPlayer(p.getLocation(), 20), ChatColor.GRAY.toString() + "-" + color + ((int) totaldamage) + "", e.getEyeLocation()
                .add(Utils.getRandomWithExclusion(-.7, .7, -0.4, 0.4),
                        ThreadLocalRandom.current().nextDouble(-0.8, .5),
                        Utils.getRandomWithExclusion(-.7, .7, -0.4, 0.4)), 10);
    }


}

