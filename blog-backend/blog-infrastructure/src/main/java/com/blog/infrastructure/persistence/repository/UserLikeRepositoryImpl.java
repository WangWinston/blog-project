package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.domain.model.UserLike;
import com.blog.domain.repository.UserLikeRepository;
import com.blog.infrastructure.persistence.mapper.UserLikeMapper;
import com.blog.infrastructure.persistence.po.UserLikePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Like Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class UserLikeRepositoryImpl implements UserLikeRepository {

    private final UserLikeMapper userLikeMapper;

    @Override
    public UserLike save(UserLike userLike) {
        UserLikePO po = toUserLikePO(userLike);
        userLikeMapper.insert(po);
        userLike.setId(po.getId());
        return userLike;
    }

    @Override
    public Optional<UserLike> findByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserLikePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLikePO::getUserId, userId)
                .eq(UserLikePO::getArticleId, articleId);
        UserLikePO po = userLikeMapper.selectOne(wrapper);
        return po != null ? Optional.of(toUserLike(po)) : Optional.empty();
    }

    @Override
    public boolean existsByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserLikePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLikePO::getUserId, userId)
                .eq(UserLikePO::getArticleId, articleId);
        return userLikeMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<Long> findArticleIdsByUserId(Long userId) {
        LambdaQueryWrapper<UserLikePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLikePO::getUserId, userId)
                .orderByDesc(UserLikePO::getCreatedTime);
        return userLikeMapper.selectList(wrapper).stream()
                .map(UserLikePO::getArticleId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserLikePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLikePO::getUserId, userId)
                .eq(UserLikePO::getArticleId, articleId);
        userLikeMapper.delete(wrapper);
    }

    @Override
    public long countByArticleId(Long articleId) {
        LambdaQueryWrapper<UserLikePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLikePO::getArticleId, articleId);
        return userLikeMapper.selectCount(wrapper);
    }

    private UserLike toUserLike(UserLikePO po) {
        UserLike userLike = new UserLike();
        userLike.setId(po.getId());
        userLike.setUserId(po.getUserId());
        userLike.setArticleId(po.getArticleId());
        userLike.setCreatedTime(po.getCreatedTime());
        return userLike;
    }

    private UserLikePO toUserLikePO(UserLike userLike) {
        UserLikePO po = new UserLikePO();
        po.setId(userLike.getId());
        po.setUserId(userLike.getUserId());
        po.setArticleId(userLike.getArticleId());
        return po;
    }
}