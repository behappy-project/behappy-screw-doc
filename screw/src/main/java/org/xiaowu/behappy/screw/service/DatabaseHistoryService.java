package org.xiaowu.behappy.screw.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.common.core.util.Result;
import org.xiaowu.behappy.screw.entity.DatabaseHistory;
import org.xiaowu.behappy.screw.mapper.DatabaseHistoryMapper;

import java.util.List;

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
