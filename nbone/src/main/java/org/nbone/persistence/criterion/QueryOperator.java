package org.nbone.persistence.criterion;

public interface QueryOperator extends IQueryTransfer
{

	public static final String eq = "=";
	public static final String not_eq = "<>";
	public static final String lt = "<";
	public static final String gt = ">";
	public static final String lt_eq = "<=";
	public static final String gt_eq = ">=";
	
	public static final String in = "in";
	public static final String not_in = "not in";
	public static final String between = "between";
	public static final String like = "like";
	public static final String left_like = "leftlike";
	public static final String right_like = "rightlike";
	public static final String is_null = "is null";
	public static final String is_not_null = "is not null";
	
	public static final String and = "and";
	public static final String or = "or";
	public static final String not = "not";
	
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	
	public static final String ORDER_BY = "order by";
	public static final String GROUP_BY = "group by";
	
	
	//-----------------------------------------------------------------
	//left blank/right blank 
	//------------------------------------------------------------------
	public static final String eq_lr_blank = " = ";
	public static final String not_eq_lr_blank = " <> ";
	public static final String lt_lr_blank = " < ";
	public static final String gt_lr_blank = " > ";
	public static final String lt_eq_lr_blank = " <= ";
	public static final String gt_eq_lr_blank = " >= ";
	
	public static final String in_lr_blank = " in ";
	public static final String not_in_lr_blank = " not in ";
	public static final String between_lr_blank = " between ";
	public static final String like_lr_blank = " like ";
	public static final String is_null_lr_blank = " is null ";
	public static final String is_not_null_lr_blank = " is not null ";
	public static final String and_lr_blank = " and ";
	public static final String or_lr_blank = " or ";
	public static final String not_lr_blank = " not ";
	
	public static final String ASC_LR_BLANK = " asc ";
	public static final String DESC_LR_BLANK = " desc ";
	

	
}
