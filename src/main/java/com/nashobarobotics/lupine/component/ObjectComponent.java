package com.nashobarobotics.lupine.component;

public class ObjectComponent extends Component {
    public Object value;

    public ObjectComponent(String name, Object value) {
        super(name, "Object");
        this.value = value;
    }

    public void setObject(Object value) { this.value = value; }
    public Object getObject() { return this.value; }
}
