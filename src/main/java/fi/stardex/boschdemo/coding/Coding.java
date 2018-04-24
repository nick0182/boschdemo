package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

import java.util.HashMap;
import java.util.Map;

public class Coding {

    public static void calculate(Injector injector, Map<String, Double> realFlowMap, Map<String, Double> nominalFlowMap) {
        Map<String, Double> deltaCodingData = new HashMap<>();

        for(String test : realFlowMap.keySet()) {
            deltaCodingData.put(test, nominalFlowMap.get(test) - realFlowMap.get(test));
        }

        for(Map.Entry<String, Double> entry : deltaCodingData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
