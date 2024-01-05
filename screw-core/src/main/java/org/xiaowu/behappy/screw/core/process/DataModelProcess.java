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
package org.xiaowu.behappy.screw.core.process;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.xiaowu.behappy.screw.core.Configuration;
import org.xiaowu.behappy.screw.core.metadata.Column;
import org.xiaowu.behappy.screw.core.metadata.Database;
import org.xiaowu.behappy.screw.core.metadata.PrimaryKey;
import org.xiaowu.behappy.screw.core.metadata.Table;
import org.xiaowu.behappy.screw.core.metadata.model.ColumnModel;
import org.xiaowu.behappy.screw.core.metadata.model.DataModel;
import org.xiaowu.behappy.screw.core.metadata.model.TableModel;
import org.xiaowu.behappy.screw.core.query.DatabaseQuery;
import org.xiaowu.behappy.screw.core.query.DatabaseQueryFactory;
import org.xiaowu.behappy.screw.core.util.StringUtils;
import org.xiaowu.behappy.screw.core.constant.DefaultConstants;

/**
 * 数据模型处理
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/22 21:12
 */
public class DataModelProcess extends AbstractProcess {

    /**
     * 构造方法
     *
     * @param configuration     {@link Configuration}
     */
    public DataModelProcess(Configuration configuration) {
        super(configuration);
    }

    /**
     * 处理
     *
     * @return {@link DataModel}
     */
    @Override
    public DataModel process() {
        //获取query对象
        DatabaseQuery query = new DatabaseQueryFactory(config.getDataSource()).newInstance();

        return this.process(query);
    }

    /**
     * 处理
     *
     * @return {@link DataModel}
     */
    @Override
    public DataModel process(DatabaseQuery query) {
        //获取query对象
        DataModel model = new DataModel();
        //Title
        model.setTitle(config.getTitle());
        //org
        model.setOrganization(config.getOrganization());
        //org url
        model.setOrganizationUrl(config.getOrganizationUrl());
        //version
        model.setVersion(config.getVersion());
        //description
        model.setDescription(config.getDescription());

        /*查询操作开始*/
        long start = System.currentTimeMillis();
        //获取数据库
        Database database = query.getDataBase();
        logger.debug("query the database time consuming:{}ms",
            (System.currentTimeMillis() - start));
        model.setDatabase(database.getDatabase());
        start = System.currentTimeMillis();
        //获取全部表
        List<? extends Table> tables = query.getTables();
        logger.debug("query the table time consuming:{}ms", (System.currentTimeMillis() - start));
        //获取全部列
        start = System.currentTimeMillis();
        List<? extends Column> columns = query.getTableColumns();
        logger.debug("query the column time consuming:{}ms", (System.currentTimeMillis() - start));
        //获取主键
        start = System.currentTimeMillis();
        List<? extends PrimaryKey> primaryKeys = query.getPrimaryKeys();
        logger.debug("query the primary key time consuming:{}ms",
            (System.currentTimeMillis() - start));
        /*查询操作结束*/

        /*处理数据开始*/
        start = System.currentTimeMillis();
        List<TableModel> tableModels = new ArrayList<>();
        tablesCaching.put(database.getDatabase(), tables);
        for (Table table : tables) {
            //处理列，表名为key，列名为值
            columnsCaching.put(table.getTableName(),
                columns.stream().filter(i -> i.getTableName().equals(table.getTableName()))
                    .collect(Collectors.toList()));
            //处理主键，表名为key，主键为值
            primaryKeysCaching.put(table.getTableName(),
                primaryKeys.stream().filter(i -> table.getTableName().equals(i.getTableName()))
                    .collect(Collectors.toList()));
        }
        for (Table table : tables) {
            /*封装数据开始*/
            TableModel tableModel = new TableModel();
            //表名称
            tableModel.setTableName(table.getTableName());
            //说明
            tableModel.setRemarks(table.getRemarks());
            //添加表
            tableModels.add(tableModel);
            //处理列
            List<ColumnModel> columnModels = new ArrayList<>();
            //获取主键
            List<String> key = primaryKeysCaching.get(table.getTableName()).stream()
                .map(PrimaryKey::getColumnName).collect(Collectors.toList());
            for (Column column : columnsCaching.get(table.getTableName())) {
                packageColumn(columnModels, key, column);
            }
            //放入列
            tableModel.setColumns(columnModels);
        }
        //设置表
        model.setTables(filterTables(tableModels));
        //优化数据
        optimizeData(model);
        /*封装数据结束*/
        logger.debug("encapsulation processing data time consuming:{}ms",
            (System.currentTimeMillis() - start));
        return model;
    }

    /**
     * packageColumn
     * @param columnModels {@link List}
     * @param keyList {@link List}
     * @param column {@link Column}
     */
    private void packageColumn(List<ColumnModel> columnModels, List<String> keyList,
                               Column column) {
        ColumnModel columnModel = new ColumnModel();
        //表中的列的索引（从 1 开始）
        columnModel.setOrdinalPosition(column.getOrdinalPosition());
        //列名称
        columnModel.setColumnName(column.getColumnName());
        //类型
        columnModel.setColumnType(column.getColumnType());
        //字段名称
        columnModel.setTypeName(column.getTypeName());
        //长度
        columnModel.setColumnLength(column.getColumnLength());
        columnModel.setColumnLength(column.getColumnLength());
        //size
        columnModel.setColumnSize(column.getColumnSize());
        //小数位
        columnModel.setDecimalDigits(
            StringUtils.defaultString(column.getDecimalDigits(), DefaultConstants.ZERO_DECIMAL_DIGITS));
        //可为空
        columnModel.setNullable(DefaultConstants.ZERO.equals(column.getNullable()) ? DefaultConstants.N : DefaultConstants.Y);
        //是否主键
        columnModel.setPrimaryKey(keyList.contains(column.getColumnName()) ? DefaultConstants.Y : DefaultConstants.N);
        //默认值
        columnModel.setColumnDef(column.getColumnDef());
        //说明
        columnModel.setRemarks(column.getRemarks());
        //放入集合
        columnModels.add(columnModel);
    }

}
