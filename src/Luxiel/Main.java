package Luxiel;

import ChatManager.ChatManager;
import ChucNghiep.ChonClass;
import Item.*;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Parties.PartiesGUI;
import Parties.PartyDungeonHook;
import Parties.ToDoiCommand;
import ThreadManager.ManaRegen;
import ThreadManager.ScoreboardThread;
import ThreadManager.TaskRegen;
import Util.Bank;
import Util.Sit;
import Util.Utils;
import attack.ChargedAttack;
import attack.SlashAttack;
import attack.TargetSystem;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import guild.*;
import levelsystem.LevelSystem;
import levelsystem.LevelTop;
import levelsystem.LevelTopCommand;
import levelsystem.Xxpcommand;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import skill.Cast;
import skill.SkillCastList;
import stat.HoiMau;
import stat.LevelTooLow;
import stat.StatPlayer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends JavaPlugin implements Listener {

    public static Main m;
    public static ConcurrentHashMap<UUID, BossBar> bossbarmap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, BossBar> bossbarmap2 = new ConcurrentHashMap<>();
    public static ProtocolManager pm;
    public static HashMap<UUID, TargetSystem> targetmap = new HashMap<>();
    //    public static DungeonsAPI dxl;
    private static Economy econ = null;
    public Scoreboard scoreboard;
    public FileConfiguration config;
    private BukkitTask saveTask;

    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public static LuckPerms getLPAPI() {
        return LuckPermsProvider.get();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void onEnable() {
        m = this;
        pm = ProtocolLibrary.getProtocolManager();
//        pm.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
//            @Override
//            public void onPacketSending(PacketEvent event) {
//                if (event.getPacket().getType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
//
//
//                }
//            }
//        });
        setupEconomy();
        loadConfigFile();
        if (config.get("Level.1") == null) {
            setupLevelConfig();
        }
        config.options().copyDefaults(true);
        saveConfig();
        GuildUtils.loadGuildFiles();
        LevelTop.updateArrayList();
        GuildTop.updateArrayList();
        TaskRegen.runHp();
//        if (Bukkit.getPluginManager().isPluginEnabled("DungeonsXL")) {
//            dxl = (DungeonsAPI) Bukkit.getPluginManager().getPlugin("DungeonsXL");


//           dxl.registerGroupAdapter();

//        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            new ManaRegen(p);
            new ScoreboardThread(p);
            new TargetSystem(p.getUniqueId());
            try {
                RPGPlayer sp = new RPGPlayer(p);
                sp.loadFromFile();
                RPGPlayerListener.put(p.getUniqueId().toString(), sp);
            } catch (Exception ex) {
            }
        }

        loadTask();
//        scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();
//        if (scoreboard.getTeam("redentitycolor") == null)
//            scoreboard.registerNewTeam("redentitycolor");
//        scoreboard.getTeam("redentitycolor").setColor(ChatColor.RED);
//        if (scoreboard.getTeam("greenally") == null)
//            scoreboard.registerNewTeam("greenally");
//        scoreboard.getTeam("greenally").setColor(ChatColor.GREEN);


        registerEvents();

        getCommand("chonclass").setExecutor(new ChonClass());
        getCommand("profile").setExecutor(new Profile());
        getCommand("g").setExecutor(new GuildGUI());
        getCommand("guild").setExecutor(new GuildCommand());
        getCommand("xxp").setExecutor(new Xxpcommand());
        getCommand("todoi").setExecutor(new ToDoiCommand());
        getCommand("td").setExecutor(new PartiesGUI());
        getCommand("thien").setExecutor(new Sit());
        getCommand("redit").setExecutor(new EditItemCommand());
        getCommand("ruttien").setExecutor(new Bank());
        getCommand("noptien").setExecutor(new Bank());
        getCommand("leveltop").setExecutor(new LevelTopCommand());

        try {
            new BukkitRunnable() {
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.isOnline()) continue;
                        RPGPlayer rpgPlayer = RPGPlayerListener.get(p);
                        // Coord
//                    Utils.sendActionBar(p,ChatColor.GRAY + "[X:"+ChatColor.RED +Math.round(p.getLocation().getX()) +ChatColor.GRAY + " Z:"+ChatColor.RED+Math.round(p.getLocation().getZ())+"] "+ ChatColor.DARK_GRAY + "["+CoordAB.getCoordBar(p)+ChatColor.DARK_GRAY + "]"+"      ");
                        //
                        if (rpgPlayer.getLevel() == 0) rpgPlayer.setLevel(1);

                        //EXP METER
                        if (p.getLevel() != rpgPlayer.getLevel()) {
                            p.setLevel(rpgPlayer.getLevel());
                        }
                        int exp = rpgPlayer.getExp();
                        int exptonextlevel = rpgPlayer.getNextLevelExp();
                        float percent = (float) exp / exptonextlevel;
                        if (p.getExp() != percent) {
                            if (p.getLevel() >= Main.m.getConfig().getInt("LevelSystem.max_level")) percent = 0;
                            if (percent > 1) percent = 1;
                            if (percent < 0) percent = 0;
                            p.setExp(percent);
                        }
                        //HEALTH
                        if (!p.isOnline()) continue;
                        try {
                            Bukkit.getScheduler().runTask(m, () -> {
                                double health = StatPlayer.mau(p) + RPGPlayerListener.get(p.getUniqueId().toString()).getVit();
                                double health_percent = StatPlayer.mau_percent(p);
                                health += health / 100 * health_percent;
                                if (health <= 1) {
                                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
                                } else
                                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) health);
                                double scale = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 50;

                                if (scale <= 1) {
                                    scale = 2;
                                }

                                if (scale > 20) {
                                    scale = 20;
                                }
                                if (p.getHealthScale() != scale) {
                                    double finalScale = scale;

                                    p.setHealthScale(finalScale);


                                }
                            });
                        } catch (NullPointerException e) {

                        }

                        if (!p.isOnline()) continue;
                        double movement_speed = 0.2 + 0.2 / 100 * StatPlayer.movement_speed(p);
                        if (!p.isOnline()) this.cancel();
                        if (movement_speed > 1) {
                            movement_speed = 1;
                        }
                        if (movement_speed < 0.1) {
                            movement_speed = 0.1;
                        }
                        double finalMovement_speed = movement_speed;
                        new BukkitRunnable() {


                            public void run() {
                                p.setWalkSpeed((float) finalMovement_speed);
                            }
                        }.runTask(m);
                        if (!p.isOnline()) continue;
                        BossBar bar;
                        if (bossbarmap.containsKey(p.getUniqueId())) {
                            bar = bossbarmap.get(p.getUniqueId());
                            if (!bar.getPlayers().contains(p)) bar.addPlayer(p);

                            bar.setTitle(ChatColor.DARK_RED + "Máu " + ChatColor.RED + (int) p.getHealth() + ChatColor.RESET + "/" + ChatColor.RED + (int) p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                            double prog = p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            if (prog > 1) prog = 1;
                            bar.setProgress(prog);
                            if (bar.getProgress() <= 0.2) {
                                bar.setColor(BarColor.RED);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 1);
                                BossBar finalBar = bar;

                                finalBar.setColor(BarColor.YELLOW);

                            } else if (bar.getProgress() <= 0.5) {
                                bar.setColor(BarColor.PINK);
                            } else {
                                bar.setColor(BarColor.GREEN);

                            }
                            if (!p.isOnline()) continue;
                        } else {
                            bar = Bukkit.createBossBar("Máu", BarColor.GREEN, BarStyle.SEGMENTED_20);
                            bar.setColor(BarColor.RED);
                            bar.setVisible(true);
                            bar.addPlayer(p);
                            bossbarmap.put(p.getUniqueId(), bar);
                        }
                        if (!p.isOnline()) continue;
                        if (bossbarmap2.containsKey(p.getUniqueId())) {
                            try {
                                bar = bossbarmap2.get(p.getUniqueId());
                                if (!bar.getPlayers().contains(p)) bar.addPlayer(p);
                                bar.setTitle(ChatColor.BLUE + "Mana " + ChatColor.AQUA + ManaRegen.getMana(p) + ChatColor.RESET + "/" + ChatColor.AQUA + StatPlayer.MaxMana(p));

                                double prog = (double) ManaRegen.getMana(p) / StatPlayer.MaxMana(p);
                                if (prog > 1) prog = 1;
                                bar.setProgress(prog);
                                if (bar.getProgress() < 0.2) {
                                    bar.setColor(BarColor.WHITE);
                                    BossBar finalBar = bar;

                                    finalBar.setColor(BarColor.RED);

                                } else if (bar.getProgress() <= 0.5) {
                                    bar.setColor(BarColor.WHITE);
                                } else {
                                    bar.setColor(BarColor.BLUE);
                                }
                            } catch (NullPointerException e) {

                            }
                            if (!p.isOnline()) continue;
                        } else {
                            bar = Bukkit.createBossBar("Mana", BarColor.BLUE, BarStyle.SEGMENTED_10);
                            bar.setVisible(true);
                            bar.addPlayer(p);
                            bossbarmap2.put(p.getUniqueId(), bar);
                        }
                    }
                }
            }.
                    runTaskTimerAsynchronously(this, 0, 20);
        } catch (Exception e) {

        }
    }

    public void onDisable() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam("redentitycolor") != null)
            scoreboard.getTeam("redentitycolor").unregister();
        if (scoreboard.getTeam("greenally") != null) scoreboard.getTeam("greenally").unregister();
        for (String uuid : RPGPlayerListener.data.keySet()) {
            RPGPlayerListener.get(uuid).saveToFile();
        }
        for (Guild g : Guild.guildlist.keySet()) {
            g.saveToFile();
        }
        for (BossBar bar : bossbarmap.values()) {
            bar.removeAll();
        }
        for (BossBar bar : bossbarmap2.values()) {
            bar.removeAll();
        }
        for (Thread s : Thread.getAllStackTraces().keySet()) {
            if (s instanceof TargetSystem) {
                ((TargetSystem) s).StopThisThread();
            }
        }

        removeAllAdvancement();
        bossbarmap.clear();
        bossbarmap2.clear();
        removeAllHolo();
    }

    public File getDataFile() {
        return getDataFolder();
    }

    private void setupLevelConfig() {
        for (int i = 1; i < 101; i++) {
            config.set("Level." + i, Math.round(Math.pow(10 * i, 1.5)));
        }
    }

    private void loadConfigFile() {
        saveDefaultConfig();
        this.config = getConfig();
    }


    private void loadTask() {
        int savePeriod = 15;
        this.saveTask = new BukkitRunnable() {
            public void run() {
                Main plugin = (Main) Bukkit.getPluginManager().getPlugin("LuxiRPG");
                plugin.getLogger().info("Bat dau luu du lieu cua nguoi choi...");
                Bukkit.broadcastMessage(ChatColor.GRAY + "Đang sao lưu dữ liệu!");

                for (Map.Entry<String, RPGPlayer> entry : RPGPlayerListener.data.entrySet()) {
                    entry.getValue().saveToFile();
                    if (!Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).isOnline()) {
                        RPGPlayerListener.data.remove(entry.getKey());
                    }
                }
                for (Map.Entry<Guild, ArrayList<UUID>> entry : Guild.guildlist.entrySet()) {
                    entry.getKey().saveToFile();
                }
                plugin.getLogger().info("Tat ca du lieu luu thanh cong.");
                Bukkit.broadcastMessage(ChatColor.GRAY + "Dữ liệu lưu thành công!");
                removeAllAdvancement();
                LevelTop.updateArrayList();
                GuildTop.updateArrayList();


            }
        }.runTaskTimerAsynchronously(this, savePeriod * 60 * 20, savePeriod * 60 * 20);

    }


    public void saveServer() {
        getLogger().info("Bat dau luu du lieu cua nguoi choi...");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Đang sao lưu dữ liệu!");
        for (Map.Entry<String, RPGPlayer> entry : RPGPlayerListener.data.entrySet()) {
            entry.getValue().saveToFile();
        }
        for (Map.Entry<Guild, ArrayList<UUID>> entry : Guild.guildlist.entrySet()) {
            entry.getKey().saveToFile();
        }

        getLogger().info("Tat ca du lieu luu thanh cong.");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Dữ liệu lưu thành công!");
        LevelTop.updateArrayList();
        GuildTop.updateArrayList();
        removeAllAdvancement();
    }


    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
        Bukkit.getPluginManager().registerEvents(new RPGPlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new LevelSystem(), this);

        Bukkit.getPluginManager().registerEvents(new ChargedAttack(), this);
        Bukkit.getPluginManager().registerEvents(new SlashAttack(), this);

        Bukkit.getPluginManager().registerEvents(new ChonClass(), this);
        Bukkit.getPluginManager().registerEvents(new Cast(), this);
        Bukkit.getPluginManager().registerEvents(new SatThuong(), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getPluginManager().registerEvents(new ItemClassCheck(), this);
        Bukkit.getPluginManager().registerEvents(new LevelTooLow(), this);


        Bukkit.getPluginManager().registerEvents(new Profile(), this);


        Bukkit.getPluginManager().registerEvents(new HoiMau(), this);

        Bukkit.getPluginManager().registerEvents(new XoaItemData(), this);
        Bukkit.getPluginManager().registerEvents(new PartiesGUI(), this);
        Bukkit.getPluginManager().registerEvents(new GuildGUI(), this);
        Bukkit.getPluginManager().registerEvents(new GuildCommand(), this);
        Bukkit.getPluginManager().registerEvents(new ToDoiCommand(), this);
        Bukkit.getPluginManager().registerEvents(new SkillCastList(), this);
        Bukkit.getPluginManager().registerEvents(new Sit(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDrop(), this);

        Bukkit.getPluginManager().registerEvents(new EnhancementGUI(), this);
        Bukkit.getPluginManager().registerEvents(new ModifierGUI(), this);

        Bukkit.getPluginManager().registerEvents(new PartyDungeonHook(), this);
        Bukkit.getPluginManager().registerEvents(new Bank(), this);

    }


    @EventHandler
    public void regain(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("remove_when_hit")) {
            e.getEntity().remove();
        }
    }


    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {
        if (!sender.hasPermission("solarpermissiontouseeverything")) return false;
        if (a.equalsIgnoreCase("rpg")) {
            if (args[0].equalsIgnoreCase("admess")) {
//                if (sender instanceof Player) {
//                    Player p = (Player) sender;
//                    AdvancementMessage advm = new AdvancementMessage(
//                            "Con cu tao dai vai ca lon ra du ma tui mayyyyyyyyy " + args[1],
//                            p.getInventory().getItemInMainHand().getType().getKey().toString());
//                    advm.showTo(p);
//                }
            }
            if (args[0].equalsIgnoreCase("test")) {
                Player p = (Player) sender;

                Bukkit.broadcastMessage((p.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) + "");


            }
            if (args[0].equalsIgnoreCase("ch")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    EnhancementGUI.openEnhancementGUI(p);
                }
            }

            if (args[0].equalsIgnoreCase("bb")) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    Player p = Bukkit.getPlayer(args[1]);
                    Bank.openBankGUI(p);
                }

            }

            if (args[0].equalsIgnoreCase("tt")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    ModifierGUI.openModifierGUI(p);
                }
            }
            if (args[0].equalsIgnoreCase("removead")) {
                if (sender instanceof Player) {
                    removeAllAdvancement();
                }
            }
            if (args[0].equalsIgnoreCase("heal")) {
                Player p = (Player) sender;
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }

            if (args[0].equalsIgnoreCase("save")) {
                saveServer();
            }

            if (args[0].equalsIgnoreCase("setmodel")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    ItemStack is = p.getInventory().getItemInMainHand();
                    ((Player) sender).getInventory().setItem(p.getInventory().getHeldItemSlot(), Utils.getCustomModel(is, Integer.valueOf(args[1])));
                }

            } else if (args[0].equalsIgnoreCase("getrandomitem")) {
                if (sender instanceof Player) {
                    ((Player) sender).getInventory().addItem(RandomItem.getRandomItem(Material.matchMaterial(args[1].toUpperCase()), ItemTier.valueOf(args[2].toUpperCase()), Integer.valueOf(args[3]), Integer.valueOf(args[4])));
                }
            } else if (args[0].equalsIgnoreCase("randomnichirin")) {
                if (Bukkit.getPlayer(args[2]) != null)
                    Bukkit.getPlayer(args[2]).getInventory().addItem(RandomItem.getRandomNichirin(Integer.valueOf(args[1])));

            } else if (args[0].equalsIgnoreCase("randomarmor")) {
                if (args.length > 3) {
                    if (Bukkit.getPlayer(args[3]) != null)
                        Bukkit.getPlayer(args[3]).getInventory().addItem(RandomItem.getRandomArmor(Integer.valueOf(args[2]), args[1]));

                }


            } else if (args[0].equalsIgnoreCase("resetstat")) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    Utils.resetStat(Bukkit.getPlayer(args[1]));
                    Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GREEN + "Đã tẩy điểm thành công! Hãy cộng cẩn thận nhé");
                }


            } else if (args[0].equalsIgnoreCase("uuid")) {
                if (Bukkit.getOfflinePlayer(args[1]) != null) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
                    sender.sendMessage(p.getName() + " uuid " + p.getUniqueId());
                }
            } else if (args[0].equalsIgnoreCase("chonclass")) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    ChonClass.open(Bukkit.getPlayer(args[1]));
                }


            } else if (args[0].equalsIgnoreCase("fixmau")) {
                if (Bukkit.getPlayer(args[1]) != null) {
                    Player p = Bukkit.getPlayer(args[1]);
                    double health = StatPlayer.mau(p) + RPGPlayerListener.get(p.getUniqueId().toString()).getVit();
                    double health_percent = StatPlayer.mau_percent(p);
                    health += health / 100 * health_percent;
                    if (health <= 1) {
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
                    } else
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) health);
                    double scale = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 50;

                    if (scale <= 1) {
                        scale = 2;
                    }

                    if (scale > 20) {
                        scale = 20;
                    }

                    double finalScale = scale;
                    p.setHealthScale(finalScale);
                }


            } else if (args[0].equalsIgnoreCase("hoimana")) {
                if (sender instanceof Player) {
                    ManaRegen.setMana((Player) sender, StatPlayer.MaxMana((Player) sender));
                }
            } else if (args[0].equalsIgnoreCase("removemod")) {
                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    Modifier.removeModifier(is);


                }
            } else if (args[0].equalsIgnoreCase("addmod")) {
                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    Modifier.valueOf(args[1].toUpperCase()).applyModifier(is);


                }
            } else if (args[0].equalsIgnoreCase("removestat")) {
                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    ItemStats.valueOf(args[1].toUpperCase()).remove(is);

                }
            } else if (args[0].equalsIgnoreCase("addstat")) {
                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    ItemStats.valueOf(args[1].toUpperCase()).remove(is);
                    ItemStats.valueOf(args[1].toUpperCase()).apply(is, Integer.valueOf(args[2]));

                }
            } else if (args[0].equalsIgnoreCase("reformat")) {
                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    RandomItem.reformatItem(is);
                }
            } else if (args[0].equalsIgnoreCase("editdura")) {

                if (sender instanceof Player) {
                    ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                    is.setDurability(Short.valueOf(args[1]));
                    ItemMeta im = is.getItemMeta();
                    im.setUnbreakable(true);
                    im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    is.setItemMeta(im);
                }
            } else if (args[0].equalsIgnoreCase("removeholo")) {
                removeAllHolo();
            } else if (args[0].equalsIgnoreCase("threadcheck")) {
                for (Thread s : Thread.getAllStackTraces().keySet()) {
                    if (s instanceof ManaRegen) {
                        ManaRegen zz = (ManaRegen) s;
                        sender.sendMessage(ChatColor.BLUE + zz.getPlayer().getName() + " Mana Regen");
                    }
                    if (s instanceof TargetSystem) {
                        TargetSystem zz = (TargetSystem) s;
                        sender.sendMessage(ChatColor.RED + zz.getPlayer().getName() + " Target System");
                    }
//                    if (s instanceof StaminaRegen) {
//                        StaminaRegen zz = (StaminaRegen) s;
//                        sender.sendMessage(ChatColor.GREEN + zz.getPlayer().getName() + " Stamina Regen");
//                    }

                }
            } else if (args[0].equalsIgnoreCase("stopallthread")) {
                for (Thread s : Thread.getAllStackTraces().keySet()) {
                    if (s != Thread.currentThread())
                        s.interrupt();
                }
            } else if (args[0].equalsIgnoreCase("setch")) {
                Player p = (Player) sender;
                ItemStack is = p.getInventory().getItemInMainHand();
                Enhancement.setEnhancementLevel(is, Integer.valueOf(args[1]));
            } else if (args[0].equalsIgnoreCase("xoach")) {
                Player p = (Player) sender;
                ItemStack is = p.getInventory().getItemInMainHand();
                Enhancement.removeEnhancement(is);
            } else if (args[0].equalsIgnoreCase("saveitem")) {
                Player p = (Player) sender;
                String name = args[1];
                ItemStack is = p.getInventory().getItemInMainHand();
                File f = new File(Bukkit.getPluginManager().getPlugin("MythicMobs").getDataFolder() + File.separator + "Items", "knyitems.yml");
                if (f.exists()) {
                    FileConfiguration ff = YamlConfiguration.loadConfiguration(f);
                    ff.set(name + ".Id", is.getType().toString());
                    ff.set(name + ".Data", is.getDurability());
                    ff.set(name + ".Display", is.getItemMeta().getDisplayName());
                    ff.set(name + ".Lore", is.getItemMeta().getLore());
                    ff.set(name + ".Hide", Arrays.asList(new String[]{"ATTRIBUTES", "ENCHANTS", "UNBREAKABLE"}));
                    ff.set(name + ".Options.Unbreakable", true);
                    try {
                        ff.save(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage("Item " + name + " đã được lưu .Nhớ dùng /mm reload để nó load");

                } else {
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }


        }
        return true;
    }

    private void removeAllAdvancement() {
        ArrayList<Advancement> arli = Lists.newArrayList(Bukkit.advancementIterator());
        for (Advancement adv : arli) {
            if (adv.getKey().toString().contains("luxirpg"))
                Bukkit.getUnsafe().removeAdvancement(adv.getKey());
        }
        Bukkit.reloadData();
    }

    private void removeAllHolo() {
        for (World z : Bukkit.getWorlds()) {
            for (Entity c : z.getEntities()) {
                if (c instanceof ArmorStand) {
                    if (c.hasMetadata("seat")) c.remove();
                }
            }
        }
    }


}
