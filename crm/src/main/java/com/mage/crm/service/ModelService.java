package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModelDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModelService {
    @Resource
    private ModelDao modelDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllsModuleDtos(Integer rid) {
        //查询到所有的资源
        List<ModuleDto> moduleDtos = modelDao.queryAllsModuleDtos();
        //做一个勾选的问题
        //查询permssion根据rid得到modelId
        List<Integer> moduleIds = permissionDao.queryPermissionModuleIdsByRid(rid);// modelid 1 2 3 4 5 6
        if (null != moduleDtos && moduleDtos.size() > 0) {
            for (ModuleDto moduleDto : moduleDtos) {
                Integer mid = moduleDto.getId();
               /* for (int i = 0; i <moduleIds.size() ; i++) {
                    if(moduleIds.get(i)==mid){
                        moduleDto.setChecked(true);// 节点选中！！！
                    }
                }*/
                if (moduleIds.contains(moduleDto.getId())) {
                    moduleDto.setChecked(true);// 节点选中！！！ }
                }
            }
        }
        return moduleDtos;
    }
    public Map<String, Object> queryModulesByParams(ModuleQuery moduleQuery) {
        // 初始化分页环境
        PageHelper.startPage(moduleQuery.getPage(), moduleQuery.getRows());
        List<Module> modules = modelDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> pageInfo = new PageInfo<>(modules);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageInfo.getTotal());
        map.put("rows", pageInfo.getList());
        return map;
    }


    public void insert(Module module) {
        /**
         * 1.参数校验
         *    模块名称非空
         *    层级 非空
         *    模块权限值 非空
         * 2.权限值不能重复
         * 3.每一层  模块名称不能重复
         * 4.非根级菜单 上级菜单必须存在
         * 5.设置额外字段
         *     isValid
         *     createDate
         *     updateDate
         * 6.执行添加
         */
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue());
        AssertUtil.isTrue(null != modelDao.queryModuleByOptValue(module.getOptValue()), "权限值不能重复!");
        AssertUtil.isTrue(null != modelDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()),
                "该层级下模块名已存在!");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == modelDao.queryModuleByPid(module.getParentId()), "父级菜单不存在!");
        }

        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(modelDao.insert(module) < 1, CrmConstant.OPS_FAILED_MSG);
    }


    private void checkModuleParams(String moduleName, Integer grade,
                                   String optValue) {
        AssertUtil.isTrue(StringUtils.isBlank(moduleName), "模块名非空!");
        AssertUtil.isTrue(null == grade, "层级值非法!");
        Boolean flag = (grade != 0 && grade != 1 && grade != 2);
        AssertUtil.isTrue(flag, "层级值非法!");
        AssertUtil.isTrue(StringUtils.isBlank(optValue), "权限值非空!");
    }

    private void checkModuleParams(String moduleName, Integer grade,
                                   String optValue, Integer id) {
        checkModuleParams(moduleName, grade, optValue);
        AssertUtil.isTrue(null == id || null == modelDao.queryModuleById(id), "待更新模块不存在!");
    }


    public void update(Module module) {
        /**
         * 1.参数校验
         *    模块名称非空
         *    层级 非空
         *    模块权限值 非空
         *    id 记录必须存在
         * 2.权限值不能重复
         * 3.每一层  模块名称不能重复
         * 4.非根级菜单 上级菜单必须存在
         * 5.设置额外字段
         *     updateDate
         * 6.执行更新
         */
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue(), module.getId());

        Module temp = modelDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null != temp && !temp.getId().equals(module.getId()), "权限值不能重复!");
        temp = modelDao.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(null != temp && !temp.getId().equals(module.getId()), "该层级下模块名不能重复!");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == modelDao.queryModuleByPid(module.getParentId()), "父级菜单不存在!");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(modelDao.update(module) < 1, CrmConstant.OPS_FAILED_MSG);
    }


    public void delete(Integer id) {
        /**
         * 删除当前节点
         *    级联删除该节点下所有后代节点
         *  后代节点id 值获取
         *     3层 级联查询
         *        查询当前节点  查询后代节点根据当前节点id
         *     层级无限制 递归查询
         *        100  1   99
         */
        AssertUtil.isTrue(null == id || null == modelDao.queryModuleById(id), "待删除记录不存在!");

        List<Integer> mids = new ArrayList<Integer>();

        mids = getSubModuleIds(id, mids);


        AssertUtil.isTrue(modelDao.delete(mids) < mids.size(), CrmConstant.OPS_FAILED_MSG);
    }


    /**
     * 递归查询子节点
     *
     * @param id
     * @param mids
     * @return
     */
    private List<Integer> getSubModuleIds(Integer id, List<Integer> mids) {// 根级
        Module module = modelDao.queryModuleById(id);//爷爷
        if (null != module) {
            mids.add(module.getId());//爷爷添加到待删除的集合中
            // 查询子记录
            List<Module> modules = modelDao.querySubModulesByPid(module.getId());//一级目录 1 2
            if (null != modules && modules.size() > 0) {
                for (Module temp : modules) {
                    mids = getSubModuleIds(temp.getId(), mids);
                }//儿子也加进去了
            }
        }
        return mids;
    }


    public List<Module> queryModulesByGrade(Integer grade) {
        return modelDao.queryModulesByGrade(grade);
    }
}