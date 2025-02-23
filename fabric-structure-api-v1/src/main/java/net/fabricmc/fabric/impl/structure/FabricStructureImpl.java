/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.structure;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.feature.StructureFeature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.mixin.structure.StructuresConfigAccessor;

public class FabricStructureImpl implements ModInitializer {
	//Keeps a map of structures to structure configs for purposes of initializing the flat chunk generator
	public static final Map<StructureFeature<?>, StructurePlacement> FLAT_STRUCTURE_TO_CONFIG_MAP = new HashMap<>();

	//Keeps a map of structures to structure configs.
	public static final Map<StructureFeature<?>, StructurePlacement> STRUCTURE_TO_CONFIG_MAP = new HashMap<>();

	@Override
	public void onInitialize() {
		ServerWorldEvents.LOAD.register((server, world) -> {
			// Need temp map as some mods use custom chunk generators with immutable maps in themselves.
			Map<StructureFeature<?>, StructurePlacement> tempMap = new HashMap<>(world.getChunkManager().getChunkGenerator().getStructuresConfig().getStructures());

			tempMap.putAll(STRUCTURE_TO_CONFIG_MAP);

			//Make it immutable again
			ImmutableMap<StructureFeature<?>, StructurePlacement> immutableMap = ImmutableMap.copyOf(tempMap);
			((StructuresConfigAccessor) world.getChunkManager().getChunkGenerator().getStructuresConfig()).setPlacements(immutableMap);
		});
	}
}
