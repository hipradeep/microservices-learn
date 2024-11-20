package com.hipradeep.rating.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("ratings")
public class Rating {

    @Id
    @JsonProperty("ratingId")
    private String ratingId;
    private String userId;
    private String hotelId;
    private String feedback;
    private int rating;

    public String getRatingId() {
        return ratingId; // Getter method to access the value of ratingId
    }
}
