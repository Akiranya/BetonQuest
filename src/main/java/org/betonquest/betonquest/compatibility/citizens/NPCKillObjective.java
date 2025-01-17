package org.betonquest.betonquest.compatibility.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.CountingObjective;
import org.betonquest.betonquest.api.MobKillNotifier.MobKilledEvent;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Player has to kill an NPC.
 */
@SuppressWarnings("PMD.CommentRequired")
public class NPCKillObjective extends CountingObjective implements Listener {

    private final int npcId;

    public NPCKillObjective(final Instruction instruction) throws InstructionParseException {
        super(instruction, "mobs_to_kill");
        npcId = instruction.getInt();
        if (npcId < 0) {
            throw new InstructionParseException("NPC ID cannot be less than 0");
        }
        targetAmount = instruction.getInt(instruction.getOptional("amount"), 1);
        if (targetAmount <= 0) {
            throw new InstructionParseException("Amount cannot be less than 1");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onNpcKill(final MobKilledEvent event) {
        final NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getEntity());
        if (npc == null) {
            return;
        }
        if (npc.getId() != npcId) {
            return;
        }
        final Profile profile = event.getProfile();
        if (containsPlayer(profile) && checkConditions(profile)) {
            getCountingData(profile).progress();
            completeIfDoneOrNotify(profile);
        }
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }
}
