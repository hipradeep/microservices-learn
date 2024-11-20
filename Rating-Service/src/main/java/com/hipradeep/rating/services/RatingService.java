package com.hipradeep.rating.services;

import com.hipradeep.rating.entities.Rating;

import java.util.List;

public interface RatingService {

    //CREATE
    Rating create(Rating rating);

    //GET All Ratings
    List<Rating> getRatings();

    //GET All by UserId
    List<Rating> getRatingByUserId(String userId);

    //GET All by HotelId
    List<Rating> getRatingByHotelId(String hotelId);

}
