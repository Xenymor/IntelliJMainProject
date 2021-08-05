package StandardClasses;

public class MyArrays {

    private MyArrays() {
    }

    public static <T extends Enum<T>> T[][] deepCloneEnum(T[][] toClone) {
        T[][] myClone = toClone.clone();
        for (int i = 0; i < myClone.length; i++) {
            myClone[i] = myClone[i].clone();
        }
        return myClone;
    }
}
