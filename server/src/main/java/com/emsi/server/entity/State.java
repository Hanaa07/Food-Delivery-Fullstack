package com.emsi.server.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="state")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_state")
    private Long id ;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "state")
    private List<Address> addressList;
}