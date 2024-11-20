package com.hipradeep.hotel.services;

import com.hipradeep.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    //CREATE
    Hotel create(Hotel hotel);

    //GET All
    List<Hotel> getAll();

    //GET single
    Hotel get(String hotelId);


}
