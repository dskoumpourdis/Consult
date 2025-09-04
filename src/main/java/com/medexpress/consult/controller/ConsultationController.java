package com.medexpress.consult.controller;

import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.domain.Question;
import com.medexpress.consult.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    public List<Consultation> getAll() {
        return consultationService.getAll();
    }

    @PostMapping
    public Consultation create(@RequestBody Consultation consultation) {
        return consultationService.save(consultation);
    }

    @PostMapping("/{id}/answers")
    public void submitAnswers(
            @PathVariable Long id,
            @RequestBody List<Answer> answers
    ) {
        // ensure each answer is linked correctly
        answers.forEach(a -> {
            Question q = new Question();
            q.setId(a.getQuestion().getId());
            a.setQuestion(q);
        });

        consultationService.saveAnswers(answers);
    }

}
