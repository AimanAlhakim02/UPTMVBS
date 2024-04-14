package com.library.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/room")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        return "room"; // Make sure 'room' is the correct Thymeleaf template for displaying rooms
    }

    @PostMapping("/saveRoom")
    public String saveRoom(@ModelAttribute Room room, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> logger.error("Validation error: " + error.toString()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.room", bindingResult);
            redirectAttributes.addFlashAttribute("room", room);
            return "admin/createroom";
        }
        try {
            roomService.save(room);
            redirectAttributes.addFlashAttribute("swal", "success");
            redirectAttributes.addFlashAttribute("message", "Room saved successfully!");
            return "admin/createroom";
        } catch (Exception e) {
            logger.error("Exception occurred during room save", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving room: " + e.getMessage());
            return "admin/createroom";
        }
    }

    @GetMapping("/room/edit/{id}")
    public String editRoom(@PathVariable Integer id, Model model) {
        try {
            Room room = roomService.get(id);
            model.addAttribute("room", room);
            return "editRoom"; // This should be the name of the Thymeleaf template for editing the room
        } catch (RoomNotFoundException e) {
            logger.error("Error finding room: " + e.getMessage());
            return "admin/manageroom";
        }
    }

    @PostMapping("/room/update/{id}")
    public String updateRoom(@PathVariable Integer id, @ModelAttribute Room room, RedirectAttributes redirectAttributes) {
        try {
            room.setId(id);
            roomService.save(room);
            redirectAttributes.addFlashAttribute("message", "Room updated successfully!");
            return "admin/manageroom";
        } catch (Exception e) {
            logger.error("Error updating room: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating room: " + e.getMessage());
            return "admin/manageroom";
        }
    }

    @GetMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roomService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Room deleted successfully!");
            return "admin/manageroom";
        } catch (RoomNotFoundException e) {
            logger.error("Error deleting room: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting room: " + e.getMessage());
            return "admin/manageroom";
        }
    }

    // Map HTTP GET requests for '/student/search/page/{pageNum}'
    @GetMapping("/room/search/page/{pageNum}")
    public String searchByPage(@PathVariable(name = "pageNum") int pageNum, @RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            Page<Room> result = roomService.search(keyword, pageNum);
            model.addAttribute("rooms", result.getContent());
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", result.getTotalElements());
            model.addAttribute("keyword", keyword);
        }
        return "admin/manageroom";// Use secondindex to render the same page with search results
    }
}