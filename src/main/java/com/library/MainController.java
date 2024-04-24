package com.library;

import com.library.room.Room;
import com.library.room.RoomService;
import com.library.roombookings.RoomBookings;
import com.library.roombookings.RoomBookingsService; // Import the service class
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;  // Import for List
import java.util.Map;  // Import for Map
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private RoomBookingsService roomBookingsService;  // Service injection

    @Autowired
    private RoomService roomService;

    @GetMapping("/")
    public String showHomePage() {
        logger.debug("Showing home page");
        return "index";
    }

    // Method to generate time slots
    public List<String> generateTimeSlots() {
        List<String> slots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(6, 0); // Start at 6:00 AM
        while (startTime.isBefore(LocalTime.of(22, 0))) { // Up to 10:00 PM
            slots.add(startTime.toString());
            startTime = startTime.plusMinutes(30); // Increment by 30 minutes
        }
        return slots;
    }

    @GetMapping("/index")
    public String secondIndex(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomService.listAll());
        return "index";
    }

    @GetMapping("/neosecondindex")
    public String neosecondIndex(Model model, @ModelAttribute("message") String message,
                                 @ModelAttribute("swal") String swal,
                                 @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        return "neosecondindex";
    }

    @GetMapping("/makebooking")
    public String makeBooking(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        List<Room> rooms = roomService.listAll(); // Fetch all rooms
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", rooms); // Add rooms to the model for dropdown
        model.addAttribute("timeSlots", generateTimeSlots());
        List<String> slots = generateTimeSlots();
        logger.debug("Generated Time Slots: {}", slots);
        model.addAttribute("today", LocalDate.now());
        return "makebooking"; // The Thymeleaf template that has the form
    }


    @GetMapping("/admin/createroom")
    public String createroom(Model model, @ModelAttribute("message") String message,
                             @ModelAttribute("swal") String swal,
                             @ModelAttribute("errorMessage") String errorMessage) {
        logger.info("swal: {}", model.getAttribute("swal"));
        logger.info("message: {}", model.getAttribute("message"));
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomService.listAll());
        return "admin/createroom"; // Remove the leading slash if not necessary depending on your config
    }

    @GetMapping("/admin/managebooking")
    public String managebooking(Model model, @ModelAttribute("message") String message,
                                @ModelAttribute("swal") String swal,
                                @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomBookingsService.listAll());  // Corrected method call
        return "/admin/managebooking";
    }

    @GetMapping("/liveTracking")
    public String liveTracking(Model model) {
        logger.debug("Entering liveTracking method");
        try {
            Map<String, List<RoomBookings>> groupedBookings = roomBookingsService.getLiveBookingsGroupedByRoom();
            logger.debug("Grouped bookings fetched, count: {}", groupedBookings.size());
            model.addAttribute("liveBookings", groupedBookings);
            return "liveTracking";
        } catch (Exception e) {
            logger.error("Error in liveTracking", e);
            model.addAttribute("errorMessage", "Error retrieving live bookings: " + e.getMessage());
            return "error";
        }
    }
}