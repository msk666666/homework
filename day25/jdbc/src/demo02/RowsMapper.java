package demo02;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowsMapper {
    public Object mapper(ResultSet rs) throws SQLException;
}
