package com.commerzi.app.dto;

import com.commerzi.app.route.actualRoute.ActualRoute;
import com.commerzi.app.route.visit.EVisitStatus;

public class UpdateVisitDTO {

    private ActualRoute actualRoute;

    private int visitIndex;

    private EVisitStatus newStatus;

    public UpdateVisitDTO() {
    }

    public UpdateVisitDTO(ActualRoute actualRoute, int visitIndex, EVisitStatus newStatus) {
        this.actualRoute = actualRoute;
        this.visitIndex = visitIndex;
        this.newStatus = newStatus;
    }

    public ActualRoute getActualRoute() {
        return actualRoute;
    }

    public void setActualRoute(ActualRoute actualRoute) {
        this.actualRoute = actualRoute;
    }

    public int getVisitIndex() {
        return visitIndex;
    }

    public void setVisitIndex(int visitIndex) {
        this.visitIndex = visitIndex;
    }

    public EVisitStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(EVisitStatus newStatus) {
        this.newStatus = newStatus;
    }

}
