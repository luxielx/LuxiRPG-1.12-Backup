package Util;

import Luxiel.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Sit implements CommandExecutor, Listener {



    public static boolean isSitting(Player p) {
        if (p.isInsideVehicle()) {
            return p.getVehicle() instanceof ArmorStand;
        }
        return false;
    }
    public static HashMap<UUID, Long> lastuse = new HashMap<>();
    public static void setSitting(Player p, boolean sit) {
        if (sit) {
            if (isSitting(p)) return;
            if(!p.isOnGround()) return;
            Location loc = p.getLocation();
            if(lastuse.containsKey(p.getUniqueId()))
            if (System.currentTimeMillis() - lastuse.get(p.getUniqueId()) <= 5 * 1000) {
                p.sendMessage(ChatColor.RED+"Bạn cần đợi để thiền tiếp");
                return;
            }
            if (!lastuse.containsKey(p.getUniqueId())) {
                lastuse.put(p.getUniqueId(), 0l);
            } else {
                lastuse.put(p.getUniqueId(), System.currentTimeMillis());
            }
            boolean enemynearby = false;
            for(LivingEntity e : Utils.getNearbyLivingEntities(p.getLocation(),10)){
                if(e.getType() != EntityType.PLAYER) {
                    enemynearby = true;
                    break;
                }

            }
            if(enemynearby){
                p.sendMessage(ChatColor.RED+"Không thể thiền khi gần quái vật");
                return;
            }
            ArmorStand seat = (ArmorStand) loc.getWorld().spawn(loc.clone(), (Class) ArmorStand.class);
            seat.setMetadata("seat", new FixedMetadataValue(Main.m, true));
            seat.setVisible(false);
            seat.setGravity(false);
            seat.setMarker(true);
            seat.addPassenger(p);
            Thread colorth = new Thread() {
                int i = 0;
                boolean run = true;
                Color color = Color.fromRGB(ThreadLocalRandom.current().nextInt(100, 255),
                        ThreadLocalRandom.current().nextInt(100, 255),
                        ThreadLocalRandom.current().nextInt(100, 255));

                public void run() {
                    while (run) {
                        if (!seat.isValid()) run = false;
                        if (seat.getPassengers().size() == 0) {
                            Bukkit.getScheduler().runTask(Main.m, () -> {
                                seat.remove();
                            });
                            run = false;
                        } else {
                            if (seat.getPassengers().get(0) instanceof Player) {
                                Player z = (Player) seat.getPassengers().get(0);
                                if (!z.isOnline()) {
                                    Bukkit.getScheduler().runTask(Main.m, () -> {
                                        seat.remove();
                                    });
                                    run = false;
                                }
                            }


                        }
                        double x = 2 * Math.cos(i * 0.1);
                        double z = 2 * Math.sin(i * 0.1);
                        if (i % 35 == 0) {
                            color = Color.fromRGB(ThreadLocalRandom.current().nextInt(100, 255),
                                    ThreadLocalRandom.current().nextInt(100, 255),
                                    ThreadLocalRandom.current().nextInt(100, 255));

                        }
                        Utils.coloredRedstone(loc.clone().add(x, 0.1, z), color);
                        i++;
                        try {
                            Thread.sleep(10l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            colorth.start();

        } else {
            if (!isSitting(p)) return;
            if (!lastuse.containsKey(p.getUniqueId())) {
                lastuse.put(p.getUniqueId(), 0l);
            } else {
                lastuse.put(p.getUniqueId(), System.currentTimeMillis());
            }
            removeSeat(p);
        }

    }

    public static void removeSeat(Player p) {
        if (isSitting(p)) {
            Entity seat = p.getVehicle();
            seat.remove();
        }
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        setSitting(p, !isSitting(p));
        return true;
    }


}
