package com.medexpress.consult.controller;

import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.domain.Question;
import com.medexpress.consult.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    public ResponseEntity<List<Consultation>> getAll() {
        List<Consultation> consultations = consultationService.getAll();
        return ResponseEntity.ok(consultations);
    }

    @PostMapping
    public ResponseEntity<Consultation> create(@RequestBody Consultation consultation) {
        Consultation saved = consultationService.save(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{id}/answers")
    public ResponseEntity<String> submitAnswers(
            @PathVariable Long id,
            @RequestBody List<Answer> answers
    ) {
        answers.forEach(a -> {
            Question q = new Question();
            q.setId(a.getQuestion().getId());
            a.setQuestion(q);
        });

        String response = consultationService.saveAnswers(answers);
        return new ResponseEntity<>("It is " + response + " that the prescription will be approved", HttpStatus.OK);
    }

}
