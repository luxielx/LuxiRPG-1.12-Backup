package guild;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Guild {
    public static ConcurrentHashMap<Guild, ArrayList<UUID>> guildlist = new ConcurrentHashMap();
    public ArrayList<UUID> members;
    String name;
    Familia fam;
    int powerpoint;
    UUID gmaster;


    public Guild(String name) {
        this.name = name;
    }

    public Guild(String name, Familia fam, UUID gmaster, int powerpoint) {
        members = new ArrayList<>();
        this.name = name;
        this.fam = fam;
        this.powerpoint = powerpoint;
        this.gmaster = gmaster;
        if (!this.members.contains(gmaster))
            this.members.add(gmaster);
        RPGPlayer rpgPlayer = RPGPlayerListener.get(gmaster.toString());
        rpgPlayer.setGuild(this);
    }


    public static Guild getGuild(UUID uuid) {
        RPGPlayer rpg = RPGPlayerListener.get(uuid.toString());
        return rpg.getGuild();
    }

    public static Guild getGuild(String guild) {
        for (Guild g : guildlist.keySet()) {
            if (g.getName().equals(guild)) {
                return g;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        update();
    }

    public Familia getFamilia() {
        return this.fam;
    }

    public int powerpoint() {
        return this.powerpoint;
    }

    public UUID getGuildMaster() {
        return this.gmaster;
    }

    protected void setGuildMaster(UUID uuid) {
        this.gmaster = uuid;
        update();
    }

    public ArrayList<UUID> getMembers() {
        return this.members;
    }

    protected void setFam(Familia fam) {
        this.fam = fam;
        update();
    }

    public void setPowerpoint(int powerpoint) {
        this.powerpoint = powerpoint;
        update();
    }

    public void update() {
        guildlist.put(this, members);
    }

    public int calcAvgLevel() {
        int totallevel = 0;
        for (UUID p : this.members) {
            totallevel += RPGPlayerListener.get(p.toString()).getLevel();
        }
        int size = members.size();
        if (size <= 0) size = 1;
        return Integer.valueOf(totallevel / size);
    }

    public int calcTotalLevel() {
        int totallevel = 0;
        for (UUID p : this.members) {
            totallevel += RPGPlayerListener.get(p.toString()).getLevel();
        }
        return Integer.valueOf(totallevel);
    }

    public void addMember(UUID player) {
        if (this.members.size() >= 35) {
            return;
        }
        if (this.members.contains(player)) return;
        members.add(player);
        RPGPlayer rpgPlayer = RPGPlayerListener.get(player.toString());
        rpgPlayer.setGuild(this);
        AnnouceAll(ChatColor.WHITE + Bukkit.getOfflinePlayer(player).getName() + ChatColor.RED + " đã tham gia Guild!!");
        update();
    }

    public void removeMember(UUID player) {
        members.remove(player);
        RPGPlayer rpgPlayer = RPGPlayerListener.get(player.toString());
        rpgPlayer.setGuild(null);
        update();
        AnnouceAll(ChatColor.WHITE + Bukkit.getOfflinePlayer(player).getName() + ChatColor.RED + " đã rời khỏi Guild!!");

    }

    public void disband() {
        ArrayList<UUID> memberclone = new ArrayList<>();
        memberclone.addAll(members);
        for (UUID m : memberclone) {
            removeMember(m);
            if (Bukkit.getPlayer(m) != null)
                if (Bukkit.getPlayer(m).isOnline()){
                    Bukkit.getPlayer(m).sendMessage(ChatColor.RED + "Guild đã giải tán!");
                    Utils.sendTitleBar(Bukkit.getPlayer(m), "", ChatColor.RED + "Guild đã giải tán!", 5, 20, 5);
                }
        }
        guildlist.remove(this);
        deleteFile();
    }

    public void AnnouceAll(String message) {
        for (UUID m : members) {
            if (Bukkit.getPlayer(m) != null)
                if (Bukkit.getPlayer(m).isOnline())
                    Bukkit.getPlayer(m).sendMessage(message);
        }
    }

    public void AnnouceAllTitle(String title, String subtitle) {
        for (UUID m : members) {
            if (Bukkit.getPlayer(m).isOnline())

                Utils.sendTitleBar(Bukkit.getPlayer(m), title, subtitle);

        }
    }

    public void AnnouceAllActionBar(String message) {
        for (UUID m : members) {
            if (Bukkit.getPlayer(m).isOnline())
                Utils.sendActionBar(Bukkit.getPlayer(m), message);
        }
    }


    public ChatColor getColor() {
        if (powerpoint >= 10000) {
            return ChatColor.GOLD;
        } else if (powerpoint >= 5000) {
            return ChatColor.RED;
        } else if (powerpoint >= 3000) {
            return ChatColor.AQUA;
        } else if (powerpoint >= 1000) {
            return ChatColor.GREEN;
        } else {
            return ChatColor.GRAY;
        }
    }

    public File getDataFile() {
        File f = new File(Main.m.getDataFolder() + File.separator + "Guild" + File.separator + name + ".yml");
        return f;
    }

    public void loadFromFile() {
        File f = this.getDataFile();
        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        if (f.exists()) {
            name = fc.getString("name", "");
            fam = Familia.valueOf(fc.getString("familia"));
            powerpoint = fc.getInt("power");
            gmaster = UUID.fromString(fc.getString("masteruuid"));
            ArrayList<UUID> mem = new ArrayList<>();
            try {
                for (String u : fc.getConfigurationSection("members").getKeys(true)) {
                    mem.add(UUID.fromString(u));
                }
            } catch (Exception e) {

            }
            members = new ArrayList<>(mem);
        }
    }

    public void deleteFile() {
        File f = this.getDataFile();
        f.delete();
    }

    public void saveToFile() {
        File f = this.getDataFile();
        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        fc.set("name", name);
        fc.set("masteruuid", gmaster.toString());
        fc.set("mastername", Bukkit.getOfflinePlayer(gmaster).getName());
        fc.set("familia", fam.toString());
        fc.set("power", powerpoint);
        fc.set("member", null);
        for (UUID u : members) {
            fc.set("members." + u.toString(), "");
        }

        try {
            fc.save(this.getDataFile());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR SAVE GUILD: " + name);
        }
    }

    public class GuildGuiHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}

