package Item;

import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static Item.RandomItem.reformatItem;

/**
 * Created by ADMIN on 26/08/2018.
 */
public enum Modifier {
    // WEAPON
    BROKEN("Vụn vỡ", ItemTier.COMMON, ToolList.WEAPON, -10, -10, 0, 0, 0, 0, 0, 0),
    DAMAGED("Hư hỏng", ItemTier.COMMON, ToolList.WEAPON, -15, 0, 0, 0, 0, 0, 0, 0),
    SHODDY("Kém", ItemTier.COMMON, ToolList.WEAPON, -10, 0, -10, 0, 0, 0, 0, 0),
    WEAK("Yếu", ItemTier.COMMON, ToolList.WEAPON, -20, -10, 0, 0, 0, 0, 0, 0),
    SLOW("Chậm", ItemTier.COMMON, ToolList.WEAPON, 0, 0, -10, 0, -10, 0, 0, 0),
    SLUGGISH("Chậm chạp", ItemTier.COMMON, ToolList.WEAPON, 0, 0, 0, 0, -20, 0, 0, 0),
    LAZY("Lười biếng", ItemTier.COMMON, ToolList.WEAPON, 0, 0, -8, 0, -5, 0, 0, 0),
    TERRIBLE("Khủng khiếp", ItemTier.COMMON, ToolList.WEAPON, -15, 0, 0, 0, 0, 0, 0, 0),
    AWFUL("Tệ hại", ItemTier.COMMON, ToolList.WEAPON, 0, -10, 0, 0, 0, 0, 0, 0),
    LETHARGIC("Mơ màng", ItemTier.COMMON, ToolList.WEAPON, 0, 0, -5, 0, -5, 0, 0, 0),
    IGNORANT("Ngốc", ItemTier.COMMON, ToolList.WEAPON, -10, 0, 0, 0, 0, 0, 0, 0),
    INEPT("Lạc lõng", ItemTier.COMMON, ToolList.STAFF, 0, 0, 0, -10, 0, 0, 0, 0),
    DERANGED("Rối loạn", ItemTier.COMMON, ToolList.STAFF, -10, 0, 0, -10, 0, 0, 0, 0),


    SUPERIOR("Tốt", ItemTier.UNCOMMON, ToolList.WEAPON, 10, 0, 0, 0, 0, 0, 5, 0),
    FORCEFUL("Mạnh mẽ", ItemTier.UNCOMMON, ToolList.WEAPON, 10, 5, 0, 0, 0, 0, 0, 0),
    STRONG("Khỏe", ItemTier.UNCOMMON, ToolList.MELEE, 20, 10, 0, 0, -10, 0, 0, 0),
    QUICK("Nhanh nhạy", ItemTier.UNCOMMON, ToolList.WEAPON, 0, 0, 0, 0, 10, 0, 0, 0),
    DEADLY("Nguy hiểm", ItemTier.UNCOMMON, ToolList.DAGGER, 20, 0, 0, 0, 0, 0, 0, 0),
    AGILE("Hoạt bát", ItemTier.UNCOMMON, ToolList.WEAPON, 0, 0, 0, 0, 10, 0, 10, 0),
    NASTY("Khó ưa", ItemTier.UNCOMMON, ToolList.WEAPON, 5, 0, 0, 0, 5, 0, 5, 0),
    STAUNCH("Kiên định", ItemTier.UNCOMMON, ToolList.WEAPON, 0, 0, 0, 0, 10, 0, 5, 0),
    POWERFUL("Mạnh", ItemTier.UNCOMMON, ToolList.WEAPON, 30, 0, 0, 0, -15, 0, 0, 0),
    FRENZY("Điên cuồng", ItemTier.UNCOMMON, ToolList.WEAPON, -10, -5, 10, 0, 25, 0, 5, 0),
    ADEPT("Thông thạo", ItemTier.UNCOMMON, ToolList.STAFF, 0, 0, 0, 10, 0, 0, 0, 0),

