package com.mengnnakk.entry;


import com.mengnnakk.entry.ExamPaper;

import java.util.List;

public class ExamPaperAnswerInfo {
    public ExamPaper examPaper;
    public ExamPaperAnswer examPaperAnswer;
    public List<com.mindskip.xzs.domain.ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers;

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    public ExamPaperAnswer getExamPaperAnswer() {
        return examPaperAnswer;
    }

    public void setExamPaperAnswer(ExamPaperAnswer examPaperAnswer) {
        this.examPaperAnswer = examPaperAnswer;
    }

    public List<ExamPaperQuestionCustomerAnswer> getExamPaperQuestionCustomerAnswers() {
        return examPaperQuestionCustomerAnswers;
    }

    public void setExamPaperQuestionCustomerAnswers(List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers) {
        this.examPaperQuestionCustomerAnswers = examPaperQuestionCustomerAnswers;
    }
}
