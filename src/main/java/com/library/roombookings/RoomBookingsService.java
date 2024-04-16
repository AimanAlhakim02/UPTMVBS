package com.library.roombookings;
import com.library.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomBookingsService {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingsService.class);

    private static final int SEARCH_RESULT_PER_PAGE = 10; // Define the constant

    @Autowired
    private RoomBookingsRepository repo;

    public List<RoomBookings> listAll() {
        return repo.findAll();
    }

    public void save(RoomBookings booking) {
        repo.save(booking);
    }

    public RoomBookings get(Integer id) throws RoomBookingsNotFoundException {
        return repo.findById(id).orElseThrow(RoomBookingsNotFoundException::new);
    }

    public Page<RoomBookings> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return repo.search(keyword, pageable); // Adjust this method if necessary to match your repository capabilities
    }

    public Page<RoomBookings> search(String keyword) {
        return search(keyword, 1); // Default to the first page
    }


    public void delete(Integer id) throws RoomBookingsNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new RoomBookingsNotFoundException("Room booking not found with ID: " + id);
        }
        repo.deleteById(id);
    }

    public List<RoomBookings> getLiveBookings() {
        LocalDate today = LocalDate.now();
        logger.debug("Fetching live bookings for date: {}", today);
        try {
            List<RoomBookings> liveBookings = repo.findLiveBookings(today);
            logger.debug("Found {} live bookings for {}", liveBookings.size(), today);
            return liveBookings;
        } catch (Exception e) {
            logger.error("Error fetching live bookings", e);
            throw e;
        }
    }

    public Map<String, List<RoomBookings>> getLiveBookingsGroupedByRoom() {
        try {
            logger.debug("Fetching grouped live bookings");
            List<RoomBookings> bookings = getLiveBookings(); // Fetch the live bookings
            logger.debug("Live bookings retrieved: {}", bookings);

            Map<String, List<RoomBookings>> groupedBookings = bookings.stream()
                    .collect(Collectors.groupingBy(RoomBookings::getRoomName));

            logger.debug("Grouped bookings: {}", groupedBookings);
            return groupedBookings;
        } catch (Exception e) {
            logger.error("Error in getLiveBookingsGroupedByRoom", e);
            return new HashMap<>(); // Return an empty map or handle as appropriate
        }
    }




}