package com.busybee.dbservice.model.specbook;


import com.busybee.dbservice.model.specbook.enums.Status;

/**
 * Created with IntelliJ IDEA.
 * User: jeffgardner
 * Date: 4/16/15
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SpecDeletable {
    void setStatus(Status status);
}
