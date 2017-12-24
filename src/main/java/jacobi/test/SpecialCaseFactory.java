package jacobi.test;

public class SpecialCaseFactory {

    public static SpecialCase createSpecialCase(int caseOrder) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (SpecialCase) Class.forName(SpecialCase.class.getName()+String.valueOf(caseOrder)).newInstance();
    }
}
