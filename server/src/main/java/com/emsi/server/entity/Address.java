package com.emsi.server.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name="address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;
    @Column(name = "street")
    private String street;
    @Column(name = "zip_code")
    private String zip_code;
    @Column(name = "city")
    private String city;

    @ManyToOne
    @JoinColumn(name = "state")
    private State state;

    @Override
    public String toString()
    {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", zip_code='" + zip_code + '\'' +
                ", city='" + city + '\'' +
                ", state=" + state +
                '}';
    }
}
