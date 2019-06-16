package com.sharath.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sharath.model.CurrencyConvertionBean;

@FeignClient(name = "currency-exchange-service") //, url = "localhost:8000")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeProxy {
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConvertionBean retrieveUSDtoINR(@PathVariable String from, @PathVariable String to);
}
