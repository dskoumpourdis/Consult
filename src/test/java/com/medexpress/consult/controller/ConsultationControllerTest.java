package com.medexpress.consult.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medexpress.consult.domain.Answer;
import com.medexpress.consult.domain.Choice;
import com.medexpress.consult.domain.Consultation;
import com.medexpress.consult.domain.Question;
import com.medexpress.consult.repository.AnswerRepository;
import com.medexpress.consult.repository.ConsultationRepository;
import com.medexpress.consult.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ConsultationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void cleanDB() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        consultationRepository.deleteAll();
    }

    @Test
    void testCreateConsultationWithQuestionsAndChoices() throws Exception {
        Consultation consultation = new Consultation();
        consultation.setTitle("Customer Satisfaction Survey");

        Question q1 = new Question();
        q1.setText("How would you rate our service?");
        q1.setType("MULTIPLE_CHOICE");
        q1.setConsultation(consultation);

        Question q2 = new Question();
        q2.setText("What can we improve?");
        q2.setType("TEXT");
        q2.setConsultation(consultation);

        Choice c1 = new Choice(); c1.setChoice("1"); c1.setQuestion(q1);
        Choice c2 = new Choice(); c2.setChoice("2"); c2.setQuestion(q1);
        Choice c3 = new Choice(); c3.setChoice("3"); c3.setQuestion(q1);
        Choice c4 = new Choice(); c4.setChoice("4"); c4.setQuestion(q1);
        q1.setChoices(List.of(c1, c2, c3, c4));

        consultation.setQuestions(List.of(q1, q2));

        consultationRepository.save(consultation);

        mockMvc.perform(get("/api/consultations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Customer Satisfaction Survey"))
                .andExpect(jsonPath("$[0].questions[0].text").value("How would you rate our service?"))
                .andExpect(jsonPath("$[0].questions[0].choices").isArray())
                .andExpect(jsonPath("$[0].questions[0].choices[0].choice").value("1"))
                .andExpect(jsonPath("$[0].questions[1].text").value("What can we improve?"))
                .andExpect(jsonPath("$[0].questions[1].choices").isEmpty());
    }

    @Test
    void testSubmitAnswersWithLikelihood() throws Exception {
        Consultation consultation = new Consultation();
        consultation.setTitle("Customer Feedback");

        Question q1 = new Question();
        q1.setText("Rate our service");
        q1.setType("MULTIPLE_CHOICE");
        q1.setConsultation(consultation);

        Choice c1 = new Choice(); c1.setChoice("1"); c1.setQuestion(q1);
        Choice c2 = new Choice(); c2.setChoice("2"); c2.setQuestion(q1);
        q1.setChoices(List.of(c1, c2));

        Question q2 = new Question();
        q2.setText("Additional comments");
        q2.setType("TEXT");
        q2.setConsultation(consultation);

        consultation.setQuestions(List.of(q1, q2));
        consultation = consultationRepository.save(consultation);

        Answer a1 = new Answer();
        a1.setResponse(2);
        a1.setQuestion(q1);
        a1.setUserId("user123");

        Answer a2 = new Answer();
        a2.setResponse(20);
        a2.setQuestion(q2);
        a2.setUserId("user123");

        List<Answer> answers = List.of(a1, a2);
        String json = objectMapper.writeValueAsString(answers);

        mockMvc.perform(post("/api/consultations/" + consultation.getId() + "/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("likely")));

        List<Answer> savedAnswers = answerRepository.findAll();
        assertEquals(2, savedAnswers.size());
        assertEquals("user123", savedAnswers.getFirst().getUserId());
    }
}
