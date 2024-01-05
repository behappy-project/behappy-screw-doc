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
package org.xiaowu.behappy.screw.core.query.postgresql.model;

import org.xiaowu.behappy.screw.core.mapping.MappingField;
import org.xiaowu.behappy.screw.core.metadata.Table;
import lombok.Data;

/**
 * 表信息
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 20:22
 */
@Data
public class PostgreSqlTableModel implements Table {
    /**
     * refGeneration
     */
    @MappingField(value = "REF_GENERATION")
    private String refGeneration;
    /**
     * typeName
     */
    @MappingField(value = "TYPE_NAME")
    private String typeName;
    /**
     * typeSchem
     */
    @MappingField(value = "TYPE_SCHEM")
    private String typeSchem;
    /**
     * tableSchem
     */
    @MappingField(value = "TABLE_SCHEM")
    private String tableSchem;
    /**
     * typeCat
     */
    @MappingField(value = "TYPE_CAT")
    private String typeCat;
    /**
     * tableCat
     */
    @MappingField(value = "TABLE_CAT")
    private Object tableCat;
    /**
     * 表名称
     */
    @MappingField(value = "TABLE_NAME")
    private String tableName;
    /**
     * selfReferencingColName
     */
    @MappingField(value = "SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;
    /**
     * 说明
     */
    @MappingField(value = "REMARKS")
    private String remarks;
    /**
     * 表类型
     */
    @MappingField(value = "TABLE_TYPE")
    private String tableType;
}
