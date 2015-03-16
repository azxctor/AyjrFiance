package com.hengxin.platform.job;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.quartz.impl.jdbcjobstore.oracle.OracleDelegate;

public class CustomizedOracleDelegate extends OracleDelegate {

	protected Blob writeDataToBlob(ResultSet rs, int column, byte[] data)
			throws SQLException {

		Blob blob = rs.getBlob(column); // get blob

		if (blob == null) {
			throw new SQLException("Driver's Blob representation is null!");
		}
		blob.setBytes(1, data);
		blob.truncate(data.length);
		return blob;
	}

}
