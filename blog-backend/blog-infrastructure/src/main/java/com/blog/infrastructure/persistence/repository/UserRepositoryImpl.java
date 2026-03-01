package com.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.domain.model.Profile;
import com.blog.domain.model.User;
import com.blog.domain.repository.UserRepository;
import com.blog.infrastructure.persistence.mapper.ProfileMapper;
import com.blog.infrastructure.persistence.mapper.UserMapper;
import com.blog.infrastructure.persistence.po.ProfilePO;
import com.blog.infrastructure.persistence.po.UserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    @Override
    public User save(User user) {
        UserPO po = toUserPO(user);
        userMapper.insert(po);
        user.setId(po.getId());

        if (user.getProfile() != null) {
            ProfilePO profilePO = toProfilePO(user.getId(), user.getProfile());
            profileMapper.insert(profilePO);
        }

        return user;
    }

    @Override
    public User update(User user) {
        UserPO po = toUserPO(user);
        userMapper.updateById(po);

        if (user.getProfile() != null) {
            LambdaQueryWrapper<ProfilePO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProfilePO::getUserId, user.getId());
            ProfilePO existing = profileMapper.selectOne(wrapper);
            if (existing != null) {
                ProfilePO profilePO = toProfilePO(user.getId(), user.getProfile());
                profilePO.setId(existing.getId());
                profileMapper.updateById(profilePO);
            } else {
                ProfilePO profilePO = toProfilePO(user.getId(), user.getProfile());
                profileMapper.insert(profilePO);
            }
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        UserPO po = userMapper.selectById(id);
        if (po == null) {
            return Optional.empty();
        }
        User user = toUser(po);
        loadProfile(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getUsername, username);
        UserPO po = userMapper.selectOne(wrapper);
        if (po == null) {
            return Optional.empty();
        }
        User user = toUser(po);
        loadProfile(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getEmail, email);
        UserPO po = userMapper.selectOne(wrapper);
        if (po == null) {
            return Optional.empty();
        }
        User user = toUser(po);
        loadProfile(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByGithubId(String githubId) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getGithubId, githubId);
        UserPO po = userMapper.selectOne(wrapper);
        if (po == null) {
            return Optional.empty();
        }
        User user = toUser(po);
        loadProfile(user);
        return Optional.of(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getUsername, username);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPO::getEmail, email);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public long count() {
        return userMapper.selectCount(null);
    }

    private User toUser(UserPO po) {
        User user = new User();
        user.setId(po.getId());
        user.setUsername(po.getUsername());
        user.setPassword(po.getPassword());
        user.setEmail(po.getEmail());
        user.setAvatar(po.getAvatar());
        user.setRole(po.getRole());
        user.setGithubId(po.getGithubId());
        user.setStatus(po.getStatus());
        user.setCreatedTime(po.getCreatedTime());
        user.setUpdatedTime(po.getUpdatedTime());
        return user;
    }

    private UserPO toUserPO(User user) {
        UserPO po = new UserPO();
        po.setId(user.getId());
        po.setUsername(user.getUsername());
        po.setPassword(user.getPassword());
        po.setEmail(user.getEmail());
        po.setAvatar(user.getAvatar());
        po.setRole(user.getRole());
        po.setGithubId(user.getGithubId());
        po.setStatus(user.getStatus());
        return po;
    }

    private void loadProfile(User user) {
        LambdaQueryWrapper<ProfilePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProfilePO::getUserId, user.getId());
        ProfilePO profilePO = profileMapper.selectOne(wrapper);
        if (profilePO != null) {
            Profile profile = new Profile();
            profile.setNickname(profilePO.getNickname());
            profile.setBio(profilePO.getBio());
            profile.setWebsite(profilePO.getWebsite());
            profile.setLocation(profilePO.getLocation());
            profile.setGithubUsername(profilePO.getGithubUsername());
            user.setProfile(profile);
        }
    }

    private ProfilePO toProfilePO(Long userId, Profile profile) {
        ProfilePO po = new ProfilePO();
        po.setUserId(userId);
        po.setNickname(profile.getNickname());
        po.setBio(profile.getBio());
        po.setWebsite(profile.getWebsite());
        po.setLocation(profile.getLocation());
        po.setGithubUsername(profile.getGithubUsername());
        return po;
    }
}