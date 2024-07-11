package com.sparta.hirello.primary.column.dto.response;

import com.sparta.hirello.primary.column.entity.Columns;
import lombok.Data;

@Data
public class ColumnResponse {

    private Long id;
    private String columnName;
    private Long userId;

    private ColumnResponse(Columns column){
        this.id=column.getColumnId();
        this.columnName=column.getColumnName();
        this.userId=column.getUser().getId();
    }

    public static ColumnResponse of(Columns column) {
        return new ColumnResponse(column);
    }
}
