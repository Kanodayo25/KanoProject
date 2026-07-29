package com.kano.project.common.utils;

import com.kano.project.common.model.PageResult;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DozerUtils {

    /**
     * List<S> --> List<T>
     */
    public static <T, S> List<T> mapList(List<S> sourceList, Class<T> targetObjectClass) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>();
        for (S s : sourceList) {
            targetList.add(map(s, targetObjectClass));
        }
        return targetList;
    }

    /**
     * PageResult<S> --> PageResult<T>
     */
    public static <T, S> PageResult<T> mapPageResult(PageResult<S> pageResult, Class<T> targetObjectClass) {
        if (pageResult == null) {
            return null;
        }

        PageResult<T> resultPage = new PageResult<>();
        resultPage.setPageCount(pageResult.getPageCount());
        resultPage.setPage(pageResult.getPage());
        resultPage.setTotalCount(pageResult.getTotalCount());
        resultPage.setPageSize(pageResult.getPageSize());

        List<T> targetList = new ArrayList<>();
        if (pageResult.getDataList() != null) {
            for (S s : pageResult.getDataList()) {
                targetList.add(map(s, targetObjectClass));
            }
        }
        resultPage.setDataList(targetList);
        return resultPage;
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = destinationClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("对象拷贝失败: " + source.getClass() + " -> " + destinationClass, e);
        }
    }

    public static void map(Object source, Object destination) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, destination);
    }
}