    SIGHTED("Khai sáng", ItemTier.RARE, ToolList.STAFF, -10, -10, 0, 20, 0, 0, 5, 0),
    INTENSE("Mãnh liệt", ItemTier.RARE, ToolList.STAFF, 15, 0, 0, 10, 0, 0, 0, 0),
    MYSTIC("Bí ẩn", ItemTier.RARE, ToolList.STAFF, 0, 0, 0, 30, 0, 0, 0, 0),
    MASTERFUL("Uy lực", ItemTier.RARE, ToolList.STAFF, 10, 0, 0, 10, 0, 0, 0, 0),
    //    ALLEGORICAL("Allegorical", ItemTier.ULTIMATE, ToolList.STAFF, 0, 0, 0, 50, 0, 0, 0),
    INTIMIDATING("Đáng sợ", ItemTier.RARE, ToolList.WEAPON, 20, 10, -20, 0, 0, 0, 0, 0),

    DANGEROUS("Hiểm họa", ItemTier.RARE, ToolList.MELEE, 20, 10, 0, 0, 0, 0, 0, 0),
    SAVAGE("Man rợ", ItemTier.RARE, ToolList.MELEE, 20, 20, 0, 0, -10, 0, 0, 0),
    HEAVY("Nặng", ItemTier.RARE, ToolList.MELEE, 30, 10, -10, 0, -20, 0, 0, 0),
    VIOLENT("Bạo lực", ItemTier.RARE, ToolList.MELEE, 20, 0, 0, 0, 20, 0, 0, 0),
    BULKY("Cồng kềnh", ItemTier.RARE, ToolList.MELEE, 30, 20, -10, 0, -10, 0, 0, 0),
    LIGHT("Nhẹ", ItemTier.RARE, ToolList.MELEE, -10, 0, 10, 0, 30, 0, 0, 0),

    RAPID("Liên Thanh", ItemTier.ULTIMATE, ToolList.WEAPON, 0, -5, 30, -20, 30, 0, 20, 0),
    SWIFT("Siêu Thanh", ItemTier.ULTIMATE, ToolList.WEAPON, 0, -40, 40, -20, 40, 0, 0, 0),
    CELESTIAL("Thiên Khí", ItemTier.ULTIMATE, ToolList.WEAPON, 30, 10, 0, 0, 0, 0, 20, 0),
    ARCANE("Kỳ Diệu", ItemTier.ULTIMATE, ToolList.WEAPON, 40, 20, 0, 0, 0, 0, 0, 0),
    ILLUSORY("Hão Huyền", ItemTier.ULTIMATE, ToolList.WEAPON, 20, 0, 20, 0, 20, 0, 20, 0),
    ALACRITOUS("Thần Tốc", ItemTier.ULTIMATE, ToolList.WEAPON, 0, 0, 0, 0, 50, 0, 0, 0),

    LEGENDARY("Huyền Thoại", ItemTier.LEGENDARY, ToolList.WEAPON, 50, 20, 20, 20, 20, 0, 20, 0),
    GODLY("Thần Thánh", ItemTier.LEGENDARY, ToolList.WEAPON, 40, 20, 20, 0, 0, 0, 50, 0),
    MYTHICAL("Thần Thoại", ItemTier.LEGENDARY, ToolList.WEAPON, 20, -20, -20, 100, 0, 0, 0, 0),
    DEMONIC("Quỷ Khí", ItemTier.LEGENDARY, ToolList.WEAPON, 50, 30, 30, 0, 0, 0, 20, 0),
    WHIRLWIND("Cuồng Phong", ItemTier.LEGENDARY, ToolList.WEAPON, 0, 0, 50, 0, 50, 0, 20, 0),
    SUPERSONIC("Âm Lực", ItemTier.LEGENDARY, ToolList.WEAPON, 0, 0, 0, 0, 70, 0, 0, 0),
    UNREAL("Hư ảo", ItemTier.LEGENDARY, ToolList.WEAPON, 20, 0, 50, -30, 50, 0, 50, 0),
    CANON("Thần Công", ItemTier.LEGENDARY, ToolList.WEAPON, 50, 0, 0, 0, 0, 0, 50, 0),
    //    SATANIC("Satanic", ItemTier.LEGENDARY, ToolList.WEAPON, 666, -66, -66, -66, -66, -66, 0),


