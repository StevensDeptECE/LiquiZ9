package org.liquiz.stevens.util;

import edu.ksu.lti.launch.model.LtiLaunchData;
import org.apache.log4j.Logger;

import java.util.List;

public class RoleChecker {
    private static final Logger LOG = Logger.getLogger(RoleChecker.class);
    private final List<LtiLaunchData.InstitutionRole> validRoles;

    public RoleChecker(List<LtiLaunchData.InstitutionRole> validRoles){
        this.validRoles = validRoles;
    }

    public boolean roleAllowed(List<LtiLaunchData.InstitutionRole> userRoles){
        if(userRoles == null || userRoles.isEmpty()){
            LOG.warn("Found empty role list");
            return false;
        }
        return validRoles.stream().anyMatch(userRoles::contains);
    }
}
