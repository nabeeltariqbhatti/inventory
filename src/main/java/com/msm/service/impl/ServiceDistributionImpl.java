package com.msm.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.msm.Client;
import com.msm.Contact;
import com.msm.common.library.dto.CustomResponse;
import com.msm.dto.DistributionDto;
import com.msm.dto.DtoSearch;
import com.msm.repository.RepositoryDistribution;
import com.msm.service.ServiceDistribution;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/**
 * 
 * @author IbrarHussain
 *
 */
@Service
public class ServiceDistributionImpl implements ServiceDistribution{

	private static final Logger  LOGGER = LoggerFactory.getLogger(ServiceDistributionImpl.class);
	
	private final RepositoryDistribution repositoryDistribution;
	private final ResourceLoader resourceLoader;
	private final HttpServletResponse httpServletResponse;
	public ServiceDistributionImpl(RepositoryDistribution repositoryDistribution,ResourceLoader resourceLoader,
			HttpServletResponse httpServletResponse) {
		this.repositoryDistribution = repositoryDistribution;
		this.resourceLoader  =resourceLoader;
		this.httpServletResponse = httpServletResponse;
	}

		@Override
		public CustomResponse saveDistribution(DistributionDto distributionDto) {
			// TODO Auto-generated method stub
			return null;
		}

	@Override
	public CustomResponse getDistributionById(Integer distributionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomResponse deleteDistributionById(Integer distributionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomResponse getForDwopDown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomResponse searchDistributionWithPaging(DtoSearch dtoSearch) {
		// TODO Auto-generated method stub
		return null;
	}
}
