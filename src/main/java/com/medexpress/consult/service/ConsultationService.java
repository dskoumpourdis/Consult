package com.medexpress.consult.service;

import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Consultation;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public interface ConsultationService {
    public List<Consultation> getAll();
    public Consultation save(Consultation questionnaire);
    public String saveAnswers(List<Answer> answers);
}