    // ARMOR
    HARD("Cứng", ItemTier.COMMON, ToolList.ARMOR, 0, 0, 0, 0, 0, 0, 0, 10),
    JAGGED("Lởm chởm", ItemTier.COMMON, ToolList.ARMOR, 0, 10, 0, 0, 0, 0, 0, 0),
    BRISK("Nhanh", ItemTier.COMMON, ToolList.BOOTS, 0, 0, 10, 0, 0, 5, 0, 0),


    GUARDING("Bảo vệ", ItemTier.UNCOMMON, ToolList.ARMOR, 0, 0, 0, 0, 0, 0, 0, 20),
    SPIKED("Nhọn", ItemTier.UNCOMMON, ToolList.ARMOR, 0, 20, 0, 0, 0, 0, 0, 0),
    FLEETING("Lướt qua", ItemTier.UNCOMMON, ToolList.BOOTS, 0, 0, 10, 0, 0, 10, 0, 0),
    HALLUCINATE("Mơ màng", ItemTier.UNCOMMON, ToolList.ARMOR, 0, 0, 0, 20, 0, 0, 0, 0),


    ARMORED("Bọc thép", ItemTier.RARE, ToolList.ARMOR, 0, 0, 0, 0, 0, 0, 0, 30),
    ANGRY("Giận dữ", ItemTier.RARE, ToolList.ARMOR, 0, 30, 0, 0, 0, 0, 0, 0),
    HASTY("Vội vàng", ItemTier.RARE, ToolList.BOOTS, 0, 0, 30, 0, 0, 10, 0, 0),
    PHANTOM("Bóng ma", ItemTier.RARE, ToolList.ARMOR, 0, 0, 0, 20, 0, 0, 0, 0),

    WARDING("Cai Ngục", ItemTier.ULTIMATE, ToolList.ARMOR, 0, 0, 0, 0, 0, 0, 0, 40),
    PRECISE("Ưng Nhãn", ItemTier.ULTIMATE, ToolList.ARMOR, 0, 0, 0, 0, 0, 0, 20, 0),
    MENACING("Hăm Dọa", ItemTier.ULTIMATE, ToolList.ARMOR, 0, 40, 0, 0, 0, 0, 0, 0),
    ILLUSION("Ảo Ảnh", ItemTier.ULTIMATE, ToolList.BOOTS, 0, 0, 20, 0, 0, 40, 0, 0),


    NIRVANA("Niết Bàn", ItemTier.LEGENDARY, ToolList.HELMET, 0, 0, 0, 100, 0, 0, 0, 20),
    MIRAGE("Huyền Ảnh", ItemTier.LEGENDARY, ToolList.BOOTS, 0, 0, 50, 0, 0, 50, 0, 20),
//    MIRAGE("Huyền Ảnh", ItemTier.LEGENDARY, ToolList.HELMET, 0, 0, 0, 100, 0, 0,0, 20),


    WRATH("Thịnh Nộ", ItemTier.LEGENDARY, ToolList.CHESTPLATE, 0, 100, 0, 0, 0, 0, 0, 20),
    CASEHARDENED("Siêu Cứng", ItemTier.LEGENDARY, ToolList.ARMOR, 0, 20, 0, 0, 0, 0, 0, 100),

    ;


    String name;
    ItemTier tier;
    ToolList tl;
    double damage;
    double str;
    double agi;
    double intel;
    double attspk;
    double mvspk;
    double crit;
    double hpp;

