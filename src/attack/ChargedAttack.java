package attack;

import Item.ToolList;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ChargedAttack implements Listener {
    static final int chargetime = 20;
    public static HashMap<UUID, Long> playermap = new HashMap<>();
    public static HashMap<UUID, Integer> stackup = new HashMap<>();
    final long betweenclick = 300;
    public HashMap<UUID, Material> itemstorer = new HashMap<>();
    private HashMap<UUID, Thread> threadmap = new HashMap<>();

    public static boolean isCharged(UUID u) {
        if (stackup.containsKey(u)) {
            return stackup.get(u) >= chargetime;
        } else {
            return false;
        }
    }

    public static void setCharged(UUID u, boolean charged) {
        if (charged) {
            stackup.put(u, chargetime);
        } else {
            playermap.remove(u);
            stackup.remove(u);
            Utils.sendActionBar(Bukkit.getPlayer(u), "");
        }
    }

    @EventHandler
    public void holdingrightclick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            Player p = e.getPlayer();
            if (p.getInventory().getItemInMainHand() == null) {
                return;
            }
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
            if(e.getClickedBlock().getType().toString().contains("DOOR") && !p.hasPermission("rpg.admin")) e.setCancelled(true);

            ItemStack is = p.getInventory().getItemInMainHand();
            if (!ToolList.WEAPON.getItemList().contains(is.getType())) {
                return;
            }
            if (itemstorer.containsKey(p.getUniqueId())) {
                if (itemstorer.get(p.getUniqueId()) != is.getType()) {
                    itemstorer.put(p.getUniqueId(), is.getType());
                    return;
                }

            } else {
                itemstorer.put(p.getUniqueId(), is.getType());
            }
            if (!isCharged(p.getUniqueId())) {
                if (playermap.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - playermap.get(p.getUniqueId()) <= betweenclick) {
                        playermap.put(p.getUniqueId(), System.currentTimeMillis());
                        if (stackup.containsKey(p.getUniqueId())) {
                            stackup.put(p.getUniqueId(), stackup.get(p.getUniqueId()) + 1);
                            if (stackup.get(p.getUniqueId()) >= chargetime) {
                                new BukkitRunnable() {
                                    UUID pu = p.getUniqueId();
                                    int count = 0;

                                    @Override
                                    public void run() {
                                        if (count == 0) {
                                            Utils.sendTitleBar(p, "", ChatColor.BOLD + ChatColor.AQUA.toString() + ChatColor.ITALIC.toString() + "Tập Trung Tuyệt Đối");
                                        }
                                        count++;
                                        if (isCharged(pu)) {
                                            ItemStack iz = p.getInventory().getItemInMainHand();
                                            if (itemstorer.containsKey(pu)) {
                                                if (itemstorer.get(pu) != iz.getType()) {
                                                    setCharged(pu, false);
                                                    this.cancel();
                                                }
                                            }
                                            playChargedParticle(p);
                                            if (count % 2 == 0) {
                                                Utils.sendActionBar(p, "§6§l⚡" + Utils.fullMeter("▂", 15, ChatColor.WHITE) + "§6§l⚡");
                                            } else {
                                                Utils.sendActionBar(p, "§6§l⚡" + Utils.fullMeter("▂", 15, ChatColor.RED) + "§6§l⚡");
                                            }
                                        } else {
                                            this.cancel();
                                        }
                                        if (count > 40) {
                                            stackup.remove(pu);
                                            Utils.sendActionBar(p, "");
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimerAsynchronously(Main.m, 0, 5);

                            } else {
                                Utils.sendActionBar(p, Utils.percentMeter(stackup.get(p.getUniqueId()), chargetime, "▂", 15, ChatColor.GRAY, ChatColor.GREEN));
                            }
                        } else {
                            stackup.put(p.getUniqueId(), 0);
                            Utils.sendActionBar(p, Utils.percentMeter(1, chargetime, "▂", 15, ChatColor.GRAY, ChatColor.GREEN));
                        }
                        new BukkitRunnable() {
                            UUID playeruuid = p.getUniqueId();

                            @Override
                            public void run() {
                                if (playermap.containsKey(playeruuid)) {
                                    if (System.currentTimeMillis() - playermap.get(playeruuid) >= betweenclick) {
                                        if (stackup.containsKey(playeruuid) && stackup.get(playeruuid) >= chargetime) {
                                            this.cancel();
                                        } else {
                                            playermap.remove(p.getUniqueId());
                                            Utils.sendActionBar(p, "");
                                            stackup.remove(playeruuid);
                                            this.cancel();
                                        }
                                    }
                                } else {
                                    this.cancel();
                                }

                            }
                        }.runTaskLater(Main.m, 10);
                    } else {
                        playermap.remove(p.getUniqueId());
                        Utils.sendActionBar(p, "");
                        stackup.remove(p.getUniqueId());
                    }
                } else {
                    playermap.put(p.getUniqueId(), System.currentTimeMillis());
                }
            } else {
                if (playermap.containsKey(p.getUniqueId())) {
                    playermap.remove(p.getUniqueId());
                    Utils.sendActionBar(p, "");
                }
            }
        }
    }

    private void playChargedParticle(final Player p) {
        RPGPlayer rpgPlayer = RPGPlayerListener.get(p);
        if (!threadmap.containsKey(p.getUniqueId())) {
            switch (rpgPlayer.getPlayerClass()) {
                case WIND:
                    for (int i = 0; i <= 2; i++)
                        p.getWorld().spawnParticle(Particle.SPELL_MOB, p.getEyeLocation().add(Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                                , Utils.getRandomWithExclusion(-0.5, 0.2, -0.2, 0), Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                        ), 0, 1, 1, 1, 1);

                    break;
                case FIRE:
                    for (int i = 0; i <= 2; i++)
                        p.getWorld().spawnParticle(Particle.FLAME, p.getEyeLocation().add(Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                                , Utils.getRandomWithExclusion(-0.5, 0.2, -0.2, 0), Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                        ), 0);
                    break;
                case THUNDER:
                    p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getEyeLocation().add(Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                            , Utils.getRandomWithExclusion(-0.5, 0.2, -0.2, 0), Utils.getRandomWithExclusion(-0.5, 0.5, -0.2, 0.2)
                    ), 0);
                    break;


            }

        }


    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        playermap.remove(e.getPlayer().getUniqueId());
        stackup.remove(e.getPlayer().getUniqueId());
        itemstorer.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void animate(PlayerAnimationEvent e) {
        if (!(e.getAnimationType() == PlayerAnimationType.ARM_SWING)) return;
        Player p = e.getPlayer();
        if (stackup.containsKey(p.getUniqueId())) {
            stackup.remove(p.getUniqueId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.m, () -> {
                Utils.sendActionBar(e.getPlayer(), "");
            }, 3);

        }
        if (isCharged(e.getPlayer().getUniqueId())) {
            setCharged(e.getPlayer().getUniqueId(), false);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.m, () -> {
                Utils.sendActionBar(e.getPlayer(), "");
            }, 3);

        }

    }

}
