package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;

import java.util.HashMap;
import java.util.Map;

public class Coding {

    public static final int CHECKSUM_BIT_LENGTH = 4;

    public static final int K_COEFF_BIT_LENGTH = 2;

    public static void calculate(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {

        Map<String, Integer> bitNumberMap = new HashMap<>();

        Map<String, Float> deltaCodingData = new HashMap<>();

        Map<String, Integer> preparedCodeData = new HashMap<>();

        Map<String, String> binaryMap = new HashMap<>();

        for (InjectorTest test : injector.getInjectorTests()) {
            bitNumberMap.put(test.getTestName().toString(), test.getTestName().getBitNumber());
        }

        bitNumberMap.put("CheckSum", CHECKSUM_BIT_LENGTH);
        bitNumberMap.put("K_Coefficient", K_COEFF_BIT_LENGTH);

        for (String test : realFlowMap.keySet()) {
            deltaCodingData.put(test, nominalFlowMap.get(test) - realFlowMap.get(test));
        }

        float coefficient;

        switch (injector.getK_coefficient()) {
            case 0:
                coefficient = 0.1f;
                break;
            case 1:
                coefficient = 0.15f;
                break;
            case 2:
                coefficient = 0.25f;
                break;
            default:
                coefficient = 0f;
                break;
        }

        for (String test : realFlowMap.keySet()) {
            preparedCodeData.put(test, Math.round(deltaCodingData.get(test) / coefficient));
        }

        int var = 0;
        for (String test : preparedCodeData.keySet()) {
            var += preparedCodeData.get(test);
        }
        var += injector.getK_coefficient();

        int checkSum = getCheckSum(var);

        for (String test : preparedCodeData.keySet()) {
            int d = preparedCodeData.get(test);
            if (d < 0) {
                preparedCodeData.put(test, d + (int) Math.pow(2, bitNumberMap.get(test)));
            }
        }

        preparedCodeData.put("CheckSum", checkSum);
        preparedCodeData.put("K_Coefficient", injector.getK_coefficient());

        for (String s : preparedCodeData.keySet()) {
            binaryMap.put(s, convertIntToBinarySystem(preparedCodeData.get(s), bitNumberMap.get(s)));
        }

        /*for (Map.Entry<String, Integer> entry : preparedCodeData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : bitNumberMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/

        for (Map.Entry<String, String> entry : binaryMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

    }

    private static int getCheckSum(int var) {
        return (var & 15) + ((var & 240) >> 4) + 1 & 15;
    }

    private static String convertIntToBinarySystem(int element, int bit_number) {
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
}
