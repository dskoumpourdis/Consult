package com.medexpress.consult.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class Consultation extends BaseEntity {
    @NotNull
    private String title;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();
}
