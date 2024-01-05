package org.xiaowu.behappy.screw.core.query.clickhouse;

import org.xiaowu.behappy.screw.core.exception.QueryException;
import org.xiaowu.behappy.screw.core.mapping.Mapping;
import org.xiaowu.behappy.screw.core.metadata.Column;
import org.xiaowu.behappy.screw.core.metadata.Database;
import org.xiaowu.behappy.screw.core.metadata.PrimaryKey;
import org.xiaowu.behappy.screw.core.metadata.Table;
import org.xiaowu.behappy.screw.core.query.AbstractDatabaseQuery;
import org.xiaowu.behappy.screw.core.query.clickhouse.model.ClickhouseColumnModel;
import org.xiaowu.behappy.screw.core.query.clickhouse.model.ClickhouseDatabaseModel;
import org.xiaowu.behappy.screw.core.query.clickhouse.model.ClickhousePrimaryKeyModel;
import org.xiaowu.behappy.screw.core.query.clickhouse.model.ClickhouseTableModel;
import org.xiaowu.behappy.screw.core.util.Assert;
import org.xiaowu.behappy.screw.core.util.CollectionUtils;
import org.xiaowu.behappy.screw.core.util.ExceptionUtils;
import org.xiaowu.behappy.screw.core.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.xiaowu.behappy.screw.core.constant.DefaultConstants.PERCENT_SIGN;

/**
 * clickhouse数据查询
 * @author xiaowu
 */
public class ClickhouseDataBaseQuery extends AbstractDatabaseQuery {

    public ClickhouseDataBaseQuery(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 获取数据库信息
     * @return
     * @throws QueryException
     */
    @Override
    public Database getDataBase() throws QueryException {
        ClickhouseDatabaseModel model = new ClickhouseDatabaseModel();
        //当前数据库名称
        model.setDatabase(getCatalog());
        return model;
    }

    /**
     * 获取表信息
     * @return
     * @throws QueryException
     */
    @Override
    public List<? extends Table> getTables() throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getTables(getCatalog(), getSchema(), PERCENT_SIGN,
                    new String[] { "TABLE" });
            //映射
            return Mapping.convertList(resultSet, ClickhouseTableModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }

    /**
     * 获取所有column信息
     * @param table {@link String} 表名
     * @return
     * @throws QueryException
     */
    @Override
    public List<? extends Column> getTableColumns(String table) throws QueryException {
        Assert.notEmpty(table, "Table name can not be empty!");
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getColumns(getCatalog(), getSchema(), table, PERCENT_SIGN);
            //映射
            List<ClickhouseColumnModel> list = Mapping.convertList(resultSet, ClickhouseColumnModel.class);
            //这里处理是为了如果是查询全部列呢？所以处理并获取唯一表名
            List<String> tableNames = list.stream().map(ClickhouseColumnModel::getTableName)
                    .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(columnsCaching)) {
                //查询全部
                if (table.equals(PERCENT_SIGN)) {
                    //获取全部表列信息SQL
                    String sql = "select table as TABLE_NAME, name as COLUMN_NAME, type as COLUMN_TYPE, null as COLUMN_LENGTH\n" +
                            "from system.columns\n" +
                            "where database = '%s'";
                    PreparedStatement statement = prepareStatement(
                            String.format(sql, getDataBase().getDatabase()));
                    resultSet = statement.executeQuery();
                }
                //单表查询
                else {
                    //获取表列信息SQL 查询表名、列名、说明、数据类型
                    String sql = "select table as TABLE_NAME, name as COLUMN_NAME, type as COLUMN_TYPE, null as COLUMN_LENGTH\n" +
                            "from system.columns\n" +
                            "where database = '%s'\n" +
                            "and table = '%s'";
                    resultSet = prepareStatement(
                            String.format(sql, getDataBase().getDatabase(), table)).executeQuery();
                }
                List<ClickhouseColumnModel> inquires = Mapping.convertList(resultSet,
                        ClickhouseColumnModel.class);
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

    @Override
    public List<? extends Column> getTableColumns() throws QueryException {
        //获取全部列
        return getTableColumns(PERCENT_SIGN);
    }

    @Override
    public List<? extends PrimaryKey> getPrimaryKeys(String table) throws QueryException {
        ResultSet resultSet = null;
        try {
            //查询
            resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), table);
            //映射
            return Mapping.convertList(resultSet, ClickhousePrimaryKeyModel.class);
        } catch (SQLException e) {
            throw ExceptionUtils.mpe(e);
        } finally {
            JdbcUtils.close(resultSet, this.connection);
        }
    }

    @Override
    public List<? extends PrimaryKey> getPrimaryKeys() throws QueryException {

        ResultSet resultSet = null;
        try {
            String sql = "select database as TABLE_CAT,\n" +
                    "       NULL     as TABLE_SCHEM,\n" +
                    "       table    as TABLE_NAME,\n" +
                    "       name     as COLUMN_NAME,\n" +
                    "       position     as KEY_SEQ,\n" +
                    "       'PRIMARY'     as PK_NAME\n" +
                    "       from system.columns\n" +
                    "where database = '%s'\n" +
                    "  and is_in_primary_key = 1";
            // 拼接参数
            resultSet = prepareStatement(String.format(sql, getDataBase().getDatabase()))
                    .executeQuery();
            return Mapping.convertList(resultSet, ClickhousePrimaryKeyModel.class);
        } catch (SQLException e) {
            throw new QueryException(e);
        } finally {
            JdbcUtils.close(resultSet);
        }
    }
}