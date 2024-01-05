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
package org.xiaowu.behappy.screw.core.query.cachedb;

import org.xiaowu.behappy.screw.core.exception.QueryException;
import org.xiaowu.behappy.screw.core.mapping.Mapping;
import org.xiaowu.behappy.screw.core.metadata.Column;
import org.xiaowu.behappy.screw.core.metadata.Database;
import org.xiaowu.behappy.screw.core.metadata.PrimaryKey;
import org.xiaowu.behappy.screw.core.metadata.Table;
import org.xiaowu.behappy.screw.core.query.AbstractDatabaseQuery;
import org.xiaowu.behappy.screw.core.query.cachedb.model.CacheDbColumnModel;
import org.xiaowu.behappy.screw.core.query.cachedb.model.CacheDbDatabaseModel;
import org.xiaowu.behappy.screw.core.query.cachedb.model.CacheDbPrimaryKeyModel;
import org.xiaowu.behappy.screw.core.query.cachedb.model.CacheDbTableModel;
import org.xiaowu.behappy.screw.core.util.Assert;
import org.xiaowu.behappy.screw.core.util.CollectionUtils;
import org.xiaowu.behappy.screw.core.util.ExceptionUtils;
import org.xiaowu.behappy.screw.core.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.xiaowu.behappy.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * CacheDB 数据库查询
 *
 * @author <a href ='jxh98@foxmail.com'>Josway</a> 2020/8/26
 * @since JDK 1.8
 */
public class CacheDbDataBaseQuery extends AbstractDatabaseQuery {

    /**
     * 构造函数
     *
     * @param dataSource {@link DataSource}
     */
    public CacheDbDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库
     *
     * @return {@link Database} 数据库信息
     * @throws QueryException QueryException
     */
    @Override
    public Database getDataBase() throws QueryException {
        CacheDbDatabaseModel model = new CacheDbDatabaseModel();
        //当前数据库名称
        model.setDatabase(getSchema());
        return model;
    }

    /**
     * 获取表信息
     *
     * @return {@link List} 所有表信息
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends Table> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), null,
                new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, CacheDbTableModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    /**
     * 获取列信息
     *
     * @param table {@link String} 表名
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends Column> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            final List<CacheDbColumnModel> list = Mapping.convertList(resultSet,
                CacheDbColumnModel.class);
            //这里处理是为了如果是查询全部列呢？所以处理并获取唯一表名
            List<String> tableNames = list.stream().map(CacheDbColumnModel::getTableName)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(columnsCaching)) {
                //查询全部
                if (table.equals(PERCENT_SIGN)) {
                    //获取全部表列信息SQL
                    String sql = MessageFormat
                        .format("select TABLE_NAME as \"TABLE_NAME\",COLUMN_NAME as "
                                + "\"COLUMN_NAME\",DESCRIPTION as \"REMARKS\","
                                + "case when CHARACTER_MAXIMUM_LENGTH is null then DATA_TYPE  || '''' "
                                + "else DATA_TYPE  || ''(''||CHARACTER_MAXIMUM_LENGTH ||'')'' end as \"COLUMN_TYPE\" "
                                + "from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ''{0}''",
                            getSchema());
                    PreparedStatement statement = prepareStatement(sql);
                    resultSet = statement.executeQuery();
                    int fetchSize = 4284;
                    if (resultSet.getFetchSize() < fetchSize) {
                        resultSet.setFetchSize(fetchSize);
                    }
                }
                //单表查询
                else {
                    //获取表列信息SQL 查询表名、列名、说明、数据类型
                    String sql = MessageFormat
                        .format("select TABLE_NAME as \"TABLE_NAME\",COLUMN_NAME as "
                                + "\"COLUMN_NAME\",DESCRIPTION as \"REMARKS\","
                                + "case when CHARACTER_MAXIMUM_LENGTH is null then DATA_TYPE  || ''''"
                                + "else DATA_TYPE  || ''(''||CHARACTER_MAXIMUM_LENGTH ||'')'' end as \"COLUMN_TYPE\" "
                                + "from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ''{0}'' and TABLE_NAME = "
                                + "''{1}''",
                            getSchema(), table);
                    resultSet = prepareStatement(sql).executeQuery();
                }
                List<CacheDbColumnModel> inquires = Mapping.convertList(resultSet,
                    CacheDbColumnModel.class);
                //处理列，表名为key，列名为值
                tableNames.forEach(name -> columnsCaching.put(name, inquires.stream()
                    .filter(i -> i.getTableName().equals(name)).collect(Collectors.toList())));
            }
            //处理备注信息
            list.forEach(i -> {
                //从缓存中根据表名获取列信息
                List<Column> columns = columnsCaching.get(i.getTableName());
                columns.forEach(j -> {
                    //列名表名一致
                    if (i.getColumnName().equals(j.getColumnName())
                        && i.getTableName().equals(j.getTableName())) {
                        //放入列类型
                        i.setColumnType(j.getColumnType());
                        i.setColumnLength(j.getColumnLength());
                        //放入注释
                        i.setRemarks(j.getRemarks());
                    }
                });
            });
            return list;
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    /**
     * 获取所有列信息
     *
     * @return {@link List} 表字段信息
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends Column> getTableColumns() throws QueryException {
        //获取全部列
        return getTableColumns(PERCENT_SIGN);
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
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), table);
            //映射
            return Mapping.convertList(resultSet, CacheDbPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    /**
     * 根据表名获取主键信息
     *
     * @return {@link List}
     * @throws QueryException QueryException
     */
    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {
        ResultSet resultSet = null;
        try {
            // 由于单条循环查询存在性能问题，所以这里通过自定义SQL查询数据库主键信息
            String sql = "select TABLE_CATALOG ,TABLE_NAME as \"TABLE_NAME\",TABLE_SCHEMA as \"TABLE_SCHEM\",COLUMN_NAME as \"COLUMN_NAME\",ORDINAL_POSITION as \"KEY_SEQ\" from INFORMATION_SCHEMA.COLUMNS where PRIMARY_KEY='YES' and TABLE_SCHEMA='%s'";
            // 拼接参数
            resultSet = prepareStatement(String.format(sql, getDataBase().getDatabase()))
                .executeQuery();
            return Mapping.convertList(resultSet, CacheDbPrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }
}
