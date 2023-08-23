package org.foundation.atomapplicantservice.entity.attributes;

import lombok.Data;

@Data
public class Blacklist {
    private boolean blacklisted = false;
    private String reason = null;
}
