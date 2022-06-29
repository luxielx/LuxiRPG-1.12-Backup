package ThreadManager;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Util.Sit;
import guild.Familia;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import stat.StatPlayer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ManaRegen extends Thread {
    public static HashMap<UUID, Integer> ManaMap = new HashMap<>();
    Player p;
    boolean run = false;

    public ManaRegen(Player p) {
        this.p = p;
        run = true;
        if (!ManaMap.containsKey(p.getUniqueId())) {
            ManaMap.put(p.getUniqueId(), StatPlayer.MaxMana(p));
        }
        for (Thread s : Thread.getAllStackTraces().keySet()) {
            if (s instanceof ManaRegen) {
                if (((ManaRegen) s).getPlayer().getUniqueId().equals(p.getUniqueId())) {
                    run = false;
                }
            }
        }
        if (run)
            this.start();
    }

    public static int getMana(Player p) {
        if (!ManaMap.containsKey(p.getUniqueId())) {
            ManaMap.put(p.getUniqueId(), StatPlayer.MaxMana(p));
        }
        return ManaMap.get(p.getUniqueId());
    }

    public static int setMana(Player p, int mana) {
        if (!ManaMap.containsKey(p.getUniqueId())) {
            ManaMap.put(p.getUniqueId(), mana);
        }
        return ManaMap.put(p.getUniqueId(), mana);
    }

    public Player getPlayer() {
        return this.p;
    }

    public void run() {
        while (run) {
            try {
                if (p == null) {
                    run = false;
                    return;
                }
                if (!p.isOnline()) {
                    run = false;
                    return;
                }
                int mana = getMana(p);
                if (mana > StatPlayer.MaxMana(p)) {
                    setMana(p, StatPlayer.MaxMana(p));
                }
                if (mana + (1) < StatPlayer.MaxMana(p)) {
                    setMana(p, mana + (1));
                } else {
                    setMana(p, StatPlayer.MaxMana(p));
                }
                long time = 1000l;
                // 1000 800
                RPGPlayer rpg = RPGPlayerListener.get(p);
                if (rpg.getGuild() != null) {
                    if (rpg.getGuild().getFamilia() == Familia.HISAME) ;
                    time /= 1.25;
                }
                if (Sit.isSitting(p)) {
                    createManaRegenParticle(p);
                    time /= 5;
                }

                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }

        }
    }

    private void createManaRegenParticle(Player p) {
        p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getEyeLocation().add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
                , ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
                , ThreadLocalRandom.current().nextDouble(-0.5, 0.5)), 0);
    }
}
