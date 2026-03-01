package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.model.UserFavorite;
import com.blog.domain.repository.UserFavoriteRepository;
import com.blog.infrastructure.persistence.mapper.UserFavoriteMapper;
import com.blog.infrastructure.persistence.po.UserFavoritePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Favorite Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class UserFavoriteRepositoryImpl implements UserFavoriteRepository {

    private final UserFavoriteMapper userFavoriteMapper;

    @Override
    public UserFavorite save(UserFavorite userFavorite) {
        UserFavoritePO po = toUserFavoritePO(userFavorite);
        userFavoriteMapper.insert(po);
        userFavorite.setId(po.getId());
        return userFavorite;
    }

    @Override
    public Optional<UserFavorite> findByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getUserId, userId)
                .eq(UserFavoritePO::getArticleId, articleId);
        UserFavoritePO po = userFavoriteMapper.selectOne(wrapper);
        return po != null ? Optional.of(toUserFavorite(po)) : Optional.empty();
    }

    @Override
    public boolean existsByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getUserId, userId)
                .eq(UserFavoritePO::getArticleId, articleId);
        return userFavoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<Long> findArticleIdsByUserId(Long userId, int page, int size) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getUserId, userId)
                .orderByDesc(UserFavoritePO::getCreatedTime);
        Page<UserFavoritePO> pageResult = userFavoriteMapper.selectPage(new Page<>(page, size), wrapper);
        return pageResult.getRecords().stream()
                .map(UserFavoritePO::getArticleId)
                .collect(Collectors.toList());
    }

    @Override
    public long countByUserId(Long userId) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getUserId, userId);
        return userFavoriteMapper.selectCount(wrapper);
    }

    @Override
    public void deleteByUserIdAndArticleId(Long userId, Long articleId) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getUserId, userId)
                .eq(UserFavoritePO::getArticleId, articleId);
        userFavoriteMapper.delete(wrapper);
    }

    @Override
    public long countByArticleId(Long articleId) {
        LambdaQueryWrapper<UserFavoritePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavoritePO::getArticleId, articleId);
        return userFavoriteMapper.selectCount(wrapper);
    }

    private UserFavorite toUserFavorite(UserFavoritePO po) {
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setId(po.getId());
        userFavorite.setUserId(po.getUserId());
        userFavorite.setArticleId(po.getArticleId());
        userFavorite.setCreatedTime(po.getCreatedTime());
        return userFavorite;
    }

    private UserFavoritePO toUserFavoritePO(UserFavorite userFavorite) {
        UserFavoritePO po = new UserFavoritePO();
        po.setId(userFavorite.getId());
        po.setUserId(userFavorite.getUserId());
        po.setArticleId(userFavorite.getArticleId());
        return po;
    }
}