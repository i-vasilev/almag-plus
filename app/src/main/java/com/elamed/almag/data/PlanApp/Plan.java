package com.elamed.almag.data.PlanApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Plan {
    private int id;
    private String description;
    private int version;
    private List<ParcelablePlanDetalization> planDetalizationList = new ArrayList<ParcelablePlanDetalization>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ParcelablePlanDetalization> getPlanDetalizationList() {
        return planDetalizationList;
    }

    public void setPlanDetalizationList(List<ParcelablePlanDetalization> planDetalizationList) {
        this.planDetalizationList = planDetalizationList;
    }

    public void sortPlanList(){
        Collections.sort(planDetalizationList);
    }
}
