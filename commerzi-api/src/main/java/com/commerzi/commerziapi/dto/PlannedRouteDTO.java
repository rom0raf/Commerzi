package com.commerzi.commerziapi.dto;

import java.util.List;

public class PlannedRouteDTO {

    private List<String> customersId;

    public PlannedRouteDTO() {
    }

    public List<String> getCustomersId() {
        return customersId;
    }

    public void setCustomersId(List<String> customersId) {
        this.customersId = customersId;
    }
}
