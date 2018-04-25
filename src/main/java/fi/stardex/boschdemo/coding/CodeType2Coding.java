package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeType2Coding extends CodeTypeCoding {

    private Injector injector;

    private Map<String, Float> realFlowMap;

    private Map<String, Float> nominalFlowMap;

    public CodeType2Coding(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {
        this.injector  =injector;
        this.realFlowMap = realFlowMap;
        this.nominalFlowMap = nominalFlowMap;
    }

    @Override
    public String calculate() {

        createBitNumberMap(bitNumberMap);

        createDeltaCodingDataMap(deltaCodingDataMap);

        createPreparedCodeDataMap(preparedCodeDataMap);

        for (String s : preparedCodeDataMap.keySet()) {
            binaryMap.put(s, convertIntToBinarySystem(preparedCodeDataMap.get(s), bitNumberMap.get(s)));
        }

        StringBuilder sb = new StringBuilder();

        for (String s : binaryMap.keySet()) {
            if (s.equals("VE2"))
                continue;
            sb.append(binaryMap.get(s));
        }

        sb.append(binaryMap.get("VE2"));

        List<String> list = getBinaryList(sb.toString(), injector.getCodetype());

        List<Integer> finalList = getIntList(list);

        return getCode(finalList);

    }

    @Override
    protected void createBitNumberMap(Map<String, Integer> bitNumberMap) {
        for (InjectorTest test : injector.getInjectorTests()) {
            bitNumberMap.put(test.getTestName().toString(), test.getTestName().getBitNumber());
        }

        bitNumberMap.put("CheckSum", CHECKSUM_BIT_LENGTH);
        bitNumberMap.put("K_Coefficient", K_COEFF_BIT_LENGTH);
    }

    @Override
    protected void createDeltaCodingDataMap(Map<String, Float> deltaCodingDataMap) {
        for (String test : realFlowMap.keySet()) {
            deltaCodingDataMap.put(test, nominalFlowMap.get(test) - realFlowMap.get(test));
        }
    }

    @Override
    protected void createPreparedCodeDataMap(Map<String,Integer> preparedCodeDataMap) {
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
