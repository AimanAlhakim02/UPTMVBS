package com.library.roombookings;

import com.library.room.Room;
import com.library.room.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Controller
public class RoomBookingsController {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingsController.class);

    @Autowired
    private RoomBookingsService roombookingsService;

    @Autowired
    private RoomService roomService; // Ensure you have this service



    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");  // Ensure this matches time input

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Time.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(new Time(timeFormat.parse(text).getTime()));  // Parse as HH:mm
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Could not parse time: " + text, e);
                }
            }
        });
    }

    private List<String> generateTimeSlots() {
        List<String> slots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(6, 0);  // Starts at 6:00 AM
        while (startTime.isBefore(LocalTime.of(22, 0))) {  // Ends at 10:00 PM
            slots.add(startTime.format(DateTimeFormatter.ofPattern("HH:mm")));  // Format to HH:mm
            startTime = startTime.plusMinutes(30);  // Increments by 30 minutes
        }
        return slots;
    }




    @GetMapping("/roombookings")
    public String listBookings(Model model) {
        try {
            model.addAttribute("bookings", roombookingsService.listAll());
            return "roombookings";
        } catch (Exception e) {
            logger.error("Error listing bookings: ", e);
            model.addAttribute("errorMessage", "Error retrieving bookings");
            return "error"; // Assume there is an 'error.html' Thymeleaf template
        }
    }


    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute("booking") RoomBookings booking, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save booking: {}", booking); // Log the state of the booking object
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.booking", bindingResult);
            redirectAttributes.addFlashAttribute("booking", booking);
            return "redirect:/makebooking"; // Redirect to the form page to display errors
        }

        try {
            roombookingsService.save(booking);
            logger.info("Booking saved successfully: {}", booking); // Log the successful save
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Booking saved successfully!");
            return "redirect:/makebooking";
        } catch (Exception e) {
            logger.error("Exception occurred during booking save: ", e);
            redirectAttributes.addFlashAttribute("swal", "error");
            redirectAttributes.addFlashAttribute("message", "Error saving booking: " + e.getMessage());
            return "redirect:/makebooking";
        }
    }

    @GetMapping("/liveBookings")
    public String liveTracking(Model model) {
        logger.debug("Entering liveTracking method");
        try {
            Map<String, List<RoomBookings>> groupedBookings = roombookingsService.getLiveBookingsGroupedByRoom();
            logger.debug("Grouped bookings: {}", groupedBookings);
            model.addAttribute("liveBookings", groupedBookings);
            return "liveTracking";
        } catch (Exception e) {
            logger.error("Error in liveTracking", e);
            model.addAttribute("errorMessage", "Error retrieving live bookings: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/roombookings/edit/{id}")
    public String editBooking(@PathVariable Integer id, Model model) {
        try {
            RoomBookings booking = roombookingsService.get(id);
            List<Room> rooms = roomService.listAll(); // Fetch all rooms
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String formattedBookingTime = timeFormat.format(booking.getBookingTime());
            String formattedBookingEndTime = timeFormat.format(booking.getBookingEnd());
            // Adding the rooms list to the model
            model.addAttribute("rooms", rooms);
            model.addAttribute("booking", booking);
            model.addAttribute("formattedBookingTime", formattedBookingTime);
            model.addAttribute("formattedBookingEndTime", formattedBookingEndTime);
            model.addAttribute("timeSlots", generateTimeSlots());
            model.addAttribute("today", LocalDate.now());
            return "editRoomBookings";
        } catch (Exception e) {
            logger.error("Error finding booking: " + e.getMessage());
            model.addAttribute("errorMessage", "Error finding booking: " + e.getMessage());
            return "redirect:/admin/managebooking";
        }
    }



    @PostMapping("/roombookings/update/{id}")
    public String updateBooking(@PathVariable Integer id, @ModelAttribute RoomBookings booking, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Handle errors
            redirectAttributes.addFlashAttribute("swal", "error");
            redirectAttributes.addFlashAttribute("errorMessage", "Validation errors occurred.");
            return "redirect:/roombookings/edit/" + id;
        }
        try {
            booking.setId(id);
            roombookingsService.save(booking);
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Booking updated successfully!");
            return "redirect:/admin/searchBooking"; // Ensure this URL maps to the page showing the Manage Room HTML
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("swal", "error");
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating booking: " + e.getMessage());
            return "redirect:/roombookings/edit/" + id;
        }
    }

    @PostMapping("/roombookings/delete/{id}")
    public String deleteBooking(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roombookingsService.delete(id);
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Booking deleted successfully!");
            return "redirect:/admin/searchBooking";
        } catch (Exception e) {
            logger.error("Error deleting booking: ", e);
            redirectAttributes.addFlashAttribute("swal", "error");
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room: " + e.getMessage());
            return "redirect:/admin/searchBooking";
        }
    }



}
