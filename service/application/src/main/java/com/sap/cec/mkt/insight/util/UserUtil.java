package com.sap.cec.mkt.insight.util;

import com.sap.cec.mkt.insight.models.UserDetails;
import com.sap.cloud.sdk.cloudplatform.security.user.SimpleUserAttribute;
import com.sap.cloud.sdk.cloudplatform.security.user.User;
import com.sap.cloud.sdk.cloudplatform.security.user.UserAccessor;

/** LoggedIn user utility class **/
public class UserUtil {

    /** Get LoggedIn User **/
    public static User getLoggedInUser() {
	return UserAccessor.getCurrentUser();
    }

    /** Get LoggedIn User details **/
    public static UserDetails getLoggedInUserDetails() {
	// UserProvider provides access to the user storage
	User user = getLoggedInUser();
	UserDetails userDetails = new UserDetails();
	userDetails.setUserId(user.getName());
	SimpleUserAttribute<?> att = (SimpleUserAttribute<?>) user.getAttribute("firstname").get();
	if (att != null && att.getValue() != null) {
	    userDetails.setFirstName(att.getValue().toString());
	}
	att = (SimpleUserAttribute<?>) user.getAttribute("lastname").get();
	if (att != null && att.getValue() != null) {
	    userDetails.setLastName(att.getValue().toString());
	}
	return userDetails;
    }
}
