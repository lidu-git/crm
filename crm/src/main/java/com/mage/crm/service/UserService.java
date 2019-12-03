package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dao.UserDao;
import com.mage.crm.dao.UserRoleDao;
import com.mage.crm.dto.UserDto;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.Md5Util;
import com.mage.crm.vo.User;
import com.mage.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleDao userRoleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private HttpSession session;

    public UserModel userLogin(String userName, String userPwd){
        User user = userDao.queryUserByName(userName);
        AssertUtil.isTrue(null==user,"用户不存在");
        //加密过后的密码
        userPwd=Md5Util.enCode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(user.getUserPwd()),"用户名或密码错误");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销");
        /**
         * 获取用户权限  根据用户拥有的角色
         */
        List<String> permissions=permissionDao.queryPermissionsByUserId(user.getId() + "");
        System.out.println(permissions);
        if(null!=permissions&&permissions.size()>0){
            session.setAttribute("userPermission", permissions);
        }
        return createUserModel(user);
    }
    public UserModel createUserModel(User user){
        UserModel userModel = new UserModel();
        userModel.setUserName(user.getUserName());
        userModel.setId(Base64Util.enCode(user.getId()));
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    public void updatePwd(String id, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(id),"id不存在");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码与老密码不能相等");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"新密码与确认密码不一致");
        User user = userDao.queryUserById(Base64Util.deCode(id));
        AssertUtil.isTrue(null==user,"用户不存在了");
        AssertUtil.isTrue("0".equals(user.getIsValid()),"用户已经被注销了");
        AssertUtil.isTrue(!Md5Util.enCode(oldPassword).equals(user.getUserPwd()),"原始密码错误");
        AssertUtil.isTrue(userDao.updatePwd(user.getId(),Md5Util.enCode(newPassword))<1,"用户密码更新失败");
    }
    public  User queryUserById(String id){
        return  userDao.queryUserById(id);
    }

    public List<User> queryAllCustomerManager() {
        return userDao.queryAllCustomerManager();
    }

    public Map<String,Object> queryUsersByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPage(),userQuery.getRows());
        List<UserDto> userDtoList =  userDao.queryUsersByParams(userQuery);
        if (userDtoList != null && userDtoList.size() > 0) {
            for (UserDto userDto : userDtoList) {
                String roleIdstr = userDto.getRoleIdsStr();
                if (roleIdstr != null) {
                    String[] roleIdArray =  roleIdstr.split(",");
                    for (int i = 0; i <roleIdArray.length ; i++) {
                        List<Integer> roleIds = userDto.getRoleIds();
                        roleIds.add(Integer.parseInt(roleIdArray[i]));
                    }
                }
            }
        }
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>(userDtoList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",userDtoPageInfo.getTotal());
        map.put("rows",userDtoPageInfo.getList());
        return map;
    }

    public void insert(User user) {
       checkParams(user.getUserName(),user.getTrueName(),user.getPhone());
        //判断userName的唯一性
        AssertUtil.isTrue(userDao.queryUserByName(user.getUserName()) != null, "用户名已经存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.enCode("123456"));
        AssertUtil.isTrue(userDao.insert(user)<1,"用户添加失败");
        List<Integer> roleIds = user.getRoleIds();
        if(roleIds!=null&&roleIds.size()>0){
            relateRoles(roleIds,Integer.parseInt(user.getId()));
        }

    }
    public void checkParams(String userName,String trueName,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(trueName),"真实姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空");
    }
    public void relateRoles(List<Integer> roleIds,Integer userId){
        List<UserRole> roleList=new ArrayList<UserRole>();
        for (Integer roleId:roleIds){
            UserRole userRole = new UserRole();
            userRole.setIsValid(1);
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            roleList.add(userRole);
        }
        AssertUtil.isTrue(userRoleDao.insertBacth(roleList)<1,"用户角色添加失败");

    }

    public void update(User user) {

        checkParams(user.getUserName(), user.getTrueName(), user.getPhone());

        user.setUpdateDate(new Date());

        User tmpUser = userDao.queryUserByName(user.getUserName());

        AssertUtil.isTrue(tmpUser!=null&&!user.getId().equals(tmpUser.getId()),"用户名称已存在");

        AssertUtil.isTrue(userDao.update(user) < 1, "用户数据添加失败");

        String userId = user.getId();

        userRoleDao.deleteUserRolesByUserId(Integer.parseInt(userId));
        List<Integer> roleIds = user.getRoleIds();
        if (roleIds != null && roleIds.size() > 0) {
            //级联操作,添加用户角色。
            relateRoles(roleIds,Integer.parseInt(userId));
        }
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(userDao.delete(id)<1,"用户数据删除失败");

        int count = userRoleDao.queryUserRoleCountsByUserId(id);

        if(count>0){
            AssertUtil.isTrue(userRoleDao.deleteUserRolesByUserId(id)<count,"用户角色级联删除失败");
        }
    }
}
