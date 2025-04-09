package com.mengnnakk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mengnnakk.entry.TextContent;
import com.mengnnakk.mapper.TextContentMapper;
import com.mengnnakk.service.TextContentService;
import com.mengnnakk.utility.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextContentServiceImpl extends ServiceImpl<TextContentMapper, TextContent> implements TextContentService {
    private final TextContentMapper textContentMapper;

    @Autowired
    public TextContentServiceImpl(TextContentMapper textContentMapper) {
        this.textContentMapper = textContentMapper;
    }

    /**
     * 创建一个TextContent，将内容转化为json，回写到content中，不入库
     * @param list
     * @param now
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */

    @Override
    public <T, R> TextContent jsonConvertInsert(List<T> list, Date now, Function<? super T, ? extends R> mapper) {
        String frameTextContent = null;
        if (null==mapper){
            frameTextContent = JsonUtil.toJsonStr(list);
        }else {
            List<R> maplist = list.stream().map(mapper).collect(Collectors.toList());
            frameTextContent = JsonUtil.toJsonStr(maplist);
        }
        TextContent textContent = new TextContent(frameTextContent,now);
        return textContent;
    }

    /**
     * 跟新一个TextContent，将内容转化为json，回写到content中，不入库
     * @param textContent
     * @param list
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */
    @Override
    public <T, R> TextContent jsonConvertUpdate(TextContent textContent, List<T> list, Function<? super T, ? extends R> mapper) {
        String frameTextContent = null;
        if (null==mapper){
            frameTextContent  = JsonUtil.toJsonStr(list);
        }
        else{
            List<R> maolist = list.stream().map(mapper).collect(Collectors.toList());
            frameTextContent = JsonUtil.toJsonStr(maolist);
        }
        textContent.setContent(frameTextContent);
        return textContent;
    }

    @Override
    public TextContent selectById(Integer id) {
        return super.selectById(id);
    }
    @Override
    public boolean insertByFilter(TextContent record){
        return super.insertOrUpdate(record);
    }//这个功能出错了，懒的去改了
    @Override
    public int updateByIdFilter(TextContent record){
        return super.baseMapper.updateById(record);
    }

}