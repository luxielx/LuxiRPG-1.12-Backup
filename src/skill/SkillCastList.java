package skill;

import ChucNghiep.ChonClass;
import ChucNghiep.Classes;
import Item.ToolList;
import Luxi.SPlayer.RPGPlayer;
import Luxi.SPlayer.RPGPlayerListener;
import Luxiel.Main;
import Util.FItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by ADMIN on 8/6/2018.
 */
public class SkillCastList implements Listener {
    Main plugin = Main.m;

    public static void setGlassPane(Player p) {
        Classes pcl = RPGPlayerListener.get(p).getPlayerClass();
        RPGPlayer sp = RPGPlayerListener.get(p.getUniqueId().toString());
        ItemStack mot = new FItem(Material.BARRIER).setName(ChatColor.RED + "Chưa đủ cấp độ").toItemStack();
        ItemStack hai = new FItem(Material.BARRIER).setName(ChatColor.RED + "Chưa đủ cấp độ").toItemStack();
        ItemStack bar = new FItem(Material.BARRIER).setName(ChatColor.RED + "Chưa đủ cấp độ").toItemStack();
        ItemStack bon = new FItem(Material.BARRIER).setName(ChatColor.RED + "Chưa đủ cấp độ").toItemStack();
        if (pcl == Classes.THUNDER) {
            if (sp.getLevel() >= Thunder1.levelreq)
                mot = new FItem(Material.BROWN_GLAZED_TERRACOTTA).setName(Thunder1.name).setAmount(Thunder1.manacost).setLore(Thunder1.desc).toItemStack();
            if (sp.getLevel() >= Thunder2.levelreq)
                hai = new FItem(Material.GREEN_GLAZED_TERRACOTTA).setName(Thunder2.name).setAmount(Thunder2.manacost).setLore(Thunder2.desc).toItemStack();
            if (sp.getLevel() >= Thunder3.levelreq)
                bar = new FItem(Material.RED_GLAZED_TERRACOTTA).setName(Thunder3.name).setAmount(Thunder3.manacost).setLore(Thunder3.desc).toItemStack();
            if (sp.getLevel() >= Thunder4.levelreq)
                bon = new FItem(Material.BLACK_GLAZED_TERRACOTTA).setName(Thunder4.name).setAmount(Thunder4.manacost).setLore(Thunder4.desc).toItemStack();
        } else if (pcl == Classes.WIND) {
            if (sp.getLevel() >= Wind1.levelreq)
                mot = new FItem(Material.YELLOW_GLAZED_TERRACOTTA).setName(Wind1.name).setAmount(Wind1.manacost).setLore(Wind1.desc).toItemStack();
            if (sp.getLevel() >= Wind2.levelreq)
                hai = new FItem(Material.LIME_GLAZED_TERRACOTTA).setName(Wind2.name).setAmount(Wind2.manacost).setLore(Wind2.desc).toItemStack();
            if (sp.getLevel() >= Wind3.levelreq)
                bar = new FItem(Material.PINK_GLAZED_TERRACOTTA).setName(Wind3.name).setAmount(Wind3.manacost).setLore(Wind3.desc).toItemStack();
            if (sp.getLevel() >= Wind4.levelreq)
                bon = new FItem(Material.GRAY_GLAZED_TERRACOTTA).setName(Wind4.name).setAmount(Wind4.manacost).setLore(Wind4.desc).toItemStack();
        } else if (pcl == Classes.WATER) {
            if (sp.getLevel() >= Water1.levelreq)
                mot = new FItem(Material.WHITE_GLAZED_TERRACOTTA).setName(Water1.name).setAmount(Water1.manacost).setLore(Water1.desc).toItemStack();
            if (sp.getLevel() >= Water2.levelreq)
                hai = new FItem(Material.ORANGE_GLAZED_TERRACOTTA).setName(Water2.name).setAmount(Water2.manacost).setLore(Water2.desc).toItemStack();
            if (sp.getLevel() >= Water3.levelreq)
                bar = new FItem(Material.MAGENTA_GLAZED_TERRACOTTA).setName(Water3.name).setAmount(Water3.manacost).setLore(Water3.desc).toItemStack();
            if (sp.getLevel() >= Water4.levelreq)
                bon = new FItem(Material.LIGHT_BLUE_GLAZED_TERRACOTTA).setName(Water4.name).setAmount(Water4.manacost).setLore(Water4.desc).toItemStack();
        } else if (pcl == Classes.FIRE) {
            if (sp.getLevel() >= Fire1.levelreq)
                mot = new FItem(Material.SILVER_GLAZED_TERRACOTTA).setName(Fire1.name).setAmount(Fire1.manacost).setLore(Fire1.desc).toItemStack();
            if (sp.getLevel() >= Fire2.levelreq)
                hai = new FItem(Material.CYAN_GLAZED_TERRACOTTA).setName(Fire2.name).setAmount(Fire2.manacost).setLore(Fire2.desc).toItemStack();
            if (sp.getLevel() >= Fire3.levelreq)
                bar = new FItem(Material.PURPLE_GLAZED_TERRACOTTA).setName(Fire3.name).setAmount(Fire3.manacost).setLore(Fire3.desc).toItemStack();
            if (sp.getLevel() >= Fire4.levelreq)
                bon = new FItem(Material.BLUE_GLAZED_TERRACOTTA).setName(Fire4.name).setAmount(Fire4.manacost).setLore(Fire4.desc).toItemStack();
        } else {
            return;
        }
        p.getInventory().setItem(0, mot);
        p.getInventory().setItem(1, hai);
        p.getInventory().setItem(2, bar);
        p.getInventory().setItem(3, bon);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void skillPrevent(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory() == null)
            return;
        if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
            return;
        if (e.getClick().isKeyboardClick())
            if (e.getHotbarButton() == 1 || e.getHotbarButton() == 2 || e.getHotbarButton() == 3
                    || e.getHotbarButton() == 0) {
                e.setCancelled(true);
            }
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            if (e.getSlot() == 0 || e.getSlot() == 1 || e.getSlot() == 2 || e.getSlot() == 3) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;
        if (e.getPlayer().getInventory().contains(Material.ARROW)) {
            e.getPlayer().getInventory().remove(Material.ARROW);
        }
        RPGPlayer sp = RPGPlayerListener.get(e.getPlayer());

        if (sp.getPlayerClass() == Classes.NONE) {
            Bukkit.getScheduler().runTaskLater(Main.m, () -> {
                ChonClass.open(e.getPlayer());
            }, 5);
        }
        if (e.getPlayer().getInventory().getHeldItemSlot() == 1 || e.getPlayer().getInventory().getHeldItemSlot() == 0
                || e.getPlayer().getInventory().getHeldItemSlot() == 2
                || e.getPlayer().getInventory().getHeldItemSlot() == 3)
            e.getPlayer().getInventory().setHeldItemSlot(4);


        Player p = e.getPlayer();
        boolean setglass = false;
        for (int i = 0; i <= 3; i++) {
            if (p.getInventory().getItem(i) == null) {
                setglass = true;
                break;
            }
            if (!p.getInventory().getItem(i).getType().toString().contains("GLAZED") && p.getInventory().getItem(i).getType() != Material.BARRIER) {
                setglass = true;
            }
            if (p.getInventory().getItem(i).getType() == Material.BARRIER) {
                switch (i) {
                    case (0):
                        if (p.getLevel() >= 1) setglass = true;
                        break;
                    case (1):
                        if (p.getLevel() >= 5) setglass = true;
                        break;
                    case (2):
                        if (p.getLevel() >= 10) setglass = true;
                        break;
                    case (3):
                        if (p.getLevel() >= 20) setglass = true;
                        break;
                }

            }

        }
        if (setglass) setGlassPane(p);
    }

