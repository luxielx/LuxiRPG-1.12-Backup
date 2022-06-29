package Item;

import ChucNghiep.Classes;
import ChucNghiep.Races;
import Luxiel.Main;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ADMIN on 20/08/2018.
 */
public class RandomItem {
    public static ItemStack getRandomItem(Material m, ItemTier tier, int minlevel, int maxlevel) {
        ItemStack is = new ItemStack(m);
        if (!ToolList.ALL.getItemList().contains(m)) return is;
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> lore = new ArrayList<>();
        String races = "";
        Classes classz = Classes.getRandom();
        String classes = classz.getName();
        boolean armor = false;
        int level = ThreadLocalRandom.current().nextInt(minlevel, maxlevel);
        int bonus = tier.getBonus();

        int point = bonus * level * 5;
        im.setDisplayName(tier.getColor() + getRandomName(m, tier));

        ArrayList<ItemStats> attrlist = new ArrayList<>();
        if (m.toString().contains("_SWORD")) {
            races = "Người";
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.INTEL);
            attrlist.add(ItemStats.ATTSPK);
            attrlist.add(ItemStats.CRIT);
            attrlist.add(ItemStats.MOVESPK);

        } else if (m == Material.SHEARS) {
            races = "Người,Quỷ";
            classes = "Lôi";
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.ATTSPK);
            attrlist.add(ItemStats.CRIT);
            attrlist.add(ItemStats.MOVESPK);
        } else if (m == Material.BOW || m.toString().contains("_PICKAXE")) {
            races = "Quỷ";
            classes = "";
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.ATTSPK);
            attrlist.add(ItemStats.CRIT);
            attrlist.add(ItemStats.MOVESPK);
        } else if (m.toString().contains("_HOE")) {
            races = "Quỷ";
            classes = "";
            attrlist.add(ItemStats.INTEL);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.CRIT);
            attrlist.add(ItemStats.ATTSPK);
        } else if (m.toString().contains("_SPADE")) {
            races = "Người,Quỷ";
            classes = "";
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.ATTSPK);
            attrlist.add(ItemStats.CRIT);
        } else if (m.toString().contains("_AXE")) {
            races = "Quỷ";
            classes = "";
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.ATTSPK);
            attrlist.add(ItemStats.CRIT);
        } else {
            attrlist.add(ItemStats.HP);
            attrlist.add(ItemStats.STR);
            attrlist.add(ItemStats.AGI);
            attrlist.add(ItemStats.INTEL);
            attrlist.add(ItemStats.VITAL);
            attrlist.add(ItemStats.CRIT);
            if (m.toString().contains("_BOOTS")) {
                attrlist.add(ItemStats.MOVESPK);
            }
        }
        lore.add("Tier: " + tier.getName());
        lore.add(ChatColor.GRAY + "       Tộc: " + ChatColor.WHITE + races + "     ");
        lore.add(ChatColor.LIGHT_PURPLE + "       Nghề: " + ChatColor.WHITE + classes + "     ");
        lore.add(ChatColor.GOLD + "       Level: " + ChatColor.WHITE + level + "     ");
        if (ToolList.ARMOR.getItemList().contains(m)) {
            armor = true;
        }
        lore.add("");
        String percent;
        int amount;
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.WHITE + ChatColor.BOLD.toString() + "Chỉ số:");


        for (ItemStats stat : attrlist) {
            int negativepercent = 14;
            if (m.toString().contains("_SWORD")) {
                if (stat == ItemStats.STR) negativepercent = 14;
            } else if (m == Material.SHEARS) {
                if (stat == ItemStats.STR) negativepercent = 0;
            } else if (m == Material.BOW || m.toString().contains("_PICKAXE")) {
                if (stat == ItemStats.AGI) negativepercent = 0;
            } else if (m.toString().contains("_HOE")) {
                if (stat == ItemStats.INTEL) negativepercent = 0;
            } else if (m.toString().contains("_SPADE")) {
                if (stat == ItemStats.STR) negativepercent = 0;
            } else if (m.toString().contains("_AXE")) {
                if (stat == ItemStats.STR) negativepercent = 0;
            } else {
                if (stat == ItemStats.HPP) negativepercent = 0;
            }
            try {
                if (point <= 0) break;
                int maxvalue = point / stat.cost;
                if (maxvalue > stat.max) maxvalue = stat.max;
                if (Utils.percentRoll(negativepercent)) {
                    amount = ThreadLocalRandom.current().nextInt(-maxvalue, -1);
                } else {
                    amount = ThreadLocalRandom.current().nextInt(1, maxvalue / 2);
                }
                if (amount > point) {
                    amount = point;
                }
                if (amount > stat.max) {
                    amount = stat.max;
                }
                if (amount < -stat.max) {
                    amount = -stat.max;
                }
                if (stat == ItemStats.HP || stat == ItemStats.VITAL) {
                    if (amount < 0) amount = -amount;
                }
                if (amount > 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §a§l+ " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;
                } else if (amount < 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §c§l- " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;

                }
            } catch (Exception e) {

            }

        }
        if (!armor) {
            lore.add(ChatColor.GRAY + ItemStats.DAMAGE.name + ": " + ItemStats.DAMAGE.color + point / ItemStats.DAMAGE.cost);
        } else {
            lore.add(ChatColor.GRAY + ItemStats.HPP.name + ": " + ItemStats.HPP.color + point / ItemStats.HPP.cost);
        }
        lore.addAll(lore2);
        im.setLore(lore);
        is.setItemMeta(im);
