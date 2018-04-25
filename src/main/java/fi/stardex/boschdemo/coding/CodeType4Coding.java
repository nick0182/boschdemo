package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;
import fi.stardex.boschdemo.persistance.orm.InjectorTest;

import java.util.List;
import java.util.Map;

public class CodeType4Coding extends CodeTypeCoding {

    public CodeType4Coding(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) {
        super(injector, realFlowMap, nominalFlowMap);
    }

    @Override
    public String calculateCode() {

        for (String s : preparedCodeDataMap.keySet()) {
            binaryMap.put(s, convertIntToBinarySystem(preparedCodeDataMap.get(s), bitNumberMap.get(s)));
        }

        StringBuilder sb = new StringBuilder();

        for (String s : binaryMap.keySet()) {
            if (s.equals("CheckSum"))
                break;
            sb.append(binaryMap.get(s));
        }

        sb.append(binaryMap.get("K_Coefficient"));
        sb.append("0");
        sb.append(binaryMap.get("CheckSum"));

        List<String> list = getBinaryList(sb.toString(), injector.getCodetype());

        return getHexCode(list);

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
