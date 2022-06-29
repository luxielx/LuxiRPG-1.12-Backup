package levelsystem;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.CenteredMessage;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import skill.SkillCastList;

@SuppressWarnings("deprecation")
public class LevelSystem implements Listener {


    public LevelSystem() {
    }

    public static void set(Player p, int exp) {
        if (p.getLevel() >= Main.m.getConfig().getInt("LevelSystem.max_level")) {
            p.setLevel(Main.m.getConfig().getInt("LevelSystem.max_level"));
            return;
        }
        RPGPlayer sp = RPGPlayerListener.get(p);
        Bukkit.getPluginManager().callEvent(new ExpChangeEvent(p, exp));
        sp.setExp(exp);
    }

    public static void add(Player p, double exp) {
        if (p.getLevel() >= Main.m.getConfig().getInt("LevelSystem.max_level")) {
            p.setLevel(Main.m.getConfig().getInt("LevelSystem.max_level"));
            return;
        }
        double ex = get(p);
        ex += exp;

        RPGPlayer sp = RPGPlayerListener.get(p);
        Bukkit.getPluginManager().callEvent(new ExpChangeEvent(p, exp));
        sp.setExp((int) ex);
    }

    public static double get(Player p) {
        RPGPlayer sp = RPGPlayerListener.get(p);
        return sp.getExp();
    }

    public static double getMax(Player p) {
        int level = p.getLevel();
        return level * level * 80;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMonsterDeath(EntityDeathEvent e) {
        try {
            Player p = e.getEntity().getKiller();
            boolean x2guild = false;
            int exp = e.getDroppedExp();
            if(p.hasPermission("rpg.exp2")) exp *= 2;
            else if(p.hasPermission("rpg.exp1")) exp *= 1.5;
            add(p, exp);
            String expformat = Main.m.getConfig().getString("LevelSystem.exp_holo")
                    .replace('&', '§')
                    .replaceAll("%exp%", exp + "")
                    .replaceAll("%player%", e.getEntity().getKiller().getName());
            Utils.spawnHologram(p, expformat, e.getEntity().getLocation().add(0, 1.5, 0), 30);
//            Utils.sendAdvancementMessage(p, Material.EXPERIENCE_BOTTLE,ChatColor.GREEN + "+ "+ ChatColor.GRAY  +exp+  " EXP");
            e.setDroppedExp(0);
        } catch (Exception ex) {
        }

        e.setDroppedExp(0);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void exp(PlayerExpChangeEvent e) {
        add(e.getPlayer(), e.getAmount());
        e.setAmount(0);
    }

    @EventHandler
    public void ExpChangeEvent(ExpChangeEvent e) {
        Player player = e.getPlayer();
        RPGPlayer rpgplayer = RPGPlayerListener.get(player.getUniqueId().toString());
        if (rpgplayer.getLevel() > 50) {
            rpgplayer.setLevel(50);
            return;
        }
        int level = Math.round(rpgplayer.getLevel());
        int exp = Math.round(rpgplayer.getExp());
        if (rpgplayer.getLevel() < Main.m.getConfig().getInt("LevelSystem.max_level") && exp >= Main.m.getConfig().getInt("Level." + (level + 1))) {
            rpgplayer.setLevel(rpgplayer.getLevel() + 1);
            SkillCastList.setGlassPane(player);
            rpgplayer.setExp(exp - Main.m.getConfig().getInt("Level." + (level + 1)));
            player.sendTitle(ChatColor.GREEN + "Chúc mừng!", ChatColor.AQUA + "Bạn đã đạt cấp độ " + rpgplayer.getLevel());
            CenteredMessage.sendCenteredMessage(player,ChatColor.GRAY+"Bạn đã thăng cấp lên cấp độ " + ChatColor.GOLD + rpgplayer.getLevel());
            CenteredMessage.sendCenteredMessage(player,ChatColor.GRAY+"+5 " + ChatColor.GOLD + "Điểm chỉ số" + ChatColor.GRAY +" /profile để dùng điểm!");

            if(rpgplayer.getLevel() == 10){
                CenteredMessage.sendCenteredMessage(player,ChatColor.GRAY+"Đã mở khóa chuỗi nhiệm vụ mới! Hãy đến tiệm rèn kế bên Sát quỷ hội để bắt đầu");
            }
            rpgplayer.setAttrPoint(rpgplayer.getAttrPoint() + 5);
            player.setLevel(rpgplayer.getLevel());
            Bukkit.getServer().getPluginManager().callEvent(new ExpChangeEvent(player, level));
        }
    }

}
