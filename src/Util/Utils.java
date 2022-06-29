package Util;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Parties.PartyUtils;
import attack.SlashAttack;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import guild.Familia;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import stat.StatPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by ADMIN on 8/14/2018.
 */
public class Utils {
    static HashMap<UUID, EntityArmorStand> armorlist = new HashMap<>();

    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static final Vector rotateAroundAxisZ(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public static void drawLineParticle(Location location, Location target, Particle particle, int particles, boolean isZigzag) {
        Vector zigZagOffset = new Vector(0, 0.1, 0);
        int zigZags = 10;
        int step = 0;
        boolean zag = false;
        double amount = particles / zigZags;
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        float ratio = length / particles;
        link.normalize();
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            if (isZigzag) {
                if (zag) {
                    loc.add(zigZagOffset);
                } else {
                    loc.subtract(zigZagOffset);
                }
            }
            if (step >= amount) {
                zag = !zag;
                step = 0;
            }
            step++;
            loc.add(v);
            if (particle == Particle.REDSTONE) {
                coloredRedstone(loc, 255, 255, 255);
            } else
                location.getWorld().spawnParticle(particle, loc, 0);
        }
    }

    public static void drawRedStoneLineParticle(Location location, Location target, int particles, int red, int green, int blue, float size, boolean isZigzag) {
        Particle particle = Particle.REDSTONE;
        Vector zigZagOffset = new Vector(0, 0.1, 0);
        int zigZags = 10;
        int step = 0;
        boolean zag = false;
        double amount = particles / zigZags;
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        float ratio = length / particles;
        link.normalize();
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            if (isZigzag) {
                if (zag) {
                    loc.add(zigZagOffset);
                } else {
                    loc.subtract(zigZagOffset);
                }
            }
            if (step >= amount) {
                zag = !zag;
                step = 0;
            }
            step++;
            loc.add(v);
            coloredRedstone(loc, red, green, blue);
        }
    }

    public static boolean damageable(Entity victim, Player damager) {
        if (isAlly(damager, victim)) return false;
        if (victim.hasMetadata("NPC")) return false;
        if (victim instanceof ArmorStand) return false;
        if (victim instanceof LivingEntity) {
            if (victim instanceof Player) {
                Player vc = (Player) victim;
                if(Utils.isAlly(vc,damager))
                    return false;
                if(Math.abs(vc.getLevel() - damager.getLevel()) > 5){
                    Utils.sendActionBar(damager ,ChatColor.RED +"Không thể pvp người chơi cách bạn 5 cấp" );
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlly(Player player, Entity player2) {
        if (!(player2 instanceof Player)) return false;
        if (player == player2) return true;
        return PartyUtils.isSameParty(player.getUniqueId(), player2.getUniqueId());
    }

    public static ArrayList<LivingEntity> getNearbyLivingEntities(Location location, double i) {
        ArrayList<LivingEntity> lz = new ArrayList<>();
        try {
            for (Entity e : location.getWorld().getNearbyEntities(location, i, i, i)) {
                if (e instanceof LivingEntity) {
                    lz.add((LivingEntity) e);
                }
            }
            return lz;
        } catch (Exception e) {
            return lz;
        }
    }

    public static ChatColor getRandomChatColor() {
        ArrayList<ChatColor> list = new ArrayList<>();
        for (ChatColor c : ChatColor.values()) {
            if (c != ChatColor.MAGIC && c != ChatColor.STRIKETHROUGH && c != ChatColor.BOLD && c != ChatColor.ITALIC && c != ChatColor.UNDERLINE) {
                list.add(c);
            }
        }
        return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
    }

    public static Color getColor(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

    public static ItemStack getRandomItemStackFromArray(ArrayList<ItemStack> array) {
        try {
            return array.get(ThreadLocalRandom.current().nextInt(array.size()));
        } catch (Throwable e) {
            return null;
        }
    }

    public static ArrayList<Player> getNearbyPlayer(Location player, double i) {
        ArrayList<Player> list = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        for (Entity online : player.getWorld().getNearbyEntities(player, i, i, i)) {
            if (online instanceof Player) {
                list.add((Player) online);
            }
        }
        return list;
    }

    public static ArrayList<Player> getNearbyPlayerAsync(Location player, double radius) {
        World world = player.getWorld();
        ArrayList<Player> list = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.getWorld().getName().equalsIgnoreCase(world.getName())) {
                if (online.getLocation().distance(player) > radius) {
                    list.remove(online);
                }
            }
        }
        return list;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static String hideS(String s) {
        String hidden = "";
        for (char c : s.toCharArray())
            hidden += ChatColor.COLOR_CHAR + "" + c;
        return hidden;
    }

    public static String unhideS(String s) {
        String r = s.replaceAll("§", "");
        return r;
    }

    public static String colorCode(String s) {
        return s.replaceAll("&", "§");
    }

    public static String convertToRoman(int mInt) {
        String[] rnChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] rnVals = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
        String retVal = "";

        for (int i = 0; i < rnVals.length; i++) {
            int numberInPlace = mInt / rnVals[i];
            if (numberInPlace == 0)
                continue;
            retVal += numberInPlace == 4 && i > 0 ? rnChars[i] + rnChars[i - 1]
                    : new String(new char[numberInPlace]).replace("\0", rnChars[i]);
            mInt = mInt % rnVals[i];
        }
        return retVal;
    }

    public static int romanConvert(String roman) {
        int decimal = 0;

        String romanNumeral = roman.toUpperCase();
        for (int x = 0; x < romanNumeral.length(); x++) {
            char convertToDecimal = roman.charAt(x);

            switch (convertToDecimal) {
                case 'M':
                    decimal += 1000;
                    break;

                case 'D':
                    decimal += 500;
                    break;

                case 'C':
                    decimal += 100;
                    break;

                case 'L':
                    decimal += 50;
                    break;

                case 'X':
                    decimal += 10;
                    break;

                case 'V':
                    decimal += 5;
                    break;

                case 'I':
                    decimal += 1;
                    break;
            }
        }
        if (romanNumeral.contains("IV")) {
            decimal -= 2;
        }
        if (romanNumeral.contains("IX")) {
            decimal -= 2;
        }
        if (romanNumeral.contains("XL")) {
            decimal -= 10;
        }
        if (romanNumeral.contains("XC")) {
            decimal -= 10;
        }
        if (romanNumeral.contains("CD")) {
            decimal -= 100;
        }
        if (romanNumeral.contains("CM")) {
            decimal -= 100;
        }
        return decimal;
    }

    public static RPGPlayer getRpgPlayer(UUID p) {
        return RPGPlayerListener.get(p.toString());
    }

    public static String percentMeter(double value, double maxvalue, String chars, int length, ChatColor firstcolor, ChatColor secondcolor) {
        StringBuilder str = new StringBuilder();
        int percent = (int) (Math.round((value / maxvalue) * 100));
        boolean addColor = true;
        for (int counter = 0; counter < length; counter++) {
            if (counter >= (percent / (100 / length)) && addColor) {
                str.append(firstcolor);
                addColor = false;
            }
            str.append(chars);

        }
        String pers = secondcolor + str.toString() + ChatColor.RED + " " + (percent) + ChatColor.YELLOW + "%";
        return pers;
    }

    public static String HealthBarpercentMeter(double value, double maxvalue, String chars, int length, ChatColor firstcolor, ChatColor secondcolor, double health) {
        StringBuilder str = new StringBuilder();
        int percent = (int) (Math.round((value / maxvalue) * 100));
        boolean addColor = true;
        for (int counter = 0; counter < length; counter++) {
            if (counter == length / 2) {
                str.append((int) Math.round(health));
            }
            if (counter >= (percent / (100 / length)) && addColor) {
                str.append(firstcolor);
                addColor = false;
            }
            str.append(chars);
        }
        String pers = secondcolor + str.toString();
        return pers;
    }

    public static String fullMeter(String chars, int length, ChatColor color) {
        double value = 1;
        double maxvalue = 1;
        StringBuilder str = new StringBuilder();
        int percent = (int) (Math.round((value / maxvalue) * 100));
        boolean addColor = true;
        for (int counter = 0; counter < length; counter++) {
            if (counter >= (percent / (100 / length)) && addColor) {
                str.append(color);
                addColor = false;
            }
            str.append(chars);

        }
        String pers = color + str.toString();
        return pers;
    }

    public static String fullMeterWithPercent(String chars, int length, ChatColor color) {
        double value = 1;
        double maxvalue = 1;
        StringBuilder str = new StringBuilder();
        int percent = (int) (Math.round((value / maxvalue) * 100));
        boolean addColor = true;
        for (int counter = 0; counter < length; counter++) {
            if (counter >= (percent / (100 / length)) && addColor) {
                str.append(color);
                addColor = false;
            }
            str.append(chars);

        }
        String pers = color + str.toString() + ChatColor.RED + " " + (percent);
        return pers;
    }

    public static boolean percentRoll(int percent) {
        return (ThreadLocalRandom.current().nextInt(0, 100) < percent);
    }

    public static boolean percentRoll(double percent) {
        return (ThreadLocalRandom.current().nextDouble(0, 100) < percent);
    }

    public static boolean percentRoll(int percent, int maxpercent) {

        return (ThreadLocalRandom.current().nextDouble(0, maxpercent) < percent);
    }

    public static String convertMiliToMinute(long milisecond) {
        if (milisecond <= 0) return "Hết hạn";
        return String.format("%02d Phút, %02d Giây",
                TimeUnit.MILLISECONDS.toMinutes(milisecond),
                TimeUnit.MILLISECONDS.toSeconds(milisecond) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milisecond))
        );
    }

    public static Color getRandom() {
        Color c = Color.WHITE;
        c = c.setRed(FNum.randomInt(0, 255));
        c = c.setGreen(FNum.randomInt(0, 255));
        c = c.setBlue(FNum.randomInt(0, 255));
        return c;
    }

    public static String randomChatColor() {
        String s = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f";
        return "§" + s.split(",")[FNum.randomInt(0, s.split(",").length - 1)];
    }

    public static void sendTitleBar(Player p, String title, String subtitle) {
        p.sendTitle(title, subtitle, 10, 10, 10);
    }

    public static void sendTitleBar(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendActionBar(Player p, String string) {
        p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new ComponentBuilder(string).create());

    }

    @SuppressWarnings("deprecation")
    public static Date DateFormat(String input) {
        Date date = new Date();

        //00:00 day/month/year
        date.setHours(FNum.ri(input.split(":")[0]));
        date.setMinutes(FNum.ri(input.split(":")[1].split(" ")[0]));
        date.setDate(FNum.ri(input.split(" ")[1].split("/")[0]));
        date.setMonth(FNum.ri(input.split(" ")[1].split("/")[1]) - 1);
        date.setYear(FNum.ri(input.split(" ")[1].split("/")[2]) - 1900);

        return date;
    }

    public static ItemStack blankItem(Material mat, int color) {
        return new FItem(mat).setColorShort(color).setName(" ").toItemStack();

    }

    //            net.minecraft.server.v1_12_R1.Entity entityPlayer = ((CraftEntity) glowingEntity).getHandle();
    public static void setGlowing(Entity glowingEntity, Player sendPacketPlayer, boolean glow) {
        if (glow) {
//            if (glowingEntity instanceof Player) {
//
//                if (Utils.isAlly(sendPacketPlayer, glowingEntity)) {
//                    if (Main.m.scoreboard.getTeam("redentitycolor").hasEntry(glowingEntity.getName()))
//                        Main.m.scoreboard.getTeam("redentitycolor").removeEntry(glowingEntity.getName());
//                    if (!Main.m.scoreboard.getTeam("greenally").hasEntry(glowingEntity.getName()))
//                        Main.m.scoreboard.getTeam("greenally").addEntry(glowingEntity.getName());
//                } else {
//                    if (!Main.m.scoreboard.getTeam("redentitycolor").hasEntry(glowingEntity.getName()))
//                        Main.m.scoreboard.getTeam("redentitycolor").addEntry(glowingEntity.getName());
//                }
//
//            } else {
//                if (!Main.m.scoreboard.getTeam("redentitycolor").hasEntry(glowingEntity.getUniqueId().toString()))
//                    Main.m.scoreboard.getTeam("redentitycolor").addEntry(glowingEntity.getUniqueId().toString());
//            }
            PacketContainer packet = Main.pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, glowingEntity.getEntityId()); //Set packet's entity id
            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
            watcher.setEntity(glowingEntity); //Set the new data watcher's target
            watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
            try {
                Main.pm.sendServerPacket(sendPacketPlayer, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
//            if (glowingEntity instanceof Player) {
//                if (Main.m.scoreboard.getTeam("redentitycolor").hasEntry(glowingEntity.getName()))
//                    Main.m.scoreboard.getTeam("redentitycolor").removeEntry(glowingEntity.getName());
//                //yellowimmortal
//                if (Main.m.scoreboard.getTeam("greenally").hasEntry(glowingEntity.getName()))
//                    Main.m.scoreboard.getTeam("greenally").removeEntry(glowingEntity.getName());
//            } else {
//                if (Main.m.scoreboard.getTeam("redentitycolor").hasEntry(glowingEntity.getUniqueId().toString()))
//                    Main.m.scoreboard.getTeam("redentitycolor").removeEntry(glowingEntity.getUniqueId().toString());
//            }
            PacketContainer packet = Main.pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, glowingEntity.getEntityId()); //Set packet's entity id
            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
            watcher.setEntity(glowingEntity); //Set the new data watcher's target
            watcher.setObject(0, serializer, (byte) (0x0)); //Set status to glowing, found on protocol page
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
            try {
                Main.pm.sendServerPacket(sendPacketPlayer, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setGlowingWhite(Entity glowingEntity, Player sendPacketPlayer, boolean glow) {
        if (glow) {
            PacketContainer packet = Main.pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, glowingEntity.getEntityId()); //Set packet's entity id
            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
            watcher.setEntity(glowingEntity); //Set the new data watcher's target
            watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
            try {
                Main.pm.sendServerPacket(sendPacketPlayer, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            PacketContainer packet = Main.pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, glowingEntity.getEntityId()); //Set packet's entity id
            WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
            watcher.setEntity(glowingEntity); //Set the new data watcher's target
            watcher.setObject(0, serializer, (byte) (0x0)); //Set status to glowing, found on protocol page
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
            try {
                Main.pm.sendServerPacket(sendPacketPlayer, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static LivingEntity getTarget(Player p) {
        if (Main.targetmap.get(p.getUniqueId()) != null) {
            return Main.targetmap.get(p.getUniqueId()).getTarget();
        }
        return null;
    }

    public static boolean hasTarget(Player p) {
        if (Main.targetmap.get(p.getUniqueId()) != null) {
            return Main.targetmap.get(p.getUniqueId()).hasTarget();
        }
        return false;
    }

    public static String toCappp(String s) {
        String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
        return cap;
    }

    public static boolean isIgnored(Player p, Player p2) {
        Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        com.earth2me.essentials.User sender = ess.getUser(p);
        com.earth2me.essentials.User receiver = ess.getUser(p2);

        return sender.isIgnoredPlayer(receiver);
    }

    public static ArrayList<Color> colorList() {
        ArrayList<Color> cl = new ArrayList<>();
        cl.add(Color.WHITE);
        cl.add(Color.BLUE);
        cl.add(Color.LIME);
        cl.add(Color.OLIVE);
        cl.add(Color.ORANGE);
        cl.add(Color.PURPLE);
        cl.add(Color.AQUA);
        cl.add(Color.BLACK);
        cl.add(Color.FUCHSIA);
        cl.add(Color.GRAY);
        cl.add(Color.GREEN);
        cl.add(Color.MAROON);
        cl.add(Color.NAVY);
        cl.add(Color.RED);
        cl.add(Color.SILVER);
        cl.add(Color.TEAL);
        cl.add(Color.YELLOW);
        return cl;
    }

    public static void setHealthBar(Player p, LivingEntity en, boolean show) {
        if (en.isDead()) return;
        if (!en.isValid()) return;
        if (show) {
            if (!armorlist.containsKey(p.getUniqueId())) {
                EntityArmorStand am = Utils.spawnHologram(p, (ChatColor.AQUA + "[" + Utils.HealthBarpercentMeter(en.getHealth(), en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), "|", 10, ChatColor.GRAY, ChatColor.GREEN, en.getHealth())) + ChatColor.AQUA + "]", en.getEyeLocation().add(0, 0.4, 0));
                armorlist.put(p.getUniqueId(), am);
                LivingEntity z = en;
                Thread cc = new Thread() {
                    boolean run = true;

                    @Override
                    public void run() {
                        while (run) {
                            try {
                                if (!am.isAlive()) run = false;
                                String health = (ChatColor.AQUA + "[" + Utils.HealthBarpercentMeter(en.getHealth(), en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), "|", 10, ChatColor.GRAY, ChatColor.GREEN, en.getHealth()) + ChatColor.AQUA + "]");
                                if (en.getHealth() / en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < 0.6)
                                    health = (ChatColor.AQUA + "[" + Utils.HealthBarpercentMeter(en.getHealth(), en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), "|", 10, ChatColor.GRAY, ChatColor.RED, en.getHealth()) + ChatColor.AQUA + "]");
                                changeHologramName(p, am, health);
                                Location lz = z.getEyeLocation().add(0, 0.4, 0);
                                teleportHologram(p, am, lz);
//                            am.setLocation(lz.getX(), lz.getY(), lz.getZ(), 0, 0);

                                if (z.isDead()) {
                                    Utils.destroyHologram(p, am);
                                    armorlist.remove(p.getUniqueId());
                                    run = false;
                                }
                                if (!armorlist.containsKey(p.getUniqueId())) {
                                    Utils.destroyHologram(p, am);
                                    run = false;
                                }
                            } catch (Exception e) {

                            }
                            try {
                                Thread.sleep(10l);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                cc.start();


            }
        } else {
            if (armorlist.containsKey(p.getUniqueId())) {
                EntityArmorStand am = armorlist.get(p.getUniqueId());
                Utils.destroyHologram(p, am);
                armorlist.remove(p.getUniqueId());
            }
        }

    }


    public static ItemStack getCustomModel(Material mat, int id) {
        ItemStack is = new ItemStack(mat);
        is.setDurability((short) id);

        return is;
    }

    public static ItemStack getCustomModel(ItemStack itemStack, int id) {
        ItemStack is = new ItemStack(itemStack);

        if (!is.hasItemMeta()) return is;
        ItemMeta im = is.getItemMeta();
        is.setDurability((short) id);
        if (!im.isUnbreakable()) {
            im.setUnbreakable(true);
        }
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static double getRandomWithExclusion(double start, double end, double excludemin, double excludemax) {
        double random = ThreadLocalRandom.current().nextDouble(start, end);
        boolean ok = false;
        int co = 0;
        while (!ok) {
            if (random >= excludemin && random <= excludemax) {
                random = ThreadLocalRandom.current().nextDouble(start, end);
            } else {
                ok = true;
            }
            co++;
            if (co >= 100) {
                if (percentRoll(50)) {
                    return end;
                } else {
                    return start;
                }
            }
        }

        return random;
    }

    public static EntityArmorStand spawnHologram(Player p, String text, Location loc) {

        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityArmorStand hd = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        hd.setInvisible(true);
        hd.setNoGravity(true);
        hd.setMarker(true);
        hd.setCustomName(text);
        hd.setCustomNameVisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(hd);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

        return hd;
    }

    public static void spawnHologram(Player p, String text, Location loc, long delay) {

        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityArmorStand hd = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        hd.setInvisible(true);
        hd.setNoGravity(true);
        hd.setMarker(true);
        hd.setCustomName(text);
        hd.setCustomNameVisible(true);
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(hd);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                destroyHologram(p, hd);
            }
        }.runTaskLaterAsynchronously(Main.m, delay);
    }

    public static ArrayList<EntityArmorStand> spawnHologram(ArrayList<Player> p, String text, Location loc) {
        ArrayList<EntityArmorStand> list = new ArrayList<>();
        for (Player z : p) {
            list.add(spawnHologram(z, text, loc));
        }
        return list;
    }

    public static void spawnHologram(ArrayList<Player> p, String text, Location loc, long delay) {
        p.forEach(z -> spawnHologram(z, text, loc, delay));
    }

    public static void destroyHologram(Player p, EntityArmorStand e) {

        PacketPlayOutEntityDestroy p2 = new PacketPlayOutEntityDestroy(e.getId());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);

    }

    public static void destroyHologram(ArrayList<Player> p, EntityArmorStand e) {
        p.forEach(z -> destroyHologram(z, e));

    }

    public static void teleportHologram(Player p, EntityArmorStand e, Location lz) {

        e.setLocation(lz.getX(), lz.getY(), lz.getZ(), 0, 0);
        PacketPlayOutEntityTeleport p2 = new PacketPlayOutEntityTeleport(e);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);

    }

    public static void changeHologramName(Player p, EntityArmorStand e, String text) {

        e.setCustomName(text);
        PacketPlayOutEntityMetadata p2 = new PacketPlayOutEntityMetadata(e.getId(), e.getDataWatcher(), false);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);


    }

    public static Color rgbRandomColor(int min) {
        return Color.fromRGB(ThreadLocalRandom.current().nextInt(min, 255),
                ThreadLocalRandom.current().nextInt(min, 255),
                ThreadLocalRandom.current().nextInt(min, 255));
    }

    public static void sendRawActionBarMessage(Player p, String s) {
        final ItemStack is = p.getInventory().getItemInMainHand(),
                fakeItem = new FItem(is).clone().setName(s).toItemStack();
        final int slot = p.getInventory().getHeldItemSlot() + 36;
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(fakeItem));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendLastItemBar(Player p) {
        final ItemStack is = p.getInventory().getItemInMainHand();
        final int slot = p.getInventory().getHeldItemSlot() + 36;
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(is));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static Color getColor(String col) {
        Color color;
        switch (col.toLowerCase()) {
            case "black":
                color = Color.BLACK;
                break;
            case "blue":
                color = Color.BLUE;
                break;
            case "gray":
                color = Color.GRAY;
                break;
            case "green":
                color = Color.GREEN;
                break;

            case "yellow":
                color = Color.YELLOW;
                break;
            case "orange":
                color = Color.ORANGE;
                break;
            case "red":
                color = Color.RED;
                break;
            case "white":
                color = Color.WHITE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + col.toLowerCase());
        }
        return color;
    }

    public static String ChineseNumberString(int z) {
        String zzz;
        switch (z) {
            case (2):
                zzz = "Nhị";
                break;
            case (3):
                zzz = "Tam";
                break;
            case (4):
                zzz = "Tứ";
                break;
            case (5):
                zzz = "Ngũ";
                break;
            case (6):
                zzz = "Lục";
                break;
            case (7):
                zzz = "Thất";
                break;
            case (8):
                zzz = "Bát";
                break;
            case (9):
                zzz = "Cửu";
                break;
            case (10):
                zzz = "Thập";
                break;
            case (1):
            default:
                zzz = "Nhất";

        }
        return zzz;
    }


    public static void SwingHand(Player player, boolean lefthand) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 0);
        if (lefthand)
            packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), 3);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void Attack(Player player) {
        SlashAttack.slash(player);
    }






    public static void Heal(LivingEntity en, double i, boolean indicate) {

        Bukkit.getScheduler().runTask(Main.m, () -> {
            double healvalue = i;
            if (en.getFireTicks() > 0) healvalue = i / 2;
            if (en.getHealth() + healvalue < en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                en.setHealth(en.getHealth() + healvalue);
            } else {
                en.setHealth(en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }

            String color = ChatColor.GREEN.toString();
            if (indicate)
                Utils.spawnHologram(Utils.getNearbyPlayer(en.getLocation(), 20), ChatColor.GRAY.toString() + "+" + color + healvalue + "", en.getEyeLocation()
                        .add(Utils.getRandomWithExclusion(-.7, .7, -0.4, 0.4),
                                ThreadLocalRandom.current().nextDouble(-0.8, .5),
                                Utils.getRandomWithExclusion(-.7, .7, -0.4, 0.4)), 10);
        });

    }

    public static void sendBlockChange(Location l, Material data, int radius) {
        Utils.getNearbyPlayerAsync(l, radius).stream().forEach(player -> player.sendBlockChange(l, data, (byte) 0));
    }

    public static void sendBlockChangeWaterLevel(Location l, Material data, int radius, int level) {
        Utils.getNearbyPlayerAsync(l, radius).stream().forEach(player -> player.sendBlockChange(l, data, (byte) level));
    }


    public static void coloredRedstone(Location loc, Color color) {
        try{
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 0, color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d);
        }catch (Exception e){

        }
    }

    public static void coloredRedstone(Location loc, int r, int g, int b) {
        try{
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 0, r / 255d, g / 255d, b / 255d);
        }catch (Exception e){

        }
    }

    public static void updateInventoryTitle(Player p, String title) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage(title), p.getOpenInventory().getTopInventory().getSize());
        ep.playerConnection.sendPacket(packet);
        ep.updateInventory(ep.activeContainer);
    }

    public static void sendCrackPacket(int state, Block block) {
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(block.getLocation().hashCode(),
                new BlockPosition(block.getX(), block.getY(), block.getZ()), state);
        int dimension = ((CraftWorld) block.getWorld()).getHandle().dimension;
        ((CraftServer) Main.m.getServer()).getHandle().sendPacketNearby(null, block.getX(), block.getY(),
                block.getZ(), 100 * 6, dimension, packet);
    }

    public static void setBlock(Block b, Material mat) {
        RegionContainer container = Main.getWorldGuard().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(b.getLocation());
        if (set.testState(null, DefaultFlag.EXP_DROPS)) {
            b.setType(mat);
        }


    }



    public static int invSpace(Inventory inv, Material m) {
        int count = 0;
        for (int slot = 0; slot < 36; slot ++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) {
                count += 64;
            }
            if (is != null) {
                if (is.getType() == m){
                    count += (64 - is.getAmount());
                }
            }
        }
        return count;
    }


    public static void addMoney(Player p , int amount){
         Main.getEconomy().depositPlayer(p, amount);

    }
    public static void takeMoney(Player p , int amount){
        Main.getEconomy().withdrawPlayer(p, amount);

    }

    public static void resetStat(Player player) {
        RPGPlayer rpg = RPGPlayerListener.get(player);
        int point = rpg.getInt() + rpg.getStr() + rpg.getVit() + rpg.getAgi() + rpg.getAttrPoint();
        rpg.setStr(0);
        rpg.setAgi(0);
        rpg.setVit(0);
        rpg.setIntel(0);
        rpg.setAttrPoint(point);

    }
}
