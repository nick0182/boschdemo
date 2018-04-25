package fi.stardex.boschdemo.coding;

import fi.stardex.boschdemo.persistance.orm.Injector;

public class CodeTypeCodingFactory {
    public static CodeTypeCoding getCodTypeCoding(Injector injector) {
        switch(injector.getCodetype()) {
            case 1:
                return new CodeType1Coding(injector);
            /*    break;
            case 2:
                return new CodeType2Coding();
                break;
            case 3:
                return new CodeType3Coding();
                break;
            case 4:
                return new CodeType4Coding();
                break;*/
            default:
                return null;
        }
    }
}
