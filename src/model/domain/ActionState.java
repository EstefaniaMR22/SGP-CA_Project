package model.domain;

public enum ActionState {
    ACTIVE("ALTA"),
    INACTIVE("BAJA");

    private final String actionState;

    ActionState(String actionState) {
        this.actionState = actionState;
    }

    public String getActionState() {
        return actionState;
    }

    @Override
    public String toString() {
        return "ActionState{" +
                "actionState='" + actionState + '\'' +
                '}';
    }
}
