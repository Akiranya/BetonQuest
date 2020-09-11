package pl.betoncraft.betonquest.conditions;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Condition;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.id.ConditionID;

import java.util.List;

/**
 * All of specified conditions have to be true
 */
public class ConjunctionCondition extends Condition {

    private List<ConditionID> conditions;

    public ConjunctionCondition(final Instruction instruction) throws InstructionParseException {
        super(instruction, false);
        conditions = instruction.getList(e -> instruction.getCondition(e));
    }

    @Override
    protected Boolean execute(final String playerID) {
        for (final ConditionID condition : conditions) {
            if (!BetonQuest.condition(playerID, condition)) {
                return false;
            }
        }
        return true;
    }
}
