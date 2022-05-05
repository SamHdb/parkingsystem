package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.parkit.parkingsystem.constants.Fare.*;
import static com.parkit.parkingsystem.constants.ParkingType.BIKE;

public class FareCalculatorService {

    public static final DecimalFormat df = new DecimalFormat("#.##");

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        long duration = outHour - inHour;

        // la durée est par défaut en ms, on la convertit en heures
        float durationInHour = duration / 3600000f;

        double coeff = 1;
        if (ticket.isRecurrent()) {
            coeff = DISCOUNT_RECURRENT_USER;
        }

        // si duration < 30 min alors prix à 0 sinon on déduit du calcul les 30 min gratuites
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                if (durationInHour < 0.5) {
                    ticket.setPrice(0);
                } else{
                    ticket.setPrice((durationInHour - THIRTY_MIN_FREE) * Fare.CAR_RATE_PER_HOUR * coeff );
                }
                break;
            }
            case BIKE: {
                if (durationInHour < 0.5) {
                    ticket.setPrice(0);
                } else {
                    ticket.setPrice((durationInHour - THIRTY_MIN_FREE) * Fare.BIKE_RATE_PER_HOUR * coeff);
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}