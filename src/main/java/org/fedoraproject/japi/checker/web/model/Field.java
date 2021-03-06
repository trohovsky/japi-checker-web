package org.fedoraproject.japi.checker.web.model;

import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;

/**
 * Field generated by hbm2java
 */
public class Field extends FieldData implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private int id;

    public Field() {
        super();
    }

    public Field(ClassData owner, int access, String name, String descriptor,
            String value) {
        super(owner, access, name, descriptor, value);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
