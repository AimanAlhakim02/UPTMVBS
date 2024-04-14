package com.library.room;

import com.library.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
// Annotate as query
    // We create a custom query to implement full text searches for columns...
    // isbn, title, genre
    // Here, nativeQuery = true means it is a native database query (MySQL)
    @Query("SELECT r FROM Room r WHERE r.code LIKE %:keyword% OR r.location LIKE %:keyword%")
    Page<Room> search(@Param("keyword") String keyword, Pageable pageable);


    public Long countById(Integer id);
}
