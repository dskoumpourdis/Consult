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
    public void saveAnswers(List<Answer> answers) {
        answerRepository.saveAll(answers);
    }
}
