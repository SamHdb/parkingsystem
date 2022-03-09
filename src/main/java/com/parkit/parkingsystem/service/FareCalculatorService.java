package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.parkit.parkingsystem.constants.Fare.*;
import static com.parkit.parkingsystem.constants.ParkingType.BIKE;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long outHour = ticket.getOutTime().getTime();
        long inHour = ticket.getInTime().getTime();
        long duration = outHour - inHour;
        float durationInHour = duration / 3600000.00f; // la durée est par défaut en ms, on la convertit en heures

        // on prend en compte les 30 min gratuites, si duration < 30 min alors prix à 0 sinon on déduit du calcul les 30 min gratuites

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (durationInHour < 0.5) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice((durationInHour - THIRTY_MIN_FREE) * Fare.CAR_RATE_PER_HOUR);
                }
                break;
            }
            case BIKE: {
                if (durationInHour < 0.5) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice((durationInHour - THIRTY_MIN_FREE) * Fare.BIKE_RATE_PER_HOUR);
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}