package com.hipradeep.hotel.repositories;

import com.hipradeep.hotel.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepo extends JpaRepository<Hotel,String> {

}
