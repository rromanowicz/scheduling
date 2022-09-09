package ex.rr.scheduling.model.enums;

public enum SettingsSubTypeEnum {

    HOUR(false),
    MAX_USERS(true);

    private final boolean isUnique;

    SettingsSubTypeEnum(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public boolean isUnique() {
        return isUnique;
    }
}
