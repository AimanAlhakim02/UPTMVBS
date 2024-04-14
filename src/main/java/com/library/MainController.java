package com.library;

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
import java.util.List;  // Import for List
import java.util.Map;  // Import for Map

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
    public String makebooking(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("rooms", roomBookingsService.listAll());  // Corrected method call
        return "makebooking";
    }

    @GetMapping("/admin/createroom")
    public String createroom(Model model, @ModelAttribute("message") String message,
                              @ModelAttribute("swal") String swal,
                              @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("message", message);
        model.addAttribute("swal", swal);
        model.addAttribute("errorMessage", errorMessage);
        return "/admin/createroom";
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
