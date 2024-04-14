package com.library.room;

import com.library.room.RoomNotFoundException;
import com.library.room.Room;
import com.library.room.RoomNotFoundException;
import com.library.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    public List<Room> listAll() {
        return repo.findAll();
    }

    public void save(Room room) {
        repo.save(room);
    }

    public Room get(Integer id) throws RoomNotFoundException {
        return repo.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    public static final int SEARCH_RESULT_PER_PAGE = 10;

    public Page<Room> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULT_PER_PAGE);
        return repo.search(keyword, pageable);
    }

    public void delete(Integer id) throws RoomNotFoundException {
        Long count = repo.countById(id);
        if (count == null || count == 0) {
            throw new RoomNotFoundException();
        }
        repo.deleteById(id);
    }
}
