package com.example.demo.entities;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class TrafficReportEntry {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String droneId;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private Integer speed;

    @Column(nullable = false)
    private TrafficCondition trafficCondition;

    @Override
    public String toString() {
        return "TrafficReportEntry [droneId=" + droneId + ", time=" + time
                + ", speed=" + speed + ", trafficCondition=" + trafficCondition
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((droneId == null) ? 0 : droneId.hashCode());
        result = prime * result + ((speed == null) ? 0 : speed.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime
                * result
                + ((trafficCondition == null) ? 0 : trafficCondition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TrafficReportEntry other = (TrafficReportEntry) obj;
        if (droneId == null) {
            if (other.droneId != null)
                return false;
        } else if (!droneId.equals(other.droneId))
            return false;
        if (speed == null) {
            if (other.speed != null)
                return false;
        } else if (!speed.equals(other.speed))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (trafficCondition != other.trafficCondition)
            return false;
        return true;
    }
}
