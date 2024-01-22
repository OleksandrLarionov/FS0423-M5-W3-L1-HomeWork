package larionovoleksanr.exam.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "device")
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceType;
    private String stateOfDevice;
//  disponibile  assegnato, in manutenzione, dismesso


    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Dipendente employee;
}