    Modifier(String name, ItemTier tier, ToolList tl, int damage, int str, int agi, int intel, int attspk, int mvspk, int crit, int hpp) {
        this.name = name;
        this.tier = tier;
        this.tl = tl;
        this.damage = damage;
        this.str = str;
        this.intel = intel;
        this.agi = agi;
        this.attspk = attspk;
        this.mvspk = mvspk;
        this.crit = crit;
        this.hpp = hpp;
    }


    public static boolean hasModifier(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        for (Modifier s : Modifier.values()) {
            if (is.getItemMeta().getDisplayName().contains(s.name)) return true;
        }
        return false;
    }

    public static Modifier getModifier(ItemStack is) {
        if (!hasModifier(is)) return null;
        Modifier mod = null;
        String displayname = is.getItemMeta().getDisplayName();
        String[] split = ChatColor.stripColor(displayname).split("  ");
        for (Modifier m : Modifier.values()) {
            if (m.name.startsWith(split[0].replace("[", "").replace("]", ""))) {
                mod = m;
                break;
            }
        }
        return mod;
    }

    public static String removeModifierFromName(String name) {
        String name2 = name;
        String[] split = name.split("  ");
        name2 = name2.replace(split[0] + "  ", "");
        return name2;
    }


    public static void removeModifier(ItemStack is) {
        if (!hasModifier(is)) return;
        ItemMeta im = is.getItemMeta();
        String displayname = im.getDisplayName();
        Modifier mod = Modifier.getModifier(is);
        String[] split = displayname.split("  ");
        String iz = im.getDisplayName();
        iz = iz.replace(split[0] + "  ", "");
        im.setDisplayName(iz);
        for (Enchantment e : im.getEnchants().keySet()) {
            im.removeEnchant(e);
        }
        is.setItemMeta(im);
        if (mod.damage != 0) {
            if (ItemStats.DAMAGE.getAdditionalStats(is) != 0) {
                ItemStats.DAMAGE.removeAdditionalStats(is);
            }

        }
        if (mod.hpp != 0) {
            if (ItemStats.HPP.getAdditionalStats(is) != 0) {
                ItemStats.HPP.removeAdditionalStats(is);
            }

        }
        if (mod.str != 0) {
            if (ItemStats.STR.getAdditionalStats(is) != 0) {
                ItemStats.STR.removeAdditionalStats(is);
            }

        }
        if (mod.intel != 0) {
            if (ItemStats.INTEL.getAdditionalStats(is) != 0) {
                ItemStats.INTEL.removeAdditionalStats(is);
            }

        }
        if (mod.agi != 0) {
            if (ItemStats.AGI.getAdditionalStats(is) != 0) {
                ItemStats.AGI.removeAdditionalStats(is);
            }
        }
        if (mod.attspk != 0) {
            if (ItemStats.ATTSPK.getAdditionalStats(is) != 0) {
                ItemStats.ATTSPK.removeAdditionalStats(is);
            }

        }
        if (mod.mvspk != 0) {
            if (ItemStats.MOVESPK.getAdditionalStats(is) != 0) {
                ItemStats.MOVESPK.removeAdditionalStats(is);
            }

        }
        if (mod.crit != 0) {
            if (ItemStats.CRIT.getAdditionalStats(is) != 0) {
                ItemStats.CRIT.removeAdditionalStats(is);
            }

        }
        RandomItem.reformatItem(is);
    }

    public static ArrayList<Modifier> getModifierByTier(ItemTier tier) {
        ArrayList<Modifier> mod = new ArrayList<>();
        for (Modifier m : Modifier.values()) {
            if (m.tier == tier) {
                mod.add(m);
            }
        }
        return mod;
    }

    public static Modifier getRandomModifierByTier(ItemTier tier) {
        ArrayList<Modifier> mod = new ArrayList<>();
        for (Modifier m : Modifier.values()) {
            if (m.tier == tier) {
                mod.add(m);
            }
        }
        return mod.get(ThreadLocalRandom.current().nextInt(mod.size()));
    }

    public static Modifier getRandomModifier() {
        ArrayList<Modifier> mod = new ArrayList<>(Arrays.asList(Modifier.values()));
        return mod.get(ThreadLocalRandom.current().nextInt(mod.size()));
    }

