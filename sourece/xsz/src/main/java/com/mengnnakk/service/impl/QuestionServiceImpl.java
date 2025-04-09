package com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mengnnakk.entry.Question;
import com.mengnnakk.mapper.QuestionMapper;
import com.mengnnakk.service.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper,Question> implements QuestionService {

}


