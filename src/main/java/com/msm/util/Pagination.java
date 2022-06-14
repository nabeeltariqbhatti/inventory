package com.msm.util;

import org.springframework.data.domain.Sort;
/**
 *
 * @author IbrarHussain
 *
 */
public class Pagination extends org.springframework.data.domain.PageRequest {

	public Pagination(int page, int size, Sort sort) {
		super(page, size, sort);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
