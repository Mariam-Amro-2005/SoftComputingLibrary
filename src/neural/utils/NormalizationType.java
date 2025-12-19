package neural.utils;

public enum NormalizationType {
    MIN_MAX("min-max"),
    Z_SCORE("z-score");

    private final String text;

    NormalizationType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static NormalizationType fromString(String text) {
        if (text != null) {
            for (NormalizationType type : NormalizationType.values()) {
                if (text.equalsIgnoreCase(type.text)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Unknown normalization type: " + text);
    }
}
