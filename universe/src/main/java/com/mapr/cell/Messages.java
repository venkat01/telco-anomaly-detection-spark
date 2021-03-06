package com.mapr.cell;

import akka.actor.ActorRef;
import com.mapr.cell.common.CDR;

/**
 * Classes for each kind of message
 */
public class Messages {
    /**
     * Gives users and towers access to the rest of the universe
     */
    public static class Setup {
        final ActorRef universe;
        final ActorRef towers;
        final ActorRef users;

        public Setup(ActorRef universe, ActorRef towers, ActorRef users) {
            this.universe = universe;
            this.towers = towers;
            this.users = users;
        }
    }

    /**
     * Indicates that the universe should start the simulation
     */
    public static class Start {}

    /**
     * Indicates the passage of a bit of simulation time. Sent to all users.
     */
    public static class Tick {}

    /**
     * Requests a signal report. Sent from caller to all towers.
     */
    public static class SignalReportRequest {
        final ActorRef source;
        final double x;
        final double y;

        public SignalReportRequest(ActorRef source, double x, double y) {
            this.source = source;
            this.x = x;
            this.y = y;
        }
    }

    public static class Move {
        final String callerId;
        final double x;
        final double y;

        public Move(String callerId, double x, double y) {
            this.callerId = callerId;
            this.x = x;
            this.y = y;
        }

        public String getCallerId() {
            return callerId;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    /**
     * Signal report from tower to caller.
     */
    public static class SignalReport {
        final double distance;
        final double power;
        final String towerId;
        final ActorRef tower;

        public SignalReport(double distance, double power, String towerId, ActorRef tower) {
            this.distance = distance;
            this.power = power;
            this.towerId = towerId;
            this.tower = tower;
        }
    }

    /**
     * Tells the caller that the tower will not accept the connection. Sent in response to Hello.
     */
    public static class Fail {
        final String towerId;

        public Fail(String towerId) {
            this.towerId = towerId;
        }
    }

    /**
     * Tells the caller that the tower has accepted the connection. Sent in response to Hello.
     */
    public static class Connect {
        final String towerId;
        final ActorRef tower;

        public Connect(String towerId, ActorRef tower) {
            this.towerId = towerId;
            this.tower = tower;
        }
    }

    /**
     * Sent by the caller to indicate it is disconnecting the current call.
     */
    public static class Disconnect {
        String callerId;
        final CDR cdr;

        public Disconnect(String callerId, CDR cdr) {
            this.callerId = callerId;
            this.cdr = cdr;
        }
    }

    /**
     * Sent by the caller to ask the tower if it will accept a call.
     */
    public static class Hello {
        public final ActorRef caller;
        final CDR cdr;
        boolean reconnect;

        public Hello(ActorRef self, CDR cdr, boolean reconnect) {
            this.caller = self;
            this.cdr = cdr;
            this.reconnect = reconnect;
        }
    }

    /**
     * Sent by the caller to record data (essentially a call data record)
     */
    public static class Log {
    }
}
