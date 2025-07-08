package com.wdcftgg.carpentersblocks.carpentersblocks.util.states.loader.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class StateDTO {
	
	@SerializedName(value = "parts")
	Map<String, StatePartDTO> _parts;
		
	public Map<String, StatePartDTO> getParts() {
		return _parts;
	}
	
}