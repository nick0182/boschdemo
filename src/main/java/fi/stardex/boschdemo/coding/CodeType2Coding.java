package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;

import java.util.List;
import java.util.Map;

public class CodeType2Coding extends CodeTypeCoding {

    public CodeType2Coding(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {
        super(injector, realFlowMap, nominalFlowMap);
    }

    @Override
    public String calculateCode() {

        StringBuilder sb = new StringBuilder();

        for (String s : binaryMap.keySet()) {
            if (s.equals("VE2"))
                continue;
            sb.append(binaryMap.get(s));
        }

        sb.append(binaryMap.get("VE2"));

        List<String> list = getBinaryList(sb.toString(), injector.getCodetype());

        List<Integer> finalList = getIntList(list);

        return getMaskCode(finalList);

    }

    @Override
    protected void createBitNumberMap(Map<String, Integer> bitNumberMap) {
        for (InjectorTest test : injector.getInjectorTests()) {
            bitNumberMap.put(test.getTestName().toString(), test.getTestName().getBitNumber());
        }

        bitNumberMap.put("CheckSum", CHECKSUM_BIT_LENGTH);
        bitNumberMap.put("K_Coefficient", K_COEFF_BIT_LENGTH);
    }
}
