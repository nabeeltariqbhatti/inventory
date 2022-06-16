package com.msm.service.impl;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msm.common.library.util.StringUtil;
import com.msm.service.ServiceHome;

@Service
public class ServiceHomeImp implements ServiceHome{

	public static final String USER_ID = "userId";
	
	@Autowired(required = false)
	HttpServletRequest httpServletRequest;
	
//	@Autowired
//	private RepositoryUser repositoryUser;
	
	public int getUserId() {
		
		String userId = httpServletRequest.getHeader(USER_ID);
		if (StringUtil.isNotBlank(userId)) {
			return Integer.parseInt(userId);
		} else {
			return 1;
		}
	}
	
//	public DtoUser getUser(int userId) {
//		User user = repositoryUser.findByIdAndIsDeleted(userId, false);
//		return UserMapper.getDtoUserFromUser(new DtoUser(), user);
//	}
}