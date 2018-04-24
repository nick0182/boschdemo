package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

import java.util.HashMap;
import java.util.Map;

public class Coding {

    private static Map<String, Float> deltaCodingData = new HashMap<>();

    private static Map<String, Integer> preparedCodeData = new HashMap<>();

    public static void calculate(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {

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
        for(String test : preparedCodeData.keySet()) {
            var += preparedCodeData.get(test);
        }
        var += injector.getK_coefficient();

        int checkSum = getCheckSum(var);

        for(String test : preparedCodeData.keySet()) {
            int d = preparedCodeData.get(test);
            if(d < 0) {
                preparedCodeData.put(test, d + (int)Math.pow(2, 5));
            }
        }

        for(Map.Entry<String, Integer> entry : preparedCodeData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

    }

    private static int getCheckSum(int var) {
        return (var & 15) + ((var & 240) >> 4) + 1 &15;
    }
}