//        applyRandomModifier(is);
        reformatItem(is);
        return is;

    }

    public static ItemTier randomNichirintier() {
        if (Utils.percentRoll(40)) {
            return ItemTier.COMMON;
        } else if (Utils.percentRoll(70)) {
            return ItemTier.UNCOMMON;
        } else if (Utils.percentRoll(70)) {
            return ItemTier.RARE;
        } else if (Utils.percentRoll(5)) {
            return ItemTier.ULTIMATE;
        }
        return ItemTier.COMMON;
    }

    public static ItemStack getRandomArmor(int level, String Mat) {
        ArrayList<Material> matlist = new ArrayList<>();
        if (Mat.equalsIgnoreCase("Leather")) {
            matlist.add(Material.LEATHER_CHESTPLATE);
            matlist.add(Material.LEATHER_BOOTS);
            matlist.add(Material.LEATHER_HELMET);
            matlist.add(Material.LEATHER_LEGGINGS);
        } else if (Mat.equalsIgnoreCase("IRON")) {
            matlist.add(Material.IRON_CHESTPLATE);
            matlist.add(Material.IRON_BOOTS);
            matlist.add(Material.IRON_HELMET);
            matlist.add(Material.IRON_LEGGINGS);
        } else if (Mat.equalsIgnoreCase("GOLD")) {
            matlist.add(Material.GOLD_CHESTPLATE);
            matlist.add(Material.GOLD_BOOTS);
            matlist.add(Material.GOLD_HELMET);
            matlist.add(Material.GOLD_LEGGINGS);
        } else if (Mat.equalsIgnoreCase("DIAMOND")) {
            matlist.add(Material.DIAMOND_CHESTPLATE);
            matlist.add(Material.DIAMOND_BOOTS);
            matlist.add(Material.DIAMOND_HELMET);
            matlist.add(Material.DIAMOND_LEGGINGS);
        }
        Material m = matlist.get(ThreadLocalRandom.current().nextInt(0, matlist.size() ));
        ItemStack is = new ItemStack(m);
        if (!ToolList.ALL.getItemList().contains(m)) return is;
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> lore = new ArrayList<>();
        ItemTier tier = randomNichirintier();
        int bonus = tier.getBonus();
        int point = bonus * level * 5;
        im.setDisplayName(tier.getColor() + "Diệt Quỷ Giáp");
        ArrayList<ItemStats> attrlist = new ArrayList<>();
        attrlist.add(ItemStats.HP);
        attrlist.add(ItemStats.CRIT);
        if (m.toString().contains("_BOOTS")) {
            attrlist.add(ItemStats.MOVESPK);
            attrlist.add(ItemStats.AGI);
        }
        if (m.toString().contains("_HELMET")) {
            attrlist.add(ItemStats.INTEL);
        }
        if (m.toString().contains("_LEGGINGS")) {
            attrlist.add(ItemStats.VITAL);
        }
        if (m.toString().contains("_CHESTPLATE")) {
            attrlist.add(ItemStats.STR);
        }
        lore.add("Tier: " + tier.getName());
        lore.add(ChatColor.GOLD + "       Level: " + ChatColor.WHITE + level + "     ");
        lore.add("");
        String percent;
        int amount;
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.WHITE + ChatColor.BOLD.toString() + "Chỉ số:");
        for (ItemStats stat : attrlist) {
            int negativepercent = 10;
            try {
                if (point <= 0) break;
                int maxvalue = point / stat.cost;
                if (maxvalue > stat.max) maxvalue = stat.max;
                if (stat == ItemStats.HPP) negativepercent = 0;
                if (Utils.percentRoll(negativepercent)) {
                    amount = ThreadLocalRandom.current().nextInt(-maxvalue / 2, -1);
                } else {
                    amount = ThreadLocalRandom.current().nextInt(1, maxvalue / 2);
                }
                if (amount > point) {
                    amount = point;
                }
                if (amount > stat.max) {
                    amount = stat.max;
                }
                if (amount < -stat.max) {
                    amount = -stat.max;
                }
                if (stat == ItemStats.HP || stat == ItemStats.VITAL) {
                    if (amount < 0) amount = -amount;
                }
                if (amount > 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §a§l+ " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;
                } else if (amount < 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §c§l- " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;

                }
            } catch (Exception e) {

            }

        }
        lore.add(ChatColor.GRAY + ItemStats.HPP.name + ": " + ItemStats.HPP.color + point / ItemStats.HPP.cost);
        lore.addAll(lore2);
        im.setLore(lore);
        is.setItemMeta(im);
        reformatItem(is);
        return is;

    }

    public static ItemStack getRandomNichirin(int level) {
        Material m = Material.DIAMOND_SWORD;
        ItemStack is = new ItemStack(m);
        if (!ToolList.ALL.getItemList().contains(m)) return is;
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> lore = new ArrayList<>();
        String races = "Người";
        Classes classz = Classes.getRandom();
        String classes = classz.getName();
        ItemTier tier = randomNichirintier();
        int bonus = tier.getBonus();
        int point = bonus * level * 8;
        im.setDisplayName(tier.getColor() + "Nichirin");
        ArrayList<ItemStats> attrlist = new ArrayList<>();
        attrlist.add(ItemStats.STR);
        attrlist.add(ItemStats.AGI);
        attrlist.add(ItemStats.INTEL);
        attrlist.add(ItemStats.ATTSPK);
        attrlist.add(ItemStats.CRIT);
        attrlist.add(ItemStats.MOVESPK);
        lore.add("Tier: " + tier.getName());
        lore.add(ChatColor.GRAY + "       Tộc: " + ChatColor.WHITE + races + "     ");
        lore.add(ChatColor.LIGHT_PURPLE + "       Nghề: " + ChatColor.WHITE + classes + "     ");
        lore.add(ChatColor.GOLD + "       Level: " + ChatColor.WHITE + level + "     ");
        lore.add("");
        String percent;
        int amount;
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.WHITE + ChatColor.BOLD.toString() + "Chỉ số:");
        for (ItemStats stat : attrlist) {
            int negativepercent = 10;
            try {
                if (point <= 0) break;
                int maxvalue = point / stat.cost;
                if (maxvalue > stat.max) maxvalue = stat.max;
                if (Utils.percentRoll(negativepercent)) {
                    amount = ThreadLocalRandom.current().nextInt(-maxvalue / 2, -1);
                } else {
                    amount = ThreadLocalRandom.current().nextInt(1, maxvalue / 2);
                }
                if (amount > point) {
                    amount = point;
                }
                if (amount > stat.max) {
                    amount = stat.max;
                }
                if (amount < -stat.max) {
                    amount = -stat.max;
                }
                if (stat == ItemStats.HP || stat == ItemStats.VITAL) {
                    if (amount < 0) amount = -amount;
                }
                if (amount > 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §a§l+ " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;
                } else if (amount < 0) {
                    if (stat.isPercentage) {
                        percent = "%";
                    } else {
                        percent = "";
                    }
                    lore2.add("  §c§l- " + ChatColor.GRAY + stat.name + ": " + stat.color + amount + percent);
                    point -= amount * stat.cost;

                }
            } catch (Exception e) {

            }

        }
        lore.add(ChatColor.GRAY + ItemStats.DAMAGE.name + ": " + ItemStats.DAMAGE.color + point / ItemStats.DAMAGE.cost);
        lore.addAll(lore2);
        im.setLore(lore);
        is.setItemMeta(im);
        reformatItem(is);
        return Utils.getCustomModel(is, getModel(classz));

    }

    private static String getMaterialName(Material type) {
        if (type == Material.WOOD_PICKAXE) {
            return "Cuồng cung";
        } else if (type == Material.DIAMOND_SWORD) {
            return "Kiếm";
        } else if (type == Material.DIAMOND_SPADE) {
            return "Thương";
        } else if (type == Material.DIAMOND_HOE) {
            return "Trượng";
        } else if (type == Material.SHEARS) {
            return "Dao găm";
        } else if (type == Material.DIAMOND_AXE) {
            return "Rìu";
        } else if (type == Material.BOW) {
            return "Cung";
        } else if (type == Material.DIAMOND_BOOTS) {
            return "Giày";
        } else if (type == Material.DIAMOND_LEGGINGS) {
            return "Quần";
        } else if (type == Material.DIAMOND_CHESTPLATE) {
            return "Áo";
        } else if (type == Material.DIAMOND_HELMET) {
            return "Nón";
        } else {
            if (ToolList.WEAPON.getItemList().contains(type)) {
                return "Vũ Khí";
            } else if (ToolList.ARMOR.getItemList().contains(type)) {
                return "Vật Phẩm";
            }
        }
        return "Vật Phẩm";
    }

    public static String getRandomName(Material mat, ItemTier tier) {
        String name = "";
        if (tier == ItemTier.COMMON || tier == ItemTier.UNCOMMON) {
            name = getPrefixFromConfig(false) + " " + getMaterialName(mat);
        } else if (tier == ItemTier.LEGENDARY) {
            name = getRandomNameFromConfig(mat);
        } else {
            name = getPrefixFromConfig(true) + " " + getMaterialName(mat);

        }


        return name;
    }

    private static String getRandomNameFromConfig(Material mat) {
        List<String> list = Main.m.getConfig().getStringList("itemname." + mat.toString());
        if (list.size() == 0) return "";
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));


    }

    public static void reformatPotion(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> lore = new ArrayList<>();
        if (ItemStats.HPR.getStats(is) != 0) {
            lore.add(ChatColor.GRAY + ItemStats.HPR.name + ": " + ItemStats.HPR.color + ItemStats.HPR.getStats(is));
        }
        if (ItemStats.MANAR.getStats(is) != 0) {
            lore.add(ChatColor.GRAY + ItemStats.MANAR.name + ": " + ItemStats.MANAR.color + ItemStats.MANAR.getStats(is));
        }
    }

    public static void reformatItem(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> lore = new ArrayList<>();
        lore.addAll(Rarity.BIGLETTERS(Rarity.RarityLevel(is)));
        ////////////////////////////////////////////////////////////////////////////////////////////
        String basename = ChatColor.stripColor(im.getDisplayName());
        Modifier modifier = null;
        int enhancementlevel = 0;
        String modifierstring = "";
        String enhancestring = "";
        if (Modifier.hasModifier(is)) {
            modifier = Modifier.getModifier(is);
            basename = Modifier.removeModifierFromName(basename);
            modifierstring = ChatColor.GRAY + "[" + modifier.tier.color + modifier.name + ChatColor.GRAY + "]" + "  ";
        }
        if (Enhancement.haveEnhancementlevel(is)) {
            enhancementlevel = Enhancement.getEnhancementLevel(is);
            basename = Enhancement.removeEnhancementFromName(basename);
            enhancestring = ChatColor.GRAY + "  [" + Enhancement.getColor(enhancementlevel) + Enhancement.getDisplay(enhancementlevel) + ChatColor.GRAY + "]";
        }
        im.setDisplayName(modifierstring + Rarity.raritycolor(Rarity.RarityLevel(is)) + basename + enhancestring);
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (ItemTier.getTier(is) != null) {
            ItemTier tier = ItemTier.getTier(is);
            lore.add(Utils.hideS("Tier: " + tier.getName()));
            lore.add("       " + tier.getColor() + getMaterialName(is.getType()) + " " + tier.getName() + "       ");
        }
        if (!ToolList.ARMOR.getItemList().contains(is.getType()) && !ItemStats.getRace(is).isEmpty()) {
            String z = "";
            for (Races s : ItemStats.getRace(is)) {
                if (z != "") z += ",";
                z += s.getName();
            }
            z = ChatColor.stripColor(z);
            lore.add(ChatColor.GRAY + "       Tộc: " + ChatColor.WHITE + z + "     ");
        }


        if (!ToolList.ARMOR.getItemList().contains(is.getType()) && !ItemStats.getClasses(is).isEmpty()) {
            String z = "";
            for (Classes s : ItemStats.getClasses(is)) {
                if (z != "") z += ",";
                z += s.getName();
            }
//            z = ChatColor.stripColor(z);
//            lore.add(ChatColor.LIGHT_PURPLE + "       Nghề: " + ChatColor.WHITE + z + "     ");
            lore.add(ChatColor.LIGHT_PURPLE + "       Nghề: " + z + "     ");
        }
        if (ItemStats.LEVEL.getStats(is) != 0) {
            lore.add(ChatColor.GOLD + "       Level: " + ChatColor.WHITE + ItemStats.LEVEL.getStats(is) + "     ");
        }
        lore.add("");
        if (modifier != null) {
            if (modifier.hpp != 0) {
                int a = ItemStats.HPP.getBase(is);
                ItemStats.HPP.applyAdditionalStats(is, (int) (a * (modifier.hpp / 100)));
            }
            if (modifier.damage != 0) {
                int a = ItemStats.DAMAGE.getBase(is);
                ItemStats.DAMAGE.applyAdditionalStats(is, (int) (a * (modifier.damage / 100)));
            }
            if (modifier.str != 0) {
                int a = ItemStats.STR.getBase(is);
                ItemStats.STR.applyAdditionalStats(is, (int) (a * (modifier.str / 100)));
            }
            if (modifier.intel != 0) {
                int a = ItemStats.INTEL.getBase(is);
                ItemStats.INTEL.applyAdditionalStats(is, (int) (a * (modifier.intel / 100)));
            }
            if (modifier.agi != 0) {
                int a = ItemStats.AGI.getBase(is);
                ItemStats.AGI.applyAdditionalStats(is, (int) (a * (modifier.agi / 100)));
            }
            if (modifier.attspk != 0) {
                int a = ItemStats.ATTSPK.getBase(is);
                ItemStats.ATTSPK.applyAdditionalStats(is, (int) (a * (modifier.attspk / 100)));
            }
            if (modifier.mvspk != 0) {
                int a = ItemStats.MOVESPK.getBase(is);
                ItemStats.MOVESPK.applyAdditionalStats(is, (int) (a * (modifier.mvspk / 100)));
            }
            if (modifier.crit != 0) {
                int a = ItemStats.CRIT.getBase(is);
                ItemStats.CRIT.applyAdditionalStats(is, (int) (a * (modifier.crit / 100)));
            }

        }
        if (ItemStats.DAMAGE.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.DAMAGE.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.DAMAGE.getAdditionalStats(is) + ")";
            if (ItemStats.DAMAGE.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.DAMAGE.getAdditionalStats(is) + ")";
            lore.add(ChatColor.GRAY + ItemStats.DAMAGE.name + ": " + ItemStats.DAMAGE.color + ItemStats.DAMAGE.getBase(is) + addstats);
        }
        if (ItemStats.HPP.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.HPP.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.HPP.getAdditionalStats(is) + ")";
            if (ItemStats.HPP.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.HPP.getAdditionalStats(is) + ")";
            lore.add(ChatColor.GRAY + ItemStats.HPP.name + ": " + ItemStats.HPP.color + ItemStats.HPP.getBase(is) + addstats);
        }
        lore.add(ChatColor.WHITE + ChatColor.BOLD.toString() + "Chỉ số:");
        if (ItemStats.STR.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.STR.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.STR.getAdditionalStats(is) + ")";
            if (ItemStats.STR.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.STR.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.STR;
            int a = ItemStats.STR.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.AGI.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.AGI.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.AGI.getAdditionalStats(is) + ")";
            if (ItemStats.AGI.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.AGI.getAdditionalStats(is) + ")";



            ItemStats stat = ItemStats.AGI;
            int a = ItemStats.AGI.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.INTEL.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.INTEL.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.INTEL.getAdditionalStats(is) + ")";
            if (ItemStats.INTEL.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.INTEL.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.INTEL;
            int a = ItemStats.INTEL.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.VITAL.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.VITAL.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.VITAL.getAdditionalStats(is) + ")";
            if (ItemStats.VITAL.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.VITAL.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.VITAL;
            int a = ItemStats.VITAL.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.HP.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.HP.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.HP.getAdditionalStats(is) + ")";
            if (ItemStats.HP.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.HP.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.HP;
            int a = ItemStats.HP.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.CRIT.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.CRIT.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.CRIT.getAdditionalStats(is) + "%)";
            if (ItemStats.CRIT.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.CRIT.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.CRIT;
            int a = ItemStats.CRIT.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + "%" + addstats);
        }

        if (ItemStats.ATTSPK.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.ATTSPK.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.ATTSPK.getAdditionalStats(is) + ")";
            if (ItemStats.ATTSPK.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.ATTSPK.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.ATTSPK;
            int a = ItemStats.ATTSPK.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + addstats);
        }
        if (ItemStats.MOVESPK.getStats(is) != 0) {
            String addstats = "";
            if (ItemStats.MOVESPK.getAdditionalStats(is) != 0)
                addstats = ChatColor.GRAY + " (+" + ItemStats.MOVESPK.getAdditionalStats(is) + "%)";
            if (ItemStats.MOVESPK.getAdditionalStats(is) < 0)
                addstats = ChatColor.GRAY + " (" + ItemStats.MOVESPK.getAdditionalStats(is) + ")";
            ItemStats stat = ItemStats.MOVESPK;
            int a = ItemStats.MOVESPK.getBase(is);
            String congtru = "  §c§l- ";
            if (a > 0) congtru = "  §a§l+ ";
            lore.add(congtru + ChatColor.GRAY + stat.name + ": " + stat.color + a + "%" + addstats);
        }
        if (Modifier.hasModifier(is)) {
            lore.add("");
            lore.add(ChatColor.WHITE + ChatColor.BOLD.toString() + "Đặc Tính:");
            Modifier mod = Modifier.getModifier(is);
            String z = ChatColor.GRAY + ChatColor.ITALIC.toString() + "  ";
            if (mod.damage != 0) {
                int a = (int) mod.damage;
                lore.add(z + a + "% " + ItemStats.DAMAGE.name);
            }
            if (mod.str != 0) {
                int a = (int) mod.str;
                lore.add(z + a + "% " + ItemStats.STR.name);
            }
            if (mod.intel != 0) {
                int a = (int) mod.intel;
                lore.add(z + a + "% " + ItemStats.INTEL.name);
            }
            if (mod.agi != 0) {
                int a = (int) mod.agi;
                lore.add(z + a + "% " + ItemStats.AGI.name);
            }
            if (mod.attspk != 0) {
                int a = (int) mod.attspk;
                lore.add(z + a + "% " + ItemStats.ATTSPK.name);
            }
            if (mod.crit != 0) {
                int a = (int) mod.crit;
                lore.add(z + a + "% " + ItemStats.CRIT.name);
            }
            if (mod.hpp != 0) {
                int a = (int) mod.hpp;
                lore.add(z + a + "% " + ItemStats.HPP.name);
            }
        } else {
            if (lore.contains(ChatColor.WHITE + "Đặc Tính:")) {
                int index = 0;
                for (String s : lore) {
                    if (s.contains(ChatColor.WHITE + "Đặc Tính:")) {
                        break;
                    }
                    index++;
                }
                while (index < lore.size()) {
                    lore.remove(index);
                    index++;
                }

            }
        }


        im.setLore(lore);
        is.setItemMeta(im);


    }

    public static String getPrefixFromConfig(boolean isRare) {
        if (isRare) {
            List<String> list = Main.m.getConfig().getStringList("itemprefixrare");
            if (list.size() == 0) return "";
            return list.get(ThreadLocalRandom.current().nextInt(list.size()));
        } else {
            List<String> list = Main.m.getConfig().getStringList("itemprefix");
            if (list.size() == 0) return "";
            return list.get(ThreadLocalRandom.current().nextInt(list.size()));
        }
    }

    public static ItemStack MysteriousWeapon(Material m) {
        ItemStack is = new ItemStack(m);
        ItemMeta im = is.getItemMeta();
        return is;
    }

    private static int getModel(Classes classes) {
        if (classes == Classes.THUNDER) {
            return 4;
        } else if (classes == Classes.FIRE) {
            return 2;
        } else if (classes == Classes.WATER) {
            return 1;
        } else if (classes == Classes.WIND) {
            return 3;
        }
        return 0;
    }
}
