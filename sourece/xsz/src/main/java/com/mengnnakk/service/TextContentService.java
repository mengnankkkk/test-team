package com.mengnnakk.service;

import com.baomidou.mybatisplus.service.IService;
import com.mengnnakk.entry.TextContent;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public interface TextContentService extends IService<TextContent> {
    /**
     * 创建一个TextContent，将内容转化为json，回写到content中，不入库
     * @param list
     * @param now
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */
    <T,R> TextContent jsonConvertInsert(List<T> list, Date now, Function<? super T,? extends R> mapper);

    /**
     * 修改一个TextContent，将内容转化为json，回写到content中，不入库
     * @param textContent
     * @param list
     * @param mapper
     * @return
     * @param <T>
     * @param <R>
     */
    <T,R> TextContent jsonConvertUpdate(TextContent textContent,List<T> list,Function<? super T,? extends R> mapper);

    TextContent selectById(Integer id);

    boolean insertByFilter(TextContent record);

    int updateByIdFilter(TextContent record);
}