package com.blog.domain.service;

import com.blog.domain.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comment Domain Service
 */
public class CommentDomainService {

    /**
     * Build comment tree from flat list
     */
    public List<Comment> buildTree(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, Comment> commentMap = new HashMap<>();
        List<Comment> rootComments = new ArrayList<>();

        // First pass: create map
        for (Comment comment : comments) {
            commentMap.put(comment.getId(), comment);
            comment.setChildren(new ArrayList<>());
        }

        // Second pass: build tree
        for (Comment comment : comments) {
            if (comment.isRoot()) {
                rootComments.add(comment);
            } else {
                Comment parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.addChild(comment);
                }
            }
        }

        return rootComments;
    }

    /**
     * Count total comments including replies
     */
    public int countTotal(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Comment comment : comments) {
            count++;
            if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
                count += countTotal(comment.getChildren());
            }
        }
        return count;
    }

    /**
     * Find all descendant comment IDs
     */
    public List<Long> findAllDescendantIds(List<Comment> allComments, Long commentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(commentId);

        collectDescendantIds(allComments, commentId, ids);
        return ids;
    }

    private void collectDescendantIds(List<Comment> comments, Long parentId, List<Long> ids) {
        for (Comment comment : comments) {
            if (parentId.equals(comment.getParentId())) {
                ids.add(comment.getId());
                collectDescendantIds(comments, comment.getId(), ids);
            }
        }
    }

    /**
     * Check if comment content is valid
     */
    public boolean isValidContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        // Check max length
        if (content.length() > 2000) {
            return false;
        }
        return true;
    }
}