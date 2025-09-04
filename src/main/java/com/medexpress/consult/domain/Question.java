package com.medexpress.consult.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {
    @NotNull
    private String text;
    @NotNull
    private String type; // e.g., TEXT, MULTIPLE_CHOICE, RATING

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    @JsonBackReference
    private Consultation consultation;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Choice> choices = new ArrayList<>();
}
