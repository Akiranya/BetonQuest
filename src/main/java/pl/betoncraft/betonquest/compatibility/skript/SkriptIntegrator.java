package pl.betoncraft.betonquest.compatibility.skript;

import ch.njol.skript.Skript;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.compatibility.Integrator;


public class SkriptIntegrator implements Integrator {

    private BetonQuest plugin;

    public SkriptIntegrator() {
        plugin = BetonQuest.getInstance();
    }

    @Override
    public void hook() {
        Skript.registerCondition(SkriptConditionBQ.class, "%player% (meet|meets) [betonquest] condition %string%");
        Skript.registerEffect(SkriptEffectBQ.class, "fire [betonquest] event %string% for %player%");
        Skript.registerEvent("betonquest", SkriptEventBQ.class, BQEventSkript.CustomEventForSkript.class,
                "[betonquest] event %string%");
        plugin.registerEvents("skript", BQEventSkript.class);
    }

    @Override
    public void reload() {

    }

    @Override
    public void close() {

    }

}
