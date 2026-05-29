package co.edu.udistrital.controller;

import co.edu.udistrital.view.*;
import co.edu.udistrital.model.entities.*;
import co.edu.udistrital.model.enums.*;
import co.edu.udistrital.model.structures.*;

public class AppController {
    private MainView mainView;
    private UnitsView unitsView;
    private RequestsView requestsView;

    private SimpleList<ServiceUnit> unitList;

    public AppController() {
        this.unitList = new SimpleList<>();
        
        generateTestData();
    }

    // Inyectamos las vistas creadas para manipularlas
    public void setViews(MainView main, UnitsView units, RequestsView requests) {
        this.mainView = main;
        this.unitsView = units;
        this.requestsView = requests;
    }

    private void generateTestData() {
        // Simulando Unidades de Servicio
        unitList.add(new ServiceUnit(UnitType.CRANE, UnitStatus.AVAILABLE, OperationZone.KENNEDY));
        unitList.add(new ServiceUnit(UnitType.MOTORCYCLE, UnitStatus.ASSIGNED, OperationZone.CHAPINERO));
        unitList.add(new ServiceUnit(UnitType.TRUCK, UnitStatus.MAINTENANCE, OperationZone.SUBA));
        unitList.add(new ServiceUnit(UnitType.CAR, UnitStatus.AVAILABLE, OperationZone.FONTIBON));
    }
}