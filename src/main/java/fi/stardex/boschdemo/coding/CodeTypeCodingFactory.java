package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

import java.util.Map;

public class CodeTypeCodingFactory {
    public static CodeTypeCoding getCodTypeCoding(Injector injector, Map<String, Float> realFlowMap, Map<String, Float> nominalFlowMap) throws Exception {
        switch(injector.getCodetype()) {
            case 1:
                return new CodeType1Coding(injector, realFlowMap, nominalFlowMap);
            case 2:
                return new CodeType2Coding(injector, realFlowMap, nominalFlowMap);
            case 3:
                return new CodeType3Coding(injector, realFlowMap, nominalFlowMap);
            case 4:
                return new CodeType4Coding(injector, realFlowMap, nominalFlowMap);
            default:
                throw new Exception("Wrong code type!");
        }
    }
}
