package com.busybee.dbservice.model.specbook;

/**
 * Created with IntelliJ IDEA.
 * User: jeffgardner
 * Date: 6/11/15
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceType {
    private Long id;
    private String displayName;
    private String name;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return displayName;
    }

    public String getCode() {
        return displayName;
    }

    public void setCode(String code) {
        this.displayName = name;
        this.code = code;
    }

    public void setName(String name) {
        this.displayName = name;
        this.name = name;
    }
}
