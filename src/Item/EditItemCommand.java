package Item;

import Item.ItemStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                sender.sendMessage("§e/redit class ");
                sender.sendMessage("§e/redit race ");
                sender.sendMessage("§e/redit level ");
                sender.sendMessage("§e/redit dmg ");
                sender.sendMessage("§e/redit crit | %");
                sender.sendMessage("§e/redit mspeed | %");
                sender.sendMessage("§e/redit aspeed | %");
                sender.sendMessage("§e/redit armor ");
                sender.sendMessage("§e/redit hp ");
                sender.sendMessage("§e/redit str ");
                sender.sendMessage("§e/redit agi ");
                sender.sendMessage("§e/redit int ");
                sender.sendMessage("§e/redit vital ");

                sender.sendMessage("§e/redit manar | Binh thuoc ");
                sender.sendMessage("§e/redit hpr | Binh thuoc ");
            }
            if (args.length == 2) {
                if (!(sender instanceof Player)) return false;
                Player p = (Player) sender;
                if (p.getInventory().getItemInMainHand() == null) return false;
                ItemStack is = p.getInventory().getItemInMainHand();


                if (args[0].equalsIgnoreCase("class")) {
                    ItemStats.CLASS.apply(is, args[1]);
                } else if (args[0].equalsIgnoreCase("race")) {
                    ItemStats.RACE.apply(is, args[1]);
                } else if (args[0].equalsIgnoreCase("level")) {
                    ItemStats.LEVEL.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("dmg")) {
                    ItemStats.DAMAGE.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("crit")) {
                    ItemStats.CRIT.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("mspeed")) {
                    ItemStats.MOVESPK.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("aspeed")) {
                    ItemStats.ATTSPK.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("armor")) {
                    ItemStats.HPP.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("hp")) {
                    ItemStats.HP.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("str")) {
                    ItemStats.STR.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("agi")) {
                    ItemStats.AGI.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("int")) {
                    ItemStats.INTEL.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("vital")) {
                    ItemStats.VITAL.apply(is, Integer.valueOf(args[1]));


                } else if (args[0].equalsIgnoreCase("manar")) {
                    ItemStats.MANAR.apply(is, Integer.valueOf(args[1]));
                } else if (args[0].equalsIgnoreCase("hpr")) {
                    ItemStats.HPR.apply(is, Integer.valueOf(args[1]));
                }
            }

        }


        return false;
    }
}
