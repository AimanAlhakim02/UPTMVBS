package com.library.roombookings;

import com.library.roombookings.RoomBookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import org.springframework.data.repository.query.Param;


@Repository
public interface RoomBookingsRepository extends JpaRepository<RoomBookings, Integer> {

    // Changed method for searching using LIKE for partial matches
    @Query(value = "SELECT rb FROM RoomBookings rb WHERE rb.roomCode LIKE %:keyword% OR rb.roomName LIKE %:keyword% OR rb.customerName LIKE %:keyword%", nativeQuery = false)
    Page<RoomBookings> search(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM room_bookings WHERE booking_date = CAST(:date AS DATE)", nativeQuery = true)
    List<RoomBookings> findLiveBookings(@Param("date") LocalDate date);


    // Method to count bookings by ID - you already have this
    Long countById(Integer id);

}