    public static Modifier applyRandomModifier(ItemStack is) {
        if(is == null) return null;
        Modifier mod = null;
        int rate = 70;
        while (mod == null) {
            if (Utils.percentRoll(rate)) {
                mod = Modifier.getRandomModifierByTier(ItemTier.COMMON);
            } else if (Utils.percentRoll(rate)) {
                mod = Modifier.getRandomModifierByTier(ItemTier.UNCOMMON);
            } else if (Utils.percentRoll(rate)) {
                mod = Modifier.getRandomModifierByTier(ItemTier.RARE);
            } else if (Utils.percentRoll(5)) {
                mod = Modifier.getRandomModifierByTier(ItemTier.ULTIMATE);
            } else if (Utils.percentRoll(1)) {
                mod = Modifier.getRandomModifierByTier(ItemTier.LEGENDARY);
            }
            try{
                if (!mod.tl.getItemList().contains(is.getType())) {
                    mod = null;
                }
            }catch (Exception e){

            }

        }
        if (mod != null)
            mod.applyModifier(is);
        return mod;

    }


    public void applyModifier(ItemStack is) {
        if (hasModifier(is)) removeModifier(is);
        if (!tl.getItemList().contains(is.getType())) return;
        ItemMeta im = is.getItemMeta();
        String in = im.getDisplayName();
        String rn = ChatColor.GRAY + "[" + tier.color + name + ChatColor.GRAY + "]" + "  " + in;
        im.setDisplayName(rn);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.setUnbreakable(true);
        is.setItemMeta(im);
        if (hpp != 0) {
            int a = ItemStats.HPP.getBase(is);
            if(a!=0) {
                if (a < 0) a *= -1;
                ItemStats.HPP.applyAdditionalStats(is, (int) (a * (hpp / 100)));
            }
        }
        if (damage != 0) {
            int a = ItemStats.DAMAGE.getBase(is);
            if (a<0) a*=-1;
            ItemStats.DAMAGE.applyAdditionalStats(is, (int) (a * (damage / 100)));
        }
        if (str != 0) {
            int a = ItemStats.STR.getBase(is);
            if (a<0) a*=-1;
            ItemStats.STR.applyAdditionalStats(is, (int) (a * (str / 100)));
        }
        if (intel != 0) {
            int a = ItemStats.INTEL.getBase(is);
            if (a<0) a*=-1;
            ItemStats.INTEL.applyAdditionalStats(is, (int) (a * (intel / 100)));
        }
        if (agi != 0) {
            int a = ItemStats.AGI.getBase(is);
            if (a<0) a*=-1;
            ItemStats.AGI.applyAdditionalStats(is, (int) (a * (agi / 100)));
        }
        if (attspk != 0) {
            int a = ItemStats.ATTSPK.getBase(is);
            if (a<0) a*=-1;
            ItemStats.ATTSPK.applyAdditionalStats(is, (int) (a * (attspk / 100)));
        }
        if (mvspk != 0) {

            int a = ItemStats.MOVESPK.getBase(is);
            if (a<0) a*=-1;
            ItemStats.MOVESPK.applyAdditionalStats(is, (int) (a * (mvspk / 100)));
        }
        if (crit != 0) {
            int a = ItemStats.CRIT.getBase(is);
            if (a<0) a*=-1;
            ItemStats.CRIT.applyAdditionalStats(is, (int) (a * (crit / 100)));
        }
        reformatItem(is);
    }

    public int getColor() {
        if (this.tier == ItemTier.COMMON) {
            return 15;
        } else if (this.tier == ItemTier.UNCOMMON) {
            return 5;
        } else if (this.tier == ItemTier.RARE) {
            return 3;
        } else if (this.tier == ItemTier.ULTIMATE) {
            return 14;
        } else if (this.tier == ItemTier.LEGENDARY) {
            return 4;
        }
        return 0;
    }
    public String getName() {
        return name;
    }

}
