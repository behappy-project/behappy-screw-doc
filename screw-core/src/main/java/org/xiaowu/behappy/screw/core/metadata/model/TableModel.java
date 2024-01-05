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
package org.xiaowu.behappy.screw.core.metadata.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表信息领域对象
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/17 20:22
 */
@Data
public class TableModel implements Serializable {

    private static final long serialVersionUID = 825666678767312142L;
    /**
     * 表名
     */
    private String            tableName;
    /**
     * 备注
     */
    private String            remarks;
    /**
     * 表列
     */
    private List<ColumnModel> columns;

    /**
     * 是否弃用
     */
    private Boolean           deprecated;
}
