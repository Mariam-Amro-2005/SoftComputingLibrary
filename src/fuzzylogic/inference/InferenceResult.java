package fuzzylogic.inference;

public class InferenceResult {

    private final MamdaniOutput mamdaniOutput;
    private final SugenoOutput sugenoOutput;

    public InferenceResult(MamdaniOutput mamdaniOutput) {
        this.mamdaniOutput = mamdaniOutput;
        this.sugenoOutput = null;
    }

    public InferenceResult(SugenoOutput sugenoOutput) {
        this.mamdaniOutput = null;
        this.sugenoOutput = sugenoOutput;
    }

    public boolean isMamdani() {
        return mamdaniOutput != null;
    }

    public boolean isSugeno() {
        return sugenoOutput != null;
    }

    public MamdaniOutput getMamdaniOutput() {
        return mamdaniOutput;
    }

    public SugenoOutput getSugenoOutput() {
        return sugenoOutput;
    }
}
