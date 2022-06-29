package stat;

import Item.ItemStats;
import Item.StatsType;
import Item.ToolList;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import guild.Familia;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class StatPlayer {


    public static int sTr(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();

        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.STR.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.STR.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.STR.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.STR.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.STR.getStats(b4);
        }
        value += RPGPlayerListener.get(p.getUniqueId().toString()).getStr();
        return value;
    }

    public static int inTel(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.INTEL.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.INTEL.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.INTEL.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.INTEL.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.INTEL.getStats(b4);
        }
        value += RPGPlayerListener.get(p.getUniqueId().toString()).getInt();

        return value;
    }

    public static int aGi(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.AGI.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.AGI.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.AGI.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.AGI.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.AGI.getStats(b4);
        }
        value += RPGPlayerListener.get(p.getUniqueId().toString()).getAgi();

        return value;
    }

    public static int vIt(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.VITAL.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.VITAL.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.VITAL.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.VITAL.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.VITAL.getStats(b4);
        }
        value += RPGPlayerListener.get(p.getUniqueId().toString()).getVit();
        return value;
    }

    public static int giap(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();

        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.HPP.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.HPP.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.HPP.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.HPP.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.HPP.getStats(b4);
        }

        int level = p.getLevel();
        value += level * 0.1;
        return value;
    }

    public static int MaxMana(Player p) {
        return 100 + inTel(p) * 2 + p.getLevel() * 5;
    }

    public static int mau(Player p) {
        int value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();

        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.HP.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.HP.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.HP.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.HP.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.HP.getStats(b4);
        }
        int basehp = 10;
        int currentlv = p.getLevel();
        value += basehp + (4.9 * currentlv);
        return value + (StatPlayer.vIt(p) * 10);
    }


    public static double movement_speed(Player p) {
        double value = 0;

        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();

        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.MOVESPK.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.MOVESPK.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.MOVESPK.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.MOVESPK.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.MOVESPK.getStats(b4);
        }
        RPGPlayer rpg = RPGPlayerListener.get(p);
        if(rpg.getGuild() != null){
            if(rpg.getGuild().getFamilia() == Familia.KAMINARI)
                value +=10;

        }
        return value + (StatPlayer.aGi(p));
    }


    public static double chimang(Player p) {
        double value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.CRIT.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.CRIT.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.CRIT.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.CRIT.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.CRIT.getStats(b4);
        }
        return value + (StatPlayer.aGi(p) / 3.5);
    }

    public static double satthuongchimang(Player p) {
        double value = chimang(p);
        double damage = 2;
        if (value >= 100) {
            damage *= value / 100;
        }
        return damage;
    }


    public static long attackspeed(Player p) {
        long value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += ItemStats.ATTSPK.getStats(hand);
        }
        if (h1 != null) {
            value += ItemStats.ATTSPK.getStats(h1);
        }
        if (c2 != null) {
            value += ItemStats.ATTSPK.getStats(c2);
        }
        if (l3 != null) {
            value += ItemStats.ATTSPK.getStats(l3);
        }
        if (b4 != null) {
            value += ItemStats.ATTSPK.getStats(b4);
        }
        RPGPlayer rpg = RPGPlayerListener.get(p);
        if(rpg.getGuild() != null){
            if(rpg.getGuild().getFamilia() == Familia.RAIJIN)
                value +=15;

        }
        value = value + (StatPlayer.aGi(p) / 5);
        if(value > 85) value = 85;
        return value;
    }

    public static double mau_percent(Player p) {
        double value = 0;
        ItemStack hand = p.getInventory().getItemInMainHand();
        ItemStack h1 = p.getInventory().getHelmet();
        ItemStack c2 = p.getInventory().getChestplate();
        ItemStack l3 = p.getInventory().getLeggings();
        ItemStack b4 = p.getInventory().getBoots();
        if (hand != null && !ToolList.ARMOR.getItemList().contains(hand.getType())) {
            value += StatsType.percent(hand, "máu");
        }
        if (h1 != null) {
            value += StatsType.percent(h1, "máu");
        }
        if (c2 != null) {
            value += StatsType.percent(c2, "máu");
        }
        if (l3 != null) {
            value += StatsType.percent(l3, "máu");
        }
        if (b4 != null) {
            value += StatsType.percent(b4, "máu");
        }

        return value;
    }
}
