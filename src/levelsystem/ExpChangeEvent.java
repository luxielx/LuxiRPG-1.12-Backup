package levelsystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExpChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double amount;

    public ExpChangeEvent(Player player, double amount) {
        this.player = player;
        this.amount = amount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public double getAmount() {
        return amount;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}