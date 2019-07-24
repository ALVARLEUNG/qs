package com.myproject.qs.qs.model.common;

import java.util.Date;

public interface Auditable {
    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getModifiedBy();

    void setModifiedBy(String modifiedBy);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getModifiedDate();

    void setModifiedDate(Date modifiedDate);
}
