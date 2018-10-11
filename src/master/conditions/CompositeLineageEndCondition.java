package master.conditions;

import beast.core.Input;
import master.model.Node;
import master.model.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeLineageEndCondition extends LineageEndCondition {

    public Input<List<LineageEndCondition>> endConditionsInput = new Input<>(
            "lineageEndCondition",
            "Lineage end condition.",
            new ArrayList<>());

    public Input<Boolean> andModeInput = new Input<>("andMode",
            "If true, condition is met if ALL of the constituent " +
                    "conditions are met. Otherwise it is met if ANY of the " +
                    "constituent conditions are met. (Default true, i.e. ALL.)",
            true);

    List<LineageEndCondition> endConditions;
    boolean andMode;

    public CompositeLineageEndCondition() {
        nLineagesInput.setRule(Input.Validate.FORBIDDEN);
        populationInput.setRule(Input.Validate.FORBIDDEN);
    }

    @Override
    public void initAndValidate() {
        endConditions = endConditionsInput.get();
        andMode = andModeInput.get();
    }

    @Override
    public boolean isMet(Map<Population, List<Node>> activeLineages) {

        if (andMode)
            return endConditions.stream().allMatch(c -> c.isMet(activeLineages));
        else
            return endConditions.stream().anyMatch(c -> c.isMet(activeLineages));
    }
}
