package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;

import java.util.*;

public class CodeType1Coding extends CodeTypeCoding {

    private Injector injector;

    public CodeType1Coding(Injector injector) {
        this.injector  =injector;
    }

    @Override
    public String calculate(Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {

        Map<String, Integer> bitNumberMap = new LinkedHashMap<>();

        Map<String, Float> deltaCodingData = new LinkedHashMap<>();

        Map<String, Integer> preparedCodeData = new LinkedHashMap<>();

        Map<String, String> binaryMap = new LinkedHashMap<>();

        createBitNumberMap(injector, bitNumberMap);

        for (String test : realFlowMap.keySet()) {
            deltaCodingData.put(test, nominalFlowMap.get(test) - realFlowMap.get(test));
        }

        for (String test : realFlowMap.keySet()) {
            preparedCodeData.put(test, Math.round(deltaCodingData.get(test) / k_coeffMap.get(injector.getK_coefficient())));
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

        StringBuilder sb = new StringBuilder();

        for (String s : binaryMap.keySet()) {
            if (s.equals("VE2"))
                continue;
            sb.append(binaryMap.get(s));
        }

        List<String> list = getBinaryList(sb.toString(), injector.getCodetype());

        List<Integer> finalList = getIntList(list);

        return getCode(finalList);

        /*for (Map.Entry<String, Integer> entry : preparedCodeData.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : bitNumberMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        for (Map.Entry<String, String> entry : binaryMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/

    }

    private void createBitNumberMap(Injector injector, Map<String, Integer> bitNumberMap) {
        for (InjectorTest test : injector.getInjectorTests()) {
            bitNumberMap.put(test.getTestName().toString(), test.getTestName().getBitNumber());
        }

        bitNumberMap.put("CheckSum", CHECKSUM_BIT_LENGTH);
        bitNumberMap.put("K_Coefficient", K_COEFF_BIT_LENGTH);
    }

    private int getCheckSum(int var) {
        return (var & 15) + ((var & 240) >> 4) + 1 & 15;
    }

    private String convertIntToBinarySystem(int element, int bit_number) {
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

    private List<String> getBinaryList(String element, int codetype) {
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

    private List<Integer> getIntList(List<String> list) {
        List<Integer> intList = new LinkedList<>();
        for(String s : list) {
            intList.add(Integer.parseInt(s, 2));
        }
        return intList;
    }

    private String getCode(List<Integer> finalList) {
        StringBuilder sb = new StringBuilder();
        char[] mask = MASK.toCharArray();
        for(int i : finalList) {
            sb.append(mask[i]);
        }
        return sb.toString();
    }
}
