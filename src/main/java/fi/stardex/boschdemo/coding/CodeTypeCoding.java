package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CodeTypeCoding {

    protected static final String MASK = "ABCDEFGHIKLMNOPRSTUVWXYZ12345678";

    protected static final int CHECKSUM_BIT_LENGTH = 4;

    protected static final int K_COEFF_BIT_LENGTH = 2;

    public static Map<Integer, Float> k_coeffMap = new HashMap<>();

    static {
        k_coeffMap.put(0, 0.1f);
        k_coeffMap.put(1, 0.15f);
        k_coeffMap.put(2, 0.25f);
    }

    protected Map<String, Integer> bitNumberMap = new LinkedHashMap<>();

    protected Map<String, Float> deltaCodingDataMap = new LinkedHashMap<>();

    protected Map<String, Integer> preparedCodeDataMap = new LinkedHashMap<>();

    protected Map<String, String> binaryMap = new LinkedHashMap<>();

    protected Injector injector;

    protected Map<String, Float> realFlowMap;

    protected Map<String, Float> nominalFlowMap;

    public CodeTypeCoding(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {
        this.injector = injector;
        this.realFlowMap = realFlowMap;
        this.nominalFlowMap = nominalFlowMap;
    }

    public abstract String calculate();

    protected abstract void createBitNumberMap(Map<String, Integer> bitNumberMap);

    protected abstract void createDeltaCodingDataMap(Map<String, Float> deltaCodingDataMap);

    protected void createPreparedCodeDataMap(Map<String, Integer> preparedCodeDataMap) {
        for (String test : realFlowMap.keySet()) {
            preparedCodeDataMap.put(test, Math.round(deltaCodingDataMap.get(test) / k_coeffMap.get(injector.getK_coefficient())));
        }

        int var = 0;
        for (String test : preparedCodeDataMap.keySet()) {
            var += preparedCodeDataMap.get(test);
        }
        var += injector.getK_coefficient();

        int checkSum = getCheckSum(var);

        for (String test : preparedCodeDataMap.keySet()) {
            int d = preparedCodeDataMap.get(test);
            if (d < 0) {
                preparedCodeDataMap.put(test, d + (int) Math.pow(2, bitNumberMap.get(test)));
            }
        }

        preparedCodeDataMap.put("CheckSum", checkSum);
        preparedCodeDataMap.put("K_Coefficient", injector.getK_coefficient());
    }

    private int getCheckSum(int var) {
        return (var & 15) + ((var & 240) >> 4) + 1 & 15;
    }
}
