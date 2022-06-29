package Item;

import Util.FItem;
import Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class EnhancementGUI implements Listener {


    public static void openEnhancementGUI(Player p) {
        Inventory inv = Bukkit.createInventory(new EnhancementGuiHolder(), 27, ChatColor.GOLD + "Cường Hóa" + ChatColor.GRAY + " (Chưa có trang bị)");
        setGlass(inv);

        p.openInventory(inv);
        updateInv(inv, p);
    }

    public static void setGlass(Inventory inv) {
        // Slot Trống
        inv.setItem(10, new ItemStack(Material.AIR));
        inv.setItem(13, new ItemStack(Material.AIR));
        inv.setItem(15, new ItemStack(Material.AIR));
        // Màu Hồng
        inv.setItem(0, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(1, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(2, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(9, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(18, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(19, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        inv.setItem(20, new FItem(Material.STAINED_GLASS_PANE).setColorShort(6).setName(" ").toItemStack());
        // Màu Đỏ
        inv.setItem(3, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(5, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(21, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        inv.setItem(23, new FItem(Material.STAINED_GLASS_PANE).setColorShort(15).setName(" ").toItemStack());
        // Màu Tím
        inv.setItem(4, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(" ").toItemStack());
        inv.setItem(12, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Hãy cho Đá vào đây" + ChatColor.BOLD + " -->").toItemStack());
        inv.setItem(14, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "<-- " + "Hãy cho Đá vào đây").toItemStack());
        inv.setItem(22, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(" ").toItemStack());
        inv.setItem(9, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "Hãy cho Trang Bị vào đây" + " -->").toItemStack());
        inv.setItem(11, new FItem(Material.STAINED_GLASS_PANE).setColorShort(2).setName(ChatColor.GRAY + ChatColor.BOLD.toString() + "<-- " + "Hãy cho Trang Bị vào đây").toItemStack());
        // Màu Trắng
        inv.setItem(6, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(7, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(8, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(16, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(17, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(24, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(25, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        inv.setItem(26, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
    }


    private static void updateInv(Inventory inv, Player p) {
        if (inv.getItem(13) == null) {
            if (inv.getItem(10) != null)
                if (inv.getItem(10).getType() == Material.STAINED_GLASS_PANE) {
                    setGlass(inv);
                }
        }
        boolean ready = false;
        String scroll = ChatColor.YELLOW + "(Đã An Toàn)";
        if (inv.getItem(15) == null) {
            if (inv.getItem(10) != null) {
                if (Enhancement.getEnhancementLevel(inv.getItem(10)) >= 10) {
                    scroll = ChatColor.RED + "(Có thể rớt cấp)";
                }
            } else {
                scroll = ChatColor.RED + "(Có thể rớt cấp)";
            }

        }
        if (inv.getItem(10) != null) {
            if (inv.getItem(13) != null) {
                if (Enhancement.getStoneRequireLevel(Enhancement.getEnhancementLevel(inv.getItem(10)) + 1) == Enhancement.EnhanceStoneLevel(inv.getItem(13))) {
//                updateTitle(p, "Tỉ lệ thành công " + Enhancement.getSuccessChanceDisPlay(Enhancement.getEnhancementLevel(inv.getItem(10)))   );
                    String chance = Enhancement.getSuccessChanceDisPlay(Enhancement.getEnhancementLevel(inv.getItem(10)) + 1);
                    Utils.updateInventoryTitle(p, ChatColor.GREEN + "Tỉ lệ thành công " + ChatColor.WHITE + chance + " " + scroll);
                    ready = true;
                } else {
                    Utils.updateInventoryTitle(p, ChatColor.RED + "Hãy sử dụng Đá Cường Hóa Cấp " + ChatColor.AQUA + Enhancement.getStoneRequireLevel(Enhancement.getEnhancementLevel(inv.getItem(10)) + 1));
                }
            } else {
                Utils.updateInventoryTitle(p, ChatColor.GOLD + "Cường Hóa" + ChatColor.GRAY + " (Chưa có Đá Cường Hóa)");
            }
        } else {
            Utils.updateInventoryTitle(p, ChatColor.GOLD + "Cường Hóa" + ChatColor.GRAY + " (Chưa có Vật Phẩm)");
        }
        if (ready) {
            inv.setItem(26, new FItem(Material.STAINED_GLASS_PANE).setColorShort(1).setName(ChatColor.GREEN + "Bấm vào đây để Cường Hóa").addLore(scroll).toItemStack());
        } else {
            inv.setItem(26, new FItem(Material.STAINED_GLASS_PANE).setColorShort(0).setName(" ").toItemStack());
        }

    }


    public static boolean isEnhanceStone(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        if(is.getType() != Material.DIAMOND) return false;
        return ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("Đá cường hóa");
    }

    public static boolean isProtectScroll(ItemStack is) {
        if (!is.hasItemMeta()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        if(is.getType() != Material.PAPER) return false;

        return ChatColor.stripColor(is.getItemMeta().getDisplayName()).contains("Giấy bảo hộ");
    }

    @EventHandler
    public void invclose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof EnhancementGuiHolder) {
            Inventory inv = e.getInventory();
            Player p = (Player) e.getPlayer();
            if (inv.getItem(10) != null && inv.getItem(10).getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(inv.getItem(10));
            }
            if (inv.getItem(13) != null && inv.getItem(13).getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(inv.getItem(13));
            }
            if (inv.getItem(15) != null && inv.getItem(15).getType() != Material.STAINED_GLASS_PANE) {
                p.getInventory().addItem(inv.getItem(15));
            }
        }

    }

    @EventHandler
    public void invclick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getInventory().getHolder() instanceof EnhancementGuiHolder) {
            Inventory inv = e.getInventory();
            ItemStack air = new ItemStack(Material.AIR);
            ItemStack current = e.getCurrentItem();
            if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                e.setCancelled(true);
                return;
            }
            Player p = (Player) e.getWhoClicked();
            int clickslot = e.getSlot();
            if (e.getClickedInventory().getHolder() instanceof EnhancementGuiHolder) {
                if (clickslot == 13) {
                    if (inv.getItem(13) != null) {
                        inv.setItem(13, air);
                        p.getInventory().addItem(current);
                    }
                }

                if (inv.getItem(13) == null) {
                    if (inv.getItem(10) != null)
                        if (inv.getItem(10).getType() == Material.STAINED_GLASS_PANE) {
                            e.setCancelled(true);
                            updateInv(inv, p);
                            return;
                        }
                } else {
                    if (inv.getItem(10) != null)
                        if (inv.getItem(10).getType() == Material.STAINED_GLASS_PANE) {
                            e.setCancelled(true);
                            return;
                        }
                }
                if (clickslot == 26) {
                    if (current.getDurability() == 1) {
                        startEnhance(inv.getItem(10), p, inv);
                        e.setCancelled(true);
                        return;
                    }
                }
                if (clickslot == 10) {
                    if (inv.getItem(10) != null) {
                        inv.setItem(10, air);
                        p.getInventory().addItem(current);
                    }
                }

                if (clickslot == 15) {
                    if (inv.getItem(15) != null) {
                        inv.setItem(15, air);
                        p.getInventory().addItem(current);
                    }
                }

            } else {
                if (ToolList.ALL.getItemList().contains(current.getType())) {
                    if (inv.getItem(10) == null) {
                        e.setCurrentItem(air);
                        inv.setItem(10, current);
                    }
                }
                if (isEnhanceStone(current)) {
                    if (inv.getItem(13) == null) {
                        ItemStack iz = current.clone();
                        if (iz.getAmount() <= 1) {
                            e.setCurrentItem(air);
                            inv.setItem(13, current);
                        } else {
                            current.setAmount(current.getAmount() - 1);
                            iz.setAmount(1);
                            inv.setItem(13, iz);
                        }

                    }
                }
                if (isProtectScroll(current)) {
                    if (inv.getItem(15) == null) {
                        ItemStack iz = current.clone();
                        if (iz.getAmount() <= 1) {
                            e.setCurrentItem(air);
                            inv.setItem(15, current);
                        } else {
                            current.setAmount(current.getAmount() - 1);
                            iz.setAmount(1);
                            inv.setItem(15, iz);
                        }

                    }
                }

            }
            e.setCancelled(true);
            updateInv(inv, p);

        }
    }

    private void startEnhance(ItemStack is, Player p, Inventory v) {
        if (p.getOpenInventory() == null) return;
        int currentlevel = Enhancement.getEnhancementLevel(is);
        boolean protect = false;
        if (currentlevel <= 9) protect = true;
        if (v.getItem(15) != null) protect = true;

        // thanh cong
        if (Enhancement.EnhanceWithChance(Enhancement.getEnhancementLevel(is))) {
            Utils.updateInventoryTitle(p, ChatColor.GREEN + ChatColor.BOLD.toString() + "Thành Công");
            Enhancement.setEnhancementLevel(is, currentlevel + 1);
            v.setItem(13, is);
            for (int i = 0; i <= 26; i++) {
                if (i != 13) {
                    v.setItem(i, new FItem(Material.STAINED_GLASS_PANE).setColorShort(5).setName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Thành Công").toItemStack());
                }
            }
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE , 1, 2);
            // that bai
        } else {
            if (!protect) {
                Enhancement.setEnhancementLevel(is, currentlevel - 1);
            }
            Utils.updateInventoryTitle(p, ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Thất Bại");
            v.setItem(13, is);
            for (int i = 0; i <= 26; i++) {
                if (i != 13) {
                    v.setItem(i, new FItem(Material.STAINED_GLASS_PANE).setColorShort(14).setName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Thất Bại").toItemStack());
                }
            }
            p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK , 1, 1);
        }

    }


}

class EnhancementGuiHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }

}