package com.kano.project.common.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MapperModelContext;
import com.github.dozermapper.core.MappingException;
import com.kano.project.common.model.PageResult;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DozerUtils {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * 封装dozer处理集合的方法：List<S> --> List<T>
     */
    public static <T, S> List<T> mapList(final Mapper mapper, List<S> sourceList, Class<T> targetObjectClass) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<T>();
        for (S s : sourceList) {
            targetList.add(mapper.map(s, targetObjectClass));
        }
        return targetList;
    }

    /**
     * 封装dozer处理集合的方法：List<S> --> List<T>
     */
    public static <T, S> List<T> mapList(List<S> sourceList, Class<T> targetObjectClass) {
        return mapList(mapper, sourceList, targetObjectClass);
    }

    /**
     * 封装dozer处理mapPageResult的方法：PageResult<S> --> PageResult<T>
     */
    public static <T, S> PageResult<T> mapPageResult(final Mapper mapper, PageResult<S> pageResult, Class<T> targetObjectClass) {
        if (pageResult == null) {
            return null;
        }

        // 拷贝其他page参数
        PageResult<T> resultPage = new PageResult<T>();
        resultPage.setPageCount(pageResult.getPageCount());
        resultPage.setPage(pageResult.getPage());
        resultPage.setTotalCount(pageResult.getTotalCount());
        resultPage.setPageSize(pageResult.getPageSize());

        // 转换目标list
        List<T> targetList = new ArrayList<T>();
        if (pageResult.getDataList() != null) {
            List<S> sourceList = pageResult.getDataList();
            for (S s : sourceList) {
                targetList.add(mapper.map(s, targetObjectClass));
            }
        }
        resultPage.setDataList(targetList);
        return resultPage;
    }

    public static <T, S> PageResult<T> mapPageResult(PageResult<S> pageResult, Class<T> targetObjectClass) {
        return mapPageResult(mapper, pageResult, targetObjectClass);
    }

    public static <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        if (source == null) {
            return null;
        }
        return mapper.map(source, destinationClass);
    }

    public static void map(Object source, Object destination) throws MappingException {
        if (source == null) {
            return;
        }
        mapper.map(source, destination);
    }

    public <T> T map(Object source, Class<T> destinationClass, String mapId) throws MappingException {
        if (source == null) {
            return null;
        }
        return mapper.map(source, destinationClass, mapId);
    }

    public static void map(Object source, Object destination, String mapId) throws MappingException {
        if (source == null) {
            return;
        }
        mapper.map(source, destination, mapId);
    }

    public static MapperModelContext getMapperModelContext() {
        return mapper.getMapperModelContext();
    }
}
