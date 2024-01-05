/*
 * screw-core - 简洁好用的数据库表结构文档生成工具
 * Copyright © 2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xiaowu.behappy.screw.core.query.sqlite;

import org.xiaowu.behappy.screw.core.exception.QueryException;
import org.xiaowu.behappy.screw.core.metadata.Column;
import org.xiaowu.behappy.screw.core.metadata.Database;
import org.xiaowu.behappy.screw.core.metadata.PrimaryKey;
import org.xiaowu.behappy.screw.core.metadata.Table;
import org.xiaowu.behappy.screw.core.query.AbstractDatabaseQuery;
import org.xiaowu.behappy.screw.core.util.Assert;
import org.xiaowu.behappy.screw.core.util.ExceptionUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.xiaowu.behappy.screw.core.constant.DefaultConstants.NOT_SUPPORTED;

/**
 * Sqlite 查询
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/18 13:58
 */
public class SqliteDataBaseQuery extends AbstractDatabaseQuery {
    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public SqliteDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     */
    @Override
    public Database getDataBase() throws QueryException {
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     */
    @Override
    public List<Table> getTables() {
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

    /**
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<Column> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

    /**
     * 获取所有列信息
     *
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends Column> getTableColumns() throws QueryException {
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

    /**
     * 根据表名获取主键
     *
     * @param table {@link String}
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys(String table) throws QueryException {
        throw ExceptionUtils.mpe(NOT_SUPPORTED);
    }

}
