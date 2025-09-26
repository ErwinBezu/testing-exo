package org.example.exo3.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "furniture")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int stock;
}
