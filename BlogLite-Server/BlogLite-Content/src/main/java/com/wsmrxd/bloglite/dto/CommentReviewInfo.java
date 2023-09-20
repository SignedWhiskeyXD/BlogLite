package com.wsmrxd.bloglite.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class CommentReviewInfo {

    private List<Integer> enableIDs = Collections.emptyList();

    private List<Integer> deleteIDs = Collections.emptyList();
}
