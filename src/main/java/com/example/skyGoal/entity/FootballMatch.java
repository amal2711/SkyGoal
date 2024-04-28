package com.example.skyGoal.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "football_matches")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootballMatch {
    @Id
    private String matchId;
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private long timestamp;
    private String location;
}