    @EventHandler
    public void dropSkill(PlayerDropItemEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;
        if(ToolList.ALL.getItemList().contains(e.getItemDrop().getItemStack().getType())){
            e.getPlayer().sendMessage(ChatColor.RED +"Vật phẩm này không thể vứt./trash nếu bạn muốn bỏ nó./Trade nếu muốn cho người khác.");
            e.setCancelled(true);
        }
        if (e.getPlayer().getInventory().getHeldItemSlot() == 0 || e.getPlayer().getInventory().getHeldItemSlot() == 1
                || e.getPlayer().getInventory().getHeldItemSlot() == 2
                || e.getPlayer().getInventory().getHeldItemSlot() == 3) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void CastSkill(PlayerItemHeldEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;
        Player p = e.getPlayer();
        boolean setglass = false;
        for (int i = 0; i <= 3; i++) {
            if (p.getInventory().getItem(i) == null) {
                setglass = true;
                break;
            }
            if (!p.getInventory().getItem(i).getType().toString().contains("GLAZED") && p.getInventory().getItem(i).getType() != Material.BARRIER) {
                setglass = true;
            }
            if (p.getInventory().getItem(i).getType() == Material.BARRIER) {
                switch (i) {
                    case (0):
                        if (p.getLevel() >= 1) setglass = true;
                        break;
                    case (1):
                        if (p.getLevel() >= 5) setglass = true;
                        break;
                    case (2):
                        if (p.getLevel() >= 10) setglass = true;
                        break;
                    case (3):
                        if (p.getLevel() >= 20) setglass = true;
                        break;
                }

            }

        }
        if (setglass) setGlassPane(p);

        if (e.getNewSlot() == 0) {
            Bukkit.getPluginManager().callEvent(new SkillCastEvent(e.getPlayer(), 1));
            e.setCancelled(true);
        }
        if (e.getNewSlot() == 1) {
            Bukkit.getPluginManager().callEvent(new SkillCastEvent(e.getPlayer(), 2));
            e.setCancelled(true);
        }
        if (e.getNewSlot() == 2) {
            Bukkit.getPluginManager().callEvent(new SkillCastEvent(e.getPlayer(), 3));
            e.setCancelled(true);
        }
        if (e.getNewSlot() == 3) {
            Bukkit.getPluginManager().callEvent(new SkillCastEvent(e.getPlayer(), 4));
            e.setCancelled(true);
        }
        if (e.getPlayer().getInventory().getHeldItemSlot() == 0 || e.getPlayer().getInventory().getHeldItemSlot() == 1
                || e.getPlayer().getInventory().getHeldItemSlot() == 2
                || e.getPlayer().getInventory().getHeldItemSlot() == 3)

            e.getPlayer().getInventory().setHeldItemSlot(4);

    }

    // No offhand
    @EventHandler
    public void dontChangeOffHandSlot(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory() == null)
            return;
        if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
            return;

        if (e.getClick().isKeyboardClick()) {
            if (e.getSlot() == 40) {
                e.setCancelled(true);
            }
        }
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            if (e.getSlot() == 40) {
                e.setCancelled(true);
            }
        }
    }

    // NO OffHand Please
    @EventHandler
    public void playerChangeItemSlot(PlayerSwapHandItemsEvent e) {
        if (!(e.getPlayer().getGameMode() == GameMode.CREATIVE)) {
            Dash.r(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void ded(PlayerDeathEvent e) {
        ArrayList<ItemStack> rm = new ArrayList<>();
        for (ItemStack s : e.getDrops()) {
            if (s.getType() == Material.BARRIER) rm.add(s);
            if (s.getType().toString().contains("TERRACOTTA")) rm.add(s);
        }
        e.getDrops().removeAll(rm);
    }


}
