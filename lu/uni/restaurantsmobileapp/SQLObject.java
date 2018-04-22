package lu.uni.restaurantsmobileapp;

public class SQLObject {

    private Object object;
    private int type;

    public SQLObject(Object object, int type) {
        this.object = object;
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public int getType() {
        return type;
    }
}
