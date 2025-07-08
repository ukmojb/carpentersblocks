package com.wdcftgg.carpentersblocks.carpentersblocks.util.states;

import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.Quad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.Map.Entry;

public class StateMap {

	protected Map<String, State> _stateMap;
	
	protected StateMap() {
		_stateMap = new HashMap<String, State>();
	}
	
	State putState(String key, State value) {
		return _stateMap.put(key, value);
	}
	
	public State getState(String key) {
		return _stateMap.get(key);
	}
	
	@SideOnly(Side.CLIENT)
	public List<Quad> getInventoryQuads() {
		State state = getState(StateConstants.DEFAULT_STATE);
		List<Quad> list = new ArrayList<Quad>();
		Iterator<Entry<String, StatePart>> iter = state.getStateParts().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, StatePart> entry = iter.next();
			list.addAll(StateUtil.getQuads(entry.getValue()));
		}
		return list;
	}
	
}
