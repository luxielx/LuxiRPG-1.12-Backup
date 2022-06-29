package levelsystem;

import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Xxpcommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String a, String[] args) {

        if (sender.isOp() || !(sender instanceof Player)) {

            if (args.length == 0) {
                sender.sendMessage("§e/xxp <tên player>§f: Xem thông tin của player");
                sender.sendMessage("§e/xxp set <tên player> <số lượng>§f: Set exp player");
                sender.sendMessage("§e/xxp set <tên player> <cấp độ>l§f: Set level cho player (khi sử dụng exp hiện tại của player sẽ về 0)");
                sender.sendMessage("§e/xxp set <tên player> <tỉ lệ>%§f: Set % kinh nghiệm cho player");
                sender.sendMessage("§e/xxp add <tên player> <số lượng>§f: Thêm exp player");
                sender.sendMessage("§e/xxp add <tên player> <cấp độ>l§f: Thêm level cho player (khi sử dụng exp hiện tại của player sẽ về 0)");
                sender.sendMessage("§e/xxp add <tên player> <tỉ lệ>%§f: Thêm % kinh nghiệm cho player");
            }

            if (args.length == 1) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null) {
                    sender.sendMessage("§aHiện tại " + p.getName() + " có " + LevelSystem.get(p) + " Exp §7(cấp " + p.getLevel() + ")");
                } else {
                    sender.sendMessage("§cKhông tìm thấy player đó!");
                }

            }


            if (args.length == 3 && args[0].equals("set")) {
                Player p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    RPGPlayer sp = RPGPlayerListener.get(p);
                    String se = args[2].toLowerCase();
                    if (se.endsWith("l")) {
                        se = se.replaceAll("l", "");
                        sp.setLevel(Integer.valueOf(se));
                        sp.setExp(0);
                        sender.sendMessage("§aĐã set " + se + " level cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("exp")) {
                        se = se.replaceAll("exp", "");
                        sp.setExp(Integer.valueOf(se));
                        sender.sendMessage("§aĐã set " + se + " exp cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("str")) {
                        se = se.replaceAll("str", "");
                        sp.setStr(Integer.valueOf(se));
                        sender.sendMessage("§aĐã set " + se + " str cho §f" + args[1] + " §a!");

                    } else if (se.endsWith("agi")) {
                        se = se.replaceAll("agi", "");
                        sp.setAgi(Integer.valueOf(se));
                        sender.sendMessage("§aĐã set " + se + " agi cho §f" + args[1] + " §a!");

                    } else if (se.endsWith("int")) {
                        se = se.replaceAll("int", "");
                        sp.setIntel(Integer.valueOf(se));
                        sender.sendMessage("§aĐã set " + se + " int cho §f" + args[1] + " §a!");

                    } else if (se.endsWith("vit")) {
                        se = se.replaceAll("vit", "");

                        sp.setVit(Integer.valueOf(se));

                        sender.sendMessage("§aĐã set " + se + " vit cho §f" + args[1] + " §a!");

                    } else if (se.endsWith("p")) {
                        se = se.replaceAll("p", "");

                        sp.setAttrPoint(Integer.valueOf(se));

                        sender.sendMessage("§aĐã set " + se + "p cho §f" + args[1] + " §a!");

                    } else if (se.endsWith("%")) {
                        double percent = Double.valueOf(se.replaceAll("%", ""));
                        double exp = LevelSystem.getMax(p) * (percent / 100);
                        LevelSystem.set(p, (int) exp);
                        sender.sendMessage("§aĐã set " + se + " kinh nghiệm cho §f" + args[1] + " §a!");
                    } else {
                        LevelSystem.set(p, Integer.valueOf(se));
                        sender.sendMessage("§aĐã set " + se + " kinh nghiệm cho §f" + args[1] + " §a!");
                    }
                } else {
                    sender.sendMessage("§cKhông tìm thấy player đó!");
                }
            }


            if (args.length == 3 && args[0].equals("add")) {
                Player p = Bukkit.getPlayer(args[1]);

                if (p != null) {
                    RPGPlayer sp = RPGPlayerListener.get(p);

                    String se = args[2].toLowerCase();

                    if (se.endsWith("l")) {
                        se = se.replaceAll("l", "");
                        sp.setLevel(sp.getLevel() + Integer.valueOf(se));
                        sp.setExp(0);
                        sender.sendMessage("§aĐã thêm " + se + " level cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("str")) {
                        se = se.replaceAll("str", "");

                        sp.setStr(sp.getStr() + Integer.valueOf(se));

                        sender.sendMessage("§aĐã thêm " + se + " str cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("int")) {
                        se = se.replaceAll("int", "");

                        sp.setIntel(sp.getInt() + Integer.valueOf(se));

                        sender.sendMessage("§aĐã thêm " + se + " int cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("agi")) {
                        se = se.replaceAll("agi", "");

                        sp.setAgi(sp.getAgi() + Integer.valueOf(se));

                        sender.sendMessage("§aĐã thêm " + se + " agi cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("vit")) {
                        se = se.replaceAll("vit", "");

                        sp.setVit(sp.getVit() + Integer.valueOf(se));

                        sender.sendMessage("§aĐã thêm " + se + " vit cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("p")) {
                        se = se.replaceAll("p", "");

                        sp.setAttrPoint(sp.getAttrPoint() + Integer.valueOf(se));

                        sender.sendMessage("§aĐã thêm " + se + " p cho §f" + args[1] + " §a!");
                    } else if (se.endsWith("%")) {
                        double percent = Double.valueOf(se.replaceAll("%", ""));
                        double exp = LevelSystem.getMax(p) * (percent / 100);
                        LevelSystem.add(p, exp);
                        sender.sendMessage("§aĐã thêm " + se + " kinh nghiệm cho §f" + args[1] + " §a!");
                    } else {
                        LevelSystem.add(p, Integer.valueOf(se));
                        sender.sendMessage("§aĐã thêm " + se + " kinh nghiệm cho §f" + args[1] + " §a!");
                    }
                } else {
                    sender.sendMessage("§cKhông tìm thấy player đó!");
                }
            }
        }
        return true;
    }
}
