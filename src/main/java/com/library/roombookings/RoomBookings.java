package com.library.roombookings;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "room_bookings")
public class RoomBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_code", nullable = false, length = 50)
    private String roomCode;

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "booking_date", nullable = false)
    private Date bookingDate;

    @Column(name = "booking_time", nullable = false)
    private Time bookingTime;

    @Column(name = "booking_end", nullable = false)
    private Time bookingEnd;

    @Column(name = "room_purpose", nullable = false, length = 50)
    private String roomPurpose;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Time getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Time bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Time getBookingEnd() {
        return bookingEnd;
    }

    public void setBookingEnd(Time bookingEnd) {
        this.bookingEnd = bookingEnd;
    }

    public String getRoomPurpose() {
        return roomPurpose;
    }

    public void setRoomPurpose(String roomPurpose) {
        this.roomPurpose = roomPurpose;
    }

    // Override equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomBookings that = (RoomBookings) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // toString method for debugging

    @Override
    public String toString() {
        return "RoomBookings{" +
                "id=" + getId() +
                ", roomCode='" + getRoomCode() + '\'' +
                ", roomName='" + getRoomName() + '\'' +
                ", customerName='" + getCustomerName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", bookingDate=" + getBookingDate() +
                ", bookingTime=" + getBookingTime() +
                ", bookingEnd=" + getBookingEnd() +
                ", roomPurpose='" + getRoomPurpose() + '\'' +
                '}';
    }
}