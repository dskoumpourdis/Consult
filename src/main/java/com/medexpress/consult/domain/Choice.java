package com.medexpress.consult.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Choice extends BaseEntity {
    @NotNull
    private String choice;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;
}
