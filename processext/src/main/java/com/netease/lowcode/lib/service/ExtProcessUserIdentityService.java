package com.netease.lowcode.lib.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.netease.codewave.domain.iam.api.account.IAccountService;
import com.netease.codewave.domain.iam.api.authentication.IAuthenticationService;
import com.netease.codewave.domain.iam.api.domain.Page;
import com.netease.codewave.domain.iam.api.domain.User;
import com.netease.codewave.domain.iam.api.domain.UserQuery;
import com.netease.codewave.process.api.domain.CWPageOf;
import com.netease.codewave.process.util.ProcessConstant;
import com.netease.lowcode.lib.logic.structure.ProcessUser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhuzaishao
 * @date 2024/12/5 11:11
 * @description
 */

@Service
public class ExtProcessUserIdentityService {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private static final ThreadLocal<ProcessUser> currentProcessUserThreadLocal = new ThreadLocal<>();

    private static final Cache<String, User> USER_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @Resource
    private IAccountService accountService;

    @Resource
    private IAuthenticationService authenticationService;

    /**
     * 设置当前流程用户
     *
     * @param processUser 流程用户
     */
    public void setCurrentProcessUser(ProcessUser processUser) {
        currentProcessUserThreadLocal.set(processUser);
    }

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    public ProcessUser getCurrentUserInfo() {

        // 优先获取流程侧ThreadLocal下的当前用户信息，若不存在，则从认证侧获取当前用户信息
        if (currentProcessUserThreadLocal.get() != null) {
            return currentProcessUserThreadLocal.get();
        }

        User currentUser = authenticationService.getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return toProcessUser(currentUser);
    }

    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    public String getCurrentUserName() {
        ProcessUser currentUser = getCurrentUserInfo();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserName();
    }

    /**
     * 查询用户
     *
     * @param page                页码
     * @param size                每页数量
     * @param userName            用户名
     * @param excludeUserNameList 排除的用户名列表
     * @return 用户列表
     */
    public CWPageOf<ProcessUser> getUserList(Long page, Long size, String userName, List<String> excludeUserNameList) {

        page = page == null ? 1 : page;
        size = size == null ? 20 : size;

        UserQuery userQuery = new UserQuery();
        userQuery.setUserName(userName);
        userQuery.setPageIndex(page);
        userQuery.setPageSize(size);
        userQuery.setExcludeUserNameList(excludeUserNameList);
        Page<User> userPage = accountService.getUserPage(userQuery);

        return CWPageOf.of(userPage.getData().stream().map(this::toProcessUser).collect(Collectors.toList()), userPage.getTotal());
    }

    /**
     * 获取当前用户所在部门的用户
     *
     * @param deptId      部门ID
     * @param managerOnly 是否只获取部门主管
     * @return 当前用户所在部门的用户
     */
    public List<ProcessUser> getDeptUsers(String deptId, Boolean managerOnly) {
        List<User> userList = accountService.getDeptUsers(deptId, managerOnly);
        if (CollectionUtil.isEmpty(userList)) {
            return Collections.emptyList();
        }
        return userList.stream().map(this::toProcessUser).collect(Collectors.toList());
    }

    /**
     * 获取角色下的用户列表
     *
     * @param roleName 角色名
     * @return 角色下的用户列表
     */
    public List<ProcessUser> getUserListByRoleName(String roleName) {
        List<User> userList = accountService.getUserListByRoleId(roleName);
        if (CollectionUtil.isEmpty(userList)) {
            return Collections.emptyList();
        }
        return userList.stream().map(this::toProcessUser).collect(Collectors.toList());
    }

    /**
     * 获取用户向上第N级直接主管
     *
     * @param userId 用户ID
     * @param level  主管层级
     * @return 用户向上第N级主管
     */
    public ProcessUser getUserLevelNthUpDirectManager(String userId, Long level) {
        User user = accountService.getUserLevelNthUpDirectManager(userId, level);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        return toProcessUser(user);
    }

    /**
     * 获取用户向上第N级部门主管
     *
     * @param userId 用户ID
     * @param level  主管层级
     * @return
     */
    public ProcessUser getUserLevelNthUpDeptManager(String userId, Long level) {
        User user = accountService.getUserLevelNthUpDeptManager(userId, level);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        return toProcessUser(user);
    }

    /**
     * 获取用户所在组织向下第N级部门主管
     *
     * @param userId 用户ID
     * @param level  主管层级
     * @return 用户所在组织向下第N级部门主管
     */
    public ProcessUser getUserLevelNthDeptManager(String userId, Long level) {
        User user = accountService.getUserLevelNthDeptManager(userId, level);

        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        return toProcessUser(user);
    }

    /**
     * 通过用户名获取用户ID
     *
     * @param userName 用户名
     * @return 用户ID
     */
    public String getUserIdByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        User user = USER_CACHE.getIfPresent(userName);
        if (user == null) {
            List<User> userList = accountService.getUsersByUserName(userName);
            if (CollectionUtil.isEmpty(userList)) {
                return null;
            }

            User firstUser = userList.get(0);
            return firstUser.getUserId();
        } else {
            return user.getUserId();
        }
    }

    /**
     * 根据用户名获取用户
     *
     * @param userName 用户名
     * @return 用户
     */
    public ProcessUser getUsersByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }

        if (ProcessConstant.SYSTEM_USER.equals(userName)) {
            return new ProcessUser(ProcessConstant.SYSTEM_USER, "系统");
        }

        User user = null;
        if (USER_CACHE.getIfPresent(userName) == null) {
            List<User> userList = accountService.getUsersByUserName(userName);
            if (CollectionUtil.isNotEmpty(userList)) {
                user = userList.get(0);
                USER_CACHE.put(userName, user);
            }
        } else {
            user = USER_CACHE.getIfPresent(userName);
        }

        if (ObjectUtils.isEmpty(user)) {
            user = new User();
            user.setUserName(userName);
        }

        return toProcessUser(user);
    }

    /**
     * 根据用户名获取用户
     *
     * @param userNameList 用户名
     * @return 用户
     */
    public List<ProcessUser> getUsersByUserNameList(List<String> userNameList) {
        if (CollectionUtil.isEmpty(userNameList)) {
            return Collections.emptyList();
        }

        return userNameList.stream()
                .map(this::getUsersByUserName)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    private ProcessUser toProcessUser(User user) {
        ProcessUser processUser = new ProcessUser();
        processUser.setUserName(user.getUserName());
        processUser.setDisplayName(user.getDisplayName());
        return processUser;
    }

}
