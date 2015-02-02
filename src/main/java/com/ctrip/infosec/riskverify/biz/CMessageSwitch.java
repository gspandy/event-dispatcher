package com.ctrip.infosec.riskverify.biz;

import com.ctrip.infosec.riskverify.orderIndex.oiimpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangsx on 2015/2/2.
 */
@Component
public class CMessageSwitch {
    @Autowired
    private Activity activity;
    @Autowired
    private AirportBus airportBus;
    @Autowired
    private Bus bus;
    @Autowired
    private Car car;
    @Autowired
    private Cruise cruise;
    @Autowired
    private DIY diy;
    @Autowired
    private Flight flight;
    @Autowired
    private GlobalBuy globalBuy;
    @Autowired
    private Golf golf;
    @Autowired
    private HHTravel hhTravel;
    @Autowired
    private Hotel hotel;
    @Autowired
    private Lipin lipin;
    @Autowired
    private Mall mall;
    @Autowired
    private Piao piao;
    @Autowired
    private SceneryHotel sceneryHotel;
    @Autowired
    private Trains trains;
    @Autowired
    private Tuan tuan;
    @Autowired
    private Vacation vacation;
    @Autowired
    private VacationInsurance vacationInsurance;
    @Autowired
    private Visa visa;
    public void stop(){
        activity.stop();
        airportBus.stop();
        bus.stop();
        car.stop();
        cruise.stop();
        diy.stop();
        flight.stop();
        globalBuy.stop();
        golf.stop();
        hhTravel.stop();
        hotel.stop();
        lipin.stop();
        mall.stop();
        piao.stop();
        sceneryHotel.stop();
        trains.stop();
        tuan.stop();
        vacation.stop();
        vacationInsurance.stop();
        visa.stop();
    }

    public void start(){
        activity = new Activity();
        airportBus = new AirportBus();
        bus = new Bus();
        car = new Car();
        cruise = new Cruise();
        diy = new DIY();
        flight = new Flight();
        globalBuy = new GlobalBuy();
        golf = new Golf();
        hhTravel = new HHTravel();
        hotel = new Hotel();
        lipin = new Lipin();
        mall = new Mall();
        piao = new Piao();
        sceneryHotel = new SceneryHotel();
        trains = new Trains();
        tuan = new Tuan();
        vacation = new Vacation();
        vacationInsurance = new VacationInsurance();
        visa = new Visa();

        activity.init();
        airportBus.init();
        bus.init();
        car.init();
        cruise.init();
        diy.init();
        flight.init();
        globalBuy.init();
        golf.init();
        hhTravel.init();
        hotel.init();
        lipin.init();
        mall.init();
        piao.init();
        sceneryHotel.init();
        trains.init();
        tuan.init();
        vacation.init();
        vacationInsurance.init();
        visa.init();
    }
}
