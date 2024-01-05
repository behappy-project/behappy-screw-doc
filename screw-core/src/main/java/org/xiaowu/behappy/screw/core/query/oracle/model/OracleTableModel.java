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
package org.xiaowu.behappy.screw.core.query.oracle.model;

import org.xiaowu.behappy.screw.core.mapping.MappingField;
import org.xiaowu.behappy.screw.core.metadata.Table;
import lombok.Data;

/**
 * oracle table
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/27 8:44
 */
@Data
public class OracleTableModel implements Table {
    /**
     * TABLE_CAT
     */
    @MappingField(value = "TABLE_CAT")
    private String tableCat;
    /**
     * 表名
     */
    @MappingField(value = "TABLE_NAME")
    private String tableName;
    /**
     * 表模式
     */
    @MappingField(value = "TABLE_SCHEM")
    private String tableSchem;
    /**
     * 表类型
     */
    @MappingField(value = "TABLE_TYPE")
    private String tableType;
    /**
     * 备注
     */
    @MappingField(value = "REMARKS")
    private String remarks;
}
