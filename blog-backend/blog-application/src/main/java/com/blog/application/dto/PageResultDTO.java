package com.blog.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Page Result DTO
 */
@Data
public class PageResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> list;
    private Long total;
    private Integer page;
    private Integer size;
    private Integer totalPages;

    public static <T> PageResultDTO<T> of(List<T> list, Long total, Integer page, Integer size) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setList(list);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages((int) Math.ceil((double) total / size));
        return result;
    }
}