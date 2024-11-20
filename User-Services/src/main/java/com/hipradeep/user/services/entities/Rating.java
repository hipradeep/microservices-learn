package com.hipradeep.user.services.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {

    private String ratingId;
    private String userId;
    private String hotelId;
    private String feedback;
    private int rating;
    private Hotel hotel;
}
