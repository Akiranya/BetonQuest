package pl.betoncraft.betonquest.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Should be fired when the quest data updates
 */
public class QuestDataUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private String playerID;
    private String objID;
    private String data;

    public QuestDataUpdateEvent(final String playerID, final String objID, final String data) {
        super();
        this.playerID = playerID;
        this.objID = objID;
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getObjID() {
        return objID;
    }

    public String getData() {
        return data;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
