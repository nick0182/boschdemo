package fi.stardex.boschdemo.coding;

import java.util.HashMap;
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

    public abstract String calculate(Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap);

}
