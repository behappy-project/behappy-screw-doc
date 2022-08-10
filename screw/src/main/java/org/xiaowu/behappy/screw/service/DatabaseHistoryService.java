package org.xiaowu.behappy.screw.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.core.config.ScrewProperties;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.dto.ScrewSchemaDto;
import org.xiaowu.behappy.screw.dto.UpdateDocDto;
import org.xiaowu.behappy.screw.entity.Database;
import org.xiaowu.behappy.screw.entity.DatabaseHistory;
import org.xiaowu.behappy.screw.mapper.DatabaseHistoryMapper;
import org.xiaowu.behappy.screw.mapper.DatabaseMapper;
import org.xiaowu.behappy.screw.util.ScrewUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@RequiredArgsConstructor
public class DatabaseHistoryService extends ServiceImpl<DatabaseHistoryMapper, DatabaseHistory> implements IService<DatabaseHistory> {

    public Result history(int databaseId) {
        LambdaQueryWrapper<DatabaseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DatabaseHistory::getDatabaseId,databaseId);
        List<DatabaseHistory> list = list(queryWrapper);
        return Result.success(list);
    }
}
