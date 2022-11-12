package com.csg.ib.batch.support.infra.spring.batch;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

		var properties = load(resource);
		if (null != properties) {
			return new PropertiesPropertySource(name != null ? name
					: Objects.requireNonNull(resource.getResource().getFilename(),
							"application-page-size.yaml propert file not found"),
					properties);
		}

		throw new IOException("Add custom IO exception");
	}

	private Properties load(EncodedResource resource) {

		var factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

}
