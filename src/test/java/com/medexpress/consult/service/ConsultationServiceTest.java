package com.medexpress.consult.service;

import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Choice;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.domain.Question;
import com.medexpress.consult.repository.AnswerRepository;
import com.medexpress.consult.repository.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private ConsultationServiceImpl consultationService;
    private Consultation consultation;
    private Question question;
    private Choice choice;

    @BeforeEach
    void setUp() {
        consultation = new Consultation();
        consultation.setTitle("Test Consultation");

        question = new Question();
        question.setText("How do you rate service?");
        question.setType("TEXT");
        question.setConsultation(consultation);

        choice = new Choice();
        choice.setChoice("5");
        choice.setQuestion(question);

        question.setChoices(List.of(choice));
        consultation.setQuestions(List.of(question));
    }

    @Test
    void testSaveConsultation() {
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        Consultation saved = consultationService.save(consultation);

        assertNotNull(saved);
        assertEquals("Test Consultation", saved.getTitle());
        assertEquals(1, saved.getQuestions().size());
        verify(consultationRepository, times(1)).save(consultation);
        // verify that question's consultation reference is set
        assertEquals(consultation, saved.getQuestions().get(0).getConsultation());
        // verify that choice's question reference is set
        assertEquals(question, saved.getQuestions().get(0).getChoices().get(0).getQuestion());
    }

    @Test
    void testSaveAnswers_LikelihoodCalculation() {
        Answer a1 = new Answer();
        a1.setResponse(10);
        a1.setQuestion(new Question());

        Answer a2 = new Answer();
        a2.setResponse(25);
        a2.setQuestion(new Question());

        List<Answer> answers = List.of(a1, a2);

        when(answerRepository.saveAll(anyList())).thenReturn(answers);

        String result = consultationService.saveAnswers(answers);

        // sum = 35 → LIKELY_RESPONSE
        assertEquals("likely", result.toLowerCase());
        verify(answerRepository, times(1)).saveAll(answers);
    }

    @Test
    void testGetAllConsultations() {
        when(consultationRepository.findAll()).thenReturn(List.of(consultation));

        List<Consultation> result = consultationService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Consultation", result.get(0).getTitle());
        verify(consultationRepository, times(1)).findAll();
    }

    @Test
    void testSaveAnswers_SumZero_NotLikely() {
        Answer a1 = new Answer();
        a1.setResponse(0);
        a1.setQuestion(new Question());

        List<Answer> answers = List.of(a1);

        when(answerRepository.saveAll(anyList())).thenReturn(answers);

        String result = consultationService.saveAnswers(answers);

        assertEquals("not likely", result.toLowerCase());
    }

    @Test
    void testSaveAnswers_SumHigh_VeryLikely() {
        Answer a1 = new Answer();
        a1.setResponse(70);
        a1.setQuestion(new Question());

        List<Answer> answers = List.of(a1);

        when(answerRepository.saveAll(anyList())).thenReturn(answers);

        String result = consultationService.saveAnswers(answers);

        assertEquals("very likely", result.toLowerCase());
    }
}
