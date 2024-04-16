/**
 * Controller class for Admin
 */
package com.library.user.auth;

import com.library.roombookings.RoomBookings;
import com.library.roombookings.RoomBookingsService;
import com.library.roombookings.RoomBookingsNotFoundException;
import com.library.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

// Annotate as controller
@Controller
public class    AdminController {

    // Inject instance of UserService
    @Autowired
    private UserService userService;

    @Autowired
    private RoomBookingsService roomBookingsService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    // Map HTTP GET requests for '/admin/student'
    @GetMapping("/admin/manageroom")
    public String manageRoom(Model model) {
        // Use listAll() from studentService service class...
        // to get a List of student
        // Store List of student in liststudent attribute, so we can populate it in Thymeleaf
        // Get size of List of student, so that we know how many entries in the entity

        // Return templates/admin/manageroom.html
        return "admin/manageroom";
    }

    // Add a method to handle search for room bookings
    @GetMapping("/admin/searchBooking")
    public String adminSearchBookings(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        Page<RoomBookings> searchResultPage = roomBookingsService.search(keyword, 1);
        List<RoomBookings> bookingsList = searchResultPage.getContent();

        // Logging the model attributes to check for nulls
        bookingsList.forEach(booking -> {
            logger.info("Booking ID: {}, Code: {}, Name: {}",
                    booking.getId(),
                    booking.getRoomCode(),
                    booking.getRoomName()); // Continue with other fields as necessary
        });

        model.addAttribute("roomBookings", bookingsList);
        model.addAttribute("totalPages", searchResultPage.getTotalPages());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalItems", searchResultPage.getTotalElements());
        model.addAttribute("keyword", keyword);

        logger.info("Model: {}", model); // Print the entire model

        return "admin/searchBooking"; // Make sure this is the correct path to your Thymeleaf template
    }



    @GetMapping("/admin/searchBooking/page/{pageNum}")
    public String adminSearchBookingsByPage(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                            Model model,
                                            @PathVariable(name = "pageNum") int pageNum)  {
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                Page<RoomBookings> result = roomBookingsService.search(keyword, pageNum);
                logger.info("Page {} of search results for keyword '{}'", pageNum, keyword);
                model.addAttribute("roomBookings", result.getContent());
                model.addAttribute("totalPages", result.getTotalPages());
                model.addAttribute("currentPage", pageNum);
                model.addAttribute("totalItems", result.getTotalElements());
                model.addAttribute("keyword", keyword);
            } else {
                logger.info("Listing all room bookings as no search keyword was provided");
                model.addAttribute("roomBookings", roomBookingsService.listAll());
            }
        } catch (Exception e) {
            logger.error("Search exception for keyword '{}': ", keyword, e);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            // Optionally, you can redirect to a custom error page
        }
        return "admin/searchBooking"; // Make sure this is the correct path to your Thymeleaf template
    }


    @GetMapping("/admin/test")
    public String testEndpoint(Model model) {
        return "admin/test"; // This should correspond to src/main/resources/templates/admin/test.html
    }


}