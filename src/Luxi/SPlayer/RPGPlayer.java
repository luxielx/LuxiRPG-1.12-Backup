package Luxi.SPlayer;

import ChucNghiep.Classes;
import Luxiel.Main;
import guild.Guild;
import levelsystem.ExpChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;


public class RPGPlayer {
    public String name = "";
    public String uuid;
    Player p;
    private int mau = 1;
    private int level = 1;
    private int exp = 0;
    private int danhvong = 0;
    private int lucchien = 0;
    private int str = 0;
    private int agi = 0;
    private int vit = 0;
    private int intel = 0;
    private int attrp = 0;
    private Classes playerclass = Classes.NONE;
    private Guild guild;


    public RPGPlayer(Player p) {
        this.p = p;
        name = p.getName();
        uuid = p.getUniqueId().toString();


    }

    public RPGPlayer(String uuid) {
        this.uuid = uuid;
        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        if (op != null) {
            name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        } else {
            File f = this.getDataFile();
            FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
            if (f.exists()) {
                name = fc.getString("name");
            }
        }
    }

    private void update() {
        RPGPlayerListener.put(uuid, this);

    }

    public void setClass(Classes clas) {
        this.playerclass = clas;
        update();
    }

    public void setMana(int mana) {
        update();
    }

    public void setIntel(int x) {
        this.intel = x;
        update();
    }

    public Classes getPlayerClass() {
        if (this.playerclass == null) return Classes.NONE;
        return this.playerclass;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
        update();
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
        Bukkit.getPluginManager().callEvent(new ExpChangeEvent(p, exp));
        update();
    }

    public int getExpToNextLevel() {
        return Main.m.config.getInt("Level." + (level + 1)) - this.exp;
    }

    public int getNextLevelExp() {
        return Main.m.config.getInt("Level." + (level + 1));
    }

    public int getDanhVong() {
        return this.danhvong;
    }

    public int getLucChien() {
        return this.lucchien;
    }

    public int getStr() {
        return this.str;
    }

    public void setStr(int x) {
        this.str = x;
        update();
    }

    public int getHP() {
        return this.mau;
    }

    public void setHP(int x) {
        this.mau = x;
        update();
    }

    public int getAgi() {
        return this.agi;
    }

    public void setAgi(int x) {
        this.agi = x;
        update();
    }

    public int getInt() {
        return this.intel;
    }

    public int getVit() {
        return this.vit;
    }

    public void setVit(int x) {
        this.vit = x;
        update();
    }

    public int getAttrPoint() {
        return this.attrp;
    }

    public void setAttrPoint(int x) {
        this.attrp = x;
        update();
    }

    public File getDataFile() {
        File f = new File(Main.m.getDataFolder() + File.separator + "PlayerData" + File.separator + uuid + ".yml");
        return f;
    }

    public boolean isExist() {
        return this.getDataFile().exists();
    }

    public Guild getGuild() {
        return this.guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
        update();
    }

    public void loadFromFile() {
        File f = this.getDataFile();
        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        if (f.exists()) {
            this.name = fc.getString("name", "");
            this.uuid = fc.getString("uuid", "");
            this.level = fc.getInt("level", 1);
            this.exp = fc.getInt("exp", 0);
            this.danhvong = fc.getInt("danhvong", 0);
            this.lucchien = fc.getInt("lucchien", 0);
            this.playerclass = Classes.valueOf(fc.getString("class", "None"));
            this.attrp = fc.getInt("attrp", 0);
            this.intel = fc.getInt("intel", 0);
            this.str = fc.getInt("str", 0);
            this.vit = fc.getInt("vit", 0);
            this.agi = fc.getInt("agi", 0);
            if (Guild.getGuild(fc.getString("guild")) != null) {
                this.guild = Guild.getGuild(fc.getString("guild"));
            }

        }
    }

    public void saveToFile() {
        File f = this.getDataFile();
        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        fc.set("name", name);
        fc.set("uuid", uuid);
        fc.set("level", level);
        fc.set("exp", exp);
        fc.set("danhvong", danhvong);
        fc.set("lucchien", lucchien);
        fc.set("class", playerclass.toString());
        fc.set("agi", agi);
        fc.set("str", str);
        fc.set("intel", intel);
        fc.set("vit", vit);
        fc.set("attrp", attrp);
        if (this.guild != null) {
            fc.set("guild", this.guild.getName());
        } else {
            fc.set("guild", null);
        }
        try {
            fc.save(this.getDataFile());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR SAVE PLAYER: ");
            System.out.println("Name: " + name);
            System.out.println("UUID: " + uuid);
            System.out.println("Level: " + level);
            System.out.println("Exp: " + exp);
            System.out.println("Danh vong: " + danhvong);
            System.out.println("Luc chien: " + lucchien);
        }
    }


}
