package com.medexpress.consult.service;

import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.repository.AnswerRepository;
import com.medexpress.consult.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AnswerRepository answerRepository;

    private final String LIKELY_RESPONSE = "likely";
    private final String NOT_LIKELY_RESPONSE = "not likely";
    private final String VERY_LIKELY_RESPONSE = "very likely";

    @Override
    public List<Consultation> getAll() {
        return consultationRepository.findAll();
    }

    @Override
    public Consultation save(Consultation consultation) {
        if (consultation.getQuestions() != null) {
            consultation.getQuestions().forEach(q -> {
                q.setConsultation(consultation);
                if (q.getChoices() != null) {
                    q.getChoices().forEach(c -> c.setQuestion(q));
                }
            });
        }
        return consultationRepository.save(consultation);
    }

    @Override
    public String saveAnswers(List<Answer> answers) {
        int sum = 0;
        String response;

        answerRepository.saveAll(answers);

        for (Answer answer : answers) {
            sum += answer.getResponse();
        }

        if (sum >= 0 && sum <= 30) {
            response = NOT_LIKELY_RESPONSE;
        } else if (sum > 30 && sum <= 60) {
            response = LIKELY_RESPONSE;
        } else {
            response = VERY_LIKELY_RESPONSE;
        }
        return response;
    }
}
