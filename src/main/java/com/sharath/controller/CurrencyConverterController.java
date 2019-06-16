/**
 * 
 */
package com.sharath.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sharath.model.CurrencyConvertionBean;
import com.sharath.proxy.CurrencyExchangeProxy;

/**
 * @author shar939
 *
 */

@RestController
public class CurrencyConverterController {

	@Autowired
	private Environment env; 
	
	@GetMapping(path = "/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConvertionBean convertCurrency(@PathVariable String from, 
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		Map<String, String> uriVariables = new HashMap<String, String>(); 
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConvertionBean> response = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConvertionBean.class, 
				uriVariables);
		CurrencyConvertionBean body = response.getBody();
		String port = String.valueOf(Integer.parseInt(env.getProperty("local.server.port")));
		return new CurrencyConvertionBean(body.getId(),from,to,
				body.getConversionMultiple(),
				quantity,(quantity.multiply(body.getConversionMultiple())),port); 
	}
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@GetMapping(path = "/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConvertionBean convertCurrencyFeign(@PathVariable String from, 
			@PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		CurrencyConvertionBean body = currencyExchangeProxy.retrieveUSDtoINR(from, to);  
		String port = String.valueOf(Integer.parseInt(env.getProperty("local.server.port")));
		return new CurrencyConvertionBean(body.getId(),from,to,
				body.getConversionMultiple(),
				quantity,(quantity.multiply(body.getConversionMultiple())),port); 
	}
}
