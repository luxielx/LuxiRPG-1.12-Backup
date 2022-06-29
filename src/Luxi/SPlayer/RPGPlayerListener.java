package Luxi.SPlayer;

import Luxiel.Main;
import ThreadManager.ManaRegen;
import Util.Sit;
import attack.TargetSystem;
import guild.Guild;
import guild.GuildUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static skill.Wind1.usetime;

public class RPGPlayerListener implements Listener {

    public static ConcurrentHashMap<String, RPGPlayer> data = new ConcurrentHashMap<String, RPGPlayer>();

    public RPGPlayerListener() {
        for (Player pi : Bukkit.getOnlinePlayers()) {
            if (!data.containsKey(pi.getUniqueId().toString())) {
                RPGPlayer sp = new RPGPlayer(pi);
                sp.loadFromFile();
                data.put(pi.getUniqueId().toString(), sp);
//				sp.setLucChien(StatPlayer.dmg_canchien(pi) + StatPlayer.dmg_vientrinh(pi));
                sp.saveToFile();
            }
        }
    }

    public static RPGPlayer get(Player p) {
        RPGPlayer sp = data.get(p.getUniqueId().toString());
        if (sp == null) {
            sp = new RPGPlayer(p);
            sp.loadFromFile();
            data.put(p.getUniqueId().toString(), sp);
        }
        return sp;
    }

    public static RPGPlayer get(String uuid) {
        RPGPlayer sp = data.get(uuid);
        if (sp == null) {
            sp = new RPGPlayer(uuid);
            sp.loadFromFile();
        }
        if (!data.containsKey(uuid)) data.put(uuid, sp);
        return sp;
    }

    public static void put(String uuid, RPGPlayer rpgPlayer) {
        data.put(uuid, rpgPlayer);

    }


    @EventHandler
    public void join(PlayerJoinEvent e) {
        RPGPlayer sp = new RPGPlayer(e.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, new Runnable() {
            @Override
            public void run() {
                if (sp.isExist()) {
                    sp.loadFromFile();
                } else {
                    sp.saveToFile();
                    sp.loadFromFile();
                }
                if (sp.getGuild() != null) {
                    Guild g = GuildUtils.getGuild(sp.getGuild().getName());
                    if (!g.members.contains(UUID.fromString(sp.uuid))) {
                        g.removeMember(UUID.fromString(sp.uuid));
                        sp.setGuild(null);
                    }
                }
                data.put(e.getPlayer().getUniqueId().toString(), sp);
            }
        });

        if (Sit.isSitting(e.getPlayer())) Sit.setSitting(e.getPlayer(), false);
//        new StaminaRegen(e.getPlayer());
        new ManaRegen(e.getPlayer());
//        new ScoreboardThread(e.getPlayer());
        new TargetSystem(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        if (Sit.isSitting(e.getPlayer())) Sit.setSitting(e.getPlayer(), false);
        usetime.remove(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, new Runnable() {
            @Override
            public void run() {
                try {
                    RPGPlayer sp = data.get(e.getPlayer().getUniqueId().toString());
                    Guild g = GuildUtils.getGuild(sp.getGuild().getName());
                    if (!g.members.contains(UUID.fromString(sp.uuid))) {
                        g.removeMember(UUID.fromString(sp.uuid));
                        sp.setGuild(null);
                    }
                    sp.saveToFile();
                    data.remove(e.getPlayer().getUniqueId().toString());
                    if (sp.getGuild() != null)
                        if (!sp.getGuild().members.contains(UUID.fromString(sp.uuid)))
                            sp.getGuild().removeMember(UUID.fromString(sp.uuid));
                    for (Thread s : Thread.getAllStackTraces().keySet()) {
                        if (s instanceof TargetSystem) {
                            if (((TargetSystem) s).getPlayerUUID() == e.getPlayer().getUniqueId())
                                ((TargetSystem) s).StopThisThread();
                        }
                    }
                } catch (Exception e) {

                }

            }
        });

    }

    @EventHandler
    public void anvil(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void anvil(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.WORKBENCH) {
                e.setCancelled(true);
            }
        }
    }
}
