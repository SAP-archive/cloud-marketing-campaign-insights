package com.sap.cec.mkt.insight.models;

import java.util.ArrayList;
import java.util.List;

import com.sap.cloud.sdk.result.ElementName;

public class AccountTeamMembers {

    @ElementName("results")
    private List<AccountTeamMember> results = new ArrayList<>();

    public List<AccountTeamMember> getResults() {
	return results;
    }

    public void setResults(List<AccountTeamMember> results) {
	this.results = results;
    }
}
