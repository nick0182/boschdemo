package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

import java.util.*;

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

    public String calculate() {
        createBitNumberMap(bitNumberMap);

        createDeltaCodingDataMap(deltaCodingDataMap);

        createPreparedCodeDataMap(preparedCodeDataMap);

        for (String s : preparedCodeDataMap.keySet()) {
            binaryMap.put(s, convertIntToBinarySystem(preparedCodeDataMap.get(s), bitNumberMap.get(s)));
        }

        return calculateCode();
    }

    protected abstract String calculateCode();

    protected abstract void createBitNumberMap(Map<String, Integer> bitNumberMap);

    protected void createDeltaCodingDataMap(Map<String, Float> deltaCodingDataMap) {
        for (String test : realFlowMap.keySet()) {
            deltaCodingDataMap.put(test, nominalFlowMap.get(test) - realFlowMap.get(test));
        }
    }

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

    protected List<String> getBinaryList(String element, int codetype) {
        List<String> binaryList = new LinkedList<>();
        if(codetype == 1 || codetype == 2) {
            for (int i = 0; i < element.length(); i += 5) {
                binaryList.add(element.substring(i, i + 5));
            }
        }
        if(codetype == 3 || codetype == 4) {
            for (int i = 0; i < element.length(); i += 4) {
                binaryList.add(element.substring(i, i + 4));
            }
        }
        return binaryList;
    }

    protected String convertIntToBinarySystem(int element, int bit_number) {
        String s = Integer.toBinaryString(element);
        int count = s.length();
        StringBuilder sb = new StringBuilder();
        while (count < bit_number) {
            sb.append('0');
            count++;
        }
        sb.append(s);
        return sb.toString();
    }

    protected List<Integer> getIntList(List<String> list) {
        List<Integer> intList = new LinkedList<>();
        for(String s : list) {
            intList.add(Integer.parseInt(s, 2));
        }
        return intList;
    }

    protected String getMaskCode(List<Integer> finalList) {
        StringBuilder sb = new StringBuilder();
        char[] mask = MASK.toCharArray();
        for(Integer i : finalList) {
            sb.append(mask[i]);
        }
        return sb.toString();
    }

    protected String getHexCode(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String x : list) {
            sb.append(Integer.toString(Integer.parseInt(x, 2), 16).toUpperCase());
        }
        return sb.toString();
    }
}
