package com.msm.tenant.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
@Component
public class TenantDataSource implements Serializable {

    /**
	 * 
	 */

	private static final long serialVersionUID = 1616502360284857737L;
	private HashMap<String, DataSource> dataSources = new HashMap<>();

    public DataSource getDataSource(String name) {
        if (dataSources.get(name) != null) {
            return dataSources.get(name);
        }
        if(null ==name || name.trim().isEmpty()) {
        	dataSources.put(name, defaultDataSource());
        	return defaultDataSource();
        }
        DataSource dataSource = createDataSource(name);
        if (dataSource != null) {
            dataSources.put(name, dataSource);
        
        }
        return dataSource;
    }

    private DataSource createDataSource(String name) {
            DataSourceBuilder<?> factory = DataSourceBuilder
                    .create().driverClassName("com.mysql.jdbc.Driver")
                    .username("root")
                    .password("root")
                    .url("jdbc:mysql://localhost:3306/"+name);
            DataSource ds = factory.build();
            return ds;
    }
    
    private DataSource defaultDataSource() {
    	  DataSourceBuilder<?> factory = DataSourceBuilder
                  .create().driverClassName("com.mysql.jdbc.Driver")
                  .username("root")
                  .password("root")
                  .url("jdbc:mysql://localhost:3306/abc");
          DataSource ds = factory.build();
          return ds;
	}
}
