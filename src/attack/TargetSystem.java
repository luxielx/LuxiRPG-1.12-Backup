package attack;

import Luxiel.Main;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class TargetSystem extends Thread {

    UUID u;
    boolean run = false;
    LivingEntity en;

    public TargetSystem(UUID u) {
        if (!Main.targetmap.containsKey(u)) {
            this.u = u;
            run = true;
            this.start();
            Main.targetmap.put(u, this);
        }
    }

    public LivingEntity getTarget() {
        if (!getPlayer().isOnline()) return null;
        return en;
    }

    public boolean hasTarget() {
        if (!getPlayer().isOnline()) return false;
        return en != null;
    }

    public void StopThisThread() {
        run = false;
        Main.targetmap.remove(u);
    }

    public UUID getPlayerUUID() {
        return this.u;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.u);
    }

    public boolean isRunning() {
        return run;
    }

    public void run() {
        while (run) {
            try {
                if (Bukkit.getPlayer(u) == null) {
                    run = false;
                    return;
                }
                if (!Bukkit.getPlayer(u).isOnline()) {
                    run = false;
                    return;
                }
                Player p = Bukkit.getPlayer(u);
                HashMap<LivingEntity, Float> entityangles = new HashMap<>();
                for (Entity en : p.getNearbyEntities(30, 30, 30)) {
                    if (en.hasMetadata("seat")) continue;
                    if (en instanceof LivingEntity) {
                        if (en instanceof ArmorStand) continue;
                        if (((LivingEntity) en).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            if (en.getLocation().distance(p.getLocation()) >= 5) continue;
                        }
                        Vector playerLookDir = p.getEyeLocation().getDirection();
                        Vector playerEyeLoc = p.getEyeLocation().toVector();
                        Location asd = en.getLocation();
                        Location asdd = ((LivingEntity) en).getEyeLocation();
                        asd.setY((asd.getY() + asdd.getY()) / 2);
                        Vector entityLoc = asd.toVector();
                        Vector playerEntityVec = entityLoc.subtract(playerEyeLoc);
                        float angle = playerLookDir.angle(playerEntityVec);
                        double dis = p.getLocation().distance(en.getLocation());
                        if (angle <= 0.3f * 5 / dis) {
                            if (p.hasLineOfSight(en))
                                entityangles.put((LivingEntity) en, angle);
                        }
                    }
                }
                LivingEntity target = null;
                if (entityangles.keySet().size() >= 0) {
                    for (LivingEntity ez : entityangles.keySet()) {
                        if (target != null) {
                            if (entityangles.get(target) >= entityangles.get(ez)) {
                                target = ez;
                            }
                        } else {
                            target = ez;
                        }

                    }
                }
                if (target != null) {
                    Utils.setGlowing(target, p, true);
                    Utils.setHealthBar(p, target, true);
                    if (en != null) {
                        if (en.getUniqueId() != target.getUniqueId()) {
                            Utils.setGlowing(en, p, false);
                            Utils.setHealthBar(p, en, false);

                        }
                    }
                    en = target;
                } else {
                    if (en != null) {
                        Utils.setGlowing(en, p, false);
                        Utils.setHealthBar(p, en, false);
                        en = null;
                    }

                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }

        }
    }


}
