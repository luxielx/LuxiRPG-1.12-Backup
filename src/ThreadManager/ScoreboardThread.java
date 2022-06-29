package ThreadManager;

import Luxiel.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardThread extends BukkitRunnable {
    Player p;
    ScoreboardManager sbm = Bukkit.getScoreboardManager();
    Scoreboard sb;
    Objective o;

    public ScoreboardThread(Player p) {
        this.p = p;
        this.runTaskTimer(Main.m, 0, 20);
    }

    public Player getPlayer() {
        return this.p;
    }

    public void run() {
//        if (p == null) {
//            this.cancel();
//        }
//        if (!p.isOnline()) {
//            this.cancel();
//        }
//
//        String questname = "%quests_player_current_quest_names%";
//        questname = PlaceholderAPI.setPlaceholders(p, questname);
//        try {
//            if (questname.length() > 1) {
//                String quest = "%quests_player_current_objectives_<quest>%".replace("<quest>", questname);
//                quest = PlaceholderAPI.setPlaceholders(p, quest);
//
////            String[] parts;
//                String wrap = WordUtils.wrap(quest, 15);
//                String[] parts = wrap.split(System.lineSeparator());
//
//
//                if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
//                    sb = sbm.getNewScoreboard();
//                    o = sb.registerNewObjective("QuestsSB", "Dummy");
//                    o.setDisplaySlot(DisplaySlot.SIDEBAR);
//                    o.setDisplayName(ChatColor.GRAY + "Nhiệm Vụ: " + ChatColor.GOLD + questname);
//                    Team questprog = sb.registerNewTeam("questprog");
//                    Team questprog2 = sb.registerNewTeam("questprog2");
//                    Team questprog3 = sb.registerNewTeam("questprog3");
//
//                    questprog.addEntry(ChatColor.RED + "");
//                    o.getScore(ChatColor.RED + "").setScore(-1);
//                    o.getScore(ChatColor.GOLD + "").setScore(-2);
//                    o.getScore(ChatColor.GREEN + "").setScore(-3);
////                questprog.setPrefix(ChatColor.GRAY + quest);
//                    questprog.setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[0] + " "));
//                    if (parts.length > 1) {
//                        questprog.setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[1]));
//                    }
//                    if (parts.length > 2) {
//                        questprog2.addEntry(ChatColor.GOLD + "");
//                        questprog2.setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[2] + " "));
//                        if (parts.length > 3) {
//                            questprog2.setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[3]));
//                        }
//                    }
//                    if (parts.length > 4) {
//                        questprog3.addEntry(ChatColor.GREEN + "");
//                        questprog3.setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[4] + " "));
//                        if (parts.length > 5) {
//                            questprog3.setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[5]));
//                        }
//                    }
//                    p.setScoreboard(sb);
//                } else {
//                    sb.getTeam("questprog").setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[0]));
//                    if (parts.length > 1)
//                    sb.getTeam("questprog").setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[1]));
//                    if (parts.length > 2) {
//                        sb.getTeam("questprog2").setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[2] + " "));
//                        if (parts.length > 3)sb.getTeam("questprog2").setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[3]));
//                    } else {
//                        sb.getTeam("questprog2").removeEntry(ChatColor.GOLD + "");
//                    }
//                    if (parts.length > 4) {
//                        sb.getTeam("questprog3").setPrefix(ChatColor.GRAY + ChatColor.stripColor(parts[4] + " "));
//                        if (parts.length > 5)sb.getTeam("questprog3").setSuffix(ChatColor.GRAY + ChatColor.stripColor(parts[5]));
//                    } else {
//                        sb.getTeam("questprog3").removeEntry(ChatColor.GREEN + "");
//                    }
//                }
//            } else if (p.getScoreboard() != null) {
//                p.setScoreboard(sbm.getNewScoreboard());
//            }
//        } catch (Exception e) {
//
//        }
    }

}
