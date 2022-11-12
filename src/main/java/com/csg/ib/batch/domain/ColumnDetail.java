package com.csg.ib.batch.domain;

public class ColumnDetail {

	private String columnName;
	private String dataType;
	private String columnSize;
	private String decimalDigits;
	private String isNullable;

	public ColumnDetail(String columnName, String dataType, String columnSize, String decimalDigits,
			String isNullable) {
		super();
		this.columnName = columnName;
		this.dataType = dataType;
		this.columnSize = columnSize;
		this.decimalDigits = decimalDigits;
		this.isNullable = isNullable;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}

	public String getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(String decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((columnSize == null) ? 0 : columnSize.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((decimalDigits == null) ? 0 : decimalDigits.hashCode());
		result = prime * result + ((isNullable == null) ? 0 : isNullable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnDetail other = (ColumnDetail) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (columnSize == null) {
			if (other.columnSize != null)
				return false;
		} else if (!columnSize.equals(other.columnSize))
			return false;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (decimalDigits == null) {
			if (other.decimalDigits != null)
				return false;
		} else if (!decimalDigits.equals(other.decimalDigits))
			return false;
		if (isNullable == null) {
			if (other.isNullable != null)
				return false;
		} else if (!isNullable.equals(other.isNullable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ColumnDetail [columnName=" + columnName + ", dataType=" + dataType + ", columnSize=" + columnSize
				+ ", decimalDigits=" + decimalDigits + ", isNullable=" + isNullable + "]";
	}

}